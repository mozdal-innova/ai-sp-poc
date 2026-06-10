package com.acme.ord.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * T_SIPARIS_CEVAP_TYPE karsiligi
 */
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
    private Integer islenenKayitSayisi;
    private BigDecimal islenenKayitTutari;
    private Integer kalanKayitSayisi;
    private Integer onayBekleyenAdeti;
    private Long dekontGrupNo;
    private String hesapNo;
}
