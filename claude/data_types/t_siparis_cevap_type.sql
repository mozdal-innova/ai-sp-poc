create type     "T_SIPARIS_CEVAP_TYPE" as object
(
  stok_analiz_id               number,
  stok_analiz_detay_id         number,
  hesap_id                     number,
  musteri_id                   number,
  siparis_sahibi_id            number,
  siparis_id                   number,
  siparis_hareket_id           number,
  siparis_detay_id             number,
  islem_id                     number,
  hareket_id                   number,
  tahsilat_id                  number,
  siparis_tutari               number (20,2),
  alacak_tutari                number (20,2),
  islenen_kayit_sayisi         number,
  islenen_kayit_tutari         number(24,6),
  kalan_kayit_sayisi           number,
  onay_bekleyen_adeti          number,
  dekont_grup_no               number,
  hesap_no                     nvarchar2(20)
)
/
