package com.acme.ord.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "belge")
@Getter
@Setter
@NoArgsConstructor
public class Belge {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_belge")
    @SequenceGenerator(name = "seq_belge", sequenceName = "seq_belge", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "hesap_id")
    private Long hesapId;

    @Column(name = "belge_no")
    private Long belgeNo;

    @Column(name = "donem_kodu")
    private Integer donemKodu;

    @Column(name = "sirket_id")
    private Long sirketId;

    @Column(name = "kalan_tutar", precision = 20, scale = 2)
    private BigDecimal kalanTutar;

    @Column(name = "siparis_duzeltme_tutar", precision = 20, scale = 2)
    private BigDecimal siparisDuzeltmeTutar;

    @Column(name = "belge_durum")
    private String belgeDurum;

    @Column(name = "belge_tipi")
    private String belgeTipi;

    @Column(name = "hesap_tipi_id")
    private Long hesapTipiId;

    @Column(name = "odeme_tarihi")
    private Instant odemeTarihi;

    @Column(name = "guncelleme_zaman")
    private Instant guncellemeZaman;

    @Column(name = "guncelleyen_kullanici")
    private Long guncelleyenKullanici;

    @Column(name = "tanitim_zaman")
    private Instant tanitimZaman;

    @Column(name = "kilitli")
    private Integer kilitli;

    @Column(name = "ana_belge_id")
    private Long anaBelgeId;

}
