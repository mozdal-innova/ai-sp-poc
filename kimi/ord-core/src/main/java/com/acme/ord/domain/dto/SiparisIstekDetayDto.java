package com.acme.ord.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class SiparisIstekDetayDto {

    private Integer tdkMi;
    private Integer vergiHaric;
    private Long siparisSebebiId;
    private String aciklama;
    private String siparisReferansNo;
    private Long teslimatTalepId;
    private String siparisBelgeNo;
    private String vergiDairesi;
    private Long vergiNo;
    private String sebepBildirimNo;
    private String sebepKullaniciSicilNo;
    private String sebepBayiKodu;
    private String sebepCagriMerkezi;
    private Integer erpGonderilecekMi;
    private String erpIban;
    private String erpAliciAdSoyad;
    private String erpAliciTelefon;
    private String erpAciklama;
    private Long cid;
    private LocalDate islemTarihi;
    private String temsilciAdiSoyadi;
    private Long temsilciSicilNo;
    private String portalKullaniciKodu;
    private String portalKullaniciAdSoyad;

}
