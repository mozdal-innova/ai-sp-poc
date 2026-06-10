package com.acme.ord.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "siparis_dagitim_rapor")
@SequenceGenerator(name = "seq_siparis_dagitim_rapor", sequenceName = "seq_siparis_dagitim_rapor", allocationSize = 1)
public class SiparisDagitimRapor {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_siparis_dagitim_rapor")
    @Column(name = "id")
    private Long id;

    @Column(name = "siparis_sahibi_id")
    private Long siparisSahibiId;

    @Column(name = "sirket_id")
    private Long sirketId;

    @Column(name = "rapor_tarihi")
    private LocalDate raporTarihi;

    @Column(name = "toplam_tutar", precision = 24, scale = 6)
    private BigDecimal toplamTutar;

    @Column(name = "tanitim_zamani")
    private LocalDateTime tanitimZamani;

    @Column(name = "guncelleme_zamani")
    private LocalDateTime guncellemeZamani;
}
