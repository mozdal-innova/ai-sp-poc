package com.acme.ord.service.siparis;

import com.acme.ord.domain.dto.SiparisCevapDto;
import com.acme.ord.domain.dto.SiparisIstekDto;
import com.acme.ord.domain.dto.SiparisMunferitDto;
import com.acme.ord.domain.repository.SiparisDetayRepository;
import com.acme.ord.domain.repository.SiparisHareketRepository;
import com.acme.ord.domain.repository.SiparisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SiparisServiceImpl implements SiparisService {

    private final SiparisRepository siparisRepository;
    private final SiparisDetayRepository siparisDetayRepository;
    private final SiparisHareketRepository siparisHareketRepository;

    @Override
    @Transactional
    public SiparisCevapDto siparisOlusturWs(List<SiparisIstekDto> istekListesi) {
        log.info("siparisOlusturWs cagrildi. Istek sayisi: {}", istekListesi.size());
        SiparisCevapDto cevap = new SiparisCevapDto();
        cevap.setCevapKodu("0000"); // c_success
        return cevap;
    }

    @Override
    @Transactional(readOnly = true)
    public SiparisCevapDto siparisKontrolEkran(Long belgeId, Long hesapId, List<SiparisIstekDto> istekListesi) {
        SiparisCevapDto cevap = new SiparisCevapDto();
        cevap.setCevapKodu("0000");
        return cevap;
    }

    @Override
    @Transactional
    public SiparisCevapDto siparisYap(Long belgeId, List<SiparisIstekDto> istekListesi) {
        SiparisCevapDto cevap = new SiparisCevapDto();
        cevap.setCevapKodu("0000");
        return cevap;
    }

    @Override
    @Transactional
    public SiparisCevapDto siparisIptali(Long hareketId, Date mutabakatTarihi, Long stan, Long islemKaynagi, Date islemKabulTarihi, Long islemTipi, Integer origIl, String origSube, String origGise, String origKullanici, Long kullanici, Integer uygulama) {
        SiparisCevapDto cevap = new SiparisCevapDto();
        cevap.setCevapKodu("0000");
        return cevap;
    }

    @Override
    @Transactional
    public SiparisCevapDto siparisIptaliWs(SiparisIstekDto istek) {
        SiparisCevapDto cevap = new SiparisCevapDto();
        cevap.setCevapKodu("0000");
        return cevap;
    }

    @Override
    @Transactional
    public SiparisCevapDto topluSiparisIsle(Long topluSiparisId, Integer kanalNo, Long kurumKodu, Integer origIl, String origSube, String origGise, String origKullanici) {
        SiparisCevapDto cevap = new SiparisCevapDto();
        cevap.setCevapKodu("0000");
        return cevap;
    }

    @Override
    @Transactional
    public SiparisCevapDto siparisOnay(Long siparisId, Long siparisHareketId, Long hareketId, Long alacakId, Long vergiNo, String vergiDairesi, String siparisBelgeNo, Long kullaniciId, Integer taksitliMi) {
        SiparisCevapDto cevap = new SiparisCevapDto();
        cevap.setCevapKodu("0000");
        return cevap;
    }

    @Override
    @Transactional
    public SiparisCevapDto munferitleSiparisYap(Long shId, List<SiparisMunferitDto> munferitList, List<SiparisIstekDto> istekListesi) {
        SiparisCevapDto cevap = new SiparisCevapDto();
        cevap.setCevapKodu("0000");
        return cevap;
    }
}
