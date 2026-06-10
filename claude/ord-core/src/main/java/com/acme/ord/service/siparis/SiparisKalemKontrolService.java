package com.acme.ord.service.siparis;

import com.acme.ord.common.Constants;
import com.acme.ord.common.ServiceResult;
import com.acme.ord.domain.dto.SiparisIstekDto;
import com.acme.ord.domain.dto.SiparisIstekFdyDto;
import com.acme.ord.domain.dto.SiparisKalemDetayiDto;
import com.acme.ord.domain.entity.Belge;
import com.acme.ord.domain.entity.Hesap;
import com.acme.ord.domain.entity.SiparisDetay;
import com.acme.ord.domain.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * siparis_kalem_kontrol fonksiyonunun Java karsiligi.
 * Siparis kalemleri uzerindeki validasyon ve is kurallarini uygular.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SiparisKalemKontrolService {

    private final SiparisDetayRepository siparisDetayRepository;
    private final KalemTipiRepository kalemTipiRepository;
    private final PaketTipiRepository paketTipiRepository;
    private final BelgeSahibiRepository belgeSahibiRepository;
    private final SiparisHareketRepository siparisHareketRepository;
    private final SiparisHareketTahsilatBilgiRepository tahsilatBilgiRepository;

    /**
     * Kalem kontrol: ws_detay_list veya kalem_detay_list uzerinden kontrol yapar.
     * Basarili ise kalem listesini istek DTO'suna set eder.
     */
    public ServiceResult<List<SiparisKalemDetayiDto>> kontrolEt(Belge belge, Hesap hesap, SiparisIstekDto istek) {

        // Urun seri no duzeltme (tarih araligi kontrolu)
        siparisDetayUrunSerinoKontrol(belge, istek);

        istek.setSiparisToplmTutar(istek.getSiparisToplmTutar().setScale(2, RoundingMode.HALF_UP));

        List<SiparisKalemDetayiDto> kalemDetList = new ArrayList<>();
        Map<Long, Long> sdKampanyaKoduMap = new HashMap<>(); // siparis_detay_id -> kampanya_kodu

        // ws_detay_list varsa kalem detay olustur
        if (istek.getWsDetayList() != null && !istek.getWsDetayList().isEmpty()) {
            for (SiparisIstekFdyDto fdy : istek.getWsDetayList()) {
                String infoMsj = " Belge:" + belge.getId() + ",Urun:" + fdy.getUrunSeriNo()
                        + ",KalemTipi:" + fdy.getKalemTipiId() + ",TedarikciId:" + fdy.getTedarikciId();

                // Paket tipi kontrolu
                if (fdy.getPaketTipiId() != null) {
                    if (!paketTipiRepository.existsById(fdy.getPaketTipiId())) {
                        return ServiceResult.fail(Constants.RC_PAKET_TIPI_HATALI, "APP", -20304,
                                "Hatali Paket Tipi ID: " + fdy.getPaketTipiId());
                    }
                }

                // siparis_detay + kalem_tipi join sorgusu
                List<SiparisDetay> detaylar = siparisDetayRepository.findByBelgeId(belge.getId());
                SiparisDetay matchedDetay = findMatchingDetay(detaylar, belge, fdy);

                if (matchedDetay == null) {
                    return ServiceResult.fail(Constants.RC_SIPARIS_DETAY_YOK, "APP", -20304,
                            "Siparis detay bulunamadi." + infoMsj);
                }

                var kalemTipi = kalemTipiRepository.findById(matchedDetay.getKalemTipiId()).orElse(null);
                if (kalemTipi == null) {
                    return ServiceResult.fail(Constants.RC_SIPARIS_DETAY_YOK, "APP", -20304,
                            "Kalem tipi bulunamadi." + infoMsj);
                }

                SiparisKalemDetayiDto kalemDetay = new SiparisKalemDetayiDto();
                kalemDetay.setSiparisDetayId(matchedDetay.getId());
                kalemDetay.setKalemTipiId(matchedDetay.getKalemTipiId());
                kalemDetay.setUrunGrubuId(kalemTipi.getUrunGrubuId());
                kalemDetay.setTedarikciId(matchedDetay.getTedarikciId());
                kalemDetay.setPaketTipiId(fdy.getPaketTipiId() != null ? fdy.getPaketTipiId()
                        : matchedDetay.getPaketTipi());
                kalemDetay.setOrjinalTutar(matchedDetay.getOrjinalTutar());
                kalemDetay.setSiparisDuzeltmeTutari(matchedDetay.getSiparisDuzeltmeTutar());
                kalemDetay.setRevizeTutari(fdy.getSiparisTutari());
                kalemDetay.setKdvOrani(matchedDetay.getKdvOrani());
                kalemDetay.setKdvTutari(BigDecimal.ZERO);
                kalemDetay.setOtvOrani(matchedDetay.getOtvOrani());
                kalemDetay.setOtvTutari(BigDecimal.ZERO);
                kalemDetay.setDuzeltmeYapilabilirMi(kalemTipi.getDuzeltmeYapilabilirMi());
                kalemDetay.setCarpan(kalemTipi.getCarpan());
                kalemDetay.setUrunSeriNo(matchedDetay.getUrunSeriNo());
                kalemDetay.setTaksitliSatisId(matchedDetay.getTaksitliSatisId());
                kalemDetay.setTaksitNo(matchedDetay.getTaksitNo());

                // Temlik kalem kontrolu
                if (kalemDetay.getUrunGrubuId() != null
                        && kalemDetay.getUrunGrubuId() == Constants.C_KALEM_TEMLIK) {
                    if (fdy.getKampanyaKodu() == null) {
                        return ServiceResult.fail(Constants.RC_KALEM_KAMPANYA_ZORUNLU, "APP", -20304,
                                "Kalem Tipi:" + Constants.C_KALEM_TEMLIK + " icin Kampanya Kodu alani zorunludur.");
                    }
                    if (matchedDetay.getKampanyaId() == null) {
                        sdKampanyaKoduMap.put(matchedDetay.getId(), fdy.getKampanyaKodu());
                    }
                }

                kalemDetList.add(kalemDetay);
            }
        }

        // Dijital prepaid hesap kontrolu
        if (belge.getHesapTipiId() != null
                && belge.getHesapTipiId().equals(Constants.C_HESAP_TIPI_DIJITAL_PREPAID)) {
            return ServiceResult.fail(Constants.RC_DIJITAL_PREPAID, "APP", -20304,
                    "Dijital prepaid hesap icin siparis yapilamaz.");
        }

        // TDK ise ve kalem listesi bos, tum kalemleri getir
        if (istek.getSiparisDetayBilgi() != null
                && Integer.valueOf(1).equals(istek.getSiparisDetayBilgi().getTdkMi())
                && kalemDetList.isEmpty()) {
            List<SiparisDetay> tdkKalemler = siparisDetayRepository.findTdkKalemler(
                    belge.getId(),
                    List.of((long) Constants.C_KALEM_KDV, (long) Constants.C_KALEM_OTV));
            for (SiparisDetay sd : tdkKalemler) {
                var kt = kalemTipiRepository.findById(sd.getKalemTipiId()).orElse(null);
                if (kt == null) continue;
                SiparisKalemDetayiDto dto = new SiparisKalemDetayiDto();
                dto.setSiparisDetayId(sd.getId());
                dto.setKalemTipiId(sd.getKalemTipiId());
                dto.setUrunGrubuId(kt.getUrunGrubuId());
                dto.setTedarikciId(sd.getTedarikciId());
                dto.setPaketTipiId(sd.getPaketTipi());
                dto.setOrjinalTutar(sd.getOrjinalTutar());
                dto.setSiparisDuzeltmeTutari(sd.getSiparisDuzeltmeTutar());
                dto.setRevizeTutari(BigDecimal.ZERO);
                dto.setKdvOrani(sd.getKdvOrani());
                dto.setKdvTutari(BigDecimal.ZERO);
                dto.setOtvOrani(sd.getOtvOrani());
                dto.setOtvTutari(BigDecimal.ZERO);
                dto.setDuzeltmeYapilabilirMi(kt.getDuzeltmeYapilabilirMi());
                dto.setCarpan(kt.getCarpan());
                dto.setUrunSeriNo(sd.getUrunSeriNo());
                dto.setTaksitliSatisId(sd.getTaksitliSatisId());
                dto.setTaksitNo(sd.getTaksitNo());
                kalemDetList.add(dto);
            }
        }

        if (kalemDetList.isEmpty()) {
            return ServiceResult.fail(Constants.RC_SIPARIS_DETAY_BOS, "APP", -20304,
                    "Siparis detay listesi bos.");
        }

        // Kalem validasyonlari
        BigDecimal detaySiparisTutari = BigDecimal.ZERO;
        Long kdvId = null;
        Long otvId = null;

        for (SiparisKalemDetayiDto kd : kalemDetList) {
            if (kd.getCarpan() != null && kd.getCarpan() < 0 && kd.getRevizeTutari() != null) {
                kd.setRevizeTutari(kd.getRevizeTutari().abs().negate());
            }

            long urunGrubuId = kd.getUrunGrubuId() != null ? kd.getUrunGrubuId() : 0;
            if (urunGrubuId == Constants.C_KALEM_KDV) {
                if (kdvId != null) {
                    return ServiceResult.fail(Constants.RC_BIRDEN_FZLA_KDV_VAR, "APP", -20304,
                            "Birden fazla KDV detayi var.");
                }
                kdvId = kd.getSiparisDetayId();
            } else if (urunGrubuId == Constants.C_KALEM_OTV) {
                if (otvId != null) {
                    return ServiceResult.fail(Constants.RC_BIRDEN_FZLA_OTV_VAR, "APP", -20304,
                            "Birden fazla OTV detayi var.");
                }
                otvId = kd.getSiparisDetayId();
            } else if (urunGrubuId != Constants.C_KALEM_MAHSUP) {
                detaySiparisTutari = detaySiparisTutari.add(
                        kd.getRevizeTutari().setScale(2, RoundingMode.HALF_UP));
            }

            // Duzeltme tutar kontrolu
            if (kd.getDuzeltmeYapilabilirMi() != null && kd.getDuzeltmeYapilabilirMi() == 0) {
                if (kd.getRevizeTutari().abs().compareTo(kd.getSiparisDuzeltmeTutari().abs()) > 0) {
                    return ServiceResult.fail(Constants.RC_DETAY_DUZELTME_UYUMSUZ, "APP", -20304,
                            "Detay duzeltme tutar uyumsuzlugu. DetayId:" + kd.getSiparisDetayId());
                }
            }

            // Negatif siparis kontrolu
            if (kd.getCarpan() != null && kd.getCarpan() > 0
                    && kd.getRevizeTutari().compareTo(BigDecimal.ZERO) < 0) {
                return ServiceResult.fail(Constants.RC_SIPARIS_NEGATIF, "APP", -20304,
                        "Negatif siparis.");
            }
            if (kd.getCarpan() != null && kd.getCarpan() < 0
                    && kd.getRevizeTutari().compareTo(BigDecimal.ZERO) > 0) {
                return ServiceResult.fail(Constants.RC_KALEM_CARPAN_NEG, "APP", -20304,
                        "Kalem carpan negatif.");
            }
        }

        // Toplam tutar tolerans kontrolu
        BigDecimal fark = istek.getSiparisToplmTutar().setScale(2, RoundingMode.HALF_UP)
                .subtract(detaySiparisTutari.setScale(2, RoundingMode.HALF_UP)).abs();
        if (fark.compareTo(BigDecimal.valueOf(Constants.C_TOLERANS_TOPLAM_LIMIT)) > 0) {
            return ServiceResult.fail(Constants.RC_SIPARIS_TUTARSIZ, "APP", -20304,
                    "Siparis tutarsiz. Toplam tutar:" + istek.getSiparisToplmTutar());
        }

        // Belge sahibi kontrolu
        long belgeSahibiCount = belgeSahibiRepository.countByBelgeIdAndHesapId(belge.getId(), hesap.getId());
        if (belgeSahibiCount == 0) {
            return ServiceResult.fail(Constants.RC_BELGE_SAHIBI_YOK, "APP", -20304,
                    "Belge sahibi bulunamadi.");
        }

        // Munferit revize kontrolu (TDK degilse)
        if (istek.getSiparisDetayBilgi() == null
                || !Integer.valueOf(1).equals(istek.getSiparisDetayBilgi().getTdkMi())) {

            List<Long> siparisDetayIds = kalemDetList.stream()
                    .filter(k -> k.getUrunGrubuId() != null
                            && k.getUrunGrubuId() != Constants.C_KALEM_OTV
                            && k.getUrunGrubuId() != Constants.C_KALEM_KDV)
                    .map(SiparisKalemDetayiDto::getSiparisDetayId)
                    .collect(Collectors.toList());

            if (!siparisDetayIds.isEmpty()) {
                List<Long> hareketIds = siparisHareketRepository.findHareketIdsByBelgeAndIslemKodlari(
                        belge.getId(),
                        List.of(Constants.C_AMOUNT_CORRECTION, Constants.C_ISLEM_MUSTERI_MEMNUNIYETI,
                                Constants.C_ISLEM_MAKAM_ONAYI),
                        Constants.C_NOTSEND,
                        siparisDetayIds);

                if (hareketIds != null && !hareketIds.isEmpty()) {
                    long tahsilatCount = tahsilatBilgiRepository.countByHareketIdsAndTipiAndDetayIds(
                            hareketIds, 4L, siparisDetayIds);
                    if (tahsilatCount > 0) {
                        return ServiceResult.fail(Constants.RC_SIPARIS_MUNFERIT_DAHIL, "APP", -20304,
                                "Munferit islem yapilmis.");
                    }
                    return ServiceResult.fail(Constants.RC_SIPARIS_MUNFERIT_REVIZE_VAR, "APP", -20304,
                            "Munferit revize var.");
                }
            }
        }

        // Tedarikci toplam kontrolu
        List<Long> tedarikciIds = kalemDetList.stream()
                .map(SiparisKalemDetayiDto::getTedarikciId)
                .distinct()
                .collect(Collectors.toList());
        BigDecimal tedarikciToplam = siparisDetayRepository.sumTedarikciToplam(belge.getId(), tedarikciIds);
        if (tedarikciToplam != null
                && detaySiparisTutari.setScale(2, RoundingMode.HALF_UP)
                        .subtract(tedarikciToplam.setScale(2, RoundingMode.HALF_UP))
                        .compareTo(BigDecimal.ZERO) > 0) {
            return ServiceResult.fail(Constants.RC_SIPARIS_TUTARSIZ, "APP", -20304,
                    "Siparis tutarsiz (tedarikci toplam).");
        }

        // Kampanya kodu guncellemelerini istek'e set et
        istek.setKalemDetayList(kalemDetList);

        // Kampanya kodu update'lerini kaydet
        LocalDateTime now = LocalDateTime.now();
        for (Map.Entry<Long, Long> entry : sdKampanyaKoduMap.entrySet()) {
            siparisDetayRepository.updateKampanyaId(entry.getKey(), entry.getValue(),
                    now, Constants.C_INST_SISTEM_KULLANICI_ID);
        }

        return ServiceResult.success(kalemDetList);
    }

    private SiparisDetay findMatchingDetay(List<SiparisDetay> detaylar, Belge belge, SiparisIstekFdyDto fdy) {
        for (SiparisDetay sd : detaylar) {
            if (!sd.getKalemTipiId().equals(fdy.getKalemTipiId())) continue;
            if (!sd.getTedarikciId().equals(fdy.getTedarikciId())) continue;
            if (sd.getKdvOrani().compareTo(fdy.getKdvOrani()) != 0) continue;
            if (sd.getOtvOrani().compareTo(fdy.getOtvOrani()) != 0) continue;
            if (!Objects.equals(String.valueOf(sd.getUrunSeriNo()), String.valueOf(fdy.getUrunSeriNo()))) continue;

            // Sirket bazli paket tipi kontrolu
            if (belge.getSirketId() == Constants.C_COMPANY_DIJITAL) {
                if (fdy.getPaketTipiId() != null && !fdy.getPaketTipiId().equals(sd.getPaketTipi())) continue;
            }

            // Bayi kodu kontrolu
            if (fdy.getBayiKodu() != null && !fdy.getBayiKodu().equals(sd.getBayiKodu())) continue;

            // Kampanya kontrolu
            if (sd.getKampanyaId() != null && !sd.getKampanyaId().equals(fdy.getKampanyaKodu())) continue;

            // Taksit kontrolu
            if (fdy.getTaksitNo() != null && !fdy.getTaksitNo().equals(sd.getTaksitNo())) continue;
            if (sd.getTaksitliSatisId() != null && !sd.getTaksitliSatisId().equals(fdy.getTaksitliSatisId()))
                continue;

            return sd;
        }
        return null;
    }

    /**
     * siparis_detay_urun_serino_kntrl: Belirli tarih araligi icin urun seri no duzeltmesi.
     */
    private void siparisDetayUrunSerinoKontrol(Belge belge, SiparisIstekDto istek) {
        LocalDate startDate = LocalDate.of(2014, 6, 22);
        LocalDate endDate = LocalDate.of(2020, 2, 29);

        if (belge.getTanitimZaman() == null) return;
        LocalDate tanitimDate = belge.getTanitimZaman().toLocalDate();
        if (tanitimDate.isAfter(endDate) || tanitimDate.isBefore(startDate)) return;

        if (istek.getWsDetayList() == null || istek.getWsDetayList().isEmpty()) return;

        for (SiparisIstekFdyDto fdy : istek.getWsDetayList()) {
            try {
                long numVal = Long.parseLong(fdy.getUrunSeriNo());
                fdy.setUrunSeriNo(String.valueOf(numVal));
            } catch (NumberFormatException e) {
                // value_error gibi - sessizce devam et
            }
        }
    }
}
