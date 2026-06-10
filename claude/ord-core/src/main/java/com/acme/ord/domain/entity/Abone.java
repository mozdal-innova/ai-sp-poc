package com.acme.ord.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "abone")
public class Abone {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "hesap_id")
    private Long hesapId;

    @Column(name = "hizmet_no")
    private String hizmetNo;
}
