package com.acme.ord.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SiparisIstekDto {

    private LocalDate islemTarihi;
    private Long kurumId;
    private Integer stan;
    private Long sirketId;
    private Long origIl;
    private String origSube;
    private String origGise;
    private String origKullanici;
    private Long kullaniciId;
    private Integer islemSiraNo;
    private SiparisIstekDetayDto siparisDetayBilgi = new SiparisIstekDetayDto();
    private LocalDate refIslemTarihi;
    private Integer refStan;
    private Integer refIslemSiraNo;
    private Long stokAnalizId;
    private Integer toplamKalemSayisi;
    private BigDecimal siparisToplamTutar;
    private String hesapNo;
    private Integer taksitNo;
    private Long belgeNo;
    private Integer donemKodu;
    private Integer uygulama;
    private Long islemKaynagi;
    private Long durumId;
    private Long islemKodu;
    private String businessId;
    private String conversationId;
    private List<SiparisIstekFdyDto> wsDetayList = new ArrayList<>();
    private List<SiparisKalemDetayiDto> kalemDetayList = new ArrayList<>();
    private Long tahsilatKurumKodu;
    private Integer taksitliMi;

}
