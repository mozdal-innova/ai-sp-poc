package com.acme.ord.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "paket_tipi")
public class PaketTipi {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "aciklama_json")
    private String aciklamaJson;
}
