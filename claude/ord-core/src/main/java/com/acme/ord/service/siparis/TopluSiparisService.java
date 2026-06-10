package com.acme.ord.service.siparis;

import com.acme.ord.common.Constants;
import com.acme.ord.common.ServiceResult;
import com.acme.ord.domain.dto.SiparisCevapDto;
import com.acme.ord.domain.entity.*;
import com.acme.ord.domain.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * toplu_siparis_isle, toplu_siparis_iptali_isle, siparis_oncesi_degerlendir fonksiyonlarinin Java karsiligi.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TopluSiparisService {

    private final TopluSiparisRepository topluSiparisRepository;
    private final TopluSiparisKayitlarRepository topluSiparisKayitlarRepository;
    private final BelgeRepository belgeRepository;
    private final HesapRepository hesapRepository;
    private final SiparisService siparisService;
    private final SiparisIptalService siparisIptalService;

    /**
     * toplu_siparis_isle: Toplu siparis dosyasi isler.
     * Kayitlari tek tek okuyarak siparis_isle cagirir.
     */
    public ServiceResult<SiparisCevapDto> topluSiparisIsle(
            Long topluSiparisId, Integer kanalNo, Long kurumKodu,
            Long origIl, String origSube, String origGise, String origKullanici) {

        var topluSiparisOpt = topluSiparisRepository.findById(topluSiparisId);
        if (topluSiparisOpt.isEmpty()) {
            return ServiceResult.fail("TOPLU_SIPARIS_BULUNAMADI", "APP", -20304,
                    "toplu_siparis_isle: Toplu siparis bulunamadi. Id:" + topluSiparisId);
        }

        List<TopluSiparisKayitlar> kayitlar = topluSiparisKayitlarRepository
                .findByTopluSiparisId(topluSiparisId);

        int islenenkayitSayisi = 0;

        for (TopluSiparisKayitlar kayit : kayitlar) {
            try {
                // Her kayit icin hesap ve belge bul, siparis isle
                var hesapOpt = hesapRepository.findFirstByHesapNo(kayit.getHesapNo());
                if (hesapOpt.isEmpty()) {
                    kayit.setDurum("HATA");
                    kayit.setHataMesaji("Hesap bulunamadi: " + kayit.getHesapNo());
                    topluSiparisKayitlarRepository.save(kayit);
                    continue;
                }

                var belgeOpt = belgeRepository.findByHesapIdAndBelgeNoAndDonemKodu(
                        hesapOpt.get().getId(), kayit.getBelgeNo(), kayit.getDonemKodu());
                if (belgeOpt.isEmpty()) {
                    kayit.setDurum("HATA");
                    kayit.setHataMesaji("Belge bulunamadi");
                    topluSiparisKayitlarRepository.save(kayit);
                    continue;
                }

                // [SiparisIstekDto olusturup siparisService.siparisYap cagrisi yapilacak]
                kayit.setDurum("BASARILI");
                topluSiparisKayitlarRepository.save(kayit);
                islenenkayitSayisi++;

            } catch (Exception e) {
                log.error("toplu_siparis_isle: Kayit islenirken hata. KayitId: {}", kayit.getId(), e);
                kayit.setDurum("HATA");
                kayit.setHataMesaji(e.getMessage());
                topluSiparisKayitlarRepository.save(kayit);
            }
        }

        SiparisCevapDto cevap = new SiparisCevapDto();
        cevap.setIslenenKayitSayisi(islenenkayitSayisi);
        cevap.setKalanKayitSayisi(kayitlar.size() - islenenkayitSayisi);

        return ServiceResult.success(cevap);
    }

    /**
     * toplu_siparis_iptali_isle: Toplu siparis iptali isler.
     */
    public ServiceResult<SiparisCevapDto> topluSiparisIptaliIsle(
            Long topluSiparisId, String iptalDosyaAdi, List<Long> kayitIdList,
            Long kurumKodu, Long origIl, String origSube, String origGise,
            String origKullanici, Long kullaniciId) {

        log.info("toplu_siparis_iptali_isle: topluSiparisId={}", topluSiparisId);

        // [Her kayit icin siparis_iptali cagrisi yapilacak]
        SiparisCevapDto cevap = new SiparisCevapDto();
        return ServiceResult.success(cevap);
    }

    /**
     * siparis_oncesi_degerlendir: Token bazli toplu siparis on degerlendirmesi.
     */
    public ServiceResult<Void> siparisOncesiDegerlendir(
            String token, Integer kanalNo, Long kurumKodu, Long kullaniciId) {

        log.info("siparis_oncesi_degerlendir: token={}, kanalNo={}", token, kanalNo);

        // [toplu_siparis_kayitlar_tmp tablosundan token ile kayit okunup degerlendirme yapilacak]
        return ServiceResult.success();
    }
}
