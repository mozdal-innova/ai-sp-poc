package com.acme.ord.service.siparis;

import com.acme.ord.domain.dto.*;
import com.acme.ord.domain.entity.*;
import com.acme.ord.domain.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SiparisService {

    private final BelgeRepository belgeRepository;
    private final HesapRepository hesapRepository;
    private final SiparisSahibiRepository siparisSahibiRepository;
    private final SiparisRepository siparisRepository;
    private final SiparisHareketRepository siparisHareketRepository;

    @Transactional
    public SiparisCevapDto siparisYap(Long belgeId, List<SiparisIstekDto> istekListe) {
        if (istekListe == null || istekListe.isEmpty()) {
            throw new IllegalArgumentException("Istek listesi bos");
        }

        SiparisIstekDto istek = istekListe.get(0);

        Belge belge = belgeRepository.findById(belgeId)
                .orElseThrow(() -> new IllegalArgumentException("Belge bulunamadi: " + belgeId));

        Hesap hesap = hesapRepository.findById(belge.getHesapId())
                .orElseThrow(() -> new IllegalArgumentException("Hesap bulunamadi"));

        return siparisIsle(belge, hesap, istek);
    }

    private SiparisCevapDto siparisIsle(Belge belge, Hesap hesap, SiparisIstekDto istek) {
        Long islemId = istek.getIslemKodu();

        SiparisSahibi siparisSahibi = siparisSahibiRepository
                .findByHesapIdAndMusteriId(hesap.getId(), hesap.getMusteriId())
                .orElse(null);

        if (siparisSahibi == null) {
            siparisSahibi = new SiparisSahibi();
            siparisSahibi.setHesapId(hesap.getId());
            siparisSahibi.setMusteriId(hesap.getMusteriId());
            siparisSahibi.setSirketId(belge.getSirketId());
            siparisSahibi.setTanitimZamani(LocalDateTime.now());
            siparisSahibi.setGuncellemeZamani(LocalDateTime.now());
            siparisSahibi = siparisSahibiRepository.save(siparisSahibi);
        }

        Siparis siparis = new Siparis();
        siparis.setSiparisDurumId(1L);
        siparis.setSiparisSahibiId(siparisSahibi.getId());
        siparis.setBelgeId(belge.getId());
        siparis.setSiparisTutari(istek.getSiparisToplamTutar());
        siparis.setUygulama(istek.getUygulama() != null ? istek.getUygulama().intValue() : 0);
        siparis.setTanitimZamani(LocalDateTime.now());
        siparis.setGuncellemeZamani(LocalDateTime.now());
        siparis = siparisRepository.save(siparis);

        SiparisHareket siparisHareket = new SiparisHareket();
        siparisHareket.setIslemTarihi(istek.getIslemTarihi() != null ? istek.getIslemTarihi() : LocalDate.now());
        siparisHareket.setKurumId(istek.getKurumId());
        siparisHareket.setStan(istek.getStan());
        siparisHareket.setSiparisId(siparis.getId());
        siparisHareket.setSiparisDurumId(siparis.getSiparisDurumId());
        siparisHareket.setIslemId(islemId);
        siparisHareket.setSiparisTutari(istek.getSiparisToplamTutar());
        siparisHareket.setBusinessId(istek.getBusinessId());
        siparisHareket.setConversationId(istek.getConversationId());
        siparisHareket.setTanitimZamani(LocalDateTime.now());
        siparisHareket.setGuncellemeZamani(LocalDateTime.now());
        siparisHareket = siparisHareketRepository.save(siparisHareket);

        BigDecimal yeniKalanTutar = belge.getKalanTutar().subtract(istek.getSiparisToplamTutar());
        belge.setKalanTutar(yeniKalanTutar);
        belge.setGuncellemeZaman(LocalDateTime.now());
        belgeRepository.save(belge);

        return SiparisCevapDto.builder()
                .hesapId(hesap.getId())
                .musteriId(hesap.getMusteriId())
                .siparisSahibiId(siparisSahibi.getId())
                .siparisId(siparis.getId())
                .siparisHareketId(siparisHareket.getId())
                .siparisTutari(istek.getSiparisToplamTutar())
                .hesapNo(hesap.getHesapNo())
                .build();
    }
}