package com.acme.ord.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "siparis_hareket")
@Getter
@Setter
@NoArgsConstructor
public class SiparisHareket {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_siparis_hareket")
    @SequenceGenerator(name = "seq_siparis_hareket", sequenceName = "seq_siparis_hareket", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "siparis_id")
    private Long siparisId;

    @Column(name = "islem_tipi")
    private Long islemTipi;

    @Column(name = "islem_id")
    private Long islemId;

    @Column(name = "islem_kodu")
    private Long islemKodu;

    @Column(name = "siparis_hareket_durum_id")
    private Long siparisHareketDurumId;

    @Column(name = "kurum_id")
    private Long kurumId;
    
    @Column(name = "kurum_kodu")
    private Long kurumKodu;

    @Column(name = "stan")
    private Long stan;

    @Column(name = "islem_kaynagi")
    private Long islemKaynagi;
    
    @Column(name = "business_id")
    private String businessId;
    
    @Column(name = "conversation_id")
    private String conversationId;

    @Column(name = "islem_tarihi")
    private Date islemTarihi;
    
    @Column(name = "mutabakat_tarihi")
    private Date mutabakatTarihi;
    
    @Column(name = "islem_kabul_tarihi")
    private Date islemKabulTarihi;

    @Column(name = "orig_il")
    private Integer origIl;

    @Column(name = "orig_sube")
    private String origSube;

    @Column(name = "orig_gise")
    private String origGise;

    @Column(name = "orig_kullanici")
    private String origKullanici;

    @Column(name = "tanitim_kullanici")
    private Long tanitimKullanici;

    @Column(name = "tanitan_kullanici_id")
    private Long tanitanKullaniciId;
    
    @Column(name = "uygulama")
    private Integer uygulama;
}
