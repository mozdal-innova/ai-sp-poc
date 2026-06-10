package com.acme.ord.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SiparisIstekDto {
    private Date islemTarihi;
    private Long kurumId;
    private Long stan;
    private Long sirketId;
    private Integer origIl;
    private String origSube;
    private String origGise;
    private String origKullanici;
    private Long kullaniciId;
    private Long islemSiraNo;
    private SiparisIstekDetayDto siparisDetayBilgi;
    private Date refIslemTarihi;
    private Long refStan;
    private Long refIslemSiraNo;
    private Long stokAnalizId;
    private Integer toplamKalemSayisi;
    private BigDecimal siparisToplamTutar;
    private String hesapNo;
    private Integer taksitNo;
    private Long belgeNo;
    private Long donemKodu;
    private Integer uygulama;
    private Long islemKaynagi;
    private Long durumId;
    private Long islemKodu;
    private String businessId;
    private String conversationId;
    private List<SiparisIstekFdyDto> wsDetayList;
    private List<SiparisKalemDetayiDto> kalemDetayList;
    private Long tahsilatKurumKodu;
    private Integer taksitliMi;
}
