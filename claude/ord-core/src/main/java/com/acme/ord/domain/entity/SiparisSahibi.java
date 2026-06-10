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
@Table(name = "siparis_sahibi")
@SequenceGenerator(name = "seq_siparis_sahibi", sequenceName = "seq_siparis_sahibi", allocationSize = 1)
public class SiparisSahibi {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_siparis_sahibi")
    @Column(name = "id")
    private Long id;

    @Column(name = "hesap_id")
    private Long hesapId;

    @Column(name = "musteri_id")
    private Long musteriId;

    @Column(name = "sirket_id")
    private Long sirketId;

    @Column(name = "stok_analiz_detay_id")
    private Long stokAnalizDetayId;

    @Column(name = "siparis_sahibi_tipi_id")
    private Long siparisSahibiTipiId;

    @Column(name = "transfere_aktarilan_tutar", precision = 24, scale = 6)
    private BigDecimal transfereAktarilanTutar;

    @Column(name = "kesinti_yapilan_tutar", precision = 24, scale = 6)
    private BigDecimal kesintiYapilanTutar;

    @Column(name = "nakit_talep_tarihi")
    private LocalDate nakitTalepTarihi;

    @Column(name = "siparis_toplam_tutari", precision = 24, scale = 6)
    private BigDecimal siparisToplmTutari;

    @Column(name = "tanitim_zamani")
    private LocalDateTime tanitimZamani;

    @Column(name = "tanitan_kullanici_id")
    private Long tanitanKullaniciId;

    @Column(name = "guncelleme_zamani")
    private LocalDateTime guncellemeZamani;

    @Column(name = "guncelleyen_kullanici_id")
    private Long guncellleyenKullaniciId;
}
