package com.acme.ord.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "stok_analiz")
public class StokAnaliz {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "tip")
    private Integer tip;
}
