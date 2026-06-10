package com.acme.ord.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class SiparisKalemDetayiDto {

    private Long siparisDetayId;
    private Long kalemTipiId;
    private Long urunGrubuId;
    private Long tedarikciId;
    private Long paketTipiId;
    private BigDecimal orjinalTutar;
    private BigDecimal siparisDuzeltmeTutari;
    private BigDecimal revizeTutari;
    private BigDecimal kdvOrani;
    private BigDecimal kdvTutari;
    private BigDecimal otvOrani;
    private BigDecimal otvTutari;
    private Integer duzeltmeYapilabilirMi;
    private Integer carpan;
    private Long kdvSiparisDetayId;
    private Long otvSiparisDetayId;
    private String urunSeriNo;
    private Long mahsupSiparisDetayId;
    private Long taksitliSatisId;
    private Integer taksitNo;

}
