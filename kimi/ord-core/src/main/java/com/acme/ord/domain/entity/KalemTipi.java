package com.acme.ord.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "kalem_tipi")
@Getter
@Setter
@NoArgsConstructor
public class KalemTipi {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_kalem_tipi")
    @SequenceGenerator(name = "seq_kalem_tipi", sequenceName = "seq_kalem_tipi", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "urun_grubu_id")
    private Long urunGrubuId;

    @Column(name = "duzeltme_yapilabilir_mi")
    private Integer duzeltmeYapilabilirMi;

    @Column(name = "carpan")
    private Integer carpan;

}
