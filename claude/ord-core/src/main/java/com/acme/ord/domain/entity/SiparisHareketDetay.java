package com.acme.ord.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "siparis_hareket_detay")
@SequenceGenerator(name = "seq_siparis_hareket_detay", sequenceName = "seq_siparis_hareket_detay", allocationSize = 1)
public class SiparisHareketDetay {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_siparis_hareket_detay")
    @Column(name = "id")
    private Long id;

    @Column(name = "siparis_hareket_id")
    private Long siparisHareketId;

    @Column(name = "siparis_detay_id")
    private Long siparisDetayId;

    @Column(name = "islem_tutari", precision = 24, scale = 6)
    private BigDecimal islemTutari;

    @Column(name = "tanitim_zamani")
    private LocalDateTime tanitimZamani;

    @Column(name = "guncelleme_zamani")
    private LocalDateTime guncellemeZamani;
}
