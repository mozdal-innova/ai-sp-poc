package com.acme.ord.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "kalem_tipi")
public class KalemTipi {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "urun_grubu_id")
    private Long urunGrubuId;

    @Column(name = "duzeltme_yapilabilir_mi")
    private Integer duzeltmeYapilabilirMi;

    @Column(name = "carpan")
    private Integer carpan;
}
