create TYPE "T_SIPARIS_ISTEK_FDY_TYPE" as object
(
  urun_seri_no       NVARCHAR2(30),
  kalem_tipi_id      NUMBER (18),
  tedarikci_id       NUMBER (8),
  siparis_tutari     NUMBER (20,6),
  kdv_orani          NUMBER (10,4),
  otv_orani          NUMBER (10,4),
  paket_tipi_id      NUMBER (18),
  bayi_kodu          NUMBER,
  kampanya_kodu      NUMBER,
  taksitliSatisID    NUMBER(18),
  taksitNo           NUMBER(2)
);

/
