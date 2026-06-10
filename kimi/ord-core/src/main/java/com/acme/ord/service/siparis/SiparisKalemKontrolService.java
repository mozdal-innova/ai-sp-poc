package com.acme.ord.service.siparis;

import com.acme.ord.common.constants.DefConstants;
import com.acme.ord.common.util.GeneralUtil;
import com.acme.ord.domain.dto.SiparisIstekDto;
import com.acme.ord.domain.dto.SiparisIstekFdyDto;
import com.acme.ord.domain.dto.SiparisKalemDetayiDto;
import com.acme.ord.domain.entity.Belge;
import com.acme.ord.domain.entity.Hesap;
import com.acme.ord.domain.entity.SiparisDetay;
import com.acme.ord.domain.entity.SiparisHareket;
import com.acme.ord.domain.repository.SiparisDetayRepository;
import com.acme.ord.domain.repository.SiparisHareketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SiparisKalemKontrolService {

    private static final BigDecimal TOLERANS_TOPLAM_LIMIT = new BigDecimal("0.02");
    private static final long KALEM_KDV = 3L;
    private static final long KALEM_OTV = 4L;
    private static final long KALEM_MAHSUP = 10L;
    private static final long KALEM_TEMLIK = 26L;

    private final SiparisDetayRepository siparisDetayRepository;
    private final SiparisHareketRepository siparisHareketRepository;

    public static class SiparisKalemKontrolException extends RuntimeException {
        public final String rc;
        public SiparisKalemKontrolException(String rc, String message) {
            super(message);
            this.rc = rc;
        }
    }

    @Transactional(readOnly = true)
    public List<SiparisKalemDetayiDto> kontrol(Belge belge, Hesap hesap, SiparisIstekDto istek,
                                                List<SiparisDetayKontrolRecord> sdKontrolCol,
                                                StringBuilder cevapKodu,
                                                StringBuilder errorType,
                                                StringBuilder errorNum,
                                                StringBuilder errorStr) {
        String infoMsj = "";
        try {
            // Urun seri no kontrol
            siparisDetayUrunSerinoKontrol(belge, istek);

            List<SiparisKalemDetayiDto> kalemDetList = istek.getKalemDetayList();
            istek.setSiparisToplamTutar(istek.getSiparisToplamTutar().setScale(2, RoundingMode.HALF_UP));

            if (istek.getWsDetayList() != null && !istek.getWsDetayList().isEmpty()) {
                kalemDetList = new ArrayList<>();
                for (SiparisIstekFdyDto fdy : istek.getWsDetayList()) {
                    infoMsj = " Belge:" + belge.getId() + ",Urun:" + fdy.getUrunSeriNo() +
                              ",KalemTipi:" + fdy.getKalemTipiId() + ",TedarikciId:" + fdy.getTedarikciId();

                    // Paket tipi kontrol stub - [ ] tam implemente edilecek
                    // [Paket tipi kontrol]

                    Optional<SiparisDetay> detayOpt = siparisDetayRepository.findMatchingDetail(
                        belge.getId(), fdy.getKalemTipiId(), fdy.getTedarikciId(),
                        fdy.getKdvOrani(), fdy.getOtvOrani(), String.valueOf(fdy.getUrunSeriNo()),
                        fdy.getPaketTipiId(), fdy.getBayiKodu(), fdy.getKampanyaKodu(),
                        fdy.getTaksitliSatisId(), fdy.getTaksitNo()
                    );

                    if (detayOpt.isEmpty()) {
                        throw new SiparisKalemKontrolException(DefConstants.RC_SIPARIS_DETAY_YOK,
                            "Detay bulunamadi" + infoMsj);
                    }

                    SiparisDetay sd = detayOpt.get();
                    SiparisKalemDetayiDto kalem = new SiparisKalemDetayiDto();
                    kalem.setSiparisDetayId(sd.getId());
                    kalem.setKalemTipiId(sd.getKalemTipiId());
                    kalem.setUrunGrubuId(null); // kalem_tipi.urun_grubu_id - [ ] join gerekir
                    kalem.setTedarikciId(sd.getTedarikciId());
                    kalem.setPaketTipiId(sd.getPaketTipi());
                    kalem.setOrjinalTutar(sd.getOrjinalTutar());
                    kalem.setSiparisDuzeltmeTutari(sd.getSiparisDuzeltmeTutar());
                    kalem.setRevizeTutari(fdy.getSiparisTutari());
                    kalem.setKdvOrani(sd.getKdvOrani());
                    kalem.setKdvTutari(BigDecimal.ZERO);
                    kalem.setOtvOrani(sd.getOtvOrani());
                    kalem.setOtvTutari(BigDecimal.ZERO);
                    kalem.setDuzeltmeYapilabilirMi(null); // [ ] kalem_tipi join
                    kalem.setCarpan(null); // [ ] kalem_tipi join
                    kalem.setUrunSeriNo(sd.getUrunSeriNo());
                    kalem.setTaksitliSatisId(sd.getTaksitliSatisId());
                    kalem.setTaksitNo(sd.getTaksitNo());

                    Long kampanyaId = sd.getKampanyaId();
                    if (kalem.getUrunGrubuId() != null && kalem.getUrunGrubuId() == KALEM_TEMLIK) {
                        if (fdy.getKampanyaKodu() == null) {
                            throw new SiparisKalemKontrolException(DefConstants.RC_KALEM_KAMPANYA_ZORUNLU,
                                "Kalem Tipi:" + KALEM_TEMLIK + " icin Kampanya Kodu alani zorunludur.");
                        }
                        if (kampanyaId == null) {
                            sdKontrolCol.add(new SiparisDetayKontrolRecord(sd.getId(), fdy.getKampanyaKodu()));
                        }
                    }
                    kalemDetList.add(kalem);
                }
            }

            // Dijital prepaid hesap kontrolu
            if (hesap.getHesapTipiId() != null && hesap.getHesapTipiId() == DefConstants.HESAP_TIPI_DIJITAL_PREPAID) {
                throw new SiparisKalemKontrolException(DefConstants.RC_DIJITAL_PREPAID, "Dijital prepaid hesap");
            }

            // TDK modu: detay yoksa siparis_duzeltme_tutar <> 0 olanlari topla
            if (istek.getSiparisDetayBilgi() != null && Integer.valueOf(1).equals(istek.getSiparisDetayBilgi().getTdkMi())
                && (kalemDetList == null || kalemDetList.isEmpty())) {
                // [ ] TDK modunda siparis_detay'dan duzeltme_tutar <> 0 olanlari topla
                // [ ] kalem_tipi.urun_grubu_id not in (c_kalem_otv, c_kalem_kdv)
                throw new SiparisKalemKontrolException(DefConstants.RC_SIPARIS_DETAY_BOS,
                    "TDK modu detay listesi bos - implementasyon devam ediyor");
            }

            if (kalemDetList == null || kalemDetList.isEmpty()) {
                throw new SiparisKalemKontrolException(DefConstants.RC_SIPARIS_DETAY_BOS, "Detay listesi bos");
            }

            BigDecimal detaySiparisTutari = BigDecimal.ZERO;
            Long kdvId = null;
            Long otvId = null;
            Long mhspId = null;

            for (SiparisKalemDetayiDto kalem : kalemDetList) {
                if (kalem.getCarpan() != null && kalem.getCarpan() < 0) {
                    kalem.setRevizeTutari(kalem.getRevizeTutari().abs().negate());
                }
                Long urunGrubuId = kalem.getUrunGrubuId();
                if (urunGrubuId != null && urunGrubuId == KALEM_KDV) {
                    if (kdvId != null) {
                        throw new SiparisKalemKontrolException(DefConstants.RC_BIRDEN_FZLA_KDV_VAR,
                            "Birden fazla KDV detayi" + infoMsj);
                    }
                    kdvId = kalem.getSiparisDetayId();
                } else if (urunGrubuId != null && urunGrubuId == KALEM_OTV) {
                    if (otvId != null) {
                        throw new SiparisKalemKontrolException(DefConstants.RC_BIRDEN_FZLA_OTV_VAR,
                            "Birden fazla OTV detayi" + infoMsj);
                    }
                    otvId = kalem.getSiparisDetayId();
                } else if (urunGrubuId != null && urunGrubuId == KALEM_MAHSUP) {
                    mhspId = kalem.getSiparisDetayId();
                } else {
                    detaySiparisTutari = detaySiparisTutari.add(kalem.getRevizeTutari().setScale(2, RoundingMode.HALF_UP));
                }
                if (Integer.valueOf(0).equals(kalem.getDuzeltmeYapilabilirMi())) {
                    if (kalem.getRevizeTutari().abs().compareTo(kalem.getSiparisDuzeltmeTutari().abs()) > 0) {
                        infoMsj = " DetayId:" + kalem.getSiparisDetayId();
                        throw new SiparisKalemKontrolException(DefConstants.RC_DETAY_DUZELTME_UYUMSUZ,
                            "Detay duzeltme tutar uyumsuz" + infoMsj);
                    }
                }
                if (kalem.getCarpan() != null && kalem.getCarpan() > 0 && kalem.getRevizeTutari().compareTo(BigDecimal.ZERO) < 0) {
                    throw new SiparisKalemKontrolException(DefConstants.RC_SIPARIS_NEGATIF,
                        "Negatif siparis" + infoMsj);
                }
                if (kalem.getCarpan() != null && kalem.getCarpan() < 0 && kalem.getRevizeTutari().compareTo(BigDecimal.ZERO) > 0) {
                    throw new SiparisKalemKontrolException(DefConstants.RC_KALEM_CARPAN_NEG,
                        "Kalem carpan negatif" + infoMsj);
                }
            }

            if (istek.getSiparisToplamTutar().setScale(2, RoundingMode.HALF_UP)
                    .subtract(detaySiparisTutari.setScale(2, RoundingMode.HALF_UP)).abs()
                    .compareTo(TOLERANS_TOPLAM_LIMIT) > 0) {
                infoMsj = " Siparis toplam tutar:" + istek.getSiparisToplamTutar();
                throw new SiparisKalemKontrolException(DefConstants.RC_SIPARIS_TUTARSIZ,
                    "Tutarsiz siparis" + infoMsj);
            }

            // [ ] belge_sahibi kontrolu
            // [ ] munferit revize/islem kontrolu (siparis_hareket, siparis_hareket_detay, siparis_hareket_tahsilat_bilgi)
            // [ ] tedarikci toplam kontrolu
            // [ ] Onay islemi: otomatik talep iptali (pck_talep_yonetimi)
            // [ ] Tekrarli token kontrolu

            istek.setKalemDetayList(kalemDetList);
            return kalemDetList;

        } catch (SiparisKalemKontrolException e) {
            GeneralUtil.setOutVariables(cevapKodu, e.rc, errorType, "APP", errorNum, -20304, errorStr,
                GeneralUtil.getIslemCevapAck(e.rc) + " - " + e.getMessage());
            throw e;
        } catch (Exception e) {
            GeneralUtil.setOutVariables(cevapKodu, DefConstants.RC_SQL_ERROR, errorType, "ORA",
                errorNum, -1, errorStr, "siparis_kalem_kontrol: " + e.getMessage() + " " + infoMsj);
            throw e;
        }
    }

    private void siparisDetayUrunSerinoKontrol(Belge belge, SiparisIstekDto istek) {
        Instant min = LocalDate.of(2014, 6, 22).atStartOfDay().toInstant(java.time.ZoneOffset.UTC);
        Instant max = LocalDate.of(2020, 2, 29).atStartOfDay().toInstant(java.time.ZoneOffset.UTC);
        Instant belgeTarih = belge.getTanitimZaman();
        if (belgeTarih == null || belgeTarih.isAfter(max) || belgeTarih.isBefore(min)) {
            return;
        }
        if (istek.getWsDetayList() == null || istek.getWsDetayList().isEmpty()) {
            return;
        }
        for (SiparisIstekFdyDto fdy : istek.getWsDetayList()) {
            try {
                long seriNo = Long.parseLong(fdy.getUrunSeriNo());
                fdy.setUrunSeriNo(String.valueOf(seriNo));
            } catch (NumberFormatException ignored) {
            }
        }
    }

    public record SiparisDetayKontrolRecord(Long id, Long istKmpKodu) {}

}
