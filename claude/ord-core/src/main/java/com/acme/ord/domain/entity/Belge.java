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
@Table(name = "belge")
public class Belge {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "hesap_id")
    private Long hesapId;

    @Column(name = "hesap_tipi_id")
    private Long hesapTipiId;

    @Column(name = "sirket_id")
    private Long sirketId;

    @Column(name = "belge_no")
    private Long belgeNo;

    @Column(name = "donem_kodu")
    private Long donemKodu;

    @Column(name = "belge_durum")
    private String belgeDurum;

    @Column(name = "siparis_duzeltme_tutar", precision = 24, scale = 6)
    private BigDecimal siparisDuzeltmeTutar;

    @Column(name = "kalan_tutar", precision = 24, scale = 6)
    private BigDecimal kalanTutar;

    @Column(name = "odeme_tarihi")
    private LocalDate odemeTarihi;

    @Column(name = "tanitim_zaman")
    private LocalDateTime tanitimZaman;

    @Column(name = "guncelleme_zaman")
    private LocalDateTime guncellemeZaman;

    @Column(name = "guncelleyen_kullanici")
    private Long guncellleyenKullanici;
}
