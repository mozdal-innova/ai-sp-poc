package com.acme.ord.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class SiparisIstekFdyDto {
    private String urunSeriNo;
    private Long kalemTipiId;
    private Long tedarikciId;
    private BigDecimal siparisTutari;
    private BigDecimal kdvOrani;
    private BigDecimal otvOrani;
    private Long paketTipiId;
    private Long bayiKodu;
    private Long kampanyaKodu;
    private Long taksitliSatisId;
    private Integer taksitNo;
}
