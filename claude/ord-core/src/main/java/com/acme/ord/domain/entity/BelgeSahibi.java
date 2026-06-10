package com.acme.ord.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "belge_sahibi")
public class BelgeSahibi {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "belge_id")
    private Long belgeId;

    @Column(name = "hesap_id")
    private Long hesapId;
}
