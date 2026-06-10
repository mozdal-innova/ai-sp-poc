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
@Table(name = "siparis_sahibi_hareket")
public class SiparisSahibiHareket {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "siparis_sahibi_id")
    private Long siparisSahibiId;

    @Column(name = "islem_id")
    private Long islemId;

    @Column(name = "islem_tarihi")
    private LocalDateTime islemTarihi;
}
