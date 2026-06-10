package com.acme.ord.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "hesap_tipi")
public class HesapTipi {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "ortak_payli_belge")
    private Integer ortakPayliBelge;
}
