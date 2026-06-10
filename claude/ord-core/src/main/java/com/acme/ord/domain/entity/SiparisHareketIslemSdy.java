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
@Table(name = "siparis_hareket_islem_sdy")
@SequenceGenerator(name = "seq_siparis_hareket_islem_sdy", sequenceName = "seq_siparis_hareket_islem_sdy", allocationSize = 1)
public class SiparisHareketIslemSdy {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_siparis_hareket_islem_sdy")
    @Column(name = "id")
    private Long id;

    @Column(name = "siparis_hareket_id")
    private Long siparisHareketId;

    @Column(name = "siparis_detay_id")
    private Long siparisDetayId;

    @Column(name = "kalem_tipi_id")
    private Long kalemTipiId;

    @Column(name = "tedarikci_id")
    private Long tedarikciId;

    @Column(name = "revize_tutari", precision = 24, scale = 6)
    private BigDecimal revizeTutari;

    @Column(name = "kdv_orani", precision = 10, scale = 4)
    private BigDecimal kdvOrani;

    @Column(name = "kdv_tutari", precision = 24, scale = 6)
    private BigDecimal kdvTutari;

    @Column(name = "otv_orani", precision = 10, scale = 4)
    private BigDecimal otvOrani;

    @Column(name = "otv_tutari", precision = 24, scale = 6)
    private BigDecimal otvTutari;

    @Column(name = "urun_seri_no")
    private String urunSeriNo;

    @Column(name = "paket_tipi_id")
    private Long paketTipiId;

    @Column(name = "taksitli_satis_id")
    private Long taksitliSatisId;

    @Column(name = "taksit_no")
    private Integer taksitNo;

    @Column(name = "aciklama")
    private String aciklama;

    @Column(name = "tanitim_zamani")
    private LocalDateTime tanitimZamani;

    @Column(name = "tanitan_kullanici_id")
    private Long tanitanKullaniciId;

    @Column(name = "guncelleme_zamani")
    private LocalDateTime guncellemeZamani;

    @Column(name = "guncelleyen_kullanici_id")
    private Long guncellleyenKullaniciId;
}
