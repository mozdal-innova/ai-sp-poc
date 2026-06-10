package com.acme.ord.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "siparis_hareket")
@Getter
@Setter
@NoArgsConstructor
public class SiparisHareket {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_siparis_hareket")
    @SequenceGenerator(name = "seq_siparis_hareket", allocationSize = 1)
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

    @Column(name = "cevap_kodu", length = 10)
    private String cevapKodu;

    @Column(name = "cevap_mesaji", length = 500)
    private String cevapMesaji;

    @Column(name = "aciklama", length = 500)
    private String aciklama;

    @Column(name = "business_id", length = 255)
    private String businessId;

    @Column(name = "conversation_id", length = 255)
    private String conversationId;

    @Column(name = "tanitim_zamani")
    private LocalDateTime tanitimZamani;

    @Column(name = "tanitan_kullanici_id")
    private Long tanitanKullaniciId;

    @Column(name = "guncelleme_zamani")
    private LocalDateTime guncellemeZamani;

    @Column(name = "guncelleyen_kullanici_id")
    private Long guncelleyenKullaniciId;

    @Column(name = "islem_durum_id")
    private Long islemDurumId;

    @Column(name = "taksitli_satis_id")
    private Long taksitliSatisId;
}
