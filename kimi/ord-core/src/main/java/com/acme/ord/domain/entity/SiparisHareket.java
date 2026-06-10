package com.acme.ord.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "siparis_hareket")
@Getter
@Setter
@NoArgsConstructor
public class SiparisHareket {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_siparis_hareket")
    @SequenceGenerator(name = "seq_siparis_hareket", sequenceName = "seq_siparis_hareket", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "islem_tarihi")
    private LocalDate islemTarihi;

    @Column(name = "kurum_id")
    private Long kurumId;

    @Column(name = "stan")
    private Integer stan;

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
    private Instant tanitimZamani;

    @Column(name = "tanitan_kullanici_id")
    private Long tanitanKullaniciId;

    @Column(name = "guncelleme_zamani")
    private Instant guncellemeZamani;

    @Column(name = "guncelleyen_kullanici_id")
    private Long guncelleyenKullaniciId;

    @Column(name = "islem_tutari", precision = 20, scale = 2)
    private BigDecimal islemTutari;

    @Column(name = "fazla_odeme_tutari", precision = 20, scale = 2)
    private BigDecimal fazlaOdemeTutari;

    @Column(name = "mutabakat_tarihi")
    private LocalDate mutabakatTarihi;

    @Column(name = "islem_kaynagi")
    private Long islemKaynagi;

    @Column(name = "islem_kabul_tarihi")
    private LocalDate islemKabulTarihi;

    @Column(name = "islem_tipi")
    private Long islemTipi;

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
    private Long uygulama;

    @Column(name = "taksitli_satis_id")
    private Long taksitliSatisId;

}
