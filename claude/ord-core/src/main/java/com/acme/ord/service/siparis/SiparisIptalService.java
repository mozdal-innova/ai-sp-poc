package com.acme.ord.service.siparis;

import com.acme.ord.common.Constants;
import com.acme.ord.common.ServiceResult;
import com.acme.ord.domain.dto.SiparisCevapDto;
import com.acme.ord.domain.dto.SiparisIstekDto;
import com.acme.ord.domain.entity.Belge;
import com.acme.ord.domain.entity.Hesap;
import com.acme.ord.domain.entity.Siparis;
import com.acme.ord.domain.entity.SiparisHareket;
import com.acme.ord.domain.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * siparis_iptali, siparis_iptali_kontrol, siparis_iptali_ws fonksiyonlarinin Java karsiligi.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SiparisIptalService {

    private final SiparisHareketRepository siparisHareketRepository;
    private final SiparisRepository siparisRepository;
    private final SiparisSahibiRepository siparisSahibiRepository;
    private final BelgeRepository belgeRepository;
    private final HesapRepository hesapRepository;

    /**
     * siparis_iptali: Siparis iptal islemi.
     */
    public ServiceResult<Long> siparisIptali(Long hareketId, LocalDate mutabakatTarihi,
                                              Long stan, Long islemKaynagi,
                                              LocalDate islemKabulTarihi, Long islemTipi,
                                              Long origIl, String origSube,
                                              String origGise, String origKullanici,
                                              Long kullanici, Integer uygulama) {

        // On kontrol
        ServiceResult<SiparisHareket> kontrolResult = siparisIptaliKontrol(hareketId, mutabakatTarihi,
                stan, uygulama, kullanici);
        if (!kontrolResult.isSuccess()) {
            return ServiceResult.fail(kontrolResult.getCevapKodu(), kontrolResult.getErrorType(),
                    kontrolResult.getErrorNum(), kontrolResult.getErrorStr());
        }

        SiparisHareket sipHareket = kontrolResult.getData();

        // Belge guncelle
        Optional<Siparis> siparisOpt = siparisRepository.findById(sipHareket.getSiparisId());
        if (siparisOpt.isEmpty()) {
            return ServiceResult.fail(Constants.RC_SQL_ERROR, "APP", -20304, "Siparis bulunamadi");
        }
        Siparis siparis = siparisOpt.get();

        Optional<Belge> belgeOpt = belgeRepository.findById(siparis.getBelgeId());
        if (belgeOpt.isEmpty()) {
            return ServiceResult.fail(Constants.RC_BELGE_BULUNAMADI, "APP", -20304, "Belge bulunamadi");
        }

        Belge belge = belgeOpt.get();
        // Belge tutar geri ekleme islemi
        belge.setSiparisDuzeltmeTutar(belge.getSiparisDuzeltmeTutar().add(sipHareket.getSiparisTutari()));
        belge.setKalanTutar(belge.getKalanTutar().add(sipHareket.getSiparisTutari()));
        belge.setOdemeTarihi(null);
        belge.setGuncellemeZaman(LocalDateTime.now());
        belge.setGuncellleyenKullanici(kullanici);
        belgeRepository.save(belge);

        // Iptal hareket olustur
        SiparisHareket iptalHareket = new SiparisHareket();
        iptalHareket.setIslemTarihi(LocalDate.now());
        iptalHareket.setKurumId(sipHareket.getKurumId());
        iptalHareket.setStan(stan);
        iptalHareket.setSiparisId(sipHareket.getSiparisId());
        iptalHareket.setIslemId(0L); // c_siparis_iptal_islemi placeholder
        iptalHareket.setSiparisTutari(sipHareket.getSiparisTutari().negate());
        iptalHareket.setUstId(hareketId);
        iptalHareket.setIslemKaynagi(islemKaynagi);
        iptalHareket.setIslemKabulTarihi(islemKabulTarihi);
        iptalHareket.setIslemTipi(islemTipi);
        iptalHareket.setOrigIl(origIl);
        iptalHareket.setOrigSube(origSube);
        iptalHareket.setOrigGise(origGise);
        iptalHareket.setOrigKullanici(origKullanici);
        iptalHareket.setMutabakatTarihi(mutabakatTarihi);
        iptalHareket.setUygulama(uygulama);
        iptalHareket.setTanitimZamani(LocalDateTime.now());
        iptalHareket.setTanitanKullaniciId(Constants.C_INST_SISTEM_KULLANICI_ID);
        iptalHareket.setGuncellemeZamani(LocalDateTime.now());
        iptalHareket.setDenemeSayisi(0);
        iptalHareket = siparisHareketRepository.save(iptalHareket);

        // Orijinal hareket durumunu guncelle
        siparisHareketRepository.updateHareketDurum(hareketId,
                0L, // c_shd_iptal_edildi placeholder
                0L, // c_id_iptal placeholder
                Constants.RC_SUCCESS,
                LocalDateTime.now(),
                Constants.C_INST_SISTEM_KULLANICI_ID);

        return ServiceResult.success(iptalHareket.getId());
    }

    /**
     * siparis_iptali_ws: Web servis uzerinden siparis iptali.
     */
    public ServiceResult<Long> siparisIptaliWs(SiparisIstekDto istek) {
        if (istek == null) {
            return ServiceResult.fail("ISTEK_BOS", "APP", -20304, "siparis_iptali_ws: Istek bos");
        }

        return siparisIptali(
                istek.getRefStan() != null ? istek.getRefStan() : 0L, // hareket_id placeholder
                istek.getRefIslemTarihi(),
                istek.getStan(),
                istek.getIslemKaynagi(),
                null,
                null,
                istek.getOrigIl(),
                istek.getOrigSube(),
                istek.getOrigGise(),
                istek.getOrigKullanici(),
                istek.getKullaniciId(),
                istek.getUygulama());
    }

    /**
     * siparis_iptali_kontrol: Siparis iptal oncesi kontrol.
     */
    private ServiceResult<SiparisHareket> siparisIptaliKontrol(Long hareketId,
                                                                 LocalDate mutabakatTarihi,
                                                                 Long stan,
                                                                 Integer uygulama,
                                                                 Long kullanici) {
        Optional<SiparisHareket> hareketOpt = siparisHareketRepository.findById(hareketId);
        if (hareketOpt.isEmpty()) {
            return ServiceResult.fail("HAREKET_BULUNAMADI", "APP", -20304,
                    "siparis_iptali_kontrol: Hareket bulunamadi");
        }

        SiparisHareket hareket = hareketOpt.get();

        // Iptal edilebilirlik kontrolu
        // [islem_durum_id ve siparis_hareket_durum_id kontrolleri - sabit degerleri placeholder]

        return ServiceResult.success(hareket);
    }
}
