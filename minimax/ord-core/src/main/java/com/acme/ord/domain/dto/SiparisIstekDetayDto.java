package com.acme.ord.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
    private Integer cid;
    private LocalDate islemTarihi;
    private String temsilciAdiSoyadi;
    private Long temsilciSicilNo;
    private String portalKullaniciKodu;
    private String portalKullaniciAdSoyad;
}
