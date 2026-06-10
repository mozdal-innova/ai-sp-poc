package com.acme.ord.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "hesap")
public class Hesap {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "hesap_no")
    private String hesapNo;

    @Column(name = "hesap_tipi_id")
    private Long hesapTipiId;

    @Column(name = "musteri_id")
    private Long musteriId;

    @Column(name = "sanal_mi")
    private Integer sanalMi;
}
