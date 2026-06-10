package com.acme.ord.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "siparis_durum")
public class SiparisDurum {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "islem_durumu")
    private Integer islemDurumu;
}
