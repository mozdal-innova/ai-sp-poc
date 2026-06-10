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
@Table(name = "siparis_detay")
public class SiparisDetay {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "belge_id")
    private Long belgeId;

    @Column(name = "kalem_tipi_id")
    private Long kalemTipiId;

    @Column(name = "tedarikci_id")
    private Long tedarikciId;

    @Column(name = "urun_seri_no")
    private String urunSeriNo;

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

    @Column(name = "paket_tipi")
    private Long paketTipi;

    @Column(name = "bayi_kodu")
    private Long bayiKodu;

    @Column(name = "kampanya_id")
    private Long kampanyaId;

    @Column(name = "taksitli_satis_id")
    private Long taksitliSatisId;

    @Column(name = "taksit_no")
    private Integer taksitNo;

    @Column(name = "vergi_no")
    private Long vergiNo;

    @Column(name = "vergi_dairesi")
    private String vergiDairesi;

    @Column(name = "siparis_belge_no")
    private String siparisBelgeNo;

    @Column(name = "taksitli_mi")
    private Integer taksitliMi;

    @Column(name = "guncelleme_zamani")
    private LocalDateTime guncellemeZamani;

    @Column(name = "guncelleyen_kullanici_id")
    private Long guncellleyenKullaniciId;
}
