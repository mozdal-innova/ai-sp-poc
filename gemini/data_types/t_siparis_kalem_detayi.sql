create TYPE "T_SIPARIS_KALEM_DETAYI" as object
    (
  siparis_detay_id        number(18),
  kalem_tipi_id           number(18),
  urun_grubu_id           number(18),
  tedarikci_id            number(8),
  paket_tipi_id           number(8),
  orjinal_tutar           number(24,6),
  siparis_duzeltme_tutari number(24,6),
  revize_tutari           number(24,6),
  kdv_orani               number(10,4),
  kdv_tutari              number(24,6),
  otv_orani               number(10,4),
  otv_tutari              number(24,6),
  duzeltme_yapilabilir_mi number(1),
  carpan                  number(1),
  kdv_siparis_detay_id    number,
  otv_siparis_detay_id    number,
  urun_seri_no            nvarchar2(30),
  mahsup_siparis_detay_id number,
  taksitliSatisID         NUMBER(18),
  taksitNo                NUMBER(2)
);

/
