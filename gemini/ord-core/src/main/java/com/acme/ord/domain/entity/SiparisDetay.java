package com.acme.ord.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

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

    @Column(name = "siparis_id")
    private Long siparisId;
    
    @Column(name = "urun_seri_no")
    private String urunSeriNo;
    
    @Column(name = "paket_tipi")
    private Long paketTipi;
    
    @Column(name = "kampanya_id")
    private Long kampanyaId;

    @Column(name = "siparis_belge_no")
    private String siparisBelgeNo;

    @Column(name = "vergi_no")
    private Long vergiNo;

    @Column(name = "vergi_dairesi")
    private String vergiDairesi;

    @Column(name = "taksitli_mi")
    private Integer taksitliMi;

    @Column(name = "islem_durumu")
    private Long islemDurumu;

    @Column(name = "kalan_tutar")
    private BigDecimal kalanTutar;

    @Column(name = "tanitan_kullanici_id")
    private Long tanitanKullaniciId;
    
    @Column(name = "guncelleyen_kullanici_id")
    private Long guncelleyenKullaniciId;
    
    @Column(name = "tanitim_zamani")
    private Date tanitimZamani;
    
    @Column(name = "guncelleme_zamani")
    private Date guncellemeZamani;
}
