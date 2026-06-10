package com.acme.ord.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "siparis_hareket")
@SequenceGenerator(name = "seq_siparis_hareket", sequenceName = "seq_siparis_hareket", allocationSize = 1)
public class SiparisHareket {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_siparis_hareket")
    @Column(name = "id")
    private Long id;

    @Column(name = "islem_tarihi")
    private LocalDate islemTarihi;

    @Column(name = "kurum_id")
    private Long kurumId;

    @Column(name = "stan")
    private Long stan;

    @Column(name = "siparis_id")
    private Long siparisId;

    @Column(name = "siparis_hareket_durum_id")
    private Long siparisHareketDurumId;

    @Column(name = "siparis_durum_id")
    private Long siparisDurumId;

    @Column(name = "islem_id")
    private Long islemId;

    @Column(name = "siparis_tutari", precision = 20, scale = 2)
    private BigDecimal siparisTutari;

    @Column(name = "ust_id")
    private Long ustId;

    @Column(name = "cevap_kodu")
    private String cevapKodu;

    @Column(name = "cevap_mesaji")
    private String cevapMesaji;

    @Column(name = "aciklama")
    private String aciklama;

    @Column(name = "business_id")
    private String businessId;

    @Column(name = "conversation_id")
    private String conversationId;

    @Column(name = "tanitim_zamani")
    private LocalDateTime tanitimZamani;

    @Column(name = "tanitan_kullanici_id")
    private Long tanitanKullaniciId;

    @Column(name = "guncelleme_zamani")
    private LocalDateTime guncellemeZamani;

    @Column(name = "guncelleyen_kullanici_id")
    private Long guncellleyenKullaniciId;

    @Column(name = "islem_tipi")
    private Long islemTipi;

    @Column(name = "islem_durum_id")
    private Long islemDurumId;

    @Column(name = "kurum_kodu")
    private Long kurumKodu;

    @Column(name = "islem_kaynagi")
    private Long islemKaynagi;

    @Column(name = "mutabakat_tarihi")
    private LocalDate mutabakatTarihi;

    @Column(name = "islem_kabul_tarihi")
    private LocalDate islemKabulTarihi;

    @Column(name = "orig_il")
    private Long origIl;

    @Column(name = "orig_sube")
    private String origSube;

    @Column(name = "orig_gise")
    private String origGise;

    @Column(name = "orig_kullanici")
    private String origKullanici;

    @Column(name = "tanitim_kullanici")
    private Long tanitimKullanici;

    @Column(name = "uygulama")
    private Integer uygulama;

    @Column(name = "islem_kodu")
    private Long islemKodu;

    @Column(name = "deneme_sayisi")
    private Integer denemeSayisi;
}
