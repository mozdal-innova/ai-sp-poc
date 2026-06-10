package com.acme.ord.service.siparis;

import com.acme.ord.domain.dto.SiparisCevapDto;
import com.acme.ord.domain.dto.SiparisIstekDto;
import com.acme.ord.domain.dto.SiparisMunferitDto;

import java.util.Date;
import java.util.List;

public interface SiparisService {
    SiparisCevapDto siparisOlusturWs(List<SiparisIstekDto> istekListesi);
    SiparisCevapDto siparisKontrolEkran(Long belgeId, Long hesapId, List<SiparisIstekDto> istekListesi);
    SiparisCevapDto siparisYap(Long belgeId, List<SiparisIstekDto> istekListesi);
    SiparisCevapDto siparisIptali(Long hareketId, Date mutabakatTarihi, Long stan, Long islemKaynagi, Date islemKabulTarihi, Long islemTipi, Integer origIl, String origSube, String origGise, String origKullanici, Long kullanici, Integer uygulama);
    SiparisCevapDto siparisIptaliWs(SiparisIstekDto istek);
    SiparisCevapDto topluSiparisIsle(Long topluSiparisId, Integer kanalNo, Long kurumKodu, Integer origIl, String origSube, String origGise, String origKullanici);
    SiparisCevapDto siparisOnay(Long siparisId, Long siparisHareketId, Long hareketId, Long alacakId, Long vergiNo, String vergiDairesi, String siparisBelgeNo, Long kullaniciId, Integer taksitliMi);
    SiparisCevapDto munferitleSiparisYap(Long shId, List<SiparisMunferitDto> munferitList, List<SiparisIstekDto> istekListesi);
}
