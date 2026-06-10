create TYPE "T_SIPARIS_ISTEK_DETAY_TYPE" as object
(
  tdk_mi                   number(1),
  vergi_haric              number(1),
  siparis_sebebi_id        number(18),
  aciklama                 nvarchar2(255),
  siparis_referans_no      nvarchar2(20),
  teslimat_talep_id        number,
  siparis_belge_no         nvarchar2(20),
  vergi_dairesi            nvarchar2(50),
  vergi_no                 number,
  sebep_bildirim_no        nvarchar2(20),
  sebep_kullanici_sicil_no nvarchar2(20),
  sebep_bayi_kodu          nvarchar2(20),
  sebep_cagri_merkezi      nvarchar2(20),
  erp_gonderilecek_mi      number,
  erp_iban                 nvarchar2(34),
  erp_alici_ad_soyad       nvarchar2(255),
  erp_alici_telefon        nvarchar2(15),
  erp_aciklama             nvarchar2(255),
  cid                      number,
  islem_tarihi             date,
  temsilci_adi_soyadi      nvarchar2(255),
  temsilci_sicil_no        number,
  portal_kullanici_kodu    nvarchar2(50),
  portal_kullanici_ad_soyad nvarchar2(255)
);

/
