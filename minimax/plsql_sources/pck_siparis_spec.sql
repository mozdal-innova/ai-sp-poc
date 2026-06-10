create Package pck_siparis Is

  --<SURUM> Surum 3.34.0 <\SURUM>
  --<SURUM> 4.STOK_TAKIP.0 <\SURUM>
  --<SURUM> 4.KAMPANYA.0 <\SURUM>
  -- <SURUM> 4.taksitli_satis.0 <\SURUM>

  Function siparis_olustur_ws(prm_t_siparis_istek_type_dizi In t_siparis_istek_type_dizi,
                              prm_t_siparis_cevap_type      Out t_siparis_cevap_type,
                              prm_cevap_kodu                In Out Varchar2,
                              prm_error_type                In Out Nocopy database_exception.error_type%Type,
                              prm_error_num                 In Out Nocopy database_exception.error_code%Type,
                              prm_error_str                 In Out Nocopy database_exception.error_message%Type) Return Number;

  Function siparis_detay_kontrol_ekran(prm_belge_id             In belge.id%Type,
                                       prm_hesap_id             In hesap.id%Type,
                                       prm_t_siparis_istek_type In t_siparis_istek_type_dizi,
                                       prm_cevap_kodu           In Out Varchar2,
                                       prm_error_type           In Out Nocopy database_exception.error_type%Type,
                                       prm_error_num            In Out Nocopy database_exception.error_code%Type,
                                       prm_error_str            In Out Nocopy database_exception.error_message%Type) Return Number;

  Function siparis_kontrol_ekran(prm_belge_id             In belge.id%Type,
                                 prm_hesap_id             In hesap.id%Type,
                                 prm_t_siparis_istek_type In t_siparis_istek_type_dizi,
                                 prm_t_siparis_cevap_type Out t_siparis_cevap_type,
                                 prm_cevap_kodu           In Out Varchar2,
                                 prm_error_type           In Out Nocopy database_exception.error_type%Type,
                                 prm_error_num            In Out Nocopy database_exception.error_code%Type,
                                 prm_error_str            In Out Nocopy database_exception.error_message%Type) Return Number;

  -- <SURUM> 4.SATIS.0 <\SURUM>
  Function siparis_isle(prm_belge              In belge%Rowtype,
                        prm_hesap              In hesap%Rowtype,
                        prm_islem_tipi         In siparis_hareket.islem_tipi%Type,
                        prm_t_siparis_istek_type In Out t_siparis_istek_type,
                        prm_t_siparis_cevap_type In Out t_siparis_cevap_type,
                        prm_cevap_kodu           In Out Varchar2,
                        prm_error_type           In Out Nocopy database_exception.error_type%Type,
                        prm_error_num            In Out Nocopy database_exception.error_code%Type,
                        prm_error_str            In Out Nocopy database_exception.error_message%Type) Return Number ;

  Function siparis_yap(prm_belge_id                 In belge.id%Type,
                       prm_t_siparis_istek_type_dizi In t_siparis_istek_type_dizi,
                       prm_t_siparis_cevap_type      Out t_siparis_cevap_type,
                       prm_cevap_kodu                In Out Varchar2,
                       prm_error_type                In Out Nocopy database_exception.error_type%Type,
                       prm_error_num                 In Out Nocopy database_exception.error_code%Type,
                       prm_error_str                 In Out Nocopy database_exception.error_message%Type) Return Number;

  Function siparis_yap_kanal(prm_talep_no                  In teslimat_talep.talep_no%Type,
                             prm_t_siparis_istek_type_dizi In t_siparis_istek_type_dizi,
                             prm_t_siparis_cevap_type      Out t_siparis_cevap_type,
                             prm_cevap_kodu                In Out Varchar2,
                             prm_error_type                In Out Nocopy database_exception.error_type%Type,
                             prm_error_num                 In Out Nocopy database_exception.error_code%Type,
                             prm_error_str                 In Out Nocopy database_exception.error_message%Type) Return Number;

  Function siparis_isle_kanal(prm_sh_id                     In siparis_hareket.id%Type,
                              prm_t_siparis_istek_type_dizi In t_siparis_istek_type_dizi,
                              prm_hareket_id                Out siparis_hareket.id%Type,
                              prm_cevap_kodu                In Out Varchar2,
                              prm_error_type                In Out Nocopy database_exception.error_type%Type,
                              prm_error_num                 In Out Nocopy database_exception.error_code%Type,
                              prm_error_str                 In Out Nocopy database_exception.error_message%Type) Return Number;

  Function teslimat_sonuc_bildir(prm_siparis_hareket_id  siparis_hareket.id%Type,
                                 prm_islem_tarihi        siparis_hareket.islem_tarihi%Type,
                                 prm_kurum_kodu          siparis_hareket.kurum_id%Type,
                                 prm_stan                siparis_hareket.stan%Type,
                                 prm_islem_kodu          siparis_hareket.islem_id%Type,
                                 prm_islem_kaynagi       siparis_hareket.islem_kaynagi%Type,
                                 prm_business_id         siparis_hareket.business_id%Type,
                                 prm_conversation_id     siparis_hareket.conversation_id%Type,
                                 prm_t_siparis_cevap_type In Out t_siparis_cevap_type,
                                 prm_cevap_kodu           In Out Varchar2,
                                 prm_error_type           In Out Nocopy database_exception.error_type%Type,
                                 prm_error_num            In Out Nocopy database_exception.error_code%Type,
                                 prm_error_str            In Out Nocopy database_exception.error_message%Type) Return Number;

  Function stok_hazirlik(prm_sirket_id  In sirket.id%Type,
                         prm_gun        In Number,
                         prm_cevap_kodu In Out Varchar2,
                         prm_error_type In Out Nocopy database_exception.error_type%Type,
                         prm_error_num  In Out Nocopy database_exception.error_code%Type,
                         prm_error_str  In Out Nocopy database_exception.error_message%Type) Return Number;

  Function stok_faaliyet(prm_siparis_sahibi_har_id In siparis_sahibi_hareket.id%Type,
                         prm_cevap_kodu            In Out Varchar2,
                         prm_error_type            In Out Nocopy database_exception.error_type%Type,
                         prm_error_num             In Out Nocopy database_exception.error_code%Type,
                         prm_error_str             In Out Nocopy database_exception.error_message%Type) Return Number;

  Function siparis_iptali(prm_hareket_id       In siparis_hareket.id%Type,
                          prm_mutabakat_tarihi In siparis_hareket.mutabakat_tarihi%Type,
                          prm_stan             In siparis_hareket.stan%Type,
                          prm_islem_kaynagi    In siparis_hareket.islem_kaynagi%Type,
                          prm_islem_kabul_tar  In siparis_hareket.islem_kabul_tarihi%Type,
                          prm_islem_tipi       In siparis_hareket.islem_tipi%Type,
                          prm_orig_il          In siparis_hareket.orig_il%Type,
                          prm_orig_sube        In siparis_hareket.orig_sube%Type,
                          prm_orig_gise        In siparis_hareket.orig_gise%Type,
                          prm_orig_kullanici   In siparis_hareket.orig_kullanici%Type,
                          prm_kullanici        In siparis_hareket.tanitim_kullanici%Type,
                          prmuygulama          In siparis_hareket.uygulama%Type,
                          prm_iptal_hareket_id Out siparis_hareket.id%Type,
                          prm_cevap_kodu       In Out Varchar2,
                          prm_error_type       In Out Nocopy database_exception.error_type%Type,
                          prm_error_num        In Out Nocopy database_exception.error_code%Type,
                          prm_error_str        In Out Nocopy database_exception.error_message%Type)

   Return Number;

   Function siparis_iptali_ws(prm_t_siparis_istek_type In t_siparis_istek_type, -- <SURUM> 4.taksitli_satis.0 <\SURUM>
                              prm_cevap_kodu           In Out Varchar2,
                              prm_error_type           In Out Nocopy database_exception.error_type%Type,
                              prm_error_num            In Out Nocopy database_exception.error_code%Type,
                              prm_error_str            In Out Nocopy database_exception.error_message%Type)
   Return Number;

  Function toplu_siparis_isle(prm_toplu_siparis_id   toplu_siparis.id%Type,
                              prm_kanal_no           Number, -- 0,1,2,3,4
                              prm_kurum_kodu         siparis_hareket.kurum_kodu%Type,
                              prm_orig_il            siparis_hareket.orig_il%Type,
                              prm_orig_sube          siparis_hareket.orig_sube%Type,
                              prm_orig_gise          siparis_hareket.orig_gise%Type,
                              prm_orig_kullanici     siparis_hareket.orig_kullanici%Type,
                              prm_cevap_kodu         In Out Varchar2,
                              prm_error_type         In Out Nocopy database_exception.error_type%Type,
                              prm_error_num          In Out Nocopy database_exception.error_code%Type,
                              prm_error_str          In Out Nocopy database_exception.error_message%Type) Return Number;

  Function toplu_siparis_iptali_isle(prm_toplu_siparis_id  toplu_siparis.id%Type,
                                    prm_iptal_dosya_adi   toplu_siparis.dosya_adi%Type,
                                    prm_kayit_id_list     t_number_dizi,
                                    prm_kurum_kodu        siparis_hareket.kurum_kodu%Type,
                                    prm_orig_il           siparis_hareket.orig_il%Type,
                                    prm_orig_sube         siparis_hareket.orig_sube%Type,
                                    prm_orig_gise         siparis_hareket.orig_gise%Type,
                                    prm_orig_kullanici    siparis_hareket.orig_kullanici%Type,
                                    prm_kullanici_id      siparis_hareket.tanitim_kullanici%Type,
                                    prm_cevap_kodu        In Out Varchar2,
                                    prm_error_type        In Out Nocopy database_exception.error_type%Type,
                                    prm_error_num         In Out Nocopy database_exception.error_code%Type,
                                    prm_error_str         In Out Nocopy database_exception.error_message%Type) Return Number;

  Function siparis_oncesi_degerlendir(prm_token        toplu_siparis_kayitlar_tmp.token%Type,
                                     prm_kanal_no     Number, -- 0,1,2,3,4
                                     prm_kurum_kodu   In siparis_hareket.kurum_kodu%Type,
                                     prm_kullanici_id In siparis_hareket.tanitim_kullanici%Type,
                                     prm_cevap_kodu   In Out Varchar2,
                                     prm_error_type   In Out Nocopy database_exception.error_type%Type,
                                     prm_error_num    In Out Nocopy database_exception.error_code%Type,
                                     prm_error_str    In Out Nocopy database_exception.error_message%Type) Return Number;

  Procedure teslimat_hareket_at(prm_hareket_id In siparis_hareket.id%Type,
                                prm_islem_id   siparis_sahibi_hareket.islem_id%Type,
                                prm_tb_id      siparis_hareket_tahsilat_bilgi.alacak_id%Type);

  Procedure teslimat_hareket_isle(prm_bas_zamani siparis_sahibi_hareket.islem_tarihi%Type,
                                  prm_bit_zamani siparis_sahibi_hareket.islem_tarihi%Type,
                                  prm_sirket_id  siparis_sahibi.sirket_id%Type);

  Function rutin_bilgilendirme(prm_baslangic_tarihi Date,
                               prm_bitis_tarihi     Date,
                               prm_sirket_id        sirket.id%Type,
                               prm_cevap_kodu       In Out Varchar2,
                               prm_error_str        In Out Nocopy database_exception.error_message%Type) Return Number;

  Function siparis_onay(prm_siparis_id             In siparis.id%Type,
                        prm_siparis_hareket_id     In siparis_hareket.id%Type,
                        prm_hareket_id             In siparis_hareket.id%Type,
                        prm_alacak_id              In tahakkuk_bildirim.id%Type,
                        prm_vergi_no               In siparis_detay.vergi_no%Type,
                        prm_vergi_dairesi          In siparis_detay.vergi_dairesi%Type,
                        prm_siparis_belge_no       In siparis_detay.siparis_belge_no%Type,
                        prm_kullanici_id           In siparis.tanitan_kullanici_id%Type,
                        prm_taksitli_mi            In siparis_detay.taksitli_mi%Type,  --<SURUM> 4.STOK_TAKIP.0 <\SURUM>
                        prm_cevap_kodu             In Out Varchar2,
                        prm_error_type             In Out Nocopy database_exception.error_type%Type,
                        prm_error_num              In Out Nocopy database_exception.error_code%Type,
                        prm_error_str              In Out Nocopy database_exception.error_message%Type) Return Number;

  Function munferitle_siparis_yap(prm_sh_id                      In siparis_hareket.id%Type,
                                  prm_t_siparis_munferit_dizi     In t_siparis_munferit_type_dizi,
                                  prm_t_siparis_istek_type_dizi   In t_siparis_istek_type_dizi,
                                  prm_t_siparis_cevap_type        Out t_siparis_cevap_type,
                                  prm_cevap_kodu                  In Out Varchar2,
                                  prm_error_type                  In Out Nocopy database_exception.error_type%Type,
                                  prm_error_num                   In Out Nocopy database_exception.error_code%Type,
                                  prm_error_str                   In Out Nocopy database_exception.error_message%Type) Return Number;

  Procedure dagitim_detay_al(prm_sh_id            In siparis_hareket.id%Type,
                             prm_siparis_tutari    In Out siparis_sahibi_finans.siparis_toplam_tutari%Type,
                             prm_belge_mah_tutar   In Out siparis_sahibi_finans.belge_mahsup_tutari%Type,
                             prm_avans_mah_tutar   In Out siparis_sahibi_finans.avans_mahsup_tutari%Type,
                             prm_nakit_tutar       In Out siparis_sahibi_finans.nakit_siparis_tutari%Type,
                             prm_transfer_tutar    In Out siparis_sahibi_finans.transfere_aktarilan_tutar%type,  --<SURUM> 4.KAMPANYA.0 <\SURUM>
                             prm_kesinti_tutar     In Out siparis_sahibi_finans.kesinti_yapilan_tutar%type,  --<SURUM> 4.KESINTI.0 <\SURUM>
                             prm_kalan_alacak      In Out siparis_sahibi_finans.kalan_alacak_tutari%Type,
                             prm_nakit_talep_tarihi In Out siparis_sahibi_finans.nakit_talep_tarihi%Type); -- <SURUM> 4.35.0 <\SURUM>

  Function dagitim_raporu_olustur(prm_sirket_id  siparis_sahibi.sirket_id%Type,
                                  prm_cevap_kodu In Out Varchar2,
                                  prm_error_str  In Out Nocopy database_exception.error_message%Type) Return Number;

End pck_siparis;
/
