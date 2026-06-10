package com.acme.ord.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class SiparisCevapDto {

    private Long stokAnalizId;
    private Long stokAnalizDetayId;
    private Long hesapId;
    private Long musteriId;
    private Long siparisSahibiId;
    private Long siparisId;
    private Long siparisHareketId;
    private Long siparisDetayId;
    private Long islemId;
    private Long hareketId;
    private Long tahsilatId;
    private BigDecimal siparisTutari;
    private BigDecimal alacakTutari;
    private Long islenenKayitSayisi;
    private BigDecimal islenenKayitTutari;
    private Long kalanKayitSayisi;
    private Long onayBekleyenAdeti;
    private Long dekontGrupNo;
    private String hesapNo;

    public SiparisCevapDto(Long stokAnalizId, Long stokAnalizDetayId, Long hesapId, Long musteriId,
                           Long siparisSahibiId, Long siparisId, Long siparisHareketId, Long siparisDetayId,
                           Long islemId, Long hareketId, Long tahsilatId, BigDecimal siparisTutari,
                           BigDecimal alacakTutari, Long islenenKayitSayisi, BigDecimal islenenKayitTutari,
                           Long kalanKayitSayisi, Long onayBekleyenAdeti, Long dekontGrupNo, String hesapNo) {
        this.stokAnalizId = stokAnalizId;
        this.stokAnalizDetayId = stokAnalizDetayId;
        this.hesapId = hesapId;
        this.musteriId = musteriId;
        this.siparisSahibiId = siparisSahibiId;
        this.siparisId = siparisId;
        this.siparisHareketId = siparisHareketId;
        this.siparisDetayId = siparisDetayId;
        this.islemId = islemId;
        this.hareketId = hareketId;
        this.tahsilatId = tahsilatId;
        this.siparisTutari = siparisTutari;
        this.alacakTutari = alacakTutari;
        this.islenenKayitSayisi = islenenKayitSayisi;
        this.islenenKayitTutari = islenenKayitTutari;
        this.kalanKayitSayisi = kalanKayitSayisi;
        this.onayBekleyenAdeti = onayBekleyenAdeti;
        this.dekontGrupNo = dekontGrupNo;
        this.hesapNo = hesapNo;
    }

}
