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
@Table(name = "siparis_sahibi_finans")
public class SiparisSahibiFinans {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "siparis_sahibi_id")
    private Long siparisSahibiId;

    @Column(name = "siparis_sahibi_finans_tipi_id")
    private Long siparisSahibiFinansTipiId;

    @Column(name = "siparis_toplam_tutari", precision = 24, scale = 6)
    private BigDecimal siparisToplmTutari;

    @Column(name = "belge_mahsup_tutari", precision = 24, scale = 6)
    private BigDecimal belgeMahsupTutari;

    @Column(name = "avans_mahsup_tutari", precision = 24, scale = 6)
    private BigDecimal avansMahsupTutari;

    @Column(name = "nakit_siparis_tutari", precision = 24, scale = 6)
    private BigDecimal nakitSiparisTutari;

    @Column(name = "transfere_aktarilan_tutar", precision = 24, scale = 6)
    private BigDecimal transfereAktarilanTutar;

    @Column(name = "kesinti_yapilan_tutar", precision = 24, scale = 6)
    private BigDecimal kesintiYapilanTutar;

    @Column(name = "kalan_alacak_tutari", precision = 24, scale = 6)
    private BigDecimal kalanAlacakTutari;

    @Column(name = "nakit_talep_tarihi")
    private LocalDate nakitTalepTarihi;

    @Column(name = "tanitim_zamani")
    private LocalDateTime tanitimZamani;

    @Column(name = "guncelleme_zamani")
    private LocalDateTime guncellemeZamani;
}
