package com.acme.ord.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "bildirim")
@Getter
@Setter
@NoArgsConstructor
public class Bildirim {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_bildirim")
    @SequenceGenerator(name = "seq_bildirim", sequenceName = "seq_bildirim", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "belge_id")
    private Long belgeId;

    @Column(name = "bildirim_durum")
    private String bildirimDurum;

}
