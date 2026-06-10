package com.acme.ord.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
