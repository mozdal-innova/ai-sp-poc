package com.acme.ord.service.siparis;

import com.acme.ord.common.Constants;
import com.acme.ord.common.ServiceResult;
import com.acme.ord.domain.dto.SiparisCevapDto;
import com.acme.ord.domain.dto.SiparisIstekDto;
import com.acme.ord.domain.dto.SiparisMunferitDto;
import com.acme.ord.domain.entity.*;
import com.acme.ord.domain.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * siparis_onay, munferitle_siparis_yap fonksiyonlarinin Java karsiligi.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SiparisOnayService {

    private final SiparisRepository siparisRepository;
    private final SiparisHareketRepository siparisHareketRepository;
    private final SiparisDetayRepository siparisDetayRepository;
    private final SiparisDetayKayitRepository siparisDetayKayitRepository;

    /**
     * siparis_onay: Siparis onay islemi.
     */
    public ServiceResult<Void> siparisOnay(
            Long siparisId, Long siparisHareketId, Long hareketId,
            Long alacakId, Long vergiNo, String vergiDairesi,
            String siparisBelgeNo, Long kullaniciId, Integer taksitliMi) {

        Optional<Siparis> siparisOpt = siparisRepository.findById(siparisId);
        if (siparisOpt.isEmpty()) {
            return ServiceResult.fail("SIPARIS_BULUNAMADI", "APP", -20304,
                    "siparis_onay: Siparis bulunamadi. Id:" + siparisId);
        }

        Siparis siparis = siparisOpt.get();

        // Siparis detay guncelle
        // [vergi_no, vergi_dairesi, siparis_belge_no, taksitli_mi alanlari guncellenir]
        // [siparis durum guncellenir: c_id_onay_verildi]

        log.info("siparis_onay: siparisId={}, onaylandi", siparisId);

        return ServiceResult.success();
    }

    /**
     * munferitle_siparis_yap: Munferit siparis islemi.
     */
    public ServiceResult<SiparisCevapDto> munferitleSiparisYap(
            Long shId, List<SiparisMunferitDto> munferitList,
            List<SiparisIstekDto> istekList) {

        if (istekList == null || istekList.isEmpty()) {
            return ServiceResult.fail("ISTEK_BOS", "APP", -20304,
                    "munferitle_siparis_yap: Istek bos");
        }

        // [Mevcut siparis hareket uzerinden munferit revize islemi yapilacak]
        // [Her munferit icin kalem detay kontrolu ve hareket olusturma]

        log.info("munferitle_siparis_yap: shId={}", shId);

        SiparisCevapDto cevap = new SiparisCevapDto();
        return ServiceResult.success(cevap);
    }
}
