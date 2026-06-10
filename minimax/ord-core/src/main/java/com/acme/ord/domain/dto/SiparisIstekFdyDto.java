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
