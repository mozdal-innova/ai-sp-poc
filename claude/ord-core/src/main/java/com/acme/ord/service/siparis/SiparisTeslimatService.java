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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * teslimat_sonuc_bildir, teslimat_hareket_at, teslimat_hareket_isle fonksiyonlarinin Java karsiligi.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SiparisTeslimatService {

    private final SiparisHareketRepository siparisHareketRepository;
    private final SiparisRepository siparisRepository;
    private final SiparisSahibiRepository siparisSahibiRepository;
    private final BelgeRepository belgeRepository;
    private final HesapRepository hesapRepository;
    private final SiparisDetayKayitRepository siparisDetayKayitRepository;

    /**
     * teslimat_sonuc_bildir: Teslimat sonuc bildirimi.
     */
    public ServiceResult<SiparisCevapDto> teslimatSonucBildir(
            Long siparisHareketId, LocalDate islemTarihi, Long kurumKodu,
            Long stan, Long islemKodu, Long islemKaynagi,
            String businessId, String conversationId) {

        // Islem tarihi kontrolu
        if (!islemTarihi.equals(LocalDate.now())) {
            return ServiceResult.fail("ISLEM_TARIHI_HATALI", "APP", -20304,
                    "teslimat_sonuc_bildir: Islem tarihi hatali");
        }

        // Resend kontrolu
        long resendCount = siparisHareketRepository.countByIslemTarihiAndKurumIdAndStanAndIslemId(
                islemTarihi, kurumKodu, stan, islemKodu);
        if (resendCount > 0) {
            return ServiceResult.fail(Constants.RC_RESEND, "APP", -20304,
                    "teslimat_sonuc_bildir: Resend");
        }

        // Hareket bul
        Optional<SiparisHareket> hareketOpt = siparisHareketRepository.findById(siparisHareketId);
        if (hareketOpt.isEmpty()) {
            return ServiceResult.fail("HAREKET_BULUNAMADI", "APP", -20304,
                    "teslimat_sonuc_bildir: Hareket bulunamadi. Id:" + siparisHareketId);
        }
        SiparisHareket sipHareket = hareketOpt.get();

        // Siparis ve sahibi bilgilerini al
        Optional<Siparis> siparisOpt = siparisRepository.findById(sipHareket.getSiparisId());
        if (siparisOpt.isEmpty()) {
            return ServiceResult.fail(Constants.RC_SQL_ERROR, "ORA", -20304,
                    "teslimat_sonuc_bildir: Siparis bulunamadi");
        }
        Siparis siparis = siparisOpt.get();

        Optional<SiparisSahibi> sahibiOpt = siparisSahibiRepository.findById(siparis.getSiparisSahibiId());
        Optional<Belge> belgeOpt = belgeRepository.findById(siparis.getBelgeId());

        if (sahibiOpt.isEmpty() || belgeOpt.isEmpty()) {
            return ServiceResult.fail(Constants.RC_SQL_ERROR, "ORA", -20304,
                    "teslimat_sonuc_bildir: Iliskili kayitlar bulunamadi");
        }

        Belge belge = belgeOpt.get();
        Optional<Hesap> hesapOpt = hesapRepository.findById(belge.getHesapId());
        String hesapNo = hesapOpt.map(Hesap::getHesapNo).orElse(null);

        // Cevap olustur
        SiparisCevapDto cevap = new SiparisCevapDto();
        cevap.setHesapId(belge.getHesapId());
        cevap.setMusteriId(sahibiOpt.get().getMusteriId());
        cevap.setSiparisSahibiId(sahibiOpt.get().getId());
        cevap.setSiparisId(siparis.getId());
        cevap.setSiparisTutari(siparis.getSiparisTutari());
        cevap.setHesapNo(hesapNo);

        // Yeni teslimat hareket kaydi olustur
        SiparisHareket yeniHareket = new SiparisHareket();
        yeniHareket.setIslemTarihi(islemTarihi);
        yeniHareket.setKurumId(kurumKodu);
        yeniHareket.setStan(stan);
        yeniHareket.setSiparisId(siparis.getId());
        yeniHareket.setIslemId(islemKodu);
        yeniHareket.setSiparisTutari(sipHareket.getSiparisTutari());
        yeniHareket.setUstId(sipHareket.getId());
        yeniHareket.setBusinessId(businessId);
        yeniHareket.setConversationId(conversationId);
        yeniHareket.setTanitimZamani(LocalDateTime.now());
        yeniHareket.setTanitanKullaniciId(Constants.C_INST_SISTEM_KULLANICI_ID);
        yeniHareket.setGuncellemeZamani(LocalDateTime.now());
        yeniHareket.setDenemeSayisi(0);
        yeniHareket = siparisHareketRepository.save(yeniHareket);

        cevap.setSiparisHareketId(yeniHareket.getId());

        // Belge tutar guncelle
        belgeRepository.updateSiparisTutar(belge.getId(), sipHareket.getSiparisTutari(),
                "TESLIMAT_BASARILI", LocalDateTime.now(), Constants.C_INST_SISTEM_KULLANICI_ID);

        // Orijinal hareket durumunu guncelle
        siparisHareketRepository.updateHareketDurum(sipHareket.getId(),
                0L, 0L, Constants.RC_SUCCESS, LocalDateTime.now(), Constants.C_INST_SISTEM_KULLANICI_ID);

        return ServiceResult.success(cevap);
    }

    /**
     * teslimat_hareket_at: Teslimat hareket firlat (async/batch icin).
     */
    public void teslimatHareketAt(Long hareketId, Long islemId, Long tbId) {
        log.info("teslimat_hareket_at: hareketId={}, islemId={}, tbId={}", hareketId, islemId, tbId);
        // [teslimat_hareket_isle tarafindan cagrilan batch islemi - detay impl gerekli]
    }

    /**
     * teslimat_hareket_isle: Zaman araligindaki teslimat hareketlerini isler.
     */
    public void teslimatHareketIsle(LocalDateTime basZamani, LocalDateTime bitZamani, Long sirketId) {
        log.info("teslimat_hareket_isle: basZamani={}, bitZamani={}, sirketId={}",
                basZamani, bitZamani, sirketId);
        // [Batch isleme - siparis_sahibi_hareket cursor ile islenir]
    }
}
