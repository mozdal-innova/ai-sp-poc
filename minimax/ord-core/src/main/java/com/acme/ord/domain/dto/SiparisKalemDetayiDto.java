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
