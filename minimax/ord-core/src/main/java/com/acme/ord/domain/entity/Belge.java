package com.acme.ord.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "belge")
@Getter
@Setter
@NoArgsConstructor
public class Belge {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_belge")
    @SequenceGenerator(name = "seq_belge", allocationSize = 1)
    private Long id;

    @Column(name = "hesap_id")
    private Long hesapId;

    @Column(name = "belge_no", length = 16)
    private BigDecimal belgeNo;

    @Column(name = "donem_kodu", length = 6)
    private BigDecimal donemKodu;

    @Column(name = "belge_durum", length = 20)
    private String belgeDurum;

    @Column(name = "belge_tipi", length = 20)
    private String belgeTipi;

    @Column(name = "kalan_tutar", precision = 24, scale = 6)
    private BigDecimal kalanTutar;

    @Column(name = "siparis_duzeltme_tutar", precision = 24, scale = 6)
    private BigDecimal siparisDuzeltmeTutar;

    @Column(name = "odeme_tarihi")
    private LocalDate odemeTarihi;

    @Column(name = "sirket_id")
    private Long sirketId;

    @Column(name = "tanitim_zaman")
    private LocalDateTime tanitimZaman;

    @Column(name = "guncelleme_zaman")
    private LocalDateTime guncellemeZaman;

    @Column(name = "guncelleyen_kullanici")
    private Long guncelleyenKullanici;

    @Column(name = "hesap_tipi_id")
    private Long hesapTipiId;

    @Column(name = "kilitli")
    private Integer kilitli;
}
