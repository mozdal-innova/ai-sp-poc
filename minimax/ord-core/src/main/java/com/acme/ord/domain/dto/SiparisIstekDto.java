package com.acme.ord.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SiparisIstekDto {

    private LocalDate islemTarihi;
    private Long kurumId;
    private Long stan;
    private Long sirketId;
    private Integer origIl;
    private String origSube;
    private String origGise;
    private String origKullanici;
    private Long kullaniciId;
    private Integer islemSiraNo;
    private SiparisIstekDetayDto siparisDetayBilgi;
    private LocalDate refIslemTarihi;
    private Long refStan;
    private Integer refIslemSiraNo;
    private Long stokAnalizId;
    private Integer toplamKalemSayisi;
    private BigDecimal siparisToplamTutar;
    private String hesapNo;
    private Integer taksitNo;
    private BigDecimal belgeNo;
    private BigDecimal donemKodu;
    private Integer uygulama;
    private Integer islemKaynagi;
    private Long durumId;
    private Long islemKodu;
    private String businessId;
    private String conversationId;
    private List<SiparisIstekFdyDto> wsDetayList;
    private List<SiparisKalemDetayiDto> kalemDetayList;
    private Long tahsilatKurumKodu;
    private Integer taksitliMi;
}
