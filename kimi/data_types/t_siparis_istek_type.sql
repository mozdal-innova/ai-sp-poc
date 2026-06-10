create type     T_SIPARIS_ISTEK_TYPE force as object
(
  islem_tarihi                 date,
  kurum_id                     number(6),
  stan                         number(8),
  sirket_id                    NUMBER(18),
  orig_il                      NUMBER(3),
  orig_sube                    nvarchar2(20),
  orig_gise                    nvarchar2(20),
  orig_kullanici               nvarchar2(20),
  kullanici_id                 number,
  islem_sira_no                number,
  siparis_detay_bilgi          T_SIPARIS_ISTEK_DETAY_TYPE,
  ref_islem_tarihi             date,
  ref_stan                     number(8),
  ref_islem_sira_no            number,
  stok_analiz_id               number(18),
  toplam_kalem_sayisi          number,
  siparis_toplam_tutar         number (20,2),
  hesap_no                     nvarchar2(20),
  taksit_no                    number(2),
  belge_no                     number(16),
  donem_kodu                   number(6),
  uygulama                     number(2),
  islem_kaynagi                number(6),
  durum_id                     number,
  islem_kodu                   number,
  business_id                  nvarchar2(255),
  conversation_id              nvarchar2(255),
  ws_detay_list                T_SIPARIS_ISTEK_FDY_TYPE_DIZI,
  kalem_detay_list             T_SIPARIS_KALEM_DETAYI_COLLECTION,
  tahsilat_kurum_kodu          number(6),
  taksitli_mi                  number(1)
);


/
