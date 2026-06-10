package com.acme.ord.service.siparis;

import com.acme.ord.common.constants.DefConstants;
import com.acme.ord.common.util.GeneralUtil;
import com.acme.ord.domain.dto.SiparisCevapDto;
import com.acme.ord.domain.dto.SiparisIstekDto;
import com.acme.ord.domain.dto.SiparisIstekDetayDto;
import com.acme.ord.domain.dto.SiparisKalemDetayiDto;
import com.acme.ord.domain.entity.*;
import com.acme.ord.domain.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SiparisService {

    private final BelgeRepository belgeRepository;
    private final HesapRepository hesapRepository;
    private final SiparisRepository siparisRepository;
    private final SiparisHareketRepository siparisHareketRepository;
    private final SiparisDetayKayitRepository siparisDetayKayitRepository;
    private final SiparisSahibiRepository siparisSahibiRepository;
    private final SiparisSahibiFinansRepository siparisSahibiFinansRepository;
    private final SiparisHareketIslemSdyRepository siparisHareketIslemSdyRepository;
    private final SiparisKalemKontrolService kalemKontrolService;
    private final TeslimatTalepRepository teslimatTalepRepository;
    private final SiparisDurumRepository siparisDurumRepository;
    private final SiparisDetayRepository siparisDetayRepository;

    // =========================================================================
    // siparis_isle: Ana siparis isleme motoru
    // =========================================================================
    @Transactional
    public int siparisIsle(Belge prmBelge, Hesap prmHesap, Long prmIslemTipi,
                           SiparisIstekDto istek,
                           SiparisCevapDto cevap,
                           StringBuilder cevapKodu,
                           StringBuilder errorType,
                           StringBuilder errorNum,
                           StringBuilder errorStr) {
        try {
            // Resend kontrolu (revize haric)
            if (istek.getIslemKodu() == null || istek.getIslemKodu() != DefConstants.ISLEM_KODU_REVIZE) {
                Optional<SiparisHareket> existing = siparisHareketRepository
                    .findByIslemTarihiAndKurumIdAndStanAndIslemId(
                        istek.getIslemTarihi(), istek.getKurumId(), istek.getStan(), istek.getIslemKodu());
                if (existing.isPresent()) {
                    GeneralUtil.setOutVariables(cevapKodu, DefConstants.RC_RESEND,
                        errorType, "APP", errorNum, -20304, errorStr,
                        GeneralUtil.getIslemCevapAck(DefConstants.RC_RESEND));
                    return DefConstants.RC_FAIL;
                }
            }

            // Hizmet no al - sanal hesap kontrolu [ ] abone Repository gerekir
            String hizmetNo = prmHesap.getHesapNo();
            if (Integer.valueOf(DefConstants.EVET).equals(prmHesap.getSanalMi())) {
                // [ ] aboneRepository.findFirstByHesapId(prmHesap.getId()).map(...).orElse(prmHesap.getHesapNo())
            }

            // Kalem kontrol
            List<SiparisKalemKontrolService.SiparisDetayKontrolRecord> sdKontrolCol = new ArrayList<>();
            List<SiparisKalemDetayiDto> kalemDetList;
            try {
                kalemDetList = kalemKontrolService.kontrol(prmBelge, prmHesap, istek, sdKontrolCol,
                    cevapKodu, errorType, errorNum, errorStr);
            } catch (SiparisKalemKontrolService.SiparisKalemKontrolException e) {
                return DefConstants.RC_FAIL;
            }

            // Siparis sahibi bul veya olustur
            SiparisSahibi siparisSahibi = siparisSahibiRepository
                .findByHesapIdAndMusteriId(prmHesap.getId(), prmHesap.getMusteriId())
                .orElse(null);

            if (siparisSahibi == null) {
                siparisSahibi = new SiparisSahibi();
                siparisSahibi.setHesapId(prmHesap.getId());
                siparisSahibi.setMusteriId(prmHesap.getMusteriId());
                siparisSahibi.setSirketId(prmBelge.getSirketId());
                siparisSahibi.setSiparisSahibiTipiId(DefConstants.SIPARIS_SAHIBI_TIPI_NORMAL);
                siparisSahibi.setTransfereAktarilanTutar(BigDecimal.ZERO);
                siparisSahibi.setKesintiYapilanTutar(BigDecimal.ZERO);
                siparisSahibi.setNakitTalepTarihi(null);
                siparisSahibi.setTanitimZamani(Instant.now());
                siparisSahibi.setTanitanKullaniciId(DefConstants.INST_SISTEM_KULLANICI_ID);
                siparisSahibi.setGuncellemeZamani(Instant.now());
                siparisSahibi = siparisSahibiRepository.save(siparisSahibi);

                siparisSahibiFinansGuncelle(siparisSahibi.getId(), DefConstants.SIPARIS_SAHIBI_FINANS_NORMAL,
                    BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                    BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, null);
            }

            // Mevcut siparis kontrolu (stok_analiz_id ile basarisiz/belirsiz)
            Siparis siparis = null;
            if (istek.getStokAnalizId() != null) {
                List<Siparis> mevcutlar = siparisRepository.findBySiparisSahibiId(siparisSahibi.getId());
                for (Siparis s : mevcutlar) {
                    if (s.getBelgeId().equals(prmBelge.getId())) {
                        Optional<SiparisDurum> durum = siparisDurumRepository.findById(s.getSiparisDurumId());
                        if (durum.isPresent() &&
                            (durum.get().getIslemDurumu() == DefConstants.SIPARIS_DURUM_ISLEM_BASARISIZ
                             || durum.get().getIslemDurumu() == DefConstants.SIPARIS_DURUM_ISLEM_BELIRSIZ)) {
                            siparis = s;
                            break;
                        }
                    }
                }
            }

            if (siparis == null) {
                siparis = new Siparis();
                siparis.setSiparisDurumId(DefConstants.ID_BELIRSIZ);
                siparis.setSiparisSahibiId(siparisSahibi.getId());

                Long siparisTipiId = null;
                if (istek.getIslemKodu() != null && istek.getIslemKodu() == DefConstants.ONAY_ISLEMI) {
                    // [ ] stok_analiz tip kontrolu
                }
                if (istek.getIslemKodu() != null && istek.getIslemKodu() == DefConstants.ISLEM_KODU_REVIZE) {
                    siparisTipiId = DefConstants.SIPARIS_TIPI_MUNFERIT_REVIZE;
                } else {
                    // [ ] stok_analiz tip'e gore tip belirle
                }
                siparis.setSiparisTipiId(siparisTipiId);
                siparis.setIstekDenemeSayisi(0);
                siparis.setSiparisTutari(istek.getSiparisToplamTutar());
                siparis.setUygulama(istek.getUygulama());
                siparis.setSiparisReferansNo(
                    istek.getSiparisDetayBilgi() != null ? istek.getSiparisDetayBilgi().getSiparisReferansNo() : null);
                siparis.setTanitimZamani(Instant.now());
                siparis.setTanitanKullaniciId(
                    istek.getKullaniciId() != null ? istek.getKullaniciId() : DefConstants.INST_SISTEM_KULLANICI_ID);
                siparis.setGuncellemeZamani(Instant.now());
                siparis.setBelgeId(prmBelge.getId());
                siparis.setIstekHesapBelgeSayisi(istek.getToplamKalemSayisi());
                siparis.setMunferitSiparisTutari(BigDecimal.ZERO);
                siparis = siparisRepository.save(siparis);
            }

            // Siparis detay kayit
            SiparisDetayKayit detayKayit = siparisDetayKayitRepository.findBySiparisId(siparis.getId()).orElse(null);
            SiparisIstekDetayDto detayBilgi = istek.getSiparisDetayBilgi();
            if (detayKayit == null) {
                detayKayit = new SiparisDetayKayit();
                detayKayit.setSiparisId(siparis.getId());
                detayKayit.setTdkMi(detayBilgi != null ? detayBilgi.getTdkMi() : null);
                detayKayit.setVergiHaric(detayBilgi != null ? detayBilgi.getVergiHaric() : null);
                detayKayit.setSiparisSebebiId(detayBilgi != null ? detayBilgi.getSiparisSebebiId() : null);
                detayKayit.setAciklama(detayBilgi != null ? detayBilgi.getAciklama() : null);
                detayKayit.setSiparisBelgeNo(detayBilgi != null ? detayBilgi.getSiparisBelgeNo() : null);
                detayKayit.setVergiDairesi(detayBilgi != null ? detayBilgi.getVergiDairesi() : null);
                detayKayit.setVergiNo(detayBilgi != null ? detayBilgi.getVergiNo() : null);
                detayKayit.setSebepBildirimNo(detayBilgi != null ? detayBilgi.getSebepBildirimNo() : null);
                detayKayit.setSebepPersonelSicilNo(detayBilgi != null ? detayBilgi.getSebepKullaniciSicilNo() : null);
                detayKayit.setSebepBayiKodu(detayBilgi != null ? detayBilgi.getSebepBayiKodu() : null);
                detayKayit.setSebepCagriMerkezi(
                    detayBilgi != null && detayBilgi.getSebepCagriMerkezi() != null
                        ? detayBilgi.getSebepCagriMerkezi().substring(0, Math.min(20, detayBilgi.getSebepCagriMerkezi().length()))
                        : null);
                detayKayit.setErpGonderilecekMi(detayBilgi != null ? detayBilgi.getErpGonderilecekMi() : null);
                detayKayit.setErpIban(detayBilgi != null ? detayBilgi.getErpIban() : null);
                detayKayit.setErpAliciAdSoyad(detayBilgi != null ? detayBilgi.getErpAliciAdSoyad() : null);
                detayKayit.setErpAliciTelefon(detayBilgi != null ? detayBilgi.getErpAliciTelefon() : null);
                detayKayit.setErpAciklama(detayBilgi != null ? detayBilgi.getErpAciklama() : null);
                detayKayit.setCid(detayBilgi != null ? detayBilgi.getCid() : null);
                detayKayit.setTaksitliMi(istek.getTaksitliMi());
                detayKayit.setTanitimZamani(Instant.now());
                detayKayit.setTanitanKullaniciId(DefConstants.INST_SISTEM_KULLANICI_ID);
                detayKayit.setGuncellemeZamani(Instant.now());
                detayKayit = siparisDetayKayitRepository.save(detayKayit);
            } else {
                // [ ] Update logic
                detayKayit.setGuncellemeZamani(Instant.now());
                detayKayit = siparisDetayKayitRepository.save(detayKayit);
            }

            // Siparis hareket olustur
            SiparisHareket hareket = new SiparisHareket();
            hareket.setIslemTarihi(istek.getIslemTarihi());
            hareket.setKurumId(istek.getKurumId());
            hareket.setStan(istek.getStan());
            hareket.setSiparisId(siparis.getId());
            hareket.setSiparisHareketDurumId(DefConstants.SHD_DEGERLENDIRILECEK);
            hareket.setSiparisDurumId(siparis.getSiparisDurumId());
            hareket.setIslemId(istek.getIslemKodu());
            hareket.setSiparisTutari(istek.getSiparisToplamTutar());
            hareket.setBusinessId(istek.getBusinessId());
            hareket.setConversationId(istek.getConversationId());
            hareket.setTanitimZamani(Instant.now());
            hareket.setTanitanKullaniciId(DefConstants.INST_SISTEM_KULLANICI_ID);
            hareket.setGuncellemeZamani(Instant.now());
            hareket = siparisHareketRepository.save(hareket);

            // Hareket detaylari (siparis_hareket_islem_sdy)
            for (SiparisKalemDetayiDto kalem : kalemDetList) {
                SiparisHareketIslemSdy sdy = new SiparisHareketIslemSdy();
                sdy.setSiparisHareketId(hareket.getId());
                sdy.setSiparisDetayId(kalem.getSiparisDetayId());
                sdy.setKalemTipiId(kalem.getKalemTipiId());
                sdy.setTedarikciId(kalem.getTedarikciId());
                sdy.setRevizeTutari(kalem.getRevizeTutari());
                sdy.setKdvOrani(kalem.getKdvOrani());
                sdy.setKdvTutari(kalem.getKdvTutari());
                sdy.setOtvOrani(kalem.getOtvOrani());
                sdy.setOtvTutari(kalem.getOtvTutari());
                sdy.setUrunSeriNo(kalem.getUrunSeriNo());
                sdy.setPaketTipiId(kalem.getPaketTipiId());
                sdy.setTaksitliSatisId(kalem.getTaksitliSatisId());
                sdy.setTaksitNo(kalem.getTaksitNo());
                sdy.setTanitimZamani(Instant.now());
                sdy.setTanitanKullaniciId(DefConstants.INST_SISTEM_KULLANICI_ID);
                sdy.setGuncellemeZamani(Instant.now());
                siparisHareketIslemSdyRepository.save(sdy);
            }

            // Kampanya kodu guncelle
            for (SiparisKalemKontrolService.SiparisDetayKontrolRecord rec : sdKontrolCol) {
                Optional<SiparisDetay> sdOpt = siparisDetayRepository.findById(rec.id());
                if (sdOpt.isPresent()) {
                    SiparisDetay sd = sdOpt.get();
                    sd.setKampanyaId(rec.istKmpKodu());
                    sd.setGuncellemeZamani(Instant.now());
                    sd.setGuncelleyenKullaniciId(DefConstants.INST_SISTEM_KULLANICI_ID);
                    siparisDetayRepository.save(sd);
                }
            }

            // Belge guncelle
            BigDecimal yeniKalan = prmBelge.getKalanTutar().subtract(istek.getSiparisToplamTutar());
            String belgeDurum = (yeniKalan.compareTo(BigDecimal.ZERO) <= 0)
                ? DefConstants.REVIZE_ILE_KAPANMIS : DefConstants.PARTIAL;

            prmBelge.setSiparisDuzeltmeTutar(prmBelge.getSiparisDuzeltmeTutar().subtract(istek.getSiparisToplamTutar()));
            prmBelge.setKalanTutar(yeniKalan);
            prmBelge.setBelgeDurum(belgeDurum);
            if (yeniKalan.compareTo(BigDecimal.ZERO) <= 0) {
                prmBelge.setOdemeTarihi(Instant.now());
            }
            prmBelge.setGuncellemeZaman(Instant.now());
            prmBelge.setGuncelleyenKullanici(DefConstants.INST_SISTEM_KULLANICI_ID);
            belgeRepository.save(prmBelge);

            // Cevap type olustur
            cevap = new SiparisCevapDto(
                istek.getStokAnalizId(), null, prmHesap.getId(), prmHesap.getMusteriId(),
                siparisSahibi.getId(), siparis.getId(), hareket.getId(), detayKayit.getId(),
                istek.getIslemKodu(), null, null, istek.getSiparisToplamTutar(),
                BigDecimal.ZERO, 0L, BigDecimal.ZERO, 0L, 0L, null, prmHesap.getHesapNo()
            );

            // Siparis durum guncelle
            siparisGuncelle(cevap, DefConstants.ID_BASARILI, DefConstants.SHD_DEGERLENDIRILDI,
                istek.getIslemKodu(), DefConstants.INST_SISTEM_KULLANICI_ID, cevapKodu, errorStr);

            // Hareket durum guncelle
            hareket.setSiparisHareketDurumId(DefConstants.SHD_DEGERLENDIRILDI);
            hareket.setSiparisDurumId(DefConstants.ID_BASARILI);
            hareket.setCevapKodu(cevapKodu.toString());
            hareket.setGuncellemeZamani(Instant.now());
            hareket.setGuncelleyenKullaniciId(DefConstants.INST_SISTEM_KULLANICI_ID);
            siparisHareketRepository.save(hareket);

            cevapKodu.setLength(0);
            cevapKodu.append(DefConstants.RC_SUCCESS);
            return DefConstants.RC_SUCCESS;

        } catch (Exception e) {
            log.error("siparisIsle hata: {}", e.getMessage(), e);
            GeneralUtil.setOutVariables(cevapKodu, DefConstants.RC_SQL_ERROR, errorType, "ORA",
                errorNum, -1, errorStr, "siparis_isle: " + e.getMessage());
            return DefConstants.RC_FAIL;
        }
    }

    // =========================================================================
    // siparis_yap: belge_id ile siparis yap
    // =========================================================================
    @Transactional
    public int siparisYap(Long belgeId, List<SiparisIstekDto> istekList,
                          SiparisCevapDto cevap,
                          StringBuilder cevapKodu,
                          StringBuilder errorType,
                          StringBuilder errorNum,
                          StringBuilder errorStr) {
        try {
            if (istekList == null || istekList.isEmpty()) {
                GeneralUtil.setOutVariables(cevapKodu, DefConstants.RC_ISTEK_BOS, errorType, "APP",
                    errorNum, -20304, errorStr, "siparis_yap: Istek listesi bos");
                return DefConstants.RC_FAIL;
            }
            SiparisIstekDto istek = istekList.get(0);

            Belge belge = belgeRepository.findById(belgeId)
                .orElseThrow(() -> new RuntimeException("Belge bulunamadi"));
            Hesap hesap = hesapRepository.findById(belge.getHesapId())
                .orElseThrow(() -> new RuntimeException("Hesap bulunamadi"));

            int sonuc = siparisIsle(belge, hesap, istek.getIslemKodu(), istek, cevap,
                cevapKodu, errorType, errorNum, errorStr);
            if (sonuc != DefConstants.RC_SUCCESS) {
                return DefConstants.RC_FAIL;
            }
            return DefConstants.RC_SUCCESS;
        } catch (Exception e) {
            log.error("siparisYap hata: {}", e.getMessage(), e);
            GeneralUtil.setOutVariables(cevapKodu, DefConstants.RC_SQL_ERROR, errorType, "ORA",
                errorNum, -1, errorStr, "siparis_yap: " + e.getMessage());
            return DefConstants.RC_FAIL;
        }
    }

    // =========================================================================
    // siparis_olustur_ws: Web servis uzerinden siparis olustur
    // =========================================================================
    @Transactional
    public int siparisOlusturWs(List<SiparisIstekDto> istekList,
                                SiparisCevapDto cevap,
                                StringBuilder cevapKodu,
                                StringBuilder errorType,
                                StringBuilder errorNum,
                                StringBuilder errorStr) {
        try {
            if (istekList == null || istekList.isEmpty()) {
                GeneralUtil.setOutVariables(cevapKodu, DefConstants.RC_ISTEK_BOS, errorType, "APP",
                    errorNum, -20304, errorStr, "siparis_olustur_ws: Istek listesi bos");
                return DefConstants.RC_FAIL;
            }
            SiparisIstekDto istek = istekList.get(0);

            if (istek.getHesapNo() == null || istek.getHesapNo().isEmpty()) {
                GeneralUtil.setOutVariables(cevapKodu, DefConstants.RC_HESAP_NO_BOS, errorType, "APP",
                    errorNum, -20304, errorStr, "siparis_olustur_ws: Hesap no bos");
                return DefConstants.RC_FAIL;
            }

            Hesap hesap = hesapRepository.findFirstByHesapNo(istek.getHesapNo())
                .orElseThrow(() -> new RuntimeException("Hesap bulunamadi"));

            Belge belge = belgeRepository.findFirstByHesapIdAndBelgeNoAndDonemKodu(
                    hesap.getId(), istek.getBelgeNo(), istek.getDonemKodu())
                .orElseThrow(() -> new RuntimeException("Belge bulunamadi"));

            return siparisIsle(belge, hesap, istek.getIslemKodu(), istek, cevap,
                cevapKodu, errorType, errorNum, errorStr);
        } catch (Exception e) {
            log.error("siparisOlusturWs hata: {}", e.getMessage(), e);
            String rc = e.getMessage() != null && e.getMessage().contains("Hesap") ? DefConstants.RC_HESAP_BULUNAMADI
                : e.getMessage() != null && e.getMessage().contains("Belge") ? DefConstants.RC_BELGE_BULUNAMADI
                : DefConstants.RC_SQL_ERROR;
            GeneralUtil.setOutVariables(cevapKodu, rc, errorType, "APP",
                errorNum, -20304, errorStr, "siparis_olustur_ws: " + e.getMessage());
            return DefConstants.RC_FAIL;
        }
    }

    // =========================================================================
    // siparis_yap_kanal: Kanal uzerinden siparis yap
    // =========================================================================
    @Transactional
    public int siparisYapKanal(String talepNo, List<SiparisIstekDto> istekList,
                               SiparisCevapDto cevap,
                               StringBuilder cevapKodu,
                               StringBuilder errorType,
                               StringBuilder errorNum,
                               StringBuilder errorStr) {
        try {
            if (istekList == null || istekList.isEmpty()) {
                GeneralUtil.setOutVariables(cevapKodu, DefConstants.RC_ISTEK_BOS, errorType, "APP",
                    errorNum, -20304, errorStr, "siparis_yap_kanal: Istek listesi bos");
                return DefConstants.RC_FAIL;
            }
            SiparisIstekDto istek = istekList.get(0);

            TeslimatTalep talep = teslimatTalepRepository.findByTalepNo(talepNo)
                .orElseThrow(() -> new RuntimeException("Talep bulunamadi"));

            if (talep.getTalepDurumId() == null ||
                (!talep.getTalepDurumId().equals(DefConstants.TALEP_DURUM_YENI)
                 && !talep.getTalepDurumId().equals(DefConstants.TALEP_DURUM_ISLEMDE))) {
                GeneralUtil.setOutVariables(cevapKodu, DefConstants.RC_TALEP_DURUMU_UYUMSUZ, errorType, "APP",
                    errorNum, -20304, errorStr, "siparis_yap_kanal: Talep durumu uygun degil");
                return DefConstants.RC_FAIL;
            }

            istek.setUygulama(DefConstants.UYGULAMA_SIPARIS_TALEP);
            istek.setIslemKodu(DefConstants.KANAL_TALEP_GIRISI);

            Hesap hesap = hesapRepository.findById(talep.getHesapId())
                .orElseThrow(() -> new RuntimeException("Hesap bulunamadi"));

            Belge belge = belgeRepository.findFirstByHesapIdAndBelgeNoAndDonemKodu(
                    hesap.getId(), istek.getBelgeNo(), istek.getDonemKodu())
                .orElseThrow(() -> new RuntimeException("Belge bulunamadi"));

            int sonuc = siparisIsle(belge, hesap, istek.getIslemKodu(), istek, cevap,
                cevapKodu, errorType, errorNum, errorStr);
            if (sonuc != DefConstants.RC_SUCCESS) {
                return DefConstants.RC_FAIL;
            }

            talep.setTalepDurumId(DefConstants.TALEP_DURUM_TAMAMLANDI);
            talep.setGuncellemeZamani(Instant.now());
            talep.setGuncelleyenKullaniciId(DefConstants.INST_SISTEM_KULLANICI_ID);
            teslimatTalepRepository.save(talep);

            return DefConstants.RC_SUCCESS;
        } catch (Exception e) {
            log.error("siparisYapKanal hata: {}", e.getMessage(), e);
            GeneralUtil.setOutVariables(cevapKodu, DefConstants.RC_SQL_ERROR, errorType, "ORA",
                errorNum, -1, errorStr, "siparis_yap_kanal: " + e.getMessage());
            return DefConstants.RC_FAIL;
        }
    }

    // =========================================================================
    // siparis_kontrol_ekran: Ekran uzerinden siparis kontrolu
    // =========================================================================
    @Transactional
    public int siparisKontrolEkran(Long belgeId, Long hesapId, List<SiparisIstekDto> istekList,
                                   SiparisCevapDto cevap,
                                   StringBuilder cevapKodu,
                                   StringBuilder errorType,
                                   StringBuilder errorNum,
                                   StringBuilder errorStr) {
        try {
            if (istekList == null || istekList.isEmpty()) {
                GeneralUtil.setOutVariables(cevapKodu, DefConstants.RC_ISTEK_BOS, errorType, "APP",
                    errorNum, -20304, errorStr, "siparis_kontrol_ekran: Istek bos");
                return DefConstants.RC_FAIL;
            }
            SiparisIstekDto istek = istekList.get(0);

            Belge belge = belgeRepository.findById(belgeId)
                .orElseThrow(() -> new RuntimeException("Belge bulunamadi"));
            Hesap hesap = hesapRepository.findById(hesapId)
                .orElseThrow(() -> new RuntimeException("Hesap bulunamadi"));

            return siparisIsle(belge, hesap, istek.getIslemKodu(), istek, cevap,
                cevapKodu, errorType, errorNum, errorStr);
        } catch (Exception e) {
            log.error("siparisKontrolEkran hata: {}", e.getMessage(), e);
            GeneralUtil.setOutVariables(cevapKodu, DefConstants.RC_SQL_ERROR, errorType, "ORA",
                errorNum, -1, errorStr, "siparis_kontrol_ekran: " + e.getMessage());
            return DefConstants.RC_FAIL;
        }
    }

    // =========================================================================
    // siparis_detay_kontrol_ekran: Sadece kalem kontrolu (rollback)
    // =========================================================================
    @Transactional(readOnly = true)
    public int siparisDetayKontrolEkran(Long belgeId, Long hesapId, List<SiparisIstekDto> istekList,
                                        StringBuilder cevapKodu,
                                        StringBuilder errorType,
                                        StringBuilder errorNum,
                                        StringBuilder errorStr) {
        try {
            if (istekList == null || istekList.isEmpty()) {
                GeneralUtil.setOutVariables(cevapKodu, DefConstants.RC_ISTEK_BOS, errorType, "APP",
                    errorNum, -20304, errorStr, "siparis_detay_kontrol_ekran: Istek bos");
                return DefConstants.RC_FAIL;
            }
            SiparisIstekDto istek = istekList.get(0);

            Belge belge = belgeRepository.findById(belgeId)
                .orElseThrow(() -> new RuntimeException("Belge bulunamadi"));
            Hesap hesap = hesapRepository.findById(hesapId)
                .orElseThrow(() -> new RuntimeException("Hesap bulunamadi"));

            List<SiparisKalemKontrolService.SiparisDetayKontrolRecord> sdKontrolCol = new ArrayList<>();
            kalemKontrolService.kontrol(belge, hesap, istek, sdKontrolCol,
                cevapKodu, errorType, errorNum, errorStr);
            return DefConstants.RC_SUCCESS;
        } catch (SiparisKalemKontrolService.SiparisKalemKontrolException e) {
            return DefConstants.RC_FAIL;
        } catch (Exception e) {
            log.error("siparisDetayKontrolEkran hata: {}", e.getMessage(), e);
            GeneralUtil.setOutVariables(cevapKodu, DefConstants.RC_SQL_ERROR, errorType, "ORA",
                errorNum, -1, errorStr, "siparis_detay_kontrol_ekran: " + e.getMessage());
            return DefConstants.RC_FAIL;
        }
    }

    // =========================================================================
    // siparis_onay: Siparis onay islemi
    // =========================================================================
    @Transactional
    public int siparisOnay(Long siparisId, Long siparisHareketId, Long hareketId,
                           Long alacakId, Long vergiNo, String vergiDairesi,
                           String siparisBelgeNo, Long kullaniciId, Integer taksitliMi,
                           StringBuilder cevapKodu,
                           StringBuilder errorType,
                           StringBuilder errorNum,
                           StringBuilder errorStr) {
        try {
            Siparis siparis = siparisRepository.findById(siparisId)
                .orElseThrow(() -> new RuntimeException("Siparis bulunamadi"));
            if (siparis.getSiparisDurumId() == null || siparis.getSiparisDurumId() != DefConstants.ID_ONAY_BEKLIYOR) {
                GeneralUtil.setOutVariables(cevapKodu, DefConstants.RC_SIPARIS_DURUMU_UYUMSUZ, errorType, "APP",
                    errorNum, -20304, errorStr, "siparis_onay: Siparis durumu uygun degil");
                return DefConstants.RC_FAIL;
            }

            SiparisDetayKayit detay = siparisDetayKayitRepository.findBySiparisId(siparisId)
                .orElseThrow(() -> new RuntimeException("Siparis detay bulunamadi"));

            String belgeNo = siparisBelgeNo != null ? siparisBelgeNo
                : uretSiparisBelgeNo(siparisHareketId);
            if (belgeNo != null && siparisDetayKayitRepository.existsBySiparisBelgeNoAndIdNot(belgeNo, detay.getId())) {
                GeneralUtil.setOutVariables(cevapKodu, DefConstants.RC_BELGE_NO_MEVCUT, errorType, "APP",
                    errorNum, -20304, errorStr, "siparis_onay: Belge no zaten kullanilmis: " + belgeNo);
                return DefConstants.RC_FAIL;
            }

            detay.setVergiNo(vergiNo != null ? vergiNo : detay.getVergiNo());
            detay.setVergiDairesi(vergiDairesi != null ? vergiDairesi : detay.getVergiDairesi());
            detay.setSiparisBelgeNo(belgeNo);
            detay.setTaksitliMi(taksitliMi != null ? taksitliMi : detay.getTaksitliMi());
            detay.setGuncellemeZamani(Instant.now());
            detay.setGuncelleyenKullaniciId(kullaniciId);
            siparisDetayKayitRepository.save(detay);

            siparis.setSiparisDurumId(DefConstants.ID_ONAYLANDI);
            siparis.setAlacakId(alacakId);
            siparis.setGuncellemeZamani(Instant.now());
            siparisRepository.save(siparis);

            SiparisHareket sh = siparisHareketRepository.findById(siparisHareketId).orElse(null);
            if (sh != null) {
                sh.setSiparisDurumId(DefConstants.ID_ONAYLANDI);
                sh.setCevapKodu(String.valueOf(DefConstants.RC_SUCCESS));
                sh.setGuncellemeZamani(Instant.now());
                sh.setGuncelleyenKullaniciId(kullaniciId);
                siparisHareketRepository.save(sh);
            }

            return DefConstants.RC_SUCCESS;
        } catch (Exception e) {
            log.error("siparisOnay hata: {}", e.getMessage(), e);
            GeneralUtil.setOutVariables(cevapKodu, DefConstants.RC_SQL_ERROR, errorType, "ORA",
                errorNum, -1, errorStr, "siparis_onay: " + e.getMessage());
            return DefConstants.RC_FAIL;
        }
    }

    // =========================================================================
    // Yardimci metodlar
    // =========================================================================
    private String uretSiparisBelgeNo(Long siparisHareketId) {
        // [ ] siparisRepository'den siparis_durum kontrolu gerekir
        return "SIP" + LocalDate.now().getYear() + String.format("%012d", siparisHareketId);
    }

    private void siparisGuncelle(SiparisCevapDto cevap, Long siparisDurumId, Long hareketDurumId,
                                 Long islemId, Long kullaniciId,
                                 StringBuilder cevapKodu, StringBuilder errorStr) {
        Siparis siparis = siparisRepository.findById(cevap.getSiparisId()).orElse(null);
        if (siparis != null) {
            siparis.setSiparisDurumId(siparisDurumId);
            siparis.setCevapKodu(cevapKodu.toString());
            siparis.setCevapMesaji(errorStr.length() > 500 ? errorStr.substring(0, 500) : errorStr.toString());
            siparis.setIstekDenemeSayisi(siparis.getIstekDenemeSayisi() + 1);
            siparis.setGuncellemeZamani(Instant.now());
            siparisRepository.save(siparis);
        }
    }

    private void siparisSahibiFinansGuncelle(Long siparisSahibiId, Long finansTipi,
                                              BigDecimal siparisToplam, BigDecimal belgeMahsup,
                                              BigDecimal avansMahsup, BigDecimal nakit,
                                              BigDecimal transfer, BigDecimal kesinti,
                                              BigDecimal kalanAlacak, LocalDate nakitTalepTarihi) {
        Optional<SiparisSahibiFinans> mevcut = siparisSahibiFinansRepository
            .findBySiparisSahibiIdAndFinansTipi(siparisSahibiId, finansTipi);

        if (mevcut.isEmpty()) {
            SiparisSahibiFinans finans = new SiparisSahibiFinans();
            finans.setSiparisSahibiId(siparisSahibiId);
            finans.setFinansTipi(finansTipi);
            finans.setSiparisToplamTutari(siparisToplam != null ? siparisToplam : BigDecimal.ZERO);
            finans.setBelgeMahsupTutari(belgeMahsup != null ? belgeMahsup : BigDecimal.ZERO);
            finans.setAvansMahsupTutari(avansMahsup != null ? avansMahsup : BigDecimal.ZERO);
            finans.setNakitSiparisTutari(nakit != null ? nakit : BigDecimal.ZERO);
            finans.setKalanAlacakTutari(kalanAlacak != null ? kalanAlacak : BigDecimal.ZERO);
            finans.setTransfereAktarilanTutar(transfer != null ? transfer : BigDecimal.ZERO);
            finans.setKesintiYapilanTutar(kesinti != null ? kesinti : BigDecimal.ZERO);
            finans.setNakitTalepTarihi(nakitTalepTarihi);
            finans.setTanitimZamani(Instant.now());
            finans.setTanitanKullaniciId(DefConstants.INST_SISTEM_KULLANICI_ID);
            finans.setGuncellemeZamani(Instant.now());
            siparisSahibiFinansRepository.save(finans);
        } else {
            SiparisSahibiFinans finans = mevcut.get();
            if (siparisToplam != null) finans.setSiparisToplamTutari(siparisToplam);
            if (belgeMahsup != null) finans.setBelgeMahsupTutari(belgeMahsup);
            if (avansMahsup != null) finans.setAvansMahsupTutari(avansMahsup);
            if (nakit != null) finans.setNakitSiparisTutari(nakit);
            if (kalanAlacak != null) finans.setKalanAlacakTutari(kalanAlacak);
            if (transfer != null) finans.setTransfereAktarilanTutar(transfer);
            if (kesinti != null) finans.setKesintiYapilanTutar(kesinti);
            finans.setNakitTalepTarihi(nakitTalepTarihi);
            finans.setGuncellemeZamani(Instant.now());
            finans.setGuncelleyenKullaniciId(DefConstants.INST_SISTEM_KULLANICI_ID);
            siparisSahibiFinansRepository.save(finans);
        }
    }

}
