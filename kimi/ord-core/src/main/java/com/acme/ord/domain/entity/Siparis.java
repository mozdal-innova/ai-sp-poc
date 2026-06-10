package com.acme.ord.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "siparis")
@Getter
@Setter
@NoArgsConstructor
public class Siparis {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_siparis")
    @SequenceGenerator(name = "seq_siparis", sequenceName = "seq_siparis", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "siparis_durum_id")
    private Long siparisDurumId;

    @Column(name = "siparis_sahibi_id")
    private Long siparisSahibiId;

    @Column(name = "siparis_tipi_id")
    private Long siparisTipiId;

    @Column(name = "belge_id")
    private Long belgeId;

    @Column(name = "alacak_id")
    private Long alacakId;

    @Column(name = "istek_deneme_sayisi")
    private Integer istekDenemeSayisi;

    @Column(name = "siparis_tutari", precision = 20, scale = 2)
    private BigDecimal siparisTutari;

    @Column(name = "uygulama")
    private Integer uygulama;

    @Column(name = "cevap_kodu")
    private String cevapKodu;

    @Column(name = "cevap_mesaji")
    private String cevapMesaji;

    @Column(name = "surec_talep_id")
    private Long surecTalepId;

    @Column(name = "siparis_referans_no")
    private String siparisReferansNo;

    @Column(name = "istek_hesap_belge_sayisi")
    private Integer istekHesapBelgeSayisi;

    @Column(name = "tanitim_zamani")
    private Instant tanitimZamani;

    @Column(name = "tanitan_kullanici_id")
    private Long tanitanKullaniciId;

    @Column(name = "guncelleme_zamani")
    private Instant guncellemeZamani;

    @Column(name = "siparis_tarihi")
    private Instant siparisTarihi;

    @Column(name = "munferit_siparis_tutari", precision = 20, scale = 2)
    private BigDecimal munferitSiparisTutari;

}
