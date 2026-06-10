package com.acme.ord.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "siparis_durum")
@Getter
@Setter
@NoArgsConstructor
public class SiparisDurum {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_siparis_durum")
    @SequenceGenerator(name = "seq_siparis_durum", sequenceName = "seq_siparis_durum", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "islem_durumu")
    private Long islemDurumu;

}
