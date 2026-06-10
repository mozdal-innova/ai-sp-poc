package com.acme.ord.service.siparis;

import com.acme.ord.common.Constants;
import com.acme.ord.common.ServiceResult;
import com.acme.ord.domain.dto.*;
import com.acme.ord.domain.entity.*;
import com.acme.ord.domain.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * pck_siparis ana fonksiyonlarinin Java karsiligi.
 * siparis_isle, siparis_yap, siparis_olustur_ws, siparis_yap_kanal
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SiparisService {

    private final BelgeRepository belgeRepository;
    private final HesapRepository hesapRepository;
    private final SiparisRepository siparisRepository;
    private final SiparisSahibiRepository siparisSahibiRepository;
    private final SiparisHareketRepository siparisHareketRepository;
    private final SiparisDetayKayitRepository siparisDetayKayitRepository;
    private final SiparisHareketIslemSdyRepository hareketIslemSdyRepository;
    private final SiparisSahibiFinansRepository siparisSahibiFinansRepository;
    private final AboneRepository aboneRepository;
    private final StokAnalizRepository stokAnalizRepository;
    private final TeslimatTalepRepository teslimatTalepRepository;
    private final SiparisKalemKontrolService kalemKontrolService;

    // =========================================================================
    // siparis_yap: Belge id ile siparis yapar
    // =========================================================================
    public ServiceResult<SiparisCevapDto> siparisYap(Long belgeId, List<SiparisIstekDto> istekList) {
        if (istekList == null || istekList.isEmpty()) {
            return ServiceResult.fail("ISTEK_BOS", "APP", -20304, "siparis_yap: Istek listesi bos");
        }
        SiparisIstekDto istek = istekList.get(0);

        Optional<Belge> belgeOpt = belgeRepository.findById(belgeId);
        if (belgeOpt.isEmpty()) {
            return ServiceResult.fail(Constants.RC_BELGE_BULUNAMADI, "APP", -20304,
                    "siparis_yap: Belge bulunamadi. BelgeId:" + belgeId);
        }

        Belge belge = belgeOpt.get();
        Optional<Hesap> hesapOpt = hesapRepository.findById(belge.getHesapId());
        if (hesapOpt.isEmpty()) {
            return ServiceResult.fail(Constants.RC_HESAP_BULUNAMADI, "APP", -20304,
                    "siparis_yap: Hesap bulunamadi");
        }

        return siparisIsle(belge, hesapOpt.get(), istek.getIslemKodu(), istek);
    }

    // =========================================================================
    // siparis_olustur_ws: Web servis uzerinden siparis olusturur
    // =========================================================================
    public ServiceResult<SiparisCevapDto> siparisOlusturWs(List<SiparisIstekDto> istekList) {
        if (istekList == null || istekList.isEmpty()) {
            return ServiceResult.fail("ISTEK_BOS", "APP", -20304, "siparis_olustur_ws: Istek listesi bos");
        }
        SiparisIstekDto istek = istekList.get(0);

        if (istek.getHesapNo() == null || istek.getHesapNo().isBlank()) {
            return ServiceResult.fail("HESAP_NO_BOS", "APP", -20304, "siparis_olustur_ws: Hesap no bos");
        }

        Optional<Hesap> hesapOpt = hesapRepository.findFirstByHesapNo(istek.getHesapNo());
        if (hesapOpt.isEmpty()) {
            return ServiceResult.fail(Constants.RC_HESAP_BULUNAMADI, "APP", -20304,
                    "siparis_olustur_ws: Hesap bulunamadi. HesapNo:" + istek.getHesapNo());
        }
        Hesap hesap = hesapOpt.get();

        Optional<Belge> belgeOpt = belgeRepository.findByHesapIdAndBelgeNoAndDonemKodu(
                hesap.getId(), istek.getBelgeNo(), istek.getDonemKodu());
        if (belgeOpt.isEmpty()) {
            return ServiceResult.fail(Constants.RC_BELGE_BULUNAMADI, "APP", -20304,
                    "siparis_olustur_ws: Belge bulunamadi");
        }

        return siparisIsle(belgeOpt.get(), hesap, istek.getIslemKodu(), istek);
    }

    // =========================================================================
    // siparis_yap_kanal: Kanal uzerinden siparis yapar (talep no ile)
    // =========================================================================
    public ServiceResult<SiparisCevapDto> siparisYapKanal(String talepNo, List<SiparisIstekDto> istekList) {
        if (istekList == null || istekList.isEmpty()) {
            return ServiceResult.fail("ISTEK_BOS", "APP", -20304, "siparis_yap_kanal: Istek listesi bos");
        }
        SiparisIstekDto istek = istekList.get(0);

        Optional<TeslimatTalep> talepOpt = teslimatTalepRepository.findByTalepNo(talepNo);
        if (talepOpt.isEmpty()) {
            return ServiceResult.fail("TALEP_BULUNAMADI", "APP", -20304,
                    "siparis_yap_kanal: Talep bulunamadi. TalepNo:" + talepNo);
        }
        TeslimatTalep talep = talepOpt.get();

        // [Talep durum kontrolu - c_talep_durum_yeni, c_talep_durum_islemde sabitleri placeholder]

        Optional<Hesap> hesapOpt = hesapRepository.findById(talep.getHesapId());
        if (hesapOpt.isEmpty()) {
            return ServiceResult.fail(Constants.RC_HESAP_BULUNAMADI, "APP", -20304,
                    "siparis_yap_kanal: Hesap bulunamadi");
        }
        Hesap hesap = hesapOpt.get();

        Optional<Belge> belgeOpt = belgeRepository.findByHesapIdAndBelgeNoAndDonemKodu(
                hesap.getId(), istek.getBelgeNo(), istek.getDonemKodu());
        if (belgeOpt.isEmpty()) {
            return ServiceResult.fail(Constants.RC_BELGE_BULUNAMADI, "APP", -20304,
                    "siparis_yap_kanal: Belge bulunamadi");
        }

        ServiceResult<SiparisCevapDto> result = siparisIsle(belgeOpt.get(), hesap, istek.getIslemKodu(), istek);

        if (result.isSuccess()) {
            // Teslimat talep durumunu guncelle
            talep.setGuncellemeZamani(LocalDateTime.now());
            talep.setGuncellleyenKullaniciId(Constants.C_INST_SISTEM_KULLANICI_ID);
            teslimatTalepRepository.save(talep);
        }

        return result;
    }

    // =========================================================================
    // siparis_isle: Ana siparis isleme fonksiyonu
    // =========================================================================
    private ServiceResult<SiparisCevapDto> siparisIsle(Belge belge, Hesap hesap,
                                                        Long islemTipi, SiparisIstekDto istek) {
        Long islemId = istek.getIslemKodu();

        // Resend kontrolu
        if (!Long.valueOf(Constants.C_ISLEMKODU_REVIZE).equals(istek.getIslemKodu())) {
            long resendCount = siparisHareketRepository.countByIslemTarihiAndKurumIdAndStanAndIslemId(
                    istek.getIslemTarihi(), istek.getKurumId(), istek.getStan(), istek.getIslemKodu());
            if (resendCount > 0) {
                return ServiceResult.fail(Constants.RC_RESEND, "APP", -20304, "Resend tespit edildi.");
            }
        }

        // Hizmet no al
        String hizmetNo = sanalHizmetNoAl(hesap);

        // Kalem kontrol
        ServiceResult<List<SiparisKalemDetayiDto>> kalemResult = kalemKontrolService.kontrolEt(belge, hesap, istek);
        if (!kalemResult.isSuccess()) {
            return ServiceResult.fail(kalemResult.getCevapKodu(), kalemResult.getErrorType(),
                    kalemResult.getErrorNum(), kalemResult.getErrorStr());
        }
        List<SiparisKalemDetayiDto> kalemDetList = kalemResult.getData();

        // Siparis sahibi bul veya olustur
        SiparisSahibi siparisSahibi = siparisSahibiRepository
                .findByHesapIdAndMusteriId(hesap.getId(), hesap.getMusteriId())
                .orElse(null);

        if (siparisSahibi == null) {
            siparisSahibi = new SiparisSahibi();
            siparisSahibi.setHesapId(hesap.getId());
            siparisSahibi.setMusteriId(hesap.getMusteriId());
            siparisSahibi.setSirketId(belge.getSirketId());
            siparisSahibi.setTransfereAktarilanTutar(BigDecimal.ZERO);
            siparisSahibi.setKesintiYapilanTutar(BigDecimal.ZERO);
            siparisSahibi.setTanitimZamani(LocalDateTime.now());
            siparisSahibi.setTanitanKullaniciId(Constants.C_INST_SISTEM_KULLANICI_ID);
            siparisSahibi.setGuncellemeZamani(LocalDateTime.now());
            siparisSahibi = siparisSahibiRepository.save(siparisSahibi);
        }

        // Siparis olustur
        Siparis siparis = new Siparis();
        siparis.setSiparisDurumId(Constants.C_ID_ONAY_BEKLIYOR);
        siparis.setSiparisSahibiId(siparisSahibi.getId());
        siparis.setBelgeId(belge.getId());
        siparis.setIstekDenemeSayisi(0);
        siparis.setSiparisTutari(istek.getSiparisToplmTutar());
        siparis.setUygulama(istek.getUygulama());
        siparis.setSiparisReferansNo(
                istek.getSiparisDetayBilgi() != null ? istek.getSiparisDetayBilgi().getSiparisReferansNo() : null);
        siparis.setTanitimZamani(LocalDateTime.now());
        siparis.setTanitanKullaniciId(
                istek.getKullaniciId() != null ? istek.getKullaniciId() : Constants.C_INST_SISTEM_KULLANICI_ID);
        siparis.setGuncellemeZamani(LocalDateTime.now());
        siparis.setIstekHesapBelgeSayisi(istek.getToplamKalemSayisi());
        siparis.setMunferitSiparisTutari(BigDecimal.ZERO);
        siparis.setIslemTarihi(LocalDate.now());
        siparis = siparisRepository.save(siparis);

        // Siparis detay kayit olustur
        SiparisDetayKayit detayKayit = siparisDetayKayitRepository.findBySiparisId(siparis.getId())
                .orElse(null);
        if (detayKayit == null) {
            detayKayit = new SiparisDetayKayit();
            detayKayit.setSiparisId(siparis.getId());
            if (istek.getSiparisDetayBilgi() != null) {
                SiparisIstekDetayDto detayBilgi = istek.getSiparisDetayBilgi();
                detayKayit.setTdkMi(detayBilgi.getTdkMi());
                detayKayit.setVergiHaric(detayBilgi.getVergiHaric());
                detayKayit.setSiparisSebebiId(detayBilgi.getSiparisSebebiId());
                detayKayit.setAciklama(detayBilgi.getAciklama());
                detayKayit.setSiparisBelgeNo(detayBilgi.getSiparisBelgeNo());
                detayKayit.setVergiDairesi(detayBilgi.getVergiDairesi());
                detayKayit.setVergiNo(detayBilgi.getVergiNo());
                detayKayit.setSebepBildirimNo(detayBilgi.getSebepBildirimNo());
                detayKayit.setSebepPersonelSicilNo(detayBilgi.getSebepKullaniciSicilNo());
                detayKayit.setSebepBayiKodu(detayBilgi.getSebepBayiKodu());
                detayKayit.setSebepCagriMerkezi(
                        detayBilgi.getSebepCagriMerkezi() != null
                                ? detayBilgi.getSebepCagriMerkezi().substring(0,
                                        Math.min(20, detayBilgi.getSebepCagriMerkezi().length()))
                                : null);
                detayKayit.setErpGonderilecekMi(detayBilgi.getErpGonderilecekMi());
                detayKayit.setErpIban(detayBilgi.getErpIban());
                detayKayit.setErpAliciAdSoyad(detayBilgi.getErpAliciAdSoyad());
                detayKayit.setErpAliciTelefon(detayBilgi.getErpAliciTelefon());
                detayKayit.setErpAciklama(detayBilgi.getErpAciklama());
                detayKayit.setCid(detayBilgi.getCid());
                detayKayit.setTemsilciAdiSoyadi(detayBilgi.getTemsilciAdiSoyadi());
                detayKayit.setTemsilciSicilNo(detayBilgi.getTemsilciSicilNo());
                detayKayit.setIslemTarihi(detayBilgi.getIslemTarihi());
                detayKayit.setPortalKullaniciKodu(detayBilgi.getPortalKullaniciKodu());
                detayKayit.setPortalKullaniciAdsoyad(detayBilgi.getPortalKullaniciAdSoyad());
            }
            detayKayit.setOrgBelgeDurum(belge.getBelgeDurum());
            detayKayit.setTaksitliMi(istek.getTaksitliMi());
            detayKayit.setTanitimZamani(LocalDateTime.now());
            detayKayit.setTanitanKullaniciId(Constants.C_INST_SISTEM_KULLANICI_ID);
            detayKayit.setGuncellemeZamani(LocalDateTime.now());
            siparisDetayKayitRepository.save(detayKayit);
        }

        // Siparis hareket olustur
        SiparisHareket hareket = new SiparisHareket();
        hareket.setIslemTarihi(istek.getIslemTarihi());
        hareket.setKurumId(istek.getKurumId());
        hareket.setStan(istek.getStan());
        hareket.setSiparisId(siparis.getId());
        hareket.setIslemId(islemId);
        hareket.setSiparisTutari(istek.getSiparisToplmTutar());
        hareket.setBusinessId(istek.getBusinessId());
        hareket.setConversationId(istek.getConversationId());
        hareket.setTanitimZamani(LocalDateTime.now());
        hareket.setTanitanKullaniciId(Constants.C_INST_SISTEM_KULLANICI_ID);
        hareket.setGuncellemeZamani(LocalDateTime.now());
        hareket.setDenemeSayisi(0);
        hareket = siparisHareketRepository.save(hareket);

        // Siparis hareket islem detaylari olustur
        for (SiparisKalemDetayiDto kd : kalemDetList) {
            SiparisHareketIslemSdy sdy = new SiparisHareketIslemSdy();
            sdy.setSiparisHareketId(hareket.getId());
            sdy.setSiparisDetayId(kd.getSiparisDetayId());
            sdy.setKalemTipiId(kd.getKalemTipiId());
            sdy.setTedarikciId(kd.getTedarikciId());
            sdy.setRevizeTutari(kd.getRevizeTutari());
            sdy.setKdvOrani(kd.getKdvOrani());
            sdy.setKdvTutari(kd.getKdvTutari());
            sdy.setOtvOrani(kd.getOtvOrani());
            sdy.setOtvTutari(kd.getOtvTutari());
            sdy.setUrunSeriNo(kd.getUrunSeriNo());
            sdy.setPaketTipiId(kd.getPaketTipiId());
            sdy.setTaksitliSatisId(kd.getTaksitliSatisId());
            sdy.setTaksitNo(kd.getTaksitNo());
            sdy.setTanitimZamani(LocalDateTime.now());
            sdy.setTanitanKullaniciId(Constants.C_INST_SISTEM_KULLANICI_ID);
            sdy.setGuncellemeZamani(LocalDateTime.now());
            hareketIslemSdyRepository.save(sdy);
        }

        // Belge guncelle
        String belgeDurum = "PARTIAL";
        if (belge.getKalanTutar().subtract(istek.getSiparisToplmTutar()).compareTo(BigDecimal.ZERO) <= 0) {
            belgeDurum = "REVIZE_ILE_KAPANMIS";
        }
        belgeRepository.updateSiparisTutar(belge.getId(), istek.getSiparisToplmTutar(),
                belgeDurum, LocalDateTime.now(), Constants.C_INST_SISTEM_KULLANICI_ID);

        // Cevap olustur
        SiparisCevapDto cevap = new SiparisCevapDto();
        cevap.setStokAnalizId(istek.getStokAnalizId());
        cevap.setHesapId(hesap.getId());
        cevap.setMusteriId(hesap.getMusteriId());
        cevap.setSiparisSahibiId(siparisSahibi.getId());
        cevap.setSiparisId(siparis.getId());
        cevap.setSiparisHareketId(hareket.getId());
        cevap.setSiparisDetayId(detayKayit.getId());
        cevap.setIslemId(islemId);
        cevap.setSiparisTutari(istek.getSiparisToplmTutar());
        cevap.setHesapNo(hesap.getHesapNo());

        return ServiceResult.success(cevap);
    }

    /**
     * sanal_hizmet_no_al: Sanal hesap icin hizmet no bulur.
     */
    private String sanalHizmetNoAl(Hesap hesap) {
        if (hesap.getSanalMi() != null && hesap.getSanalMi() == Constants.C_EVET) {
            return aboneRepository.findFirstByHesapId(hesap.getId())
                    .map(Abone::getHizmetNo)
                    .orElse(hesap.getHesapNo());
        }
        return hesap.getHesapNo();
    }

    /**
     * siparis_belge_no_uret: Onay bekleyen siparisler icin belge no uretir.
     */
    public String siparisBelgeNoUret(Long siparisHareketId) {
        // [siparis_durum_id kontrolu ve belge no uretim - cross-package bagimlilik]
        return "SIP" + LocalDate.now().getYear()
                + String.format("%012d", siparisHareketId);
    }
}
