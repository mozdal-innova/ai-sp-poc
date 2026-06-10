package com.acme.ord.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "stok_analiz_detay")
public class StokAnalizDetay {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "stok_analiz_id")
    private Long stokAnalizId;
}
