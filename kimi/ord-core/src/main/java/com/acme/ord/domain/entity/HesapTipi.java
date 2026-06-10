package com.acme.ord.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "hesap_tipi")
@Getter
@Setter
@NoArgsConstructor
public class HesapTipi {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_hesap_tipi")
    @SequenceGenerator(name = "seq_hesap_tipi", sequenceName = "seq_hesap_tipi", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "ortak_payli_belge")
    private Integer ortakPayliBelge;

}
