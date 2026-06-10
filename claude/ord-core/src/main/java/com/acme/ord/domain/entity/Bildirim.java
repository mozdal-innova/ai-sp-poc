package com.acme.ord.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "bildirim")
@SequenceGenerator(name = "seq_bildirim", sequenceName = "seq_bildirim", allocationSize = 1)
public class Bildirim {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_bildirim")
    @Column(name = "id")
    private Long id;

    @Column(name = "durum_id")
    private Long durumId;

    @Column(name = "tanitim_zamani")
    private LocalDateTime tanitimZamani;

    @Column(name = "guncelleme_zamani")
    private LocalDateTime guncellemeZamani;
}
