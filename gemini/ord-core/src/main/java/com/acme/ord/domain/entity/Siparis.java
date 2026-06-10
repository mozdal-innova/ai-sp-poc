package com.acme.ord.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

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

    @Column(name = "belge_id")
    private Long belgeId;

    @Column(name = "hesap_id")
    private Long hesapId;

    @Column(name = "siparis_durum_id")
    private Long siparisDurumId;

    @Column(name = "siparis_tipi_id")
    private Long siparisTipiId;

    @Column(name = "uygulama")
    private Integer uygulama;

    @Column(name = "tanitan_kullanici_id")
    private Long tanitanKullaniciId;
    
    @Column(name = "toplam_tutar")
    private BigDecimal toplamTutar;

    @Column(name = "tanitim_zamani")
    private Date tanitimZamani;
    
    @Column(name = "guncelleyen_kullanici_id")
    private Long guncelleyenKullaniciId;
    
    @Column(name = "guncelleme_zamani")
    private Date guncellemeZamani;
}
