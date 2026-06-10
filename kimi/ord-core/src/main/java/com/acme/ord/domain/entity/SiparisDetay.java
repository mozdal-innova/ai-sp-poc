package com.acme.ord.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "siparis_detay")
@Getter
@Setter
@NoArgsConstructor
public class SiparisDetay {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_siparis_detay")
    @SequenceGenerator(name = "seq_siparis_detay", sequenceName = "seq_siparis_detay", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "belge_id")
    private Long belgeId;

    @Column(name = "kalem_tipi_id")
    private Long kalemTipiId;

    @Column(name = "tedarikci_id")
    private Long tedarikciId;

    @Column(name = "orjinal_tutar", precision = 24, scale = 6)
    private BigDecimal orjinalTutar;

    @Column(name = "siparis_duzeltme_tutar", precision = 24, scale = 6)
    private BigDecimal siparisDuzeltmeTutar;

    @Column(name = "kalan_tutar", precision = 24, scale = 6)
    private BigDecimal kalanTutar;

    @Column(name = "kdv_orani", precision = 10, scale = 4)
    private BigDecimal kdvOrani;

    @Column(name = "otv_orani", precision = 10, scale = 4)
    private BigDecimal otvOrani;

    @Column(name = "urun_seri_no")
    private String urunSeriNo;

    @Column(name = "paket_tipi")
    private Long paketTipi;

    @Column(name = "kampanya_id")
    private Long kampanyaId;

    @Column(name = "bayi_kodu")
    private Long bayiKodu;

    @Column(name = "taksitli_satis_id")
    private Long taksitliSatisId;

    @Column(name = "taksit_no")
    private Integer taksitNo;

    @Column(name = "guncelleme_zamani")
    private Instant guncellemeZamani;

    @Column(name = "guncelleyen_kullanici_id")
    private Long guncelleyenKullaniciId;

}
