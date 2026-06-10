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
@Table(name = "toplu_siparis")
public class TopluSiparis {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "sirket_id")
    private Long sirketId;

    @Column(name = "dosya_adi")
    private String dosyaAdi;

    @Column(name = "durum_id")
    private Long durumId;

    @Column(name = "tanitim_zamani")
    private LocalDateTime tanitimZamani;

    @Column(name = "guncelleme_zamani")
    private LocalDateTime guncellemeZamani;
}
