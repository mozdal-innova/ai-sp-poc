package com.acme.ord.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "hesap")
@Getter
@Setter
@NoArgsConstructor
public class Hesap {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_hesap")
    @SequenceGenerator(name = "seq_hesap", allocationSize = 1)
    private Long id;

    @Column(name = "hesap_no", length = 20)
    private String hesapNo;

    @Column(name = "musteri_id")
    private Long musteriId;

    @Column(name = "sirket_id")
    private Long sirketId;

    @Column(name = "hesap_tipi_id")
    private Long hesapTipiId;

    @Column(name = "hesap_durum", length = 20)
    private String hesapDurum;

    @Column(name = "sanal_mi")
    private Integer sanalMi;

    @Column(name = "tanitim_zaman")
    private LocalDateTime tanitimZaman;
}
