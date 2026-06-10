package com.acme.ord.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "toplu_siparis_kayitlar")
public class TopluSiparisKayitlar {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "toplu_siparis_id")
    private Long topluSiparisId;

    @Column(name = "hesap_no")
    private String hesapNo;

    @Column(name = "hizmet_no")
    private String hizmetNo;

    @Column(name = "belge_no")
    private Long belgeNo;

    @Column(name = "donem_kodu")
    private Long donemKodu;

    @Column(name = "tdk_flag")
    private Integer tdkFlag;

    @Column(name = "vergi_dahil_flag")
    private Integer vergiDahilFlag;

    @Column(name = "vergi_tc_no")
    private String vergiTcNo;

    @Column(name = "revize_sebebi_id")
    private Long revizeSebebiId;

    @Column(name = "aciklama")
    private String aciklama;

    @Column(name = "siparis_belge_no")
    private String siparisBelgeNo;

    @Column(name = "vergi_dairesi")
    private String vergiDairesi;

    @Column(name = "bildirim_no")
    private String bildirimNo;

    @Column(name = "personel_sicil_no")
    private String personelSicilNo;

    @Column(name = "bayi_kodu")
    private String bayiKodu;

    @Column(name = "cagri_merkezi_id")
    private String cagriMerkeziId;

    @Column(name = "kampanya_id")
    private Long kampanyaId;

    @Column(name = "taksitli_mi")
    private Integer taksitliMi;

    @Column(name = "taksitli_satis_id")
    private Long taksitliSatisId;

    @Column(name = "taksit_no")
    private Integer taksitNo;

    @Column(name = "durum")
    private String durum;

    @Column(name = "hata_mesaji")
    private String hataMesaji;
}
