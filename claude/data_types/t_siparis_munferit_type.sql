create type     "T_SIPARIS_MUNFERIT_TYPE" force as object
( hareket_id        Number(18),
  tahsilat_id       Number,
  t_munferit_list   T_SIPARIS_KALEM_DETAYI_COLLECTION
)
/
