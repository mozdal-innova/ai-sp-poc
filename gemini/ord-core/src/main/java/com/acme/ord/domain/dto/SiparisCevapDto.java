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
    private Integer islenenKayitSayisi;
    private BigDecimal islenenKayitTutari;
    private Integer kalanKayitSayisi;
    private Integer onayBekleyenAdeti;
    private Long dekontGrupNo;
    private String hesapNo;
    
    // Uygulama seviyesi hata yonetimi icin
    private String cevapKodu;
    private String errorType;
    private String errorNum;
    private String errorStr;
}
