package com.acme.ord.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "hesap")
@Getter
@Setter
@NoArgsConstructor
public class Hesap {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_hesap")
    @SequenceGenerator(name = "seq_hesap", sequenceName = "seq_hesap", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "hesap_no")
    private String hesapNo;

    @Column(name = "musteri_id")
    private Long musteriId;

    @Column(name = "sanal_mi")
    private Integer sanalMi;

    @Column(name = "hesap_tipi_id")
    private Long hesapTipiId;

    @Column(name = "hesap_durum")
    private String hesapDurum;

    @Column(name = "acilis_tarihi")
    private LocalDate acilisTarihi;

    @Column(name = "iptal_tarihi")
    private LocalDate iptalTarihi;

}
