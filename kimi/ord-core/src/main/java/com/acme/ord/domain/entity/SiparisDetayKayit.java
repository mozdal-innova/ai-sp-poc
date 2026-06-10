package com.acme.ord.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "siparis_detay_kayit")
@Getter
@Setter
@NoArgsConstructor
public class SiparisDetayKayit {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_siparis_detay_kayit")
    @SequenceGenerator(name = "seq_siparis_detay_kayit", sequenceName = "seq_siparis_detay_kayit", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "siparis_id")
    private Long siparisId;

    @Column(name = "tdk_mi")
    private Integer tdkMi;

    @Column(name = "vergi_haric")
    private Integer vergiHaric;

    @Column(name = "siparis_sebebi_id")
    private Long siparisSebebiId;

    @Column(name = "aciklama")
    private String aciklama;

    @Column(name = "siparis_belge_no")
    private String siparisBelgeNo;

    @Column(name = "vergi_dairesi")
    private String vergiDairesi;

    @Column(name = "vergi_no")
    private Long vergiNo;

    @Column(name = "sebep_bildirim_no")
    private String sebepBildirimNo;

    @Column(name = "sebep_personel_sicil_no")
    private String sebepPersonelSicilNo;

    @Column(name = "sebep_bayi_kodu")
    private String sebepBayiKodu;

    @Column(name = "sebep_cagri_merkezi")
    private String sebepCagriMerkezi;

    @Column(name = "erp_gonderilecek_mi")
    private Integer erpGonderilecekMi;

    @Column(name = "erp_iban")
    private String erpIban;

    @Column(name = "erp_alici_ad_soyad")
    private String erpAliciAdSoyad;

    @Column(name = "erp_alici_telefon")
    private String erpAliciTelefon;

    @Column(name = "erp_aciklama")
    private String erpAciklama;

    @Column(name = "cid")
    private Long cid;

    @Column(name = "islem_tarihi")
    private LocalDate islemTarihi;

    @Column(name = "temsilci_adi_soyadi")
    private String temsilciAdiSoyadi;

    @Column(name = "temsilci_sicil_no")
    private Long temsilciSicilNo;

    @Column(name = "portal_kullanici_kodu")
    private String portalKullaniciKodu;

    @Column(name = "portal_kullanici_adsoyad")
    private String portalKullaniciAdSoyad;

    @Column(name = "taksitli_mi")
    private Integer taksitliMi;

    @Column(name = "org_belge_durum")
    private String orgBelgeDurum;

    @Column(name = "tanitim_zamani")
    private Instant tanitimZamani;

    @Column(name = "tanitan_kullanici_id")
    private Long tanitanKullaniciId;

    @Column(name = "guncelleme_zamani")
    private Instant guncellemeZamani;

    @Column(name = "guncelleyen_kullanici_id")
    private Long guncelleyenKullaniciId;

}
