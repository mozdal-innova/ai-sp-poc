package com.acme.ord.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "siparis_sahibi_finans")
@Getter
@Setter
@NoArgsConstructor
public class SiparisSahibiFinans {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_siparis_sahibi_finans")
    @SequenceGenerator(name = "seq_siparis_sahibi_finans", sequenceName = "seq_siparis_sahibi_finans", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "siparis_sahibi_id")
    private Long siparisSahibiId;

    @Column(name = "finans_tipi")
    private Long finansTipi;

    @Column(name = "siparis_toplam_tutari", precision = 20, scale = 2)
    private BigDecimal siparisToplamTutari;

    @Column(name = "belge_mahsup_tutari", precision = 20, scale = 2)
    private BigDecimal belgeMahsupTutari;

    @Column(name = "avans_mahsup_tutari", precision = 20, scale = 2)
    private BigDecimal avansMahsupTutari;

    @Column(name = "nakit_siparis_tutari", precision = 20, scale = 2)
    private BigDecimal nakitSiparisTutari;

    @Column(name = "kalan_alacak_tutari", precision = 20, scale = 2)
    private BigDecimal kalanAlacakTutari;

    @Column(name = "transfere_aktarilan_tutar", precision = 20, scale = 2)
    private BigDecimal transfereAktarilanTutar;

    @Column(name = "kesinti_yapilan_tutar", precision = 20, scale = 2)
    private BigDecimal kesintiYapilanTutar;

    @Column(name = "nakit_talep_tarihi")
    private LocalDate nakitTalepTarihi;

    @Column(name = "tanitim_zamani")
    private Instant tanitimZamani;

    @Column(name = "tanitan_kullanici_id")
    private Long tanitanKullaniciId;

    @Column(name = "guncelleme_zamani")
    private Instant guncellemeZamani;

    @Column(name = "guncelleyen_kullanici_id")
    private Long guncelleyenKullaniciId;

}
