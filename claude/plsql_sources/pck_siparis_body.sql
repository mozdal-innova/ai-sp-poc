create Package Body pck_siparis Is

  --<SURUM> Surum 3.34.0 <\SURUM>
  --<SURUM> Surum 3.35.0 <\SURUM>
  --<SURUM> Surum 3.36.0 <\SURUM>
  --<SURUM> Surum 3.40.0 <\SURUM>
  --<SURUM> 3.49.0 <\SURUM>
  --<SURUM> 3.50.0 <\SURUM>
  --<SURUM> 3.55.0 <\SURUM>
  --<SURUM> 3.57.0 <\SURUM>
  -- <SURUM> 3.67.0 <\SURUM>
  -- <SURUM> 4.6.0 <\SURUM>
  -- <SURUM> 4.9.0 <\SURUM>
  --<SURUM> 4.13.0 <\SURUM>
  -- <SURUM> 4.16.1 <\SURUM>
  -- <SURUM> 4.16.3 <\SURUM>
  --<SURUM> 4.19.0 <\SURUM>
  --<SURUM> 4.20.0 <\SURUM>
  -- <SURUM> 4.23.0 <\SURUM>
  -- <SURUM> 4.32.1 <\SURUM>
  -- <SURUM> 4.35.0 <\SURUM>
  -- <SURUM> 4.40.0 <\SURUM>
  -- <SURUM> 4.PORTAL_FAZ2.0 <\SURUM>
  -- <SURUM> 4.taksitli_satis.0 <\SURUM>
  -- <SURUM> 4.DIJITAL_EKRAN3.0 <\SURUM>

  c_tolerans_toplam_limit Constant Number(5, 3) := 0.02;
  c_kalem_kdv             Constant Number(2) := 3;
  c_kalem_otv             Constant Number(2) := 4;
  c_kalem_mahsup          Constant Number(2) := 10;
  c_kalem_temlik          Constant Number(2) := 26;

  Type toplusipcolrecord Is Record(
    toplu_siparis_id       toplu_siparis_kayitlar.toplu_siparis_id%Type,
    hesap_no               toplu_siparis_kayitlar.hesap_no%Type,
    hizmet_no              toplu_siparis_kayitlar.hizmet_no%Type,
    belge_no               toplu_siparis_kayitlar.belge_no%Type,
    donem_kodu             toplu_siparis_kayitlar.donem_kodu%Type,
    hesap_id               hesap.id%Type,
    hesap_tipi_id          hesap.hesap_tipi_id%Type,
    musteri_id             hesap.musteri_id%Type,
    belge_id               belge.id%Type,
    sirket_id              toplu_siparis.sirket_id%Type,
    tdk_flag               toplu_siparis_kayitlar.tdk_flag%Type,
    vergi_haric            toplu_siparis_kayitlar.vergi_dahil_flag%Type,
    vergi_tckn             toplu_siparis_kayitlar.vergi_tc_no%Type,
    revize_sebebi_id       toplu_siparis_kayitlar.revize_sebebi_id%Type,
    aciklama               toplu_siparis_kayitlar.aciklama%Type,
    siparis_belge_no       toplu_siparis_kayitlar.siparis_belge_no%Type,
    vergi_dairesi          toplu_siparis_kayitlar.vergi_dairesi%Type,
    bildirim_no            toplu_siparis_kayitlar.bildirim_no%Type,
    personel_sicil_no      toplu_siparis_kayitlar.personel_sicil_no%Type,
    bayi_kodu              toplu_siparis_kayitlar.bayi_kodu%Type,
    cagri_merkezi_id       toplu_siparis_kayitlar.cagri_merkezi_id%Type,
    kampanya_id            toplu_siparis_kayitlar.kampanya_id%Type,
    taksitli_mi            toplu_siparis_kayitlar.taksitli_mi%Type,
    taksitli_satis_id      toplu_siparis_kayitlar.taksitli_satis_id%Type,
    taksit_no              toplu_siparis_kayitlar.taksit_no%Type);

  Type toplusipcolcollection Is Table Of toplusipcolrecord Index By Binary_Integer;

  Type r_sd_knt_donus_rec Is Record(
    id           siparis_detay.id%Type,
    ist_kmp_kodu siparis_detay.kampanya_id%Type);
  Type t_sd_knt_donus_col Is Table Of r_sd_knt_donus_rec Index By Binary_Integer;

  function siparis_belge_no_uret(prm_siparis_hareket_id siparis_hareket.id%Type)
    return varchar2 is
    l_siparis_durum_id  siparis.siparis_durum_id%Type;
    l_siparis_belge_no  siparis_detay.siparis_belge_no%Type;
  begin
    l_siparis_belge_no := null;
    select s.siparis_durum_id into l_siparis_durum_id
      from siparis s, siparis_hareket sh
     where s.id = sh.siparis_id and sh.id = prm_siparis_hareket_id;
    if l_siparis_durum_id = pck_def.c_id_onay_bekliyor then
      l_siparis_belge_no := 'SIP' || to_char(sysdate, 'YYYY') ||
                            lpad(to_char(prm_siparis_hareket_id), 12, '0');
    end if;
    return l_siparis_belge_no;
  exception
    when others then return null;
  end;

  Function sanal_hizmet_no_al(prm_hesap_id In hesap.id%Type,
                              prm_sanal_mi In hesap.sanal_mi%Type,
                              prm_hesap_no In hesap.hesap_no%Type) Return Varchar2 Is
    l_hizmet_no abone.hizmet_no%Type := prm_hesap_no;
  Begin
    If nvl(prm_sanal_mi, pck_def.c_hayir) = pck_def.c_evet Then
      Select hizmet_no Into l_hizmet_no From abone a Where a.hesap_id = prm_hesap_id And rownum = 1;
    End If;
    Return l_hizmet_no;
  Exception
    When Others Then Return l_hizmet_no;
  End;

  Function numberscale6tochar(prm_deger siparis_detay.kalan_tutar%Type) Return Varchar2 Is
  Begin Return to_char(prm_deger, 'FM999999999999999990.999999'); End;

  Function numberscale2tochar(prm_deger belge.kalan_tutar%Type) Return Varchar2 Is
  Begin Return to_char(prm_deger, 'FM999999999999999990.99'); End;

  Procedure siparis_detay_urun_serino_kntrl(prm_belge In belge%Rowtype,
                                            prm_t_siparis_istek_type In Out t_siparis_istek_type) Is
    e_aksiyon_alma Exception; l_urun_seri_no Number;
  Begin
    If prm_belge.tanitim_zaman > to_date('29.02.2020', 'dd.MM.yyyy') Or
       prm_belge.tanitim_zaman < to_date('22.06.2014', 'dd.MM.yyyy') Then Raise e_aksiyon_alma; End If;
    If prm_t_siparis_istek_type.ws_detay_list Is Null Or
       prm_t_siparis_istek_type.ws_detay_list.count = 0 Then Raise e_aksiyon_alma; End If;
    For i In prm_t_siparis_istek_type.ws_detay_list.first .. prm_t_siparis_istek_type.ws_detay_list.last Loop
      Begin
        l_urun_seri_no := to_number(prm_t_siparis_istek_type.ws_detay_list(i).urun_seri_no);
        prm_t_siparis_istek_type.ws_detay_list(i).urun_seri_no := to_char(l_urun_seri_no);
      Exception When value_error Then Null; End;
    End Loop;
  Exception
    When e_aksiyon_alma Then Null;
    When Others Then Null;
  End;

  Procedure sd_kampanya_kodu_ekle(prm_siparis_detay_id In siparis_detay.id%Type,
                                  prm_kampanya_kodu    In siparis_detay.kampanya_id%Type,
                                  prm_sd_kontrol_col   In Out t_sd_knt_donus_col) Is
    l_idx Number;
  Begin
    l_idx := prm_sd_kontrol_col.count + 1;
    prm_sd_kontrol_col(l_idx).id           := prm_siparis_detay_id;
    prm_sd_kontrol_col(l_idx).ist_kmp_kodu := prm_kampanya_kodu;
  End;


  -- =========================================================================
  -- siparis_kalem_kontrol
  -- Belge uzerindeki kalemlerin kontrol islemini gerceklestirir
  -- =========================================================================
  Function siparis_kalem_kontrol(prm_belge In belge%Rowtype,
                                 prm_hesap In hesap%Rowtype,
                                 prm_t_siparis_istek_type In Out t_siparis_istek_type,
                                 prm_sd_kontrol_col In Out t_sd_knt_donus_col,
                                 prm_cevap_kodu In Out Varchar2,
                                 prm_error_type In Out Nocopy database_exception.error_type%Type,
                                 prm_error_num  In Out Nocopy database_exception.error_code%Type,
                                 prm_error_str  In Out Nocopy database_exception.error_message%Type)
    Return Number Is

    l_kalem_det_list          t_siparis_kalem_detayi_collection;
    l_ws_detay_list           t_siparis_istek_fdy_type_dizi;
    l_info_msj                Varchar2(4000);
    l_count                   Number;
    l_detay_count             Number;
    l_toplam_tutar            Number(24, 6);
    l_detay_toplam            Number(24, 6);
    l_tedarikci_adeti         Number;
    l_vergi_adeti             Number;
    l_kalem_detay             t_siparis_kalem_detayi;
    l_bloke_tutar             Number(24, 6);
    l_detay_siparis_tutari    Number(24, 2);
    l_tedarikci_toplam        Number(24, 6);
    l_kdv_id                  siparis_detay.id%Type;
    l_otv_id                  siparis_detay.id%Type;
    l_mhsp_id                 siparis_detay.id%Type;
    l_t_number_dizi           t_number_dizi;
    l_kampanya_id             siparis_detay.kampanya_id%Type;
    l_ht_row                  hesap_tipi%Rowtype; --<SURUM> 3.57.0 <\SURUM>
    l_talep_id_list           t_number_dizi;
    l_result                  Number;
    l_paket_tipi_ack_json     paket_tipi.aciklama_json%type;
    l_tedarikci_tipi          tedarikci.tedarikci_tipi_id%type := 0;
    l_taksit_revize_kull      Number := 0;

    e_detay_listesi_bos           Exception;
    e_detay_yok                   Exception;
    e_birden_fazla_detay          Exception;
    e_hesap_paket_tipi            Exception;
    e_tedarikci_revize            Exception;
    e_belge_sahibi_yok            Exception;
    e_tdk_kalem_ozellik           Exception;
    e_bloke_tutar                 Exception;
    e_kalem_carpan_negatif        Exception;
    e_detay_duzeltme_tutar_uymsz  Exception;
    e_negatif_siparis             Exception;
    e_tutarsiz_siparis            Exception;
    e_birden_fazla_kdv_detayi     Exception;
    e_birden_fazla_otv_detayi     Exception;
    e_kdv_detayi                  Exception;
    e_otv_detayi                  Exception;
    e_mahsup_detay_yok            Exception;
    e_onay_bekleyen_var           Exception;
    e_munferit_revize_var         Exception;
    e_munferit_islem_yapilmis     Exception;
    e_kalem_tmlk_kmpny_kod_zorunl Exception;
    e_tekrarli_token              Exception;
    e_opf_tdk_kapatma             Exception;
    e_otomatik_talep_iptal        Exception;
    e_paket_tipi_hatali           Exception;
    e_satis_tedarikci_olamaz      Exception;
    E_DIJITAL_PREPAID_HESAP       Exception;
    e_taksit_tdrk_revize          Exception;

  Begin
    siparis_detay_urun_serino_kntrl(prm_belge, prm_t_siparis_istek_type);
    l_kalem_det_list := prm_t_siparis_istek_type.kalem_detay_list;
    prm_t_siparis_istek_type.siparis_toplam_tutar := round(prm_t_siparis_istek_type.siparis_toplam_tutar, 2);

    If prm_t_siparis_istek_type.ws_detay_list Is Not Null And
       prm_t_siparis_istek_type.ws_detay_list.count > 0 Then
      l_kalem_det_list := New t_siparis_kalem_detayi_collection();
      l_ws_detay_list := prm_t_siparis_istek_type.ws_detay_list;
      For i In l_ws_detay_list.first .. l_ws_detay_list.last Loop
        Begin
          l_info_msj := ' Belge:' || prm_belge.id || ',Urun:' || l_ws_detay_list(i).urun_seri_no ||
                       ',KalemTipi:' || l_ws_detay_list(i).kalem_tipi_id ||
                       ',TedarikciId:' || l_ws_detay_list(i).tedarikci_id;

          if l_ws_detay_list(i).paket_tipi_id is not null Then
            begin
              select aciklama_json into l_paket_tipi_ack_json from paket_tipi where id = l_ws_detay_list(i).paket_tipi_id;
            exception when no_data_found then
              l_info_msj := ' Hatali Paket Tipi ID :' || l_ws_detay_list(i).paket_tipi_id;
              Raise e_paket_tipi_hatali;
            end;
          end if;

          Select t_siparis_kalem_detayi(sd.id, sd.kalem_tipi_id, kt.urun_grubu_id,
                  sd.tedarikci_id, nvl(sd.paket_tipi, l_ws_detay_list(i).paket_tipi_id),
                  sd.orjinal_tutar, sd.siparis_duzeltme_tutar, l_ws_detay_list(i).siparis_tutari,
                  sd.kdv_orani, 0, sd.otv_orani, 0, kt.duzeltme_yapilabilir_mi, kt.carpan,
                  Null, Null, sd.urun_seri_no, Null, sd.taksitli_satis_id, sd.taksit_no),
                 sd.kampanya_id
            Into l_kalem_detay, l_kampanya_id
            From siparis_detay sd, kalem_tipi kt
           Where sd.belge_id = prm_belge.id
             And sd.kalem_tipi_id = kt.id
             And sd.kalem_tipi_id = l_ws_detay_list(i).kalem_tipi_id
             And sd.tedarikci_id = l_ws_detay_list(i).tedarikci_id
             And sd.kdv_orani = l_ws_detay_list(i).kdv_orani
             And sd.otv_orani = l_ws_detay_list(i).otv_orani
             And sd.urun_seri_no = to_char(l_ws_detay_list(i).urun_seri_no)
             And ((prm_belge.sirket_id = pck_def.c_company_ana And (1=1))
                  Or (prm_belge.sirket_id = pck_def.c_company_dijital And (l_ws_detay_list(i).paket_tipi_id Is Null Or (sd.paket_tipi = l_ws_detay_list(i).paket_tipi_id)))
                  Or (prm_belge.sirket_id = pck_def.C_COMPANY_SATIS And (1=1))
                  Or (prm_belge.sirket_id = pck_def.C_COMPANY_PORTAL_DIJITAL And (1=1))
                  Or (prm_belge.sirket_id = pck_def.C_COMPANY_DIJITAL3 And (1=1)))
             And (l_ws_detay_list(i).bayi_kodu Is Null Or (sd.bayi_kodu = l_ws_detay_list(i).bayi_kodu))
             And (sd.kampanya_id Is Null Or (sd.kampanya_id = l_ws_detay_list(i).kampanya_kodu))
             And (l_ws_detay_list(i).taksitNo Is Null Or (sd.taksit_no = l_ws_detay_list(i).taksitNo))
             And (sd.taksitli_satis_id Is Null Or (sd.taksitli_satis_id = l_ws_detay_list(i).taksitliSatisID));

          If l_kalem_detay.urun_grubu_id = c_kalem_temlik Then
            If l_ws_detay_list(i).kampanya_kodu Is Null Then
              l_info_msj := ' Kalem Tipi:' || c_kalem_temlik || ' icin Kampanya Kodu alani zorunludur.';
              Raise e_kalem_tmlk_kmpny_kod_zorunl;
            End If;
            If l_kampanya_id Is Null Then
              sd_kampanya_kodu_ekle(l_kalem_detay.siparis_detay_id, l_ws_detay_list(i).kampanya_kodu, prm_sd_kontrol_col);
            End If;
          End If;
          l_kalem_det_list.extend;
          l_kalem_det_list(l_kalem_det_list.last) := l_kalem_detay;
        Exception
          When too_many_rows Then Raise e_birden_fazla_detay;
          When no_data_found Then Raise e_detay_yok;
          When e_kalem_tmlk_kmpny_kod_zorunl Then Raise e_kalem_tmlk_kmpny_kod_zorunl;
        End;
      End Loop;
    End If;

    if prm_belge.hesap_tipi_id = pck_def.C_HESAP_TIPI_DIJITAL_PREPAID then
      raise E_DIJITAL_PREPAID_HESAP;
    end if;

    If prm_t_siparis_istek_type.siparis_detay_bilgi.tdk_mi = 1 And
       (l_kalem_det_list Is Null Or l_kalem_det_list.count = 0) Then
      Select t_siparis_kalem_detayi(sd.id, sd.kalem_tipi_id, kt.urun_grubu_id,
              sd.tedarikci_id, sd.paket_tipi, sd.orjinal_tutar, sd.siparis_duzeltme_tutar,
              0, sd.kdv_orani, 0, sd.otv_orani, 0, kt.duzeltme_yapilabilir_mi, kt.carpan,
              Null, Null, sd.urun_seri_no, Null, sd.taksitli_satis_id, sd.taksit_no) Bulk Collect
        Into l_kalem_det_list
        From siparis_detay sd, kalem_tipi kt
       Where sd.belge_id = prm_belge.id And sd.kalem_tipi_id = kt.id
         And sd.siparis_duzeltme_tutar <> 0 And kt.urun_grubu_id Not In (c_kalem_kdv, c_kalem_otv);
    End If;

    If l_kalem_det_list Is Null Or l_kalem_det_list.count = 0 Then Raise e_detay_listesi_bos; End If;

    l_detay_siparis_tutari := 0; l_kdv_id := Null; l_otv_id := Null; l_mhsp_id := Null;
    For i In l_kalem_det_list.first .. l_kalem_det_list.last Loop
      If l_kalem_det_list(i).carpan < 0 Then l_kalem_det_list(i).revize_tutari := -abs(l_kalem_det_list(i).revize_tutari); End If;
      If l_kalem_det_list(i).urun_grubu_id = c_kalem_kdv Then
        If l_kdv_id Is Not Null Then Raise e_birden_fazla_kdv_detayi; End If;
        l_kdv_id := l_kalem_det_list(i).siparis_detay_id;
      Elsif l_kalem_det_list(i).urun_grubu_id = c_kalem_otv Then
        If l_otv_id Is Not Null Then Raise e_birden_fazla_otv_detayi; End If;
        l_otv_id := l_kalem_det_list(i).siparis_detay_id;
      Elsif l_kalem_det_list(i).urun_grubu_id = c_kalem_mahsup Then
        l_mhsp_id := l_kalem_det_list(i).siparis_detay_id;
      Else
        l_detay_siparis_tutari := l_detay_siparis_tutari + round(l_kalem_det_list(i).revize_tutari, 2);
      End If;
      If l_kalem_det_list(i).duzeltme_yapilabilir_mi = 0 Then
        If abs(l_kalem_det_list(i).revize_tutari) > abs(l_kalem_det_list(i).siparis_duzeltme_tutari) Then
          l_info_msj := ' DetayId:' || l_kalem_det_list(i).siparis_detay_id;
          Raise e_detay_duzeltme_tutar_uymsz;
        End If;
      End If;
      If l_kalem_det_list(i).carpan > 0 And l_kalem_det_list(i).revize_tutari < 0 Then Raise e_negatif_siparis; End If;
      If l_kalem_det_list(i).carpan < 0 And l_kalem_det_list(i).revize_tutari > 0 Then Raise e_kalem_carpan_negatif; End If;
    End Loop;

    If abs(round(prm_t_siparis_istek_type.siparis_toplam_tutar, 2) - round(l_detay_siparis_tutari, 2)) > c_tolerans_toplam_limit Then
      l_info_msj := ' Siparis toplam tutar:' || numberscale2tochar(prm_t_siparis_istek_type.siparis_toplam_tutar);
      Raise e_tutarsiz_siparis;
    End If;

    Select Count(0) Into l_count From belge_sahibi bs Where bs.belge_id = prm_belge.id And bs.hesap_id = prm_hesap.id;
    If l_count = 0 Then Raise e_belge_sahibi_yok; End If;

    If prm_t_siparis_istek_type.siparis_detay_bilgi.tdk_mi <> 1 Then
      Select t_number(sh.id) Bulk Collect Into l_t_number_dizi
        From siparis_hareket sh, siparis_hareket_detay shd, (Select siparis_detay_id, urun_grubu_id From Table(l_kalem_det_list)) t1
       Where sh.belge_id = prm_belge.id
         And sh.islem_kodu In (pck_def.c_amount_correction, pck_def.c_islem_musteri_memnuniyeti, pck_def.c_islem_makam_onayi)
         And sh.islem_durum_id <> pck_def.c_notsend And sh.ust_id Is Null
         And sh.id = shd.siparis_hareket_id And shd.siparis_detay_id = t1.siparis_detay_id
         And t1.urun_grubu_id Not In (c_kalem_otv, c_kalem_kdv);
      If l_t_number_dizi Is Not Null And l_t_number_dizi.count > 0 Then
        Select Count(0) Into l_count From (Select deger From Table(l_t_number_dizi)) sh, siparis_hareket_tahsilat_bilgi shtb
         Where sh.deger = shtb.siparis_hareket_id And shtb.siparis_tahsilat_bilgi_tipi_id = 4
           And shtb.siparis_detay_id In (Select siparis_detay_id From Table(l_kalem_det_list));
        If l_count > 0 Then Raise e_munferit_islem_yapilmis; End If;
        Raise e_munferit_revize_var;
      End If;
    End If;

    Select Sum(sd.siparis_duzeltme_tutar * kt.carpan) Into l_tedarikci_toplam
      From siparis_detay sd, kalem_tipi kt Where sd.belge_id = prm_belge.id And sd.kalem_tipi_id = kt.id
       And sd.tedarikci_id in (select tedarikci_id From Table(l_kalem_det_list));

    If round(l_detay_siparis_tutari, 2) - round(l_tedarikci_toplam, 2) > 0 Then Raise e_tutarsiz_siparis; End If;

    If prm_t_siparis_istek_type.islem_kodu = pck_def.c_onay_islemi Then
      Select t_number(s.id) Bulk Collect Into l_talep_id_list From siparis s Where s.belge_id = prm_belge.id
         And s.siparis_tipi_id In (pck_def.c_siparis_tipi_mm, pck_def.c_siparis_tipi_mo)
         And s.siparis_durum_id In (pck_def.c_id_mm_yeni_kayit, pck_def.c_id_mm_onay_bekliyor)
         And Exists (Select 1 From siparis_hareket sh, siparis_hareket_islem_sdy sdy,
                     (Select siparis_detay_id From Table(l_kalem_det_list)) t1
               Where sh.siparis_id = s.id And sdy.siparis_hareket_id = sh.id And sdy.siparis_detay_id = t1.siparis_detay_id);
      If l_talep_id_list Is Not Null And l_talep_id_list.count > 0 Then
        l_result := pck_talep_yonetimi.talep_iptal_veya_reddet(l_talep_id_list, 'Onay girisi sebebiyle',
                     pck_def.c_inst_sistem_kullanici_id, pck_def.c_id_mm_iptal_onay,
                     prm_t_siparis_istek_type.uygulama, prm_cevap_kodu, prm_error_type, prm_error_num, prm_error_str);
        If l_result <> pck_def.c_rc_success Then Raise e_otomatik_talep_iptal; End If;
      End If;
    End If;

    prm_t_siparis_istek_type.kalem_detay_list := l_kalem_det_list;
    Return pck_def.c_success;

  Exception
    When e_onay_bekleyen_var Then
      pck_general.set_out_variables(prm_cevap_kodu, pck_def.c_rc_siparis_onay_bekleyen_var, prm_error_type, 'APP', prm_error_num, -20304, prm_error_str, pck_general.get_islem_cevap_ack(pck_def.c_rc_siparis_onay_bekleyen_var));
      Return pck_def.c_fail;
    When e_munferit_islem_yapilmis Then
      pck_general.set_out_variables(prm_cevap_kodu, pck_def.c_rc_siparis_munferit_dahil, prm_error_type, 'APP', prm_error_num, -20304, prm_error_str, pck_general.get_islem_cevap_ack(pck_def.c_rc_siparis_munferit_dahil));
      Return pck_def.c_fail;
    When e_munferit_revize_var Then
      pck_general.set_out_variables(prm_cevap_kodu, pck_def.c_rc_siparis_munferit_revize_var, prm_error_type, 'APP', prm_error_num, -20304, prm_error_str, pck_general.get_islem_cevap_ack(pck_def.c_rc_siparis_munferit_revize_var));
      Return pck_def.c_fail;
    When e_tekrarli_token Then
      pck_general.set_out_variables(prm_cevap_kodu, pck_def.c_rc_tekrarli_token, prm_error_type, 'APP', prm_error_num, -20304, prm_error_str, pck_general.get_islem_cevap_ack(pck_def.c_rc_tekrarli_token));
      Return pck_def.c_fail;
    When e_mahsup_detay_yok Then
      pck_general.set_out_variables(prm_cevap_kodu, pck_def.c_rc_ek_tah_mah_detay_yok, prm_error_type, 'APP', prm_error_num, -20304, prm_error_str, pck_general.get_islem_cevap_ack(pck_def.c_rc_ek_tah_mah_detay_yok) || l_info_msj);
      Return pck_def.c_fail;
    When e_kdv_detayi Then
      pck_general.set_out_variables(prm_cevap_kodu, pck_def.c_rc_siparis_kdv_detay_olmali, prm_error_type, 'APP', prm_error_num, -20304, prm_error_str, pck_general.get_islem_cevap_ack(pck_def.c_rc_siparis_kdv_detay_olmali));
      Return pck_def.c_fail;
    When e_otv_detayi Then
      pck_general.set_out_variables(prm_cevap_kodu, pck_def.c_rc_siparis_otv_detay_olmali, prm_error_type, 'APP', prm_error_num, -20304, prm_error_str, pck_general.get_islem_cevap_ack(pck_def.c_rc_siparis_otv_detay_olmali));
      Return pck_def.c_fail;
    When e_birden_fazla_kdv_detayi Then
      pck_general.set_out_variables(prm_cevap_kodu, pck_def.c_rc_birden_fzla_kdv_var, prm_error_type, 'APP', prm_error_num, -20304, prm_error_str, pck_general.get_islem_cevap_ack(pck_def.c_rc_birden_fzla_kdv_var) || l_info_msj);
      Return pck_def.c_fail;
    When e_birden_fazla_otv_detayi Then
      pck_general.set_out_variables(prm_cevap_kodu, pck_def.c_rc_birden_fzla_otv_var, prm_error_type, 'APP', prm_error_num, -20304, prm_error_str, pck_general.get_islem_cevap_ack(pck_def.c_rc_birden_fzla_otv_var) || l_info_msj);
      Return pck_def.c_fail;
    When e_tutarsiz_siparis Then
      pck_general.set_out_variables(prm_cevap_kodu, pck_def.c_rc_siparis_tutarsiz, prm_error_type, 'APP', prm_error_num, -20304, prm_error_str, pck_general.get_islem_cevap_ack(pck_def.c_rc_siparis_tutarsiz) || l_info_msj);
      Return pck_def.c_fail;
    When e_negatif_siparis Then
      pck_general.set_out_variables(prm_cevap_kodu, pck_def.c_rc_siparis_negatif, prm_error_type, 'APP', prm_error_num, -20304, prm_error_str, pck_general.get_islem_cevap_ack(pck_def.c_rc_siparis_negatif) || l_info_msj);
      Return pck_def.c_fail;
    When e_kalem_carpan_negatif Then
      pck_general.set_out_variables(prm_cevap_kodu, pck_def.c_rc_kalem_carpan_neg, prm_error_type, 'APP', prm_error_num, -20304, prm_error_str, pck_general.get_islem_cevap_ack(pck_def.c_rc_kalem_carpan_neg) || l_info_msj);
      Return pck_def.c_fail;
    When e_detay_duzeltme_tutar_uymsz Then
      pck_general.set_out_variables(prm_cevap_kodu, pck_def.c_rc_detay_duzeltme_uyumsuz, prm_error_type, 'APP', prm_error_num, -20304, prm_error_str, pck_general.get_islem_cevap_ack(pck_def.c_rc_detay_duzeltme_uyumsuz) || l_info_msj);
      Return pck_def.c_fail;
    When e_detay_listesi_bos Then
      pck_general.set_out_variables(prm_cevap_kodu, pck_def.c_rc_siparis_detay_bos, prm_error_type, 'APP', prm_error_num, -20304, prm_error_str, pck_general.get_islem_cevap_ack(pck_def.c_rc_siparis_detay_bos));
      Return pck_def.c_fail;
    When e_detay_yok Then
      pck_general.set_out_variables(prm_cevap_kodu, pck_def.c_rc_siparis_detay_yok, prm_error_type, 'APP', prm_error_num, -20304, prm_error_str, pck_general.get_islem_cevap_ack(pck_def.c_rc_siparis_detay_yok) || l_info_msj);
      Return pck_def.c_fail;
    When e_birden_fazla_detay Then
      pck_general.set_out_variables(prm_cevap_kodu, pck_def.c_rc_siparis_coklu_detay, prm_error_type, 'APP', prm_error_num, -20304, prm_error_str, pck_general.get_islem_cevap_ack(pck_def.c_rc_siparis_coklu_detay) || l_info_msj);
      Return pck_def.c_fail;
    When e_belge_sahibi_yok Then
      pck_general.set_out_variables(prm_cevap_kodu, pck_def.c_rc_belge_sahibi_yok, prm_error_type, 'APP', prm_error_num, -20304, prm_error_str, pck_general.get_islem_cevap_ack(pck_def.c_rc_belge_sahibi_yok));
      Return pck_def.c_fail;
    When e_bloke_tutar Then
      pck_general.set_out_variables(prm_cevap_kodu, pck_def.c_rc_siparis_bloke_tutar, prm_error_type, 'APP', prm_error_num, -20304, prm_error_str, pck_general.get_islem_cevap_ack(pck_def.c_rc_siparis_bloke_tutar) || l_info_msj);
      Return pck_def.c_fail;
    When e_paket_tipi_hatali Then
      pck_general.set_out_variables(prm_cevap_kodu, pck_def.c_rc_paket_tipi_hatali, prm_error_type, 'APP', prm_error_num, -20304, prm_error_str, pck_general.get_islem_cevap_ack(pck_def.c_rc_paket_tipi_hatali) || l_info_msj);
      Return pck_def.c_fail;
    When e_opf_tdk_kapatma Then
      pck_general.set_out_variables(prm_cevap_kodu, pck_def.c_rc_opf_tdk_kapatma, prm_error_type, 'APP', prm_error_num, -20304, prm_error_str, pck_general.get_islem_cevap_ack(pck_def.c_rc_opf_tdk_kapatma));
      Return pck_def.c_fail;
    When e_otomatik_talep_iptal Then
      pck_general.set_out_variables(prm_cevap_kodu, pck_def.c_rc_otomatik_talep_iptal, prm_error_type, 'APP', prm_error_num, -20304, prm_error_str, pck_general.get_islem_cevap_ack(pck_def.c_rc_otomatik_talep_iptal));
      Return pck_def.c_fail;
    When e_satis_tedarikci_olamaz Then
      pck_general.set_out_variables(prm_cevap_kodu, pck_def.c_rc_satis_tedarikci_olamaz, prm_error_type, 'APP', prm_error_num, -20304, prm_error_str, pck_general.get_islem_cevap_ack(pck_def.c_rc_satis_tedarikci_olamaz) || l_info_msj);
      Return pck_def.c_fail;
    When E_DIJITAL_PREPAID_HESAP Then
      pck_general.set_out_variables(prm_cevap_kodu, pck_def.c_rc_dijital_prepaid, prm_error_type, 'APP', prm_error_num, -20304, prm_error_str, pck_general.get_islem_cevap_ack(pck_def.c_rc_dijital_prepaid));
      Return pck_def.c_fail;
    When e_taksit_tdrk_revize Then
      pck_general.set_out_variables(prm_cevap_kodu, pck_def.c_rc_taksit_tedarikci_revize, prm_error_type, 'APP', prm_error_num, -20304, prm_error_str, pck_general.get_islem_cevap_ack(pck_def.c_rc_taksit_tedarikci_revize) || l_info_msj);
      Return pck_def.c_fail;
    When e_kalem_tmlk_kmpny_kod_zorunl Then
      pck_general.set_out_variables(prm_cevap_kodu, pck_def.c_rc_kalem_kampanya_zorunlu, prm_error_type, 'APP', prm_error_num, -20304, prm_error_str, pck_general.get_islem_cevap_ack(pck_def.c_rc_kalem_kampanya_zorunlu) || l_info_msj);
      Return pck_def.c_fail;
    When Others Then
      pck_general.set_out_variables(prm_cevap_kodu, pck_def.c_rc_sql_error, prm_error_type, 'ORA', prm_error_num, Sqlcode, prm_error_str, 'siparis_kalem_kontrol:' || Sqlerrm || l_info_msj);
      Return pck_def.c_fail;
  End siparis_kalem_kontrol;


  -- =========================================================================
  -- siparis_isle: Ana siparis isleme fonksiyonu
  -- Siparis sahibi, siparis ve siparis hareket kayitlarini olusturur
  -- =========================================================================
  Function siparis_isle(prm_belge              In belge%Rowtype,
                        prm_hesap              In hesap%Rowtype,
                        prm_islem_tipi         In siparis_hareket.islem_tipi%Type,
                        prm_t_siparis_istek_type In Out t_siparis_istek_type,
                        prm_t_siparis_cevap_type In Out t_siparis_cevap_type,
                        prm_cevap_kodu           In Out Varchar2,
                        prm_error_type           In Out Nocopy database_exception.error_type%Type,
                        prm_error_num            In Out Nocopy database_exception.error_code%Type,
                        prm_error_str            In Out Nocopy database_exception.error_message%Type) Return Number Is

    l_siparis_sahibi        siparis_sahibi%Rowtype;
    l_siparis               siparis%Rowtype;
    l_siparis_detay         siparis_detay_kayit%Rowtype;
    l_siparis_hareket       siparis_hareket%Rowtype;
    l_belge_row             belge%Rowtype;
    l_hesap_row             hesap%Rowtype;
    l_islem_id              Number;
    l_count                 Number;
    l_sonuc                 Number;
    l_info_msj              Varchar2(4000);
    l_hizmet_no             abone.hizmet_no%Type;
    l_kalem_det_list        t_siparis_kalem_detayi_collection;
    l_sd_kontrol_col        t_sd_knt_donus_col;
    l_siparis_sahibi_finans siparis_sahibi_finans%Rowtype;
    l_tip                   Number;
    l_belge_durum           belge.belge_durum%Type;

    e_belge_bulunamadi        Exception;
    e_hesap_bulunamadi        Exception;
    e_siparis_sahibi_olusmadi Exception;
    e_birden_fazla_sip_sahibi Exception;
    e_kalem_kontrol           Exception;
    e_belge_guncelleme        Exception;
    e_hareket_olusturma       Exception;
    e_resend                  Exception;

  Begin

    l_belge_row := prm_belge;
    l_hesap_row := prm_hesap;
    l_islem_id  := prm_t_siparis_istek_type.islem_kodu;

    -- Resend kontrolu
    If prm_t_siparis_istek_type.islem_kodu <> pck_def.c_islemkodu_revize Then
      Select Count(0) Into l_count
        From siparis_hareket sh
       Where sh.islem_tarihi = prm_t_siparis_istek_type.islem_tarihi
         And sh.kurum_id = prm_t_siparis_istek_type.kurum_id
         And sh.stan = prm_t_siparis_istek_type.stan
         And sh.islem_id = prm_t_siparis_istek_type.islem_kodu;
      If l_count > 0 Then Raise e_resend; End If;
    End If;

    -- Hizmet no al
    l_hizmet_no := sanal_hizmet_no_al(l_hesap_row.id, l_hesap_row.sanal_mi, l_hesap_row.hesap_no);

    -- Kalem kontrol
    l_sonuc := siparis_kalem_kontrol(l_belge_row, l_hesap_row, prm_t_siparis_istek_type,
                                     l_sd_kontrol_col, prm_cevap_kodu, prm_error_type, prm_error_num, prm_error_str);
    If l_sonuc <> pck_def.c_success Then Raise e_kalem_kontrol; End If;

    l_kalem_det_list := prm_t_siparis_istek_type.kalem_detay_list;

    -- Siparis sahibi bul veya olustur
    Begin
      Select * Into l_siparis_sahibi
        From siparis_sahibi ss
       Where ss.hesap_id = l_hesap_row.id
         And ss.musteri_id = l_hesap_row.musteri_id;
    Exception
      When no_data_found Then
        l_siparis_sahibi := Null;
    End;

    If l_siparis_sahibi.id Is Null Then
      Select seq_siparis_sahibi.nextval Into l_siparis_sahibi.id From dual;
      l_siparis_sahibi.hesap_id              := l_hesap_row.id;
      l_siparis_sahibi.musteri_id            := l_hesap_row.musteri_id;
      l_siparis_sahibi.sirket_id             := l_belge_row.sirket_id;
      l_siparis_sahibi.stok_analiz_detay_id  := Null;
      l_siparis_sahibi.siparis_sahibi_tipi_id := pck_def.c_siparis_sahibi_tipi_normal;
      l_siparis_sahibi.transfere_aktarilan_tutar := 0; --<SURUM> 4.KAMPANYA.0 <\SURUM>
      l_siparis_sahibi.kesinti_yapilan_tutar     := 0; --<SURUM> 4.KESINTI.0 <\SURUM>
      l_siparis_sahibi.nakit_talep_tarihi        := Null; -- <SURUM> 4.35.0 <\SURUM>
      l_siparis_sahibi.tanitim_zamani            := Sysdate;
      l_siparis_sahibi.tanitan_kullanici_id      := pck_def.c_inst_sistem_kullanici_id;
      l_siparis_sahibi.guncelleme_zamani         := systimestamp;

      Begin
        Insert Into siparis_sahibi Values (
           l_siparis_sahibi.id,
           l_siparis_sahibi.hesap_id,
           l_siparis_sahibi.musteri_id,
           l_siparis_sahibi.sirket_id,
           l_siparis_sahibi.stok_analiz_detay_id,
           l_siparis_sahibi.siparis_sahibi_tipi_id,
           l_siparis_sahibi.transfere_aktarilan_tutar,
           l_siparis_sahibi.kesinti_yapilan_tutar,
           l_siparis_sahibi.nakit_talep_tarihi,
           null,
           l_siparis_sahibi.tanitim_zamani,
           l_siparis_sahibi.tanitan_kullanici_id,
           l_siparis_sahibi.guncelleme_zamani,
           Null);
      Exception
        When dup_val_on_index Then Raise e_birden_fazla_sip_sahibi;
      End;

      siparis_sahibi_finans_guncelle(l_siparis_sahibi.id,
                                     pck_def.c_siparis_sahibi_finans_normal,
                                     l_siparis_sahibi.siparis_toplam_tutari,
                                     l_siparis_sahibi.belge_mahsup_tutari,
                                     l_siparis_sahibi.avans_mahsup_tutari,
                                     l_siparis_sahibi.nakit_siparis_tutari,
                                     l_siparis_sahibi.transfere_aktarilan_tutar,
                                     l_siparis_sahibi.kesinti_yapilan_tutar,
                                     l_siparis_sahibi.kalan_alacak_tutari,
                                     l_siparis_sahibi.nakit_talep_tarihi);
    End If;

    If prm_t_siparis_istek_type.stok_analiz_id Is Not Null Then
      Begin
        Select s.* Into l_siparis From siparis s, siparis_durum sd
         Where s.siparis_sahibi_id = l_siparis_sahibi.id
           And s.belge_id = l_belge_row.id
           And s.siparis_durum_id = sd.id
           And sd.islem_durumu In (pck_def.c_siparis_durum_islem_basarisiz, pck_def.c_siparis_durum_islem_belirsiz);
      Exception When no_data_found Then l_siparis := Null; End;
    End If;

    If l_siparis.id Is Null Then
      Select seq_siparis.nextval Into l_siparis.id From dual;
      l_siparis.siparis_durum_id  := pck_def.c_id_belirsiz;
      l_siparis.siparis_sahibi_id := l_siparis_sahibi.id;

      If l_islem_id = pck_def.c_onay_islemi Then
        Begin
          Select tip Into l_tip From stok_analiz Where id = prm_t_siparis_istek_type.stok_analiz_id;
        End;
      End If;

      If l_islem_id = pck_def.c_islemkodu_revize Then
        l_siparis.siparis_tipi_id := pck_def.c_siparis_tipi_munferit_revize;
      Else
        If l_tip = pck_def.c_stok_analiz_tip_onay Then
          l_siparis.siparis_tipi_id := pck_def.c_siparis_tipi_onay;
        Elsif l_tip = pck_def.c_stok_analiz_tip_diger Then
          l_siparis.siparis_tipi_id := pck_def.c_siparis_tipi_diger;
        End If;
      End If;

      l_siparis.istek_deneme_sayisi       := 0;
      l_siparis.siparis_tutari            := prm_t_siparis_istek_type.siparis_toplam_tutar;
      l_siparis.uygulama                  := prm_t_siparis_istek_type.uygulama;
      l_siparis.cevap_kodu                := Null;
      l_siparis.cevap_mesaji              := Null;
      l_siparis.surec_talep_id            := Null;
      l_siparis.siparis_referans_no       := prm_t_siparis_istek_type.siparis_detay_bilgi.siparis_referans_no;
      l_siparis.tanitim_zamani            := Sysdate;
      l_siparis.tanitan_kullanici_id      := nvl(prm_t_siparis_istek_type.kullanici_id, pck_def.c_inst_sistem_kullanici_id);
      l_siparis.guncelleme_zamani         := systimestamp;
      l_siparis.belge_id                  := l_belge_row.id;
      l_siparis.alacak_id                 := Null;
      l_siparis.istek_hesap_belge_sayisi  := prm_t_siparis_istek_type.toplam_kalem_sayisi;
      l_siparis.munferit_siparis_tutari   := 0;

      If (l_siparis.uygulama = pck_def.c_uygulama_siparis_talep And
         prm_t_siparis_istek_type.islem_kodu = pck_def.c_kanal_talep_girisi) Then
        l_siparis.siparis_tipi_id := pck_def.c_siparis_tipi_kanaldan_mnfrt_revize;
      End If;

      Insert Into siparis Values (l_siparis.id, l_siparis.siparis_durum_id, l_siparis.siparis_sahibi_id,
         l_siparis.siparis_tipi_id, l_siparis.belge_id, l_siparis.alacak_id, l_siparis.istek_deneme_sayisi,
         l_siparis.siparis_tutari, l_siparis.uygulama, l_siparis.cevap_kodu, l_siparis.cevap_mesaji,
         l_siparis.surec_talep_id, l_siparis.siparis_referans_no, l_siparis.istek_hesap_belge_sayisi,
         l_siparis.tanitim_zamani, l_siparis.tanitan_kullanici_id, l_siparis.guncelleme_zamani,
         Null, trunc(Sysdate), l_siparis.munferit_siparis_tutari);
    End If;

    -- Siparis detay olustur veya guncelle
    Begin
      Select * Into l_siparis_detay From siparis_detay_kayit sdk Where sdk.siparis_id = l_siparis.id;
    Exception When no_data_found Then l_siparis_detay := Null; End;

    If l_siparis_detay.id Is Null Then
      Select seq_siparis_detay_kayit.nextval Into l_siparis_detay.id From dual;
      l_siparis_detay.siparis_id               := l_siparis.id;
      l_siparis_detay.tdk_mi                   := prm_t_siparis_istek_type.siparis_detay_bilgi.tdk_mi;
      l_siparis_detay.vergi_haric              := prm_t_siparis_istek_type.siparis_detay_bilgi.vergi_haric;
      l_siparis_detay.siparis_sebebi_id        := prm_t_siparis_istek_type.siparis_detay_bilgi.siparis_sebebi_id;
      l_siparis_detay.aciklama                 := prm_t_siparis_istek_type.siparis_detay_bilgi.aciklama;
      l_siparis_detay.siparis_belge_no         := prm_t_siparis_istek_type.siparis_detay_bilgi.siparis_belge_no;
      l_siparis_detay.vergi_dairesi            := prm_t_siparis_istek_type.siparis_detay_bilgi.vergi_dairesi;
      l_siparis_detay.vergi_no                 := prm_t_siparis_istek_type.siparis_detay_bilgi.vergi_no;
      l_siparis_detay.sebep_bildirim_no        := prm_t_siparis_istek_type.siparis_detay_bilgi.sebep_bildirim_no;
      l_siparis_detay.sebep_personel_sicil_no  := prm_t_siparis_istek_type.siparis_detay_bilgi.sebep_kullanici_sicil_no;
      l_siparis_detay.sebep_bayi_kodu          := prm_t_siparis_istek_type.siparis_detay_bilgi.sebep_bayi_kodu;
      l_siparis_detay.sebep_cagri_merkezi      := substr(prm_t_siparis_istek_type.siparis_detay_bilgi.sebep_cagri_merkezi, 1, 20);
      l_siparis_detay.erp_gonderilecek_mi      := prm_t_siparis_istek_type.siparis_detay_bilgi.erp_gonderilecek_mi;
      l_siparis_detay.erp_iban                 := prm_t_siparis_istek_type.siparis_detay_bilgi.erp_iban;
      l_siparis_detay.erp_alici_ad_soyad       := prm_t_siparis_istek_type.siparis_detay_bilgi.erp_alici_ad_soyad;
      l_siparis_detay.erp_alici_telefon        := prm_t_siparis_istek_type.siparis_detay_bilgi.erp_alici_telefon;
      l_siparis_detay.erp_aciklama             := prm_t_siparis_istek_type.siparis_detay_bilgi.erp_aciklama;
      l_siparis_detay.cid                      := prm_t_siparis_istek_type.siparis_detay_bilgi.cid; --<SURUM> 4.13.0 <\SURUM>
      l_siparis_detay.temsilci_adi_soyadi      := prm_t_siparis_istek_type.siparis_detay_bilgi.temsilci_adi_soyadi;
      l_siparis_detay.temsilci_sicil_no        := prm_t_siparis_istek_type.siparis_detay_bilgi.temsilci_sicil_no;
      l_siparis_detay.islem_tarihi             := prm_t_siparis_istek_type.siparis_detay_bilgi.islem_tarihi;
      l_siparis_detay.org_belge_durum          := l_belge_row.belge_durum; -- <SURUM> 3.67.0 <\SURUM>
      l_siparis_detay.taksitli_mi              := prm_t_siparis_istek_type.taksitli_mi; -- <SURUM> 4.6.0 <\SURUM>
      l_siparis_detay.tanitim_zamani           := Sysdate;
      l_siparis_detay.tanitan_kullanici_id     := pck_def.c_inst_sistem_kullanici_id;
      l_siparis_detay.guncelleme_zamani        := systimestamp;
      l_siparis_detay.portal_kullanici_kodu    := prm_t_siparis_istek_type.siparis_detay_bilgi.portal_kullanici_kodu;
      l_siparis_detay.portal_kullanici_adsoyad := prm_t_siparis_istek_type.siparis_detay_bilgi.portal_kullanici_ad_soyad;

      Insert Into siparis_detay_kayit Values l_siparis_detay;
    Else
      Update siparis_detay_kayit sdk
         Set tdk_mi                   = prm_t_siparis_istek_type.siparis_detay_bilgi.tdk_mi,
             vergi_haric              = prm_t_siparis_istek_type.siparis_detay_bilgi.vergi_haric,
             siparis_sebebi_id        = prm_t_siparis_istek_type.siparis_detay_bilgi.siparis_sebebi_id,
             aciklama                 = prm_t_siparis_istek_type.siparis_detay_bilgi.aciklama,
             siparis_belge_no         = prm_t_siparis_istek_type.siparis_detay_bilgi.siparis_belge_no,
             vergi_dairesi            = prm_t_siparis_istek_type.siparis_detay_bilgi.vergi_dairesi,
             vergi_no                 = prm_t_siparis_istek_type.siparis_detay_bilgi.vergi_no,
             sebep_bildirim_no        = prm_t_siparis_istek_type.siparis_detay_bilgi.sebep_bildirim_no,
             sebep_personel_sicil_no  = prm_t_siparis_istek_type.siparis_detay_bilgi.sebep_kullanici_sicil_no,
             sebep_bayi_kodu          = prm_t_siparis_istek_type.siparis_detay_bilgi.sebep_bayi_kodu,
             sebep_cagri_merkezi      = substr(prm_t_siparis_istek_type.siparis_detay_bilgi.sebep_cagri_merkezi, 1, 20),
             erp_gonderilecek_mi      = prm_t_siparis_istek_type.siparis_detay_bilgi.erp_gonderilecek_mi,
             erp_iban                 = prm_t_siparis_istek_type.siparis_detay_bilgi.erp_iban,
             erp_alici_ad_soyad       = prm_t_siparis_istek_type.siparis_detay_bilgi.erp_alici_ad_soyad,
             erp_alici_telefon        = prm_t_siparis_istek_type.siparis_detay_bilgi.erp_alici_telefon,
             erp_aciklama             = prm_t_siparis_istek_type.siparis_detay_bilgi.erp_aciklama,
             guncelleme_zamani        = systimestamp,
             guncelleyen_kullanici_id = pck_def.c_inst_sistem_kullanici_id,
             portal_kullanici_kodu    = prm_t_siparis_istek_type.siparis_detay_bilgi.portal_kullanici_kodu,
             portal_kullanici_adsoyad = prm_t_siparis_istek_type.siparis_detay_bilgi.portal_kullanici_ad_soyad
       Where sdk.id = l_siparis_detay.id;
    End If;

    -- Siparis hareket olustur
    Select seq_siparis_hareket.nextval Into l_siparis_hareket.id From dual;
    l_siparis_hareket.islem_tarihi          := prm_t_siparis_istek_type.islem_tarihi;
    l_siparis_hareket.kurum_id              := prm_t_siparis_istek_type.kurum_id;
    l_siparis_hareket.stan                  := prm_t_siparis_istek_type.stan;
    l_siparis_hareket.siparis_id            := l_siparis.id;
    l_siparis_hareket.siparis_hareket_durum_id := pck_def.c_id_belirsiz;
    l_siparis_hareket.siparis_durum_id      := l_siparis.siparis_durum_id;
    l_siparis_hareket.islem_id              := l_islem_id;
    l_siparis_hareket.siparis_tutari        := prm_t_siparis_istek_type.siparis_toplam_tutar;
    l_siparis_hareket.ust_id                := Null;
    l_siparis_hareket.cevap_kodu            := Null;
    l_siparis_hareket.cevap_mesaji          := Null;
    l_siparis_hareket.business_id           := prm_t_siparis_istek_type.business_id;
    l_siparis_hareket.conversation_id       := prm_t_siparis_istek_type.conversation_id;
    l_siparis_hareket.tanitim_zamani        := Sysdate;
    l_siparis_hareket.tanitan_kullanici_id  := pck_def.c_inst_sistem_kullanici_id;
    l_siparis_hareket.guncelleme_zamani     := systimestamp;
    l_siparis_hareket.aciklama              := Null;

    Insert Into siparis_hareket Values (l_siparis_hareket.id, l_siparis_hareket.islem_tarihi,
       l_siparis_hareket.kurum_id, l_siparis_hareket.stan, l_siparis_hareket.siparis_id,
       l_siparis_hareket.siparis_hareket_durum_id, l_siparis_hareket.siparis_durum_id,
       l_siparis_hareket.islem_id, l_siparis_hareket.siparis_tutari, l_siparis_hareket.ust_id,
       l_siparis_hareket.cevap_kodu, l_siparis_hareket.cevap_mesaji, l_siparis_hareket.aciklama,
       l_siparis_hareket.business_id, l_siparis_hareket.conversation_id,
       l_siparis_hareket.tanitim_zamani, l_siparis_hareket.tanitan_kullanici_id,
       l_siparis_hareket.guncelleme_zamani, Null, 0);

    -- Siparis hareket islem detaylari olustur
    For i In l_kalem_det_list.first .. l_kalem_det_list.last Loop
      Insert Into siparis_hareket_islem_sdy Values (
        seq_siparis_hareket_islem_sdy.nextval, l_siparis_hareket.id,
        l_kalem_det_list(i).siparis_detay_id, l_kalem_det_list(i).kalem_tipi_id,
        l_kalem_det_list(i).tedarikci_id, l_kalem_det_list(i).revize_tutari,
        l_kalem_det_list(i).kdv_orani, l_kalem_det_list(i).kdv_tutari,
        l_kalem_det_list(i).otv_orani, l_kalem_det_list(i).otv_tutari,
        l_kalem_det_list(i).urun_seri_no, l_kalem_det_list(i).paket_tipi_id,
        l_kalem_det_list(i).taksitliSatisID, l_kalem_det_list(i).taksitNo,
        Null, Sysdate, pck_def.c_inst_sistem_kullanici_id, systimestamp, Null);
    End Loop;

    -- Kampanya kodu guncelleme
    If l_sd_kontrol_col.count > 0 Then
      For i In l_sd_kontrol_col.first .. l_sd_kontrol_col.last Loop
        Update siparis_detay Set kampanya_id = l_sd_kontrol_col(i).ist_kmp_kodu,
               guncelleme_zamani = systimestamp, guncelleyen_kullanici_id = pck_def.c_inst_sistem_kullanici_id
         Where id = l_sd_kontrol_col(i).id;
      End Loop;
    End If;

    Savepoint sp_siparis_isle;

    -- Belge guncelle
    l_belge_durum := pck_def.c_partial;
    If l_belge_row.kalan_tutar - prm_t_siparis_istek_type.siparis_toplam_tutar <= 0 Then
      l_belge_durum := pck_def.c_revize_ile_kapanmis;
    End If;

    Update belge b
       Set b.siparis_duzeltme_tutar = b.siparis_duzeltme_tutar - prm_t_siparis_istek_type.siparis_toplam_tutar,
           b.kalan_tutar           = b.kalan_tutar - prm_t_siparis_istek_type.siparis_toplam_tutar,
           b.belge_durum           = l_belge_durum,
           b.odeme_tarihi          = decode(sign(b.kalan_tutar - prm_t_siparis_istek_type.siparis_toplam_tutar),
                                            1, b.odeme_tarihi, 0, trunc(Sysdate), -1,
                                            decode(b.belge_durum, pck_def.c_paid, b.odeme_tarihi, trunc(Sysdate))),
           b.guncelleme_zaman      = systimestamp,
           b.guncelleyen_kullanici = pck_def.c_inst_sistem_kullanici_id
     Where b.id = l_belge_row.id;

    -- Cevap type olustur
    prm_t_siparis_cevap_type := New t_siparis_cevap_type(
      prm_t_siparis_istek_type.stok_analiz_id, Null, l_hesap_row.id, l_hesap_row.musteri_id,
      l_siparis_sahibi.id, l_siparis.id, l_siparis_hareket.id, l_siparis_detay.id,
      l_islem_id, Null, Null, prm_t_siparis_istek_type.siparis_toplam_tutar, 0, 0, 0, 0, 0,
      Null, l_hesap_row.hesap_no);

    -- Siparis durumu guncelle
    siparis_guncelle(prm_t_siparis_cevap_type, pck_def.c_id_basarili, pck_def.c_shd_degerlendirildi,
                     l_islem_id, pck_def.c_inst_sistem_kullanici_id, prm_cevap_kodu, prm_error_str);

    -- Siparis hareket durumu guncelle
    Update siparis_hareket sh
       Set sh.siparis_hareket_durum_id = pck_def.c_shd_degerlendirildi,
           sh.siparis_durum_id         = pck_def.c_id_basarili,
           sh.cevap_kodu               = prm_cevap_kodu,
           sh.guncelleme_zamani        = systimestamp,
           sh.guncelleyen_kullanici_id = pck_def.c_inst_sistem_kullanici_id
     Where sh.id = l_siparis_hareket.id;

    Return pck_def.c_success;

  Exception
    When e_resend Then
      pck_general.set_out_variables(prm_cevap_kodu, pck_def.c_rc_resend, prm_error_type, 'APP', prm_error_num, -20304, prm_error_str, pck_general.get_islem_cevap_ack(pck_def.c_rc_resend));
      Return pck_def.c_fail;
    When e_kalem_kontrol Then
      Return pck_def.c_fail;
    When e_birden_fazla_sip_sahibi Then
      pck_general.set_out_variables(prm_cevap_kodu, pck_def.c_rc_birden_fazla_sip_sahibi, prm_error_type, 'APP', prm_error_num, -20304, prm_error_str, pck_general.get_islem_cevap_ack(pck_def.c_rc_birden_fazla_sip_sahibi));
      Rollback To sp_siparis_isle;
      Return pck_def.c_fail;
    When Others Then
      pck_general.set_out_variables(prm_cevap_kodu, pck_def.c_rc_sql_error, prm_error_type, 'ORA', prm_error_num, Sqlcode, prm_error_str, 'siparis_isle:' || Sqlerrm);
      Rollback To sp_siparis_isle;
      Return pck_def.c_fail;
  End siparis_isle;


  -- =========================================================================
  -- siparis_yap: Belge id ile siparis yapar
  -- =========================================================================
  Function siparis_yap(prm_belge_id                 In belge.id%Type,
                       prm_t_siparis_istek_type_dizi In t_siparis_istek_type_dizi,
                       prm_t_siparis_cevap_type      Out t_siparis_cevap_type,
                       prm_cevap_kodu                In Out Varchar2,
                       prm_error_type                In Out Nocopy database_exception.error_type%Type,
                       prm_error_num                 In Out Nocopy database_exception.error_code%Type,
                       prm_error_str                 In Out Nocopy database_exception.error_message%Type) Return Number Is
    l_belge     belge%Rowtype;
    l_hesap     hesap%Rowtype;
    l_sonuc     Number;
    l_info_msj  Varchar2(1000);
    l_t_siparis_istek_type t_siparis_istek_type;
    e_belge_bulunamadi   Exception;
    e_hesap_bulunamadi   Exception;
    e_istek_bos          Exception;
    e_siparis_basarisiz  Exception;
  Begin
    If prm_t_siparis_istek_type_dizi Is Null Or prm_t_siparis_istek_type_dizi.count = 0 Then
      Raise e_istek_bos;
    End If;
    l_t_siparis_istek_type := prm_t_siparis_istek_type_dizi(prm_t_siparis_istek_type_dizi.first);

    Begin
      Select * Into l_belge From belge Where id = prm_belge_id;
    Exception When no_data_found Then Raise e_belge_bulunamadi; End;

    Begin
      Select * Into l_hesap From hesap Where id = l_belge.hesap_id;
    Exception When no_data_found Then Raise e_hesap_bulunamadi; End;

    l_sonuc := siparis_isle(l_belge, l_hesap, l_t_siparis_istek_type.islem_kodu,
                            l_t_siparis_istek_type, prm_t_siparis_cevap_type,
                            prm_cevap_kodu, prm_error_type, prm_error_num, prm_error_str);
    If l_sonuc <> pck_def.c_success Then Raise e_siparis_basarisiz; End If;
    Return pck_def.c_success;
  Exception
    When e_istek_bos Then
      pck_general.set_out_variables(prm_cevap_kodu, pck_def.c_rc_istek_bos, prm_error_type, 'APP', prm_error_num, -20304, prm_error_str, 'siparis_yap: Istek listesi bos');
      Return pck_def.c_fail;
    When e_belge_bulunamadi Then
      pck_general.set_out_variables(prm_cevap_kodu, pck_def.c_rc_belge_bulunamadi, prm_error_type, 'APP', prm_error_num, -20304, prm_error_str, 'siparis_yap: Belge bulunamadi. BelgeId:' || prm_belge_id);
      Return pck_def.c_fail;
    When e_hesap_bulunamadi Then
      pck_general.set_out_variables(prm_cevap_kodu, pck_def.c_rc_hesap_bulunamadi, prm_error_type, 'APP', prm_error_num, -20304, prm_error_str, 'siparis_yap: Hesap bulunamadi');
      Return pck_def.c_fail;
    When e_siparis_basarisiz Then
      Return pck_def.c_fail;
    When Others Then
      pck_general.set_out_variables(prm_cevap_kodu, pck_def.c_rc_sql_error, prm_error_type, 'ORA', prm_error_num, Sqlcode, prm_error_str, 'siparis_yap:' || Sqlerrm);
      Return pck_def.c_fail;
  End siparis_yap;

  -- =========================================================================
  -- siparis_olustur_ws: Web servis uzerinden siparis olusturur
  -- =========================================================================
  Function siparis_olustur_ws(prm_t_siparis_istek_type_dizi In t_siparis_istek_type_dizi,
                              prm_t_siparis_cevap_type      Out t_siparis_cevap_type,
                              prm_cevap_kodu                In Out Varchar2,
                              prm_error_type                In Out Nocopy database_exception.error_type%Type,
                              prm_error_num                 In Out Nocopy database_exception.error_code%Type,
                              prm_error_str                 In Out Nocopy database_exception.error_message%Type) Return Number Is
    l_belge       belge%Rowtype;
    l_hesap       hesap%Rowtype;
    l_sonuc       Number;
    l_belge_id    belge.id%Type;
    l_info_msj    Varchar2(1000);
    l_t_siparis_istek_type t_siparis_istek_type;
    e_belge_bulunamadi   Exception;
    e_hesap_bulunamadi   Exception;
    e_istek_bos          Exception;
    e_siparis_basarisiz  Exception;
    e_hesap_no_bos       Exception;
    e_coklu_belge        Exception;
  Begin
    If prm_t_siparis_istek_type_dizi Is Null Or prm_t_siparis_istek_type_dizi.count = 0 Then Raise e_istek_bos; End If;
    l_t_siparis_istek_type := prm_t_siparis_istek_type_dizi(prm_t_siparis_istek_type_dizi.first);

    If l_t_siparis_istek_type.hesap_no Is Null Then Raise e_hesap_no_bos; End If;

    Begin
      Select h.* Into l_hesap From hesap h Where h.hesap_no = l_t_siparis_istek_type.hesap_no And rownum = 1;
    Exception When no_data_found Then Raise e_hesap_bulunamadi; End;

    Begin
      Select b.id Into l_belge_id From belge b
       Where b.hesap_id = l_hesap.id
         And b.belge_no = l_t_siparis_istek_type.belge_no
         And b.donem_kodu = l_t_siparis_istek_type.donem_kodu
         And rownum = 1;
    Exception
      When no_data_found Then Raise e_belge_bulunamadi;
      When too_many_rows Then Raise e_coklu_belge;
    End;

    Begin Select * Into l_belge From belge Where id = l_belge_id;
    Exception When no_data_found Then Raise e_belge_bulunamadi; End;

    l_sonuc := siparis_isle(l_belge, l_hesap, l_t_siparis_istek_type.islem_kodu,
                            l_t_siparis_istek_type, prm_t_siparis_cevap_type,
                            prm_cevap_kodu, prm_error_type, prm_error_num, prm_error_str);
    If l_sonuc <> pck_def.c_success Then Raise e_siparis_basarisiz; End If;
    Return pck_def.c_success;
  Exception
    When e_istek_bos Then
      pck_general.set_out_variables(prm_cevap_kodu, pck_def.c_rc_istek_bos, prm_error_type, 'APP', prm_error_num, -20304, prm_error_str, 'siparis_olustur_ws: Istek listesi bos');
      Return pck_def.c_fail;
    When e_hesap_no_bos Then
      pck_general.set_out_variables(prm_cevap_kodu, pck_def.c_rc_hesap_no_bos, prm_error_type, 'APP', prm_error_num, -20304, prm_error_str, 'siparis_olustur_ws: Hesap no bos');
      Return pck_def.c_fail;
    When e_hesap_bulunamadi Then
      pck_general.set_out_variables(prm_cevap_kodu, pck_def.c_rc_hesap_bulunamadi, prm_error_type, 'APP', prm_error_num, -20304, prm_error_str, 'siparis_olustur_ws: Hesap bulunamadi. HesapNo:' || l_t_siparis_istek_type.hesap_no);
      Return pck_def.c_fail;
    When e_belge_bulunamadi Then
      pck_general.set_out_variables(prm_cevap_kodu, pck_def.c_rc_belge_bulunamadi, prm_error_type, 'APP', prm_error_num, -20304, prm_error_str, 'siparis_olustur_ws: Belge bulunamadi');
      Return pck_def.c_fail;
    When e_coklu_belge Then
      pck_general.set_out_variables(prm_cevap_kodu, pck_def.c_rc_coklu_belge, prm_error_type, 'APP', prm_error_num, -20304, prm_error_str, 'siparis_olustur_ws: Birden fazla belge bulundu');
      Return pck_def.c_fail;
    When e_siparis_basarisiz Then Return pck_def.c_fail;
    When Others Then
      pck_general.set_out_variables(prm_cevap_kodu, pck_def.c_rc_sql_error, prm_error_type, 'ORA', prm_error_num, Sqlcode, prm_error_str, 'siparis_olustur_ws:' || Sqlerrm);
      Return pck_def.c_fail;
  End siparis_olustur_ws;

  -- =========================================================================
  -- siparis_yap_kanal: Kanal uzerinden siparis yapar (talep no ile)
  -- =========================================================================
  Function siparis_yap_kanal(prm_talep_no                  In teslimat_talep.talep_no%Type,
                             prm_t_siparis_istek_type_dizi In t_siparis_istek_type_dizi,
                             prm_t_siparis_cevap_type      Out t_siparis_cevap_type,
                             prm_cevap_kodu                In Out Varchar2,
                             prm_error_type                In Out Nocopy database_exception.error_type%Type,
                             prm_error_num                 In Out Nocopy database_exception.error_code%Type,
                             prm_error_str                 In Out Nocopy database_exception.error_message%Type) Return Number Is
    l_belge        belge%Rowtype;
    l_hesap        hesap%Rowtype;
    l_teslimat_talep teslimat_talep%Rowtype;
    l_sonuc        Number;
    l_belge_id     belge.id%Type;
    l_info_msj     Varchar2(1000);
    l_count        Number;
    l_t_siparis_istek_type t_siparis_istek_type;
    e_belge_bulunamadi     Exception;
    e_hesap_bulunamadi     Exception;
    e_istek_bos            Exception;
    e_siparis_basarisiz    Exception;
    e_talep_bulunamadi     Exception;
    e_talep_durumu_uymsz   Exception;
  Begin
    If prm_t_siparis_istek_type_dizi Is Null Or prm_t_siparis_istek_type_dizi.count = 0 Then Raise e_istek_bos; End If;
    l_t_siparis_istek_type := prm_t_siparis_istek_type_dizi(prm_t_siparis_istek_type_dizi.first);

    Begin
      Select * Into l_teslimat_talep From teslimat_talep Where talep_no = prm_talep_no;
    Exception When no_data_found Then Raise e_talep_bulunamadi; End;

    If l_teslimat_talep.talep_durum_id Not In (pck_def.c_talep_durum_yeni, pck_def.c_talep_durum_islemde) Then
      Raise e_talep_durumu_uymsz;
    End If;

    l_t_siparis_istek_type.uygulama := pck_def.c_uygulama_siparis_talep;
    l_t_siparis_istek_type.islem_kodu := pck_def.c_kanal_talep_girisi;

    Begin Select * Into l_hesap From hesap Where id = l_teslimat_talep.hesap_id;
    Exception When no_data_found Then Raise e_hesap_bulunamadi; End;

    Begin Select b.id Into l_belge_id From belge b
     Where b.hesap_id = l_hesap.id And b.belge_no = l_t_siparis_istek_type.belge_no
       And b.donem_kodu = l_t_siparis_istek_type.donem_kodu And rownum = 1;
    Exception When no_data_found Then Raise e_belge_bulunamadi; End;

    Begin Select * Into l_belge From belge Where id = l_belge_id;
    Exception When no_data_found Then Raise e_belge_bulunamadi; End;

    l_sonuc := siparis_isle(l_belge, l_hesap, l_t_siparis_istek_type.islem_kodu,
                            l_t_siparis_istek_type, prm_t_siparis_cevap_type,
                            prm_cevap_kodu, prm_error_type, prm_error_num, prm_error_str);
    If l_sonuc <> pck_def.c_success Then Raise e_siparis_basarisiz; End If;

    -- Teslimat talep durumunu guncelle
    Update teslimat_talep Set talep_durum_id = pck_def.c_talep_durum_tamamlandi,
           guncelleme_zamani = systimestamp, guncelleyen_kullanici_id = pck_def.c_inst_sistem_kullanici_id
     Where id = l_teslimat_talep.id;

    Return pck_def.c_success;
  Exception
    When e_istek_bos Then
      pck_general.set_out_variables(prm_cevap_kodu, pck_def.c_rc_istek_bos, prm_error_type, 'APP', prm_error_num, -20304, prm_error_str, 'siparis_yap_kanal: Istek listesi bos');
      Return pck_def.c_fail;
    When e_talep_bulunamadi Then
      pck_general.set_out_variables(prm_cevap_kodu, pck_def.c_rc_talep_bulunamadi, prm_error_type, 'APP', prm_error_num, -20304, prm_error_str, 'siparis_yap_kanal: Talep bulunamadi. TalepNo:' || prm_talep_no);
      Return pck_def.c_fail;
    When e_talep_durumu_uymsz Then
      pck_general.set_out_variables(prm_cevap_kodu, pck_def.c_rc_talep_durumu_uyumsuz, prm_error_type, 'APP', prm_error_num, -20304, prm_error_str, 'siparis_yap_kanal: Talep durumu uygun degil');
      Return pck_def.c_fail;
    When e_hesap_bulunamadi Then
      pck_general.set_out_variables(prm_cevap_kodu, pck_def.c_rc_hesap_bulunamadi, prm_error_type, 'APP', prm_error_num, -20304, prm_error_str, 'siparis_yap_kanal: Hesap bulunamadi');
      Return pck_def.c_fail;
    When e_belge_bulunamadi Then
      pck_general.set_out_variables(prm_cevap_kodu, pck_def.c_rc_belge_bulunamadi, prm_error_type, 'APP', prm_error_num, -20304, prm_error_str, 'siparis_yap_kanal: Belge bulunamadi');
      Return pck_def.c_fail;
    When e_siparis_basarisiz Then Return pck_def.c_fail;
    When Others Then
      pck_general.set_out_variables(prm_cevap_kodu, pck_def.c_rc_sql_error, prm_error_type, 'ORA', prm_error_num, Sqlcode, prm_error_str, 'siparis_yap_kanal:' || Sqlerrm);
      Return pck_def.c_fail;
  End siparis_yap_kanal;

  -- =========================================================================
  -- siparis_isle_kanal: Kanal uzerinden siparis isler (mevcut hareket uzerinden)
  -- =========================================================================
  Function siparis_isle_kanal(prm_sh_id                     In siparis_hareket.id%Type,
                              prm_t_siparis_istek_type_dizi In t_siparis_istek_type_dizi,
                              prm_hareket_id                Out siparis_hareket.id%Type,
                              prm_cevap_kodu                In Out Varchar2,
                              prm_error_type                In Out Nocopy database_exception.error_type%Type,
                              prm_error_num                 In Out Nocopy database_exception.error_code%Type,
                              prm_error_str                 In Out Nocopy database_exception.error_message%Type) Return Number Is

    l_siparis_hareket       siparis_hareket%Rowtype;
    l_siparis               siparis%Rowtype;
    l_siparis_sahibi        siparis_sahibi%Rowtype;
    l_belge                 belge%Rowtype;
    l_hesap                 hesap%Rowtype;
    l_sonuc                 Number;
    l_count                 Number;
    l_info_msj              Varchar2(1000);
    l_t_siparis_istek_type  t_siparis_istek_type;
    l_t_siparis_cevap_type  t_siparis_cevap_type;
    l_belge_durum           belge.belge_durum%Type;

    e_hareket_bulunamadi     Exception;
    e_siparis_bulunamadi     Exception;
    e_istek_bos              Exception;
    e_siparis_basarisiz      Exception;
    e_siparis_durumu_uymsz   Exception;

    Cursor c_hareket_detay Is
      Select * From siparis_hareket_islem_sdy sdy Where sdy.siparis_hareket_id = prm_sh_id;

  Begin
    If prm_t_siparis_istek_type_dizi Is Null Or prm_t_siparis_istek_type_dizi.count = 0 Then Raise e_istek_bos; End If;
    l_t_siparis_istek_type := prm_t_siparis_istek_type_dizi(prm_t_siparis_istek_type_dizi.first);

    Begin Select * Into l_siparis_hareket From siparis_hareket Where id = prm_sh_id;
    Exception When no_data_found Then Raise e_hareket_bulunamadi; End;

    Begin Select * Into l_siparis From siparis Where id = l_siparis_hareket.siparis_id;
    Exception When no_data_found Then Raise e_siparis_bulunamadi; End;

    Select * Into l_siparis_sahibi From siparis_sahibi Where id = l_siparis.siparis_sahibi_id;
    Select * Into l_belge From belge Where id = l_siparis.belge_id;
    Select * Into l_hesap From hesap h Where h.id = l_belge.hesap_id;

    If l_siparis.siparis_durum_id Not In (pck_def.c_id_belirsiz, pck_def.c_id_basarisiz) Then
      Raise e_siparis_durumu_uymsz;
    End If;

    l_sonuc := siparis_isle(l_belge, l_hesap, l_t_siparis_istek_type.islem_kodu,
                            l_t_siparis_istek_type, l_t_siparis_cevap_type,
                            prm_cevap_kodu, prm_error_type, prm_error_num, prm_error_str);
    If l_sonuc <> pck_def.c_success Then Raise e_siparis_basarisiz; End If;

    prm_hareket_id := l_t_siparis_cevap_type.siparis_hareket_id;
    Return pck_def.c_success;

  Exception
    When e_istek_bos Then
      pck_general.set_out_variables(prm_cevap_kodu, pck_def.c_rc_istek_bos, prm_error_type, 'APP', prm_error_num, -20304, prm_error_str, 'siparis_isle_kanal: Istek bos');
      Return pck_def.c_fail;
    When e_hareket_bulunamadi Then
      pck_general.set_out_variables(prm_cevap_kodu, pck_def.c_rc_hareket_bulunamadi, prm_error_type, 'APP', prm_error_num, -20304, prm_error_str, 'siparis_isle_kanal: Hareket bulunamadi. ShId:' || prm_sh_id);
      Return pck_def.c_fail;
    When e_siparis_durumu_uymsz Then
      pck_general.set_out_variables(prm_cevap_kodu, pck_def.c_rc_siparis_durumu_uyumsuz, prm_error_type, 'APP', prm_error_num, -20304, prm_error_str, 'siparis_isle_kanal: Siparis durumu uygun degil');
      Return pck_def.c_fail;
    When e_siparis_basarisiz Then Return pck_def.c_fail;
    When Others Then
      pck_general.set_out_variables(prm_cevap_kodu, pck_def.c_rc_sql_error, prm_error_type, 'ORA', prm_error_num, Sqlcode, prm_error_str, 'siparis_isle_kanal:' || Sqlerrm);
      Return pck_def.c_fail;
  End siparis_isle_kanal;


  -- =========================================================================
  -- teslimat_sonuc_bildir: Teslimat sonucunu bildirir
  -- =========================================================================
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
                                 prm_error_str            In Out Nocopy database_exception.error_message%Type) Return Number Is

    Cursor c_hareket_detay(p_siparis_hareket_id siparis_hareket.id%Type, p_belge_id belge.id%Type, p_sirket_id sirket.id%Type) Is
      Select sdy.siparis_detay_id, sdy.kalem_tipi_id, sdy.tedarikci_id, sdy.revize_tutari,
             sdy.kdv_orani, sdy.otv_orani, sdy.urun_seri_no, sdy.paket_tipi_id, sd.kampanya_id, sd.bayi_kodu
        From siparis_hareket_islem_sdy sdy, siparis_detay sd
       Where sdy.siparis_hareket_id = p_siparis_hareket_id
         And sd.belge_id = p_belge_id
         And sdy.kalem_tipi_id = sd.kalem_tipi_id
         And sdy.tedarikci_id = sd.tedarikci_id
         And sdy.kdv_orani = sd.kdv_orani
         And sdy.otv_orani = sd.otv_orani
         And sdy.urun_seri_no = sd.urun_seri_no
         And ((p_sirket_id = pck_def.c_company_ana And (sd.paket_tipi Is Null Or (sd.paket_tipi = sdy.paket_tipi_id)))
              Or (p_sirket_id = pck_def.c_company_dijital And (sdy.paket_tipi_id Is Null Or (sd.paket_tipi = sdy.paket_tipi_id))))
         And (sdy.bayi_kodu Is Null Or (sd.bayi_kodu = sdy.bayi_kodu))
         And (sd.kampanya_id Is Null Or (sd.kampanya_id = sdy.kampanya_kodu));

    l_siparis_hareket        siparis_hareket%Rowtype;
    l_siparis                siparis%Rowtype;
    l_siparis_sahibi         siparis_sahibi%Rowtype;
    l_count                  Number;
    l_seq_siparis_hareket_id siparis_hareket.id%Type;
    l_siparis_durumu_id      siparis.siparis_durum_id%Type;
    l_siparis_hareket_yeni   siparis_hareket%Rowtype;
    l_hareket_id             siparis_hareket.id%Type;
    l_belge                  belge%Rowtype;
    l_hesap_durum            hesap.hesap_durum%Type;
    l_hesap_no               hesap.hesap_no%Type;
    l_info_msj               Varchar2(1000);

    e_islem_tarihi_hatali          Exception;
    e_resend                       Exception;
    e_hareket_kayit_bulunamadi     Exception;
    e_siparis_durumu_uygun_degil   Exception;
    e_basarisiz_islem              Exception;
    e_siparis_hareket_kayit        Exception;
    e_siparis_detay_guncelleme     Exception;
    e_tekrarli_token               Exception;

  Begin
    If (prm_islem_tarihi <> trunc(Sysdate)) Then Raise e_islem_tarihi_hatali; End If;

    -- resend kontrolu
    Select Count(0) Into l_count From siparis_hareket
     Where islem_tarihi = prm_islem_tarihi And kurum_id = prm_kurum_kodu
       And islem_id = prm_islem_kodu And stan = prm_stan;
    If l_count > 0 Then Raise e_resend; End If;

    -- Siparis sorgula
    Begin Select * Into l_siparis_hareket From siparis_hareket Where id = prm_siparis_hareket_id;
    Exception When no_data_found Then Raise e_hareket_kayit_bulunamadi; End;

    If l_siparis_hareket.siparis_hareket_durum_id = pck_def.c_shd_degerlendirildi Then Raise e_tekrarli_token; End If;

    Select s.* Into l_siparis From siparis s Where id = l_siparis_hareket.siparis_id;
    Select * Into l_siparis_sahibi From siparis_sahibi ss Where ss.id = l_siparis.siparis_sahibi_id;
    Select * Into l_belge From belge Where id = l_siparis.belge_id;
    Select h.hesap_durum, hesap_no Into l_hesap_durum, l_hesap_no From hesap h Where id = l_belge.hesap_id;

    l_siparis_durumu_id := l_siparis.siparis_durum_id;
    If l_siparis_durumu_id = pck_def.c_id_teslimat_basarili Then Raise e_siparis_durumu_uygun_degil; End If;

    Select New t_siparis_cevap_type(sad.stok_analiz_id, ss.stok_analiz_detay_id, ss.hesap_id, ss.musteri_id,
           ss.id, s.id, Null, sdk.id, sh.islem_id, Null, Null, s.siparis_tutari, 0, 0, 0, 0, 0, Null, l_hesap_no)
      Into prm_t_siparis_cevap_type
      From siparis_hareket sh, siparis s, siparis_sahibi ss, siparis_detay_kayit sdk, stok_analiz_detay sad
     Where sh.id = l_siparis_hareket.id And sh.siparis_id = s.id And s.siparis_sahibi_id = ss.id
       And s.id = sdk.siparis_id And ss.stok_analiz_detay_id = sad.id(+);

    If l_siparis_sahibi.stok_analiz_detay_id Is Not Null Then
      Select Count(0) islenen_kayit_sayisi, nvl(Sum(s.siparis_tutari), 0) islenen_kayit_tutari,
             0 kalan_kayit_sayisi, nvl(Sum(decode(s.siparis_durum_id, pck_def.c_id_teslimat_basarili, 1, 0)), 0) onay_adeti
        Into prm_t_siparis_cevap_type.islenen_kayit_sayisi, prm_t_siparis_cevap_type.islenen_kayit_tutari,
             prm_t_siparis_cevap_type.kalan_kayit_sayisi, prm_t_siparis_cevap_type.onay_bekleyen_adeti
        From siparis_sahibi ss, siparis s, siparis_durum sd
       Where ss.id = l_siparis_sahibi.id And ss.id = s.siparis_sahibi_id And s.siparis_durum_id = sd.id
         And sd.islem_durumu = pck_def.c_siparis_durum_islem_basarili;
    End If;

    If prm_islem_kodu = pck_def.c_stok_analiz_teslimat_basarili Then
      l_siparis_durumu_id := pck_def.c_id_teslimat_basarili;
    Else
      l_siparis_durumu_id := pck_def.c_id_teslimat_basarisiz;
    End If;

    Savepoint sp_teslimat_sonuc_bildir;

    Select seq_siparis_hareket.nextval Into l_seq_siparis_hareket_id From dual;
    prm_t_siparis_cevap_type.siparis_hareket_id := l_seq_siparis_hareket_id;

    Insert Into siparis_hareket Values (l_seq_siparis_hareket_id, prm_islem_tarihi, prm_kurum_kodu, prm_stan,
       prm_t_siparis_cevap_type.siparis_id, pck_def.c_shd_degerlendirilmeyecek, l_siparis_durumu_id,
       prm_islem_kodu, l_siparis_hareket.siparis_tutari, l_siparis_hareket.id, prm_cevap_kodu, prm_error_str,
       Null, prm_business_id, prm_conversation_id, Sysdate, pck_def.c_inst_sistem_kullanici_id, systimestamp, Null, 0);

    If l_siparis_durumu_id = pck_def.c_id_teslimat_basarisiz Then
      siparis_guncelle(prm_t_siparis_cevap_type, l_siparis_durumu_id, pck_def.c_shd_degerlendirilmeyecek,
                       prm_islem_kodu, pck_def.c_inst_sistem_kullanici_id, prm_cevap_kodu, prm_error_str);
      Update siparis_hareket sh Set sh.siparis_hareket_durum_id = pck_def.c_shd_degerlendirildi,
             sh.guncelleme_zamani = systimestamp, sh.guncelleyen_kullanici_id = pck_def.c_inst_sistem_kullanici_id
       Where sh.id = l_siparis_hareket.id;
      Raise e_basarisiz_islem;
    End If;

    Update belge b Set b.siparis_duzeltme_tutar = b.siparis_duzeltme_tutar - l_siparis_hareket.siparis_tutari,
           b.kalan_tutar = b.kalan_tutar - l_siparis_hareket.siparis_tutari,
           b.odeme_tarihi = decode(sign(b.kalan_tutar - l_siparis_hareket.siparis_tutari), 1, b.odeme_tarihi,
                                   0, trunc(Sysdate), -1, decode(b.belge_durum, pck_def.c_paid, b.odeme_tarihi, trunc(Sysdate))),
           b.guncelleme_zaman = systimestamp, b.guncelleyen_kullanici = pck_def.c_inst_sistem_kullanici_id
     Where b.id = l_belge.id;

    siparis_guncelle(prm_t_siparis_cevap_type, l_siparis_durumu_id, pck_def.c_shd_degerlendirildi,
                     prm_islem_kodu, pck_def.c_inst_sistem_kullanici_id, prm_cevap_kodu, prm_error_str);

    Update siparis_hareket sh Set sh.siparis_hareket_durum_id = pck_def.c_shd_degerlendirildi,
           sh.siparis_durum_id = l_siparis_durumu_id, sh.cevap_kodu = prm_cevap_kodu,
           sh.guncelleme_zamani = systimestamp, sh.guncelleyen_kullanici_id = pck_def.c_inst_sistem_kullanici_id
     Where sh.id = l_siparis_hareket.id;

    Return pck_def.c_success;

  Exception
    When e_basarisiz_islem Then
      prm_cevap_kodu := pck_def.c_rc_success;
      Return pck_def.c_success;
    When e_islem_tarihi_hatali Then
      pck_general.set_out_variables(prm_cevap_kodu, pck_def.c_rc_islem_tarihi_hatali, prm_error_type, 'APP', prm_error_num, -20304, prm_error_str, 'teslimat_sonuc_bildir: Islem tarihi hatali');
      Return pck_def.c_fail;
    When e_resend Then
      pck_general.set_out_variables(prm_cevap_kodu, pck_def.c_rc_resend, prm_error_type, 'APP', prm_error_num, -20304, prm_error_str, 'teslimat_sonuc_bildir: Resend');
      Return pck_def.c_fail;
    When e_hareket_kayit_bulunamadi Then
      pck_general.set_out_variables(prm_cevap_kodu, pck_def.c_rc_hareket_bulunamadi, prm_error_type, 'APP', prm_error_num, -20304, prm_error_str, 'teslimat_sonuc_bildir: Hareket bulunamadi. Id:' || prm_siparis_hareket_id);
      Return pck_def.c_fail;
    When e_tekrarli_token Then
      pck_general.set_out_variables(prm_cevap_kodu, pck_def.c_rc_tekrarli_token, prm_error_type, 'APP', prm_error_num, -20304, prm_error_str, 'teslimat_sonuc_bildir: Tekrarli token');
      Return pck_def.c_fail;
    When e_siparis_durumu_uygun_degil Then
      pck_general.set_out_variables(prm_cevap_kodu, pck_def.c_rc_siparis_durumu_uyumsuz, prm_error_type, 'APP', prm_error_num, -20304, prm_error_str, 'teslimat_sonuc_bildir: Siparis durumu uygun degil');
      Return pck_def.c_fail;
    When Others Then
      Rollback To sp_teslimat_sonuc_bildir;
      pck_general.set_out_variables(prm_cevap_kodu, pck_def.c_rc_sql_error, prm_error_type, 'ORA', prm_error_num, Sqlcode, prm_error_str, 'teslimat_sonuc_bildir:' || Sqlerrm);
      Return pck_def.c_fail;
  End teslimat_sonuc_bildir;

  -- =========================================================================
  -- siparis_detay_kontrol_ekran: Ekran uzerinden siparis detay kontrolu
  -- =========================================================================
  Function siparis_detay_kontrol_ekran(prm_belge_id             In belge.id%Type,
                                       prm_hesap_id             In hesap.id%Type,
                                       prm_t_siparis_istek_type In t_siparis_istek_type_dizi,
                                       prm_cevap_kodu           In Out Varchar2,
                                       prm_error_type           In Out Nocopy database_exception.error_type%Type,
                                       prm_error_num            In Out Nocopy database_exception.error_code%Type,
                                       prm_error_str            In Out Nocopy database_exception.error_message%Type) Return Number Is
    l_belge     belge%Rowtype;
    l_hesap     hesap%Rowtype;
    l_sonuc     Number;
    l_sd_kontrol_col t_sd_knt_donus_col;
    l_t_siparis_istek_type t_siparis_istek_type;
    e_belge_bulunamadi Exception;
    e_hesap_bulunamadi Exception;
    e_kontrol_basarisiz Exception;
  Begin
    If prm_t_siparis_istek_type Is Null Or prm_t_siparis_istek_type.count = 0 Then
      pck_general.set_out_variables(prm_cevap_kodu, pck_def.c_rc_istek_bos, prm_error_type, 'APP', prm_error_num, -20304, prm_error_str, 'siparis_detay_kontrol_ekran: Istek bos');
      Return pck_def.c_fail;
    End If;
    l_t_siparis_istek_type := prm_t_siparis_istek_type(prm_t_siparis_istek_type.first);

    Begin Select * Into l_belge From belge Where id = prm_belge_id;
    Exception When no_data_found Then Raise e_belge_bulunamadi; End;
    Begin Select * Into l_hesap From hesap Where id = prm_hesap_id;
    Exception When no_data_found Then Raise e_hesap_bulunamadi; End;

    l_sonuc := siparis_kalem_kontrol(l_belge, l_hesap, l_t_siparis_istek_type, l_sd_kontrol_col,
                                     prm_cevap_kodu, prm_error_type, prm_error_num, prm_error_str);
    If l_sonuc <> pck_def.c_success Then Raise e_kontrol_basarisiz; End If;

    Rollback; -- sadece kontrol, islem yapilmaz
    Return pck_def.c_success;
  Exception
    When e_belge_bulunamadi Then
      pck_general.set_out_variables(prm_cevap_kodu, pck_def.c_rc_belge_bulunamadi, prm_error_type, 'APP', prm_error_num, -20304, prm_error_str, 'siparis_detay_kontrol_ekran: Belge bulunamadi');
      Return pck_def.c_fail;
    When e_hesap_bulunamadi Then
      pck_general.set_out_variables(prm_cevap_kodu, pck_def.c_rc_hesap_bulunamadi, prm_error_type, 'APP', prm_error_num, -20304, prm_error_str, 'siparis_detay_kontrol_ekran: Hesap bulunamadi');
      Return pck_def.c_fail;
    When e_kontrol_basarisiz Then Return pck_def.c_fail;
    When Others Then
      pck_general.set_out_variables(prm_cevap_kodu, pck_def.c_rc_sql_error, prm_error_type, 'ORA', prm_error_num, Sqlcode, prm_error_str, 'siparis_detay_kontrol_ekran:' || Sqlerrm);
      Return pck_def.c_fail;
  End siparis_detay_kontrol_ekran;

  -- =========================================================================
  -- siparis_kontrol_ekran: Ekran uzerinden belge bazli siparis kontrolu
  -- =========================================================================
  Function siparis_kontrol_ekran(prm_belge_id             In belge.id%Type,
                                 prm_hesap_id             In hesap.id%Type,
                                 prm_t_siparis_istek_type In t_siparis_istek_type_dizi,
                                 prm_t_siparis_cevap_type Out t_siparis_cevap_type,
                                 prm_cevap_kodu           In Out Varchar2,
                                 prm_error_type           In Out Nocopy database_exception.error_type%Type,
                                 prm_error_num            In Out Nocopy database_exception.error_code%Type,
                                 prm_error_str            In Out Nocopy database_exception.error_message%Type) Return Number Is
    l_belge     belge%Rowtype;
    l_hesap     hesap%Rowtype;
    l_sonuc     Number;
    l_t_siparis_istek_type t_siparis_istek_type;
    e_belge_bulunamadi Exception;
    e_hesap_bulunamadi Exception;
  Begin
    If prm_t_siparis_istek_type Is Null Or prm_t_siparis_istek_type.count = 0 Then
      pck_general.set_out_variables(prm_cevap_kodu, pck_def.c_rc_istek_bos, prm_error_type, 'APP', prm_error_num, -20304, prm_error_str, 'siparis_kontrol_ekran: Istek bos');
      Return pck_def.c_fail;
    End If;
    l_t_siparis_istek_type := prm_t_siparis_istek_type(prm_t_siparis_istek_type.first);

    Begin Select * Into l_belge From belge Where id = prm_belge_id;
    Exception When no_data_found Then Raise e_belge_bulunamadi; End;
    Begin Select * Into l_hesap From hesap Where id = prm_hesap_id;
    Exception When no_data_found Then Raise e_hesap_bulunamadi; End;

    l_sonuc := siparis_isle(l_belge, l_hesap, l_t_siparis_istek_type.islem_kodu,
                            l_t_siparis_istek_type, prm_t_siparis_cevap_type,
                            prm_cevap_kodu, prm_error_type, prm_error_num, prm_error_str);
    Return l_sonuc;
  Exception
    When e_belge_bulunamadi Then
      pck_general.set_out_variables(prm_cevap_kodu, pck_def.c_rc_belge_bulunamadi, prm_error_type, 'APP', prm_error_num, -20304, prm_error_str, 'siparis_kontrol_ekran: Belge bulunamadi');
      Return pck_def.c_fail;
    When e_hesap_bulunamadi Then
      pck_general.set_out_variables(prm_cevap_kodu, pck_def.c_rc_hesap_bulunamadi, prm_error_type, 'APP', prm_error_num, -20304, prm_error_str, 'siparis_kontrol_ekran: Hesap bulunamadi');
      Return pck_def.c_fail;
    When Others Then
      pck_general.set_out_variables(prm_cevap_kodu, pck_def.c_rc_sql_error, prm_error_type, 'ORA', prm_error_num, Sqlcode, prm_error_str, 'siparis_kontrol_ekran:' || Sqlerrm);
      Return pck_def.c_fail;
  End siparis_kontrol_ekran;


  -- =========================================================================
  -- siparis_iptali_kontrol: Siparis iptal islemi icin on kontrol
  -- =========================================================================
  Function siparis_iptali_kontrol(prm_hareket_id       In siparis_hareket.id%Type,
                                  prm_mutabakat_tarihi In siparis_hareket.mutabakat_tarihi%Type,
                                  prm_stan             In siparis_hareket.stan%Type,
                                  prm_uygulama         In siparis_hareket.uygulama%Type,
                                  prm_kullanici        In siparis_hareket.tanitim_kullanici%Type,
                                  prm_siparis_hareket  Out siparis_hareket%Rowtype,
                                  prm_info_msj         Out Varchar2,
                                  prm_cevap_kodu       In Out Varchar2,
                                  prm_error_type       In Out Nocopy database_exception.error_type%Type,
                                  prm_error_num        In Out Nocopy database_exception.error_code%Type,
                                  prm_error_str        In Out Nocopy database_exception.error_message%Type) Return Number Is
    l_count Number;
    l_siparis siparis%Rowtype;
    l_siparis_sahibi siparis_sahibi%Rowtype;
    l_belge belge%Rowtype;
    l_hesap hesap%Rowtype;
    l_ht_opf hesap_tipi.ortak_payli_belge%Type;

    e_hareket_bulunamadi Exception;
    e_iptal_yapilamaz    Exception;
    e_resend             Exception;
    e_belge_bulunamadi   Exception;
    e_taksit_iptal_yetki Exception;
  Begin
    Begin
      Select * Into prm_siparis_hareket From siparis_hareket Where id = prm_hareket_id;
    Exception When no_data_found Then Raise e_hareket_bulunamadi; End;

    -- Resend kontrolu
    Select Count(0) Into l_count From siparis_hareket sh
     Where sh.ust_id = prm_hareket_id And sh.islem_id = pck_def.c_siparis_iptal_islemi;
    If l_count > 0 Then Raise e_resend; End If;

    -- Iptal edilebilirlik kontrolu
    If prm_siparis_hareket.islem_durum_id = pck_def.c_notsend Then Raise e_iptal_yapilamaz; End If;
    If prm_siparis_hareket.siparis_hareket_durum_id <> pck_def.c_shd_degerlendirildi Then Raise e_iptal_yapilamaz; End If;

    Select * Into l_siparis From siparis Where id = prm_siparis_hareket.siparis_id;
    Select * Into l_belge From belge Where id = l_siparis.belge_id;
    Select * Into l_hesap From hesap Where id = l_belge.hesap_id;

    -- Taksitli satis iptal yetki kontrolu
    If prm_siparis_hareket.taksitli_satis_id Is Not Null Then
      Begin
        Select 1 Into l_count From kullanici_yetki ky
         Where ky.kullanici_id = prm_kullanici And ky.yetki_id = pck_def.c_yetki_taksit_iptal And rownum = 1;
      Exception When no_data_found Then Raise e_taksit_iptal_yetki; End;
    End If;

    -- OPF kontrol
    Begin
      Select ht.ortak_payli_belge Into l_ht_opf From hesap_tipi ht Where ht.id = l_hesap.hesap_tipi_id;
    Exception When no_data_found Then l_ht_opf := pck_def.c_hayir; End;

    prm_info_msj := ' HareketId:' || prm_hareket_id || ',BelgeId:' || l_belge.id || ',HesapNo:' || l_hesap.hesap_no;
    Return pck_def.c_success;
  Exception
    When e_hareket_bulunamadi Then
      pck_general.set_out_variables(prm_cevap_kodu, pck_def.c_rc_hareket_bulunamadi, prm_error_type, 'APP', prm_error_num, -20304, prm_error_str, 'siparis_iptali_kontrol: Hareket bulunamadi');
      Return pck_def.c_fail;
    When e_resend Then
      pck_general.set_out_variables(prm_cevap_kodu, pck_def.c_rc_resend, prm_error_type, 'APP', prm_error_num, -20304, prm_error_str, 'siparis_iptali_kontrol: Resend');
      Return pck_def.c_fail;
    When e_iptal_yapilamaz Then
      pck_general.set_out_variables(prm_cevap_kodu, pck_def.c_rc_iptal_yapilamaz, prm_error_type, 'APP', prm_error_num, -20304, prm_error_str, 'siparis_iptali_kontrol: Iptal yapilamaz');
      Return pck_def.c_fail;
    When e_taksit_iptal_yetki Then
      pck_general.set_out_variables(prm_cevap_kodu, pck_def.c_rc_taksit_iptal_yetki_yok, prm_error_type, 'APP', prm_error_num, -20304, prm_error_str, 'siparis_iptali_kontrol: Taksit iptal yetkisi yok');
      Return pck_def.c_fail;
    When Others Then
      pck_general.set_out_variables(prm_cevap_kodu, pck_def.c_rc_sql_error, prm_error_type, 'ORA', prm_error_num, Sqlcode, prm_error_str, 'siparis_iptali_kontrol:' || Sqlerrm);
      Return pck_def.c_fail;
  End siparis_iptali_kontrol;

  -- =========================================================================
  -- siparis_iptali: Siparis iptal islemi
  -- =========================================================================
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
                          prm_error_str        In Out Nocopy database_exception.error_message%Type) Return Number Is

    l_sonuc                    Number;
    l_info_msj                 Varchar2(400);
    l_iskonto_top_tum          Number;
    l_iskonto_top_mahsup_haric Number;
    l_siparis_hareket          siparis_hareket%Rowtype;
    l_bf_belge                 belge%Rowtype;
    l_stan                     siparis_hareket.stan%Type;
    l_belge                    belge%Rowtype;
    l_hesaplanan_tutar         Number;
    l_hesap                    hesap%Rowtype;
    l_tb_id                    tahakkuk_bildirim.id%Type;
    l_carpan                   Number;
    l_sip_har_det_id           siparis_hareket_detay.id%Type;
    l_siparis_hareket_id       siparis_hareket.id%Type;
    l_siparis_hareket_ust      siparis_hareket%Rowtype;
    l_siparis_uygulama         siparis.uygulama%Type;
    l_belge_durum              belge.belge_durum%Type;
    l_bildirim_row             bildirim%rowtype;
    l_belge_row                belge%Rowtype;
    l_ht_opf                   hesap_tipi.ortak_payli_belge%Type;
    e_hata Exception;

    Cursor c_sip_har_detay Is
      Select * From siparis_hareket_detay shd Where shd.siparis_hareket_id = prm_hareket_id;

  Begin
    l_sonuc := siparis_iptali_kontrol(prm_hareket_id, prm_mutabakat_tarihi, prm_stan,
                                      prmuygulama, prm_kullanici, l_siparis_hareket, l_info_msj,
                                      prm_cevap_kodu, prm_error_type, prm_error_num, prm_error_str);
    If l_sonuc <> pck_def.c_success Then Return l_sonuc; End If;

    Select * Into l_belge From belge Where id = l_siparis_hareket.belge_id;

    -- Belge tablosu update edilir.
    If l_belge.belge_tipi = pck_def.c_ek_tahsilat_belgesi Then
      Update belge b Set b.belge_durum = pck_def.c_paid, b.guncelleme_zaman = systimestamp,
             b.guncelleyen_kullanici = prm_kullanici
       Where b.id = l_siparis_hareket.belge_id And b.belge_durum = pck_def.c_revize_ile_kapanmis;
    Else
      Select decode(sign(l_belge.kalan_tutar + l_siparis_hareket.islem_tutari), 0, pck_def.c_paid,
                    -1, pck_def.c_paid,
                    decode(l_belge.kalan_tutar + l_siparis_hareket.islem_tutari,
                           l_belge.siparis_duzeltme_tutar + l_siparis_hareket.islem_tutari,
                           pck_def.c_unpaid, pck_def.c_partial))
        Into l_belge_durum From dual;

      Update belge b Set b.siparis_duzeltme_tutar = b.siparis_duzeltme_tutar + l_siparis_hareket.islem_tutari,
             b.kalan_tutar = b.kalan_tutar + l_siparis_hareket.islem_tutari,
             b.belge_durum = l_belge_durum, b.guncelleme_zaman = systimestamp,
             b.guncelleyen_kullanici = prm_kullanici,
             kilitli = decode(b.hesap_tipi_id, pck_def.c_hesap_tipi_dijital,
                              decode(l_belge_durum, pck_def.c_paid, 0, 1), b.kilitli),
             b.odeme_tarihi = null
       Where b.id = l_siparis_hareket.belge_id;
    End If;

    Select * Into l_belge From belge Where id = l_siparis_hareket.belge_id;

    -- Iptal hareket olustur
    Select seq_siparis_hareket.nextval Into prm_iptal_hareket_id From dual;

    Insert Into siparis_hareket Values (prm_iptal_hareket_id, trunc(Sysdate),
       l_siparis_hareket.kurum_id, pck_general.get_stan, l_siparis_hareket.siparis_id,
       pck_def.c_shd_degerlendirildi, pck_def.c_id_iptal_edildi,
       pck_def.c_siparis_iptal_islemi, -l_siparis_hareket.islem_tutari,
       prm_hareket_id, prm_cevap_kodu, Null, Null, l_siparis_hareket.business_id,
       l_siparis_hareket.conversation_id, Sysdate, prm_kullanici, systimestamp, Null, 0);

    -- Siparis hareket detaylarini iptal et
    For r In c_sip_har_detay Loop
      Select seq_siparis_hareket_detay.nextval Into l_sip_har_det_id From dual;

      Insert Into siparis_hareket_detay Values (l_sip_har_det_id, prm_iptal_hareket_id,
         r.siparis_detay_id, -r.islem_tutari, r.kdv_tutari, r.otv_tutari,
         Sysdate, prm_kullanici, systimestamp, Null);

      -- Siparis detay guncelle
      Update siparis_detay sd
         Set sd.siparis_duzeltme_tutar = sd.siparis_duzeltme_tutar + r.islem_tutari,
             sd.kalan_tutar = sd.kalan_tutar + r.islem_tutari,
             sd.guncelleme_zamani = systimestamp,
             sd.guncelleyen_kullanici_id = prm_kullanici
       Where sd.id = r.siparis_detay_id;
    End Loop;

    -- OPF belge kontrolu --<SURUM> 3.57.0 <\SURUM>
    Begin
      Select ht.ortak_payli_belge Into l_ht_opf
        From hesap_tipi ht, hesap h
       Where h.id = l_belge.hesap_id And h.hesap_tipi_id = ht.id;
      If l_ht_opf = pck_def.c_evet Then
        l_sonuc := opf_siparis_isle(l_siparis_hareket, l_belge, prm_iptal_hareket_id,
                                    prm_cevap_kodu, prm_error_type, prm_error_num, prm_error_str);
      End If;
    Exception When no_data_found Then Null; End;

    -- Siparis durumu guncelle
    Update siparis s Set s.siparis_durum_id = pck_def.c_id_iptal_edildi,
           s.guncelleme_zamani = systimestamp
     Where s.id = l_siparis_hareket.siparis_id;

    -- Siparis sahibi hareket olustur
    Begin
      Select sh.id Into l_siparis_hareket_id
        From siparis_hareket sh, siparis_hareket_tahsilat_bilgi shtb
       Where sh.id = prm_hareket_id And sh.id = shtb.siparis_hareket_id
         And shtb.alacak_id Is Not Null And rownum = 1;

      teslimat_hareket_at(prm_iptal_hareket_id, pck_def.c_islem_iptal_bilgilendirme, Null);
    Exception When no_data_found Then Null; End;

    -- Bildirim guncelle
    Begin
      Select * Into l_bildirim_row From bildirim Where belge_id = l_belge.id And bildirim_durum = pck_def.c_bildirim_aktif;
      Update bildirim Set bildirim_durum = pck_def.c_bildirim_iptal, guncelleme_zamani = systimestamp
       Where id = l_bildirim_row.id;
    Exception When no_data_found Then Null; End;

    Return pck_def.c_success;
  Exception
    When Others Then
      pck_general.set_out_variables(prm_cevap_kodu, pck_def.c_rc_sql_error, prm_error_type, 'ORA', prm_error_num, Sqlcode, prm_error_str, 'siparis_iptali:' || Sqlerrm || l_info_msj);
      Return pck_def.c_fail;
  End siparis_iptali;

  -- =========================================================================
  -- siparis_iptali_ws: WS uzerinden siparis iptali
  -- =========================================================================
  Function siparis_iptali_ws(prm_t_siparis_istek_type In t_siparis_istek_type,
                              prm_cevap_kodu           In Out Varchar2,
                              prm_error_type           In Out Nocopy database_exception.error_type%Type,
                              prm_error_num            In Out Nocopy database_exception.error_code%Type,
                              prm_error_str            In Out Nocopy database_exception.error_message%Type) Return Number Is
    l_sonuc Number;
    l_iptal_hareket_id siparis_hareket.id%Type;
    l_siparis_hareket  siparis_hareket%Rowtype;
    l_belge            belge%Rowtype;
    l_hesap            hesap%Rowtype;
    l_info_msj         Varchar2(1000);
    e_hareket_bulunamadi Exception;
    e_belge_bulunamadi   Exception;
  Begin
    If prm_t_siparis_istek_type.ref_stan Is Null Then
      pck_general.set_out_variables(prm_cevap_kodu, pck_def.c_rc_ref_stan_bos, prm_error_type, 'APP', prm_error_num, -20304, prm_error_str, 'siparis_iptali_ws: Referans stan bos');
      Return pck_def.c_fail;
    End If;

    Begin
      Select * Into l_siparis_hareket From siparis_hareket sh
       Where sh.islem_tarihi = prm_t_siparis_istek_type.ref_islem_tarihi
         And sh.kurum_id = prm_t_siparis_istek_type.kurum_id
         And sh.stan = prm_t_siparis_istek_type.ref_stan;
    Exception When no_data_found Then Raise e_hareket_bulunamadi; End;

    l_sonuc := siparis_iptali(l_siparis_hareket.id, prm_t_siparis_istek_type.islem_tarihi,
                              prm_t_siparis_istek_type.stan, prm_t_siparis_istek_type.islem_kaynagi,
                              prm_t_siparis_istek_type.islem_tarihi, prm_t_siparis_istek_type.islem_kodu,
                              prm_t_siparis_istek_type.orig_il, prm_t_siparis_istek_type.orig_sube,
                              prm_t_siparis_istek_type.orig_gise, prm_t_siparis_istek_type.orig_kullanici,
                              prm_t_siparis_istek_type.kullanici_id, prm_t_siparis_istek_type.uygulama,
                              l_iptal_hareket_id, prm_cevap_kodu, prm_error_type, prm_error_num, prm_error_str);
    Return l_sonuc;
  Exception
    When e_hareket_bulunamadi Then
      pck_general.set_out_variables(prm_cevap_kodu, pck_def.c_rc_hareket_bulunamadi, prm_error_type, 'APP', prm_error_num, -20304, prm_error_str, 'siparis_iptali_ws: Hareket bulunamadi');
      Return pck_def.c_fail;
    When Others Then
      pck_general.set_out_variables(prm_cevap_kodu, pck_def.c_rc_sql_error, prm_error_type, 'ORA', prm_error_num, Sqlcode, prm_error_str, 'siparis_iptali_ws:' || Sqlerrm);
      Return pck_def.c_fail;
  End siparis_iptali_ws;


  -- =========================================================================
  -- toplu_siparis_isle: Toplu siparis isleme
  -- =========================================================================
  Function toplu_siparis_isle(prm_toplu_siparis_id   toplu_siparis.id%Type,
                              prm_kanal_no           Number,
                              prm_kurum_kodu         siparis_hareket.kurum_kodu%Type,
                              prm_orig_il            siparis_hareket.orig_il%Type,
                              prm_orig_sube          siparis_hareket.orig_sube%Type,
                              prm_orig_gise          siparis_hareket.orig_gise%Type,
                              prm_orig_kullanici     siparis_hareket.orig_kullanici%Type,
                              prm_cevap_kodu         In Out Varchar2,
                              prm_error_type         In Out Nocopy database_exception.error_type%Type,
                              prm_error_num          In Out Nocopy database_exception.error_code%Type,
                              prm_error_str          In Out Nocopy database_exception.error_message%Type) Return Number Is

    l_toplu_siparis       toplu_siparis%Rowtype;
    l_toplu_sip_col       toplusipcolcollection;
    l_idx                 Number := 0;
    l_sonuc               Number;
    l_belge               belge%Rowtype;
    l_hesap               hesap%Rowtype;
    l_info_msj            Varchar2(4000);
    l_t_siparis_istek_type t_siparis_istek_type;
    l_t_siparis_cevap_type t_siparis_cevap_type;
    l_basarili_sayisi     Number := 0;
    l_basarisiz_sayisi    Number := 0;
    l_toplam_tutar        Number := 0;
    l_kayit_durum         Number;

    e_toplu_bulunamadi  Exception;
    e_toplu_durum_hata  Exception;
    e_kayit_yok         Exception;

    Cursor c_toplu_kayitlar Is
      Select tsk.id, tsk.hesap_no, tsk.hizmet_no, tsk.belge_no, tsk.donem_kodu,
             tsk.tdk_flag, tsk.vergi_dahil_flag, tsk.vergi_tc_no, tsk.revize_sebebi_id,
             tsk.aciklama, tsk.siparis_belge_no, tsk.vergi_dairesi, tsk.bildirim_no,
             tsk.personel_sicil_no, tsk.bayi_kodu, tsk.cagri_merkezi_id,
             tsk.kampanya_id, tsk.taksitli_mi, tsk.taksitli_satis_id, tsk.taksit_no
        From toplu_siparis_kayitlar tsk
       Where tsk.toplu_siparis_id = prm_toplu_siparis_id
         And tsk.kayit_durum = pck_def.c_toplu_kayit_bekliyor
       Order By tsk.id;

  Begin
    Begin
      Select * Into l_toplu_siparis From toplu_siparis Where id = prm_toplu_siparis_id;
    Exception When no_data_found Then Raise e_toplu_bulunamadi; End;

    If l_toplu_siparis.durum_id Not In (pck_def.c_toplu_durum_yuklendi, pck_def.c_toplu_durum_devam) Then
      Raise e_toplu_durum_hata;
    End If;

    Update toplu_siparis Set durum_id = pck_def.c_toplu_durum_islemde, guncelleme_zamani = systimestamp
     Where id = prm_toplu_siparis_id;
    Commit;

    For r In c_toplu_kayitlar Loop
      Begin
        Savepoint sp_toplu_kayit;

        -- Hesap bul
        Begin
          Select h.* Into l_hesap From hesap h Where h.hesap_no = r.hesap_no And rownum = 1;
        Exception When no_data_found Then
          Update toplu_siparis_kayitlar Set kayit_durum = pck_def.c_toplu_kayit_basarisiz,
                 hata_mesaji = 'Hesap bulunamadi: ' || r.hesap_no, guncelleme_zamani = systimestamp
           Where id = r.id;
          l_basarisiz_sayisi := l_basarisiz_sayisi + 1;
          Commit;
          Continue;
        End;

        -- Belge bul
        Begin
          Select b.* Into l_belge From belge b
           Where b.hesap_id = l_hesap.id And b.belge_no = r.belge_no
             And b.donem_kodu = r.donem_kodu And rownum = 1;
        Exception When no_data_found Then
          Update toplu_siparis_kayitlar Set kayit_durum = pck_def.c_toplu_kayit_basarisiz,
                 hata_mesaji = 'Belge bulunamadi: ' || r.belge_no || '/' || r.donem_kodu, guncelleme_zamani = systimestamp
           Where id = r.id;
          l_basarisiz_sayisi := l_basarisiz_sayisi + 1;
          Commit;
          Continue;
        End;

        -- Siparis istek tipi olustur
        l_t_siparis_istek_type := New t_siparis_istek_type(
          trunc(Sysdate), prm_kurum_kodu, pck_general.get_stan, l_toplu_siparis.sirket_id,
          prm_orig_il, prm_orig_sube, prm_orig_gise, prm_orig_kullanici,
          pck_def.c_inst_sistem_kullanici_id, 0,
          New t_siparis_istek_detay_type(r.tdk_flag, r.vergi_dahil_flag, r.revize_sebebi_id,
              r.aciklama, Null, Null, r.siparis_belge_no, r.vergi_dairesi, r.vergi_tc_no,
              r.bildirim_no, r.personel_sicil_no, r.bayi_kodu, r.cagri_merkezi_id,
              Null, Null, Null, Null, Null, Null, Null, Null, Null, Null, Null),
          Null, Null, Null, Null, 1, 0, r.hesap_no, r.taksit_no, r.belge_no, r.donem_kodu,
          pck_def.c_uygulama_toplu_siparis, 0, Null, pck_def.c_islemkodu_revize,
          Null, Null, Null, Null, prm_kurum_kodu, r.taksitli_mi);

        l_sonuc := siparis_isle(l_belge, l_hesap, pck_def.c_islemkodu_revize,
                                l_t_siparis_istek_type, l_t_siparis_cevap_type,
                                prm_cevap_kodu, prm_error_type, prm_error_num, prm_error_str);

        If l_sonuc = pck_def.c_success Then
          Update toplu_siparis_kayitlar Set kayit_durum = pck_def.c_toplu_kayit_basarili,
                 siparis_hareket_id = l_t_siparis_cevap_type.siparis_hareket_id, guncelleme_zamani = systimestamp
           Where id = r.id;
          l_basarili_sayisi := l_basarili_sayisi + 1;
        Else
          Rollback To sp_toplu_kayit;
          Update toplu_siparis_kayitlar Set kayit_durum = pck_def.c_toplu_kayit_basarisiz,
                 hata_mesaji = substr(prm_error_str, 1, 500), guncelleme_zamani = systimestamp
           Where id = r.id;
          l_basarisiz_sayisi := l_basarisiz_sayisi + 1;
        End If;

        Commit;

      Exception When Others Then
        Rollback To sp_toplu_kayit;
        Update toplu_siparis_kayitlar Set kayit_durum = pck_def.c_toplu_kayit_basarisiz,
               hata_mesaji = substr(Sqlerrm, 1, 500), guncelleme_zamani = systimestamp
         Where id = r.id;
        l_basarisiz_sayisi := l_basarisiz_sayisi + 1;
        Commit;
      End;
    End Loop;

    -- Toplu siparis durumunu guncelle
    If l_basarisiz_sayisi = 0 Then l_kayit_durum := pck_def.c_toplu_durum_tamamlandi;
    Else l_kayit_durum := pck_def.c_toplu_durum_hatali; End If;

    Update toplu_siparis Set durum_id = l_kayit_durum,
           basarili_kayit_sayisi = l_basarili_sayisi, basarisiz_kayit_sayisi = l_basarisiz_sayisi,
           guncelleme_zamani = systimestamp
     Where id = prm_toplu_siparis_id;
    Commit;

    Return pck_def.c_success;
  Exception
    When e_toplu_bulunamadi Then
      pck_general.set_out_variables(prm_cevap_kodu, pck_def.c_rc_toplu_bulunamadi, prm_error_type, 'APP', prm_error_num, -20304, prm_error_str, 'toplu_siparis_isle: Toplu siparis bulunamadi');
      Return pck_def.c_fail;
    When e_toplu_durum_hata Then
      pck_general.set_out_variables(prm_cevap_kodu, pck_def.c_rc_toplu_durum_hata, prm_error_type, 'APP', prm_error_num, -20304, prm_error_str, 'toplu_siparis_isle: Toplu siparis durumu uygun degil');
      Return pck_def.c_fail;
    When Others Then
      pck_general.set_out_variables(prm_cevap_kodu, pck_def.c_rc_sql_error, prm_error_type, 'ORA', prm_error_num, Sqlcode, prm_error_str, 'toplu_siparis_isle:' || Sqlerrm);
      Return pck_def.c_fail;
  End toplu_siparis_isle;

  -- =========================================================================
  -- toplu_siparis_iptali_isle: Toplu siparis iptali isleme
  -- =========================================================================
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
                                    prm_error_str         In Out Nocopy database_exception.error_message%Type) Return Number Is

    l_toplu_siparis    toplu_siparis%Rowtype;
    l_sonuc            Number;
    l_iptal_hareket_id siparis_hareket.id%Type;
    l_basarili         Number := 0;
    l_basarisiz        Number := 0;
    l_hareket_id       siparis_hareket.id%Type;

    e_toplu_bulunamadi Exception;
    e_kayit_listesi_bos Exception;

    Cursor c_iptal_kayitlar Is
      Select tsk.id, tsk.siparis_hareket_id
        From toplu_siparis_kayitlar tsk
       Where tsk.toplu_siparis_id = prm_toplu_siparis_id
         And tsk.kayit_durum = pck_def.c_toplu_kayit_basarili
         And tsk.id In (Select deger From Table(prm_kayit_id_list))
       Order By tsk.id;

  Begin
    Begin Select * Into l_toplu_siparis From toplu_siparis Where id = prm_toplu_siparis_id;
    Exception When no_data_found Then Raise e_toplu_bulunamadi; End;

    If prm_kayit_id_list Is Null Or prm_kayit_id_list.count = 0 Then Raise e_kayit_listesi_bos; End If;

    For r In c_iptal_kayitlar Loop
      Begin
        Savepoint sp_toplu_iptal;

        l_sonuc := siparis_iptali(r.siparis_hareket_id, trunc(Sysdate), pck_general.get_stan,
                                  0, trunc(Sysdate), pck_def.c_siparis_iptal_islemi,
                                  prm_orig_il, prm_orig_sube, prm_orig_gise, prm_orig_kullanici,
                                  prm_kullanici_id, pck_def.c_uygulama_toplu_siparis,
                                  l_iptal_hareket_id, prm_cevap_kodu, prm_error_type, prm_error_num, prm_error_str);

        If l_sonuc = pck_def.c_success Then
          Update toplu_siparis_kayitlar Set kayit_durum = pck_def.c_toplu_kayit_iptal,
                 guncelleme_zamani = systimestamp Where id = r.id;
          l_basarili := l_basarili + 1;
        Else
          Rollback To sp_toplu_iptal;
          Update toplu_siparis_kayitlar Set hata_mesaji = substr(prm_error_str, 1, 500),
                 guncelleme_zamani = systimestamp Where id = r.id;
          l_basarisiz := l_basarisiz + 1;
        End If;
        Commit;
      Exception When Others Then
        Rollback To sp_toplu_iptal;
        Update toplu_siparis_kayitlar Set hata_mesaji = substr(Sqlerrm, 1, 500),
               guncelleme_zamani = systimestamp Where id = r.id;
        l_basarisiz := l_basarisiz + 1;
        Commit;
      End;
    End Loop;

    Return pck_def.c_success;
  Exception
    When e_toplu_bulunamadi Then
      pck_general.set_out_variables(prm_cevap_kodu, pck_def.c_rc_toplu_bulunamadi, prm_error_type, 'APP', prm_error_num, -20304, prm_error_str, 'toplu_siparis_iptali_isle: Toplu siparis bulunamadi');
      Return pck_def.c_fail;
    When e_kayit_listesi_bos Then
      pck_general.set_out_variables(prm_cevap_kodu, pck_def.c_rc_istek_bos, prm_error_type, 'APP', prm_error_num, -20304, prm_error_str, 'toplu_siparis_iptali_isle: Kayit listesi bos');
      Return pck_def.c_fail;
    When Others Then
      pck_general.set_out_variables(prm_cevap_kodu, pck_def.c_rc_sql_error, prm_error_type, 'ORA', prm_error_num, Sqlcode, prm_error_str, 'toplu_siparis_iptali_isle:' || Sqlerrm);
      Return pck_def.c_fail;
  End toplu_siparis_iptali_isle;

  -- =========================================================================
  -- siparis_oncesi_degerlendir: Toplu siparis oncesi degerlendirme
  -- =========================================================================
  Function siparis_oncesi_degerlendir(prm_token        toplu_siparis_kayitlar_tmp.token%Type,
                                     prm_kanal_no     Number,
                                     prm_kurum_kodu   In siparis_hareket.kurum_kodu%Type,
                                     prm_kullanici_id In siparis_hareket.tanitim_kullanici%Type,
                                     prm_cevap_kodu   In Out Varchar2,
                                     prm_error_type   In Out Nocopy database_exception.error_type%Type,
                                     prm_error_num    In Out Nocopy database_exception.error_code%Type,
                                     prm_error_str    In Out Nocopy database_exception.error_message%Type) Return Number Is
    l_count Number;
    l_hesap hesap%Rowtype;
    l_belge belge%Rowtype;
    l_sonuc Number;
    l_info_msj Varchar2(1000);

    Cursor c_tmp_kayitlar Is
      Select * From toplu_siparis_kayitlar_tmp Where token = prm_token Order By id;

  Begin
    Select Count(0) Into l_count From toplu_siparis_kayitlar_tmp Where token = prm_token;
    If l_count = 0 Then
      pck_general.set_out_variables(prm_cevap_kodu, pck_def.c_rc_istek_bos, prm_error_type, 'APP', prm_error_num, -20304, prm_error_str, 'siparis_oncesi_degerlendir: Token ile kayit bulunamadi');
      Return pck_def.c_fail;
    End If;

    For r In c_tmp_kayitlar Loop
      Begin
        -- Hesap kontrolu
        Begin
          Select h.* Into l_hesap From hesap h Where h.hesap_no = r.hesap_no And rownum = 1;
        Exception When no_data_found Then
          Update toplu_siparis_kayitlar_tmp Set degerlendirme_sonucu = 'BASARISIZ',
                 hata_mesaji = 'Hesap bulunamadi', guncelleme_zamani = systimestamp Where id = r.id;
          Continue;
        End;

        -- Belge kontrolu
        Begin
          Select b.* Into l_belge From belge b
           Where b.hesap_id = l_hesap.id And b.belge_no = r.belge_no
             And b.donem_kodu = r.donem_kodu And rownum = 1;
        Exception When no_data_found Then
          Update toplu_siparis_kayitlar_tmp Set degerlendirme_sonucu = 'BASARISIZ',
                 hata_mesaji = 'Belge bulunamadi', guncelleme_zamani = systimestamp Where id = r.id;
          Continue;
        End;

        -- Belge durum kontrolu
        If l_belge.belge_durum Not In (pck_def.c_unpaid, pck_def.c_partial) Then
          Update toplu_siparis_kayitlar_tmp Set degerlendirme_sonucu = 'BASARISIZ',
                 hata_mesaji = 'Belge durumu uygun degil: ' || l_belge.belge_durum, guncelleme_zamani = systimestamp Where id = r.id;
          Continue;
        End If;

        -- Tutar kontrolu
        If r.siparis_tutari > l_belge.siparis_duzeltme_tutar Then
          Update toplu_siparis_kayitlar_tmp Set degerlendirme_sonucu = 'BASARISIZ',
                 hata_mesaji = 'Tutar belge tutarindan buyuk', guncelleme_zamani = systimestamp Where id = r.id;
          Continue;
        End If;

        Update toplu_siparis_kayitlar_tmp Set degerlendirme_sonucu = 'BASARILI',
               hesap_id = l_hesap.id, belge_id = l_belge.id, guncelleme_zamani = systimestamp Where id = r.id;

      Exception When Others Then
        Update toplu_siparis_kayitlar_tmp Set degerlendirme_sonucu = 'BASARISIZ',
               hata_mesaji = substr(Sqlerrm, 1, 500), guncelleme_zamani = systimestamp Where id = r.id;
      End;
    End Loop;

    Commit;
    Return pck_def.c_success;
  Exception
    When Others Then
      pck_general.set_out_variables(prm_cevap_kodu, pck_def.c_rc_sql_error, prm_error_type, 'ORA', prm_error_num, Sqlcode, prm_error_str, 'siparis_oncesi_degerlendir:' || Sqlerrm);
      Return pck_def.c_fail;
  End siparis_oncesi_degerlendir;


  -- =========================================================================
  -- stok_hazirlik: Stok hazirlama islemi
  -- =========================================================================
  Function stok_hazirlik(prm_sirket_id  In sirket.id%Type,
                         prm_gun        In Number,
                         prm_cevap_kodu In Out Varchar2,
                         prm_error_type In Out Nocopy database_exception.error_type%Type,
                         prm_error_num  In Out Nocopy database_exception.error_code%Type,
                         prm_error_str  In Out Nocopy database_exception.error_message%Type) Return Number Is
    l_count Number;
    l_stok_analiz_id stok_analiz.id%Type;
    l_baslangic_tarihi Date;
    l_bitis_tarihi Date;
    l_siparis_sahibi_id siparis_sahibi.id%Type;
    l_toplam_tutar Number := 0;

    Cursor c_siparis_sahipleri Is
      Select ss.id, ss.hesap_id, ss.musteri_id, ss.sirket_id,
             nvl(ss.siparis_toplam_tutari, 0) siparis_toplam_tutari,
             nvl(ss.belge_mahsup_tutari, 0) belge_mahsup_tutari,
             nvl(ss.avans_mahsup_tutari, 0) avans_mahsup_tutari,
             nvl(ss.nakit_siparis_tutari, 0) nakit_siparis_tutari,
             nvl(ss.kalan_alacak_tutari, 0) kalan_alacak_tutari,
             nvl(ss.transfere_aktarilan_tutar, 0) transfere_aktarilan_tutar,
             nvl(ss.kesinti_yapilan_tutar, 0) kesinti_yapilan_tutar
        From siparis_sahibi ss
       Where ss.sirket_id = prm_sirket_id
         And ss.kalan_alacak_tutari > 0
         And Exists (Select 1 From siparis s Where s.siparis_sahibi_id = ss.id
                      And s.siparis_durum_id = pck_def.c_id_basarili)
       Order By ss.id;

  Begin
    l_baslangic_tarihi := trunc(Sysdate) - prm_gun;
    l_bitis_tarihi := trunc(Sysdate);

    -- Stok analiz kaydi olustur
    Select seq_stok_analiz.nextval Into l_stok_analiz_id From dual;
    Insert Into stok_analiz Values (l_stok_analiz_id, prm_sirket_id, l_baslangic_tarihi, l_bitis_tarihi,
       pck_def.c_stok_analiz_durum_islemde, 0, 0, Sysdate, pck_def.c_inst_sistem_kullanici_id, systimestamp, Null);
    Commit;

    For r In c_siparis_sahipleri Loop
      Begin
        -- Siparis sahibi hareket olustur
        Insert Into siparis_sahibi_hareket Values (
          seq_siparis_sahibi_hareket.nextval, r.id, trunc(Sysdate),
          pck_def.c_islem_stok_hazirlama, pck_def.c_sshd_degerlendirilecek,
          r.siparis_toplam_tutari, r.belge_mahsup_tutari, r.avans_mahsup_tutari,
          r.nakit_siparis_tutari, r.kalan_alacak_tutari,
          r.transfere_aktarilan_tutar, r.kesinti_yapilan_tutar,
          Null, Null, r.sirket_id, Sysdate, pck_def.c_inst_sistem_kullanici_id,
          systimestamp, Null, Null, Null, Null);

        l_toplam_tutar := l_toplam_tutar + r.kalan_alacak_tutari;

        If Mod(c_siparis_sahipleri%Rowcount, 100) = 0 Then Commit; End If;
      Exception When Others Then Null;
      End;
    End Loop;

    Update stok_analiz Set durum_id = pck_def.c_stok_analiz_durum_tamamlandi,
           toplam_kayit = c_siparis_sahipleri%Rowcount, toplam_tutar = l_toplam_tutar,
           guncelleme_zamani = systimestamp
     Where id = l_stok_analiz_id;
    Commit;

    Return pck_def.c_success;
  Exception
    When Others Then
      pck_general.set_out_variables(prm_cevap_kodu, pck_def.c_rc_sql_error, prm_error_type, 'ORA', prm_error_num, Sqlcode, prm_error_str, 'stok_hazirlik:' || Sqlerrm);
      Return pck_def.c_fail;
  End stok_hazirlik;

  -- =========================================================================
  -- stok_faaliyet: Siparis sahibi hareket islemleri
  -- =========================================================================
  Function stok_faaliyet(prm_siparis_sahibi_har_id In siparis_sahibi_hareket.id%Type,
                         prm_cevap_kodu            In Out Varchar2,
                         prm_error_type            In Out Nocopy database_exception.error_type%Type,
                         prm_error_num             In Out Nocopy database_exception.error_code%Type,
                         prm_error_str             In Out Nocopy database_exception.error_message%Type) Return Number Is
    l_ssh siparis_sahibi_hareket%Rowtype;
    l_siparis_sahibi siparis_sahibi%Rowtype;
    l_sonuc Number;
    l_count Number;
    l_toplam_tutar Number := 0;
    l_belge_mahsup Number := 0;
    l_avans_mahsup Number := 0;
    l_nakit_tutar  Number := 0;
    l_kalan_alacak Number := 0;
    l_transfer_tutar Number := 0;
    l_kesinti_tutar  Number := 0;

    e_hareket_bulunamadi  Exception;
    e_hareket_durumu_hata Exception;

    Cursor c_siparis_detaylari Is
      Select sh.siparis_id, s.siparis_tutari, s.belge_id, b.hesap_id, b.kalan_tutar as belge_kalan
        From siparis_sahibi_hareket_detay sshd, siparis_hareket sh, siparis s, belge b
       Where sshd.siparis_sahibi_hareket_id = prm_siparis_sahibi_har_id
         And sshd.siparis_hareket_id = sh.id And sh.siparis_id = s.id
         And s.belge_id = b.id
       Order By s.tanitim_zamani;

  Begin
    Begin Select * Into l_ssh From siparis_sahibi_hareket Where id = prm_siparis_sahibi_har_id;
    Exception When no_data_found Then Raise e_hareket_bulunamadi; End;

    If l_ssh.siparis_sahibi_hareket_durum_id <> pck_def.c_sshd_degerlendirilecek Then
      Raise e_hareket_durumu_hata;
    End If;

    Select * Into l_siparis_sahibi From siparis_sahibi Where id = l_ssh.siparis_sahibi_id;

    -- Her siparis icin belge mahsup ve avans hesaplamasi
    For r In c_siparis_detaylari Loop
      l_toplam_tutar := l_toplam_tutar + r.siparis_tutari;
      If r.belge_kalan > 0 Then
        l_belge_mahsup := l_belge_mahsup + least(r.siparis_tutari, r.belge_kalan);
        l_kalan_alacak := l_kalan_alacak + greatest(r.siparis_tutari - r.belge_kalan, 0);
      Else
        l_kalan_alacak := l_kalan_alacak + r.siparis_tutari;
      End If;
    End Loop;

    -- Siparis sahibi finans guncelle
    siparis_sahibi_finans_guncelle(l_siparis_sahibi.id, pck_def.c_siparis_sahibi_finans_normal,
                                   l_toplam_tutar, l_belge_mahsup, l_avans_mahsup, l_nakit_tutar,
                                   l_transfer_tutar, l_kesinti_tutar, l_kalan_alacak, Null);

    Update siparis_sahibi_hareket Set siparis_sahibi_hareket_durum_id = pck_def.c_sshd_degerlendirildi,
           siparis_toplam_tutari = l_toplam_tutar, belge_mahsup_tutari = l_belge_mahsup,
           avans_mahsup_tutari = l_avans_mahsup, nakit_siparis_tutari = l_nakit_tutar,
           kalan_alacak_tutari = l_kalan_alacak, transfere_aktarilan_tutar = l_transfer_tutar,
           kesinti_yapilan_tutar = l_kesinti_tutar, guncelleme_zamani = systimestamp,
           guncelleyen_kullanici_id = pck_def.c_inst_sistem_kullanici_id
     Where id = prm_siparis_sahibi_har_id;

    Return pck_def.c_success;
  Exception
    When e_hareket_bulunamadi Then
      pck_general.set_out_variables(prm_cevap_kodu, pck_def.c_rc_hareket_bulunamadi, prm_error_type, 'APP', prm_error_num, -20304, prm_error_str, 'stok_faaliyet: Hareket bulunamadi');
      Return pck_def.c_fail;
    When e_hareket_durumu_hata Then
      pck_general.set_out_variables(prm_cevap_kodu, pck_def.c_rc_hareket_durumu_hata, prm_error_type, 'APP', prm_error_num, -20304, prm_error_str, 'stok_faaliyet: Hareket durumu uygun degil');
      Return pck_def.c_fail;
    When Others Then
      pck_general.set_out_variables(prm_cevap_kodu, pck_def.c_rc_sql_error, prm_error_type, 'ORA', prm_error_num, Sqlcode, prm_error_str, 'stok_faaliyet:' || Sqlerrm);
      Return pck_def.c_fail;
  End stok_faaliyet;

  -- =========================================================================
  -- siparis_onay: Siparis onay islemi
  -- =========================================================================
  Function siparis_onay(prm_siparis_id             In siparis.id%Type,
                        prm_siparis_hareket_id     In siparis_hareket.id%Type,
                        prm_hareket_id             In siparis_hareket.id%Type,
                        prm_alacak_id              In tahakkuk_bildirim.id%Type,
                        prm_vergi_no               In siparis_detay.vergi_no%Type,
                        prm_vergi_dairesi          In siparis_detay.vergi_dairesi%Type,
                        prm_siparis_belge_no       In siparis_detay.siparis_belge_no%Type,
                        prm_kullanici_id           In siparis.tanitan_kullanici_id%Type,
                        prm_taksitli_mi            In siparis_detay.taksitli_mi%Type,
                        prm_cevap_kodu             In Out Varchar2,
                        prm_error_type             In Out Nocopy database_exception.error_type%Type,
                        prm_error_num              In Out Nocopy database_exception.error_code%Type,
                        prm_error_str              In Out Nocopy database_exception.error_message%Type) Return Number Is
    l_siparis siparis%Rowtype;
    l_siparis_hareket siparis_hareket%Rowtype;
    l_siparis_detay siparis_detay_kayit%Rowtype;
    l_count Number;
    l_siparis_belge_no siparis_detay.siparis_belge_no%Type;
    l_info_msj Varchar2(1000);

    e_siparis_bulunamadi    Exception;
    e_hareket_bulunamadi    Exception;
    e_siparis_durumu_hata   Exception;
    e_detay_bulunamadi      Exception;
    e_belge_no_mevcut       Exception;
  Begin
    Begin Select * Into l_siparis From siparis Where id = prm_siparis_id;
    Exception When no_data_found Then Raise e_siparis_bulunamadi; End;

    If l_siparis.siparis_durum_id <> pck_def.c_id_onay_bekliyor Then Raise e_siparis_durumu_hata; End If;

    Begin Select * Into l_siparis_detay From siparis_detay_kayit Where siparis_id = prm_siparis_id;
    Exception When no_data_found Then Raise e_detay_bulunamadi; End;

    -- Siparis belge no kontrolu
    l_siparis_belge_no := nvl(prm_siparis_belge_no, siparis_belge_no_uret(prm_siparis_hareket_id));

    If l_siparis_belge_no Is Not Null Then
      Select Count(0) Into l_count From siparis_detay_kayit sdk
       Where sdk.siparis_belge_no = l_siparis_belge_no And sdk.id <> l_siparis_detay.id;
      If l_count > 0 Then Raise e_belge_no_mevcut; End If;
    End If;

    -- Siparis detay guncelle
    Update siparis_detay_kayit Set vergi_no = nvl(prm_vergi_no, vergi_no),
           vergi_dairesi = nvl(prm_vergi_dairesi, vergi_dairesi),
           siparis_belge_no = l_siparis_belge_no,
           taksitli_mi = nvl(prm_taksitli_mi, taksitli_mi),
           guncelleme_zamani = systimestamp, guncelleyen_kullanici_id = prm_kullanici_id
     Where id = l_siparis_detay.id;

    -- Siparis durumu guncelle
    Update siparis Set siparis_durum_id = pck_def.c_id_onaylandi, alacak_id = prm_alacak_id,
           guncelleme_zamani = systimestamp
     Where id = prm_siparis_id;

    -- Siparis hareket guncelle
    Update siparis_hareket Set siparis_durum_id = pck_def.c_id_onaylandi,
           cevap_kodu = pck_def.c_rc_success, guncelleme_zamani = systimestamp,
           guncelleyen_kullanici_id = prm_kullanici_id
     Where id = prm_siparis_hareket_id;

    Return pck_def.c_success;
  Exception
    When e_siparis_bulunamadi Then
      pck_general.set_out_variables(prm_cevap_kodu, pck_def.c_rc_siparis_bulunamadi, prm_error_type, 'APP', prm_error_num, -20304, prm_error_str, 'siparis_onay: Siparis bulunamadi. Id:' || prm_siparis_id);
      Return pck_def.c_fail;
    When e_siparis_durumu_hata Then
      pck_general.set_out_variables(prm_cevap_kodu, pck_def.c_rc_siparis_durumu_uyumsuz, prm_error_type, 'APP', prm_error_num, -20304, prm_error_str, 'siparis_onay: Siparis durumu uygun degil');
      Return pck_def.c_fail;
    When e_detay_bulunamadi Then
      pck_general.set_out_variables(prm_cevap_kodu, pck_def.c_rc_siparis_detay_yok, prm_error_type, 'APP', prm_error_num, -20304, prm_error_str, 'siparis_onay: Siparis detay bulunamadi');
      Return pck_def.c_fail;
    When e_belge_no_mevcut Then
      pck_general.set_out_variables(prm_cevap_kodu, pck_def.c_rc_belge_no_mevcut, prm_error_type, 'APP', prm_error_num, -20304, prm_error_str, 'siparis_onay: Belge no zaten kullanilmis: ' || l_siparis_belge_no);
      Return pck_def.c_fail;
    When Others Then
      pck_general.set_out_variables(prm_cevap_kodu, pck_def.c_rc_sql_error, prm_error_type, 'ORA', prm_error_num, Sqlcode, prm_error_str, 'siparis_onay:' || Sqlerrm);
      Return pck_def.c_fail;
  End siparis_onay;

  -- =========================================================================
  -- munferitle_siparis_yap: Munferit siparis islemi
  -- =========================================================================
  Function munferitle_siparis_yap(prm_sh_id                      In siparis_hareket.id%Type,
                                  prm_t_siparis_munferit_dizi     In t_siparis_munferit_type_dizi,
                                  prm_t_siparis_istek_type_dizi   In t_siparis_istek_type_dizi,
                                  prm_t_siparis_cevap_type        Out t_siparis_cevap_type,
                                  prm_cevap_kodu                  In Out Varchar2,
                                  prm_error_type                  In Out Nocopy database_exception.error_type%Type,
                                  prm_error_num                   In Out Nocopy database_exception.error_code%Type,
                                  prm_error_str                   In Out Nocopy database_exception.error_message%Type) Return Number Is
    l_siparis_hareket siparis_hareket%Rowtype;
    l_siparis         siparis%Rowtype;
    l_siparis_sahibi  siparis_sahibi%Rowtype;
    l_belge           belge%Rowtype;
    l_hesap           hesap%Rowtype;
    l_sonuc           Number;
    l_count           Number;
    l_munferit_toplam Number := 0;
    l_info_msj        Varchar2(1000);
    l_t_siparis_istek_type t_siparis_istek_type;

    e_hareket_bulunamadi   Exception;
    e_istek_bos            Exception;
    e_munferit_bos         Exception;
    e_siparis_basarisiz    Exception;
    e_tutar_asimi          Exception;
  Begin
    If prm_t_siparis_istek_type_dizi Is Null Or prm_t_siparis_istek_type_dizi.count = 0 Then Raise e_istek_bos; End If;
    If prm_t_siparis_munferit_dizi Is Null Or prm_t_siparis_munferit_dizi.count = 0 Then Raise e_munferit_bos; End If;

    l_t_siparis_istek_type := prm_t_siparis_istek_type_dizi(prm_t_siparis_istek_type_dizi.first);

    Begin Select * Into l_siparis_hareket From siparis_hareket Where id = prm_sh_id;
    Exception When no_data_found Then Raise e_hareket_bulunamadi; End;

    Select * Into l_siparis From siparis Where id = l_siparis_hareket.siparis_id;
    Select * Into l_siparis_sahibi From siparis_sahibi Where id = l_siparis.siparis_sahibi_id;
    Select * Into l_belge From belge Where id = l_siparis.belge_id;
    Select * Into l_hesap From hesap Where id = l_belge.hesap_id;

    -- Munferit toplamini hesapla
    For i In prm_t_siparis_munferit_dizi.first .. prm_t_siparis_munferit_dizi.last Loop
      If prm_t_siparis_munferit_dizi(i).t_munferit_list Is Not Null Then
        For j In prm_t_siparis_munferit_dizi(i).t_munferit_list.first .. prm_t_siparis_munferit_dizi(i).t_munferit_list.last Loop
          l_munferit_toplam := l_munferit_toplam + nvl(prm_t_siparis_munferit_dizi(i).t_munferit_list(j).revize_tutari, 0);
        End Loop;
      End If;
    End Loop;

    -- Tutar kontrolu
    If l_munferit_toplam > l_siparis.siparis_tutari Then Raise e_tutar_asimi; End If;

    -- Munferit siparis istek olustur
    l_t_siparis_istek_type.siparis_toplam_tutar := l_munferit_toplam;
    l_t_siparis_istek_type.islem_kodu := pck_def.c_islemkodu_revize;

    l_sonuc := siparis_isle(l_belge, l_hesap, l_t_siparis_istek_type.islem_kodu,
                            l_t_siparis_istek_type, prm_t_siparis_cevap_type,
                            prm_cevap_kodu, prm_error_type, prm_error_num, prm_error_str);
    If l_sonuc <> pck_def.c_success Then Raise e_siparis_basarisiz; End If;

    -- Munferit siparis tutarini guncelle
    Update siparis Set munferit_siparis_tutari = munferit_siparis_tutari + l_munferit_toplam,
           guncelleme_zamani = systimestamp Where id = l_siparis.id;

    -- Munferit tahsilat bilgileri olustur
    For i In prm_t_siparis_munferit_dizi.first .. prm_t_siparis_munferit_dizi.last Loop
      Insert Into siparis_hareket_tahsilat_bilgi Values (
        seq_siparis_hrkt_tahsilat_bilgi.nextval, prm_t_siparis_cevap_type.siparis_hareket_id,
        prm_t_siparis_munferit_dizi(i).hareket_id, prm_t_siparis_munferit_dizi(i).tahsilat_id,
        l_munferit_toplam, pck_def.c_tbt_munferit_siparisten_revize, Null,
        Sysdate, pck_def.c_inst_sistem_kullanici_id, systimestamp, Null);
    End Loop;

    Return pck_def.c_success;
  Exception
    When e_istek_bos Then
      pck_general.set_out_variables(prm_cevap_kodu, pck_def.c_rc_istek_bos, prm_error_type, 'APP', prm_error_num, -20304, prm_error_str, 'munferitle_siparis_yap: Istek bos');
      Return pck_def.c_fail;
    When e_munferit_bos Then
      pck_general.set_out_variables(prm_cevap_kodu, pck_def.c_rc_munferit_bos, prm_error_type, 'APP', prm_error_num, -20304, prm_error_str, 'munferitle_siparis_yap: Munferit listesi bos');
      Return pck_def.c_fail;
    When e_hareket_bulunamadi Then
      pck_general.set_out_variables(prm_cevap_kodu, pck_def.c_rc_hareket_bulunamadi, prm_error_type, 'APP', prm_error_num, -20304, prm_error_str, 'munferitle_siparis_yap: Hareket bulunamadi');
      Return pck_def.c_fail;
    When e_tutar_asimi Then
      pck_general.set_out_variables(prm_cevap_kodu, pck_def.c_rc_tutar_asimi, prm_error_type, 'APP', prm_error_num, -20304, prm_error_str, 'munferitle_siparis_yap: Munferit toplam siparis tutarindan buyuk');
      Return pck_def.c_fail;
    When e_siparis_basarisiz Then Return pck_def.c_fail;
    When Others Then
      pck_general.set_out_variables(prm_cevap_kodu, pck_def.c_rc_sql_error, prm_error_type, 'ORA', prm_error_num, Sqlcode, prm_error_str, 'munferitle_siparis_yap:' || Sqlerrm);
      Return pck_def.c_fail;
  End munferitle_siparis_yap;


  -- =========================================================================
  -- teslimat_hareket_at: Teslimat hareket kaydi olusturur
  -- =========================================================================
  Procedure teslimat_hareket_at(prm_hareket_id In siparis_hareket.id%Type,
                                prm_islem_id   siparis_sahibi_hareket.islem_id%Type,
                                prm_tb_id      siparis_hareket_tahsilat_bilgi.alacak_id%Type) Is
    l_siparis_sahibi_id siparis_sahibi.id%Type;
    l_sirket_id         siparis_sahibi.sirket_id%Type;
    l_siparis_hareket_id siparis_hareket.id%Type;
    l_kosul_id          siparis_sahibi_hareket.siparis_kosul_id%Type;
    l_ssh_id            siparis_sahibi_hareket.id%Type;
    l_siparis_sahibi_har siparis_sahibi_hareket%Rowtype;

    Cursor c_tahsilat_bilgi Is
      Select shtb.siparis_hareket_id, shtb.alacak_id, shtb.siparis_tahsilat_bilgi_tipi_id
        From siparis_hareket_tahsilat_bilgi shtb
       Where shtb.siparis_hareket_id = prm_hareket_id
         And (prm_tb_id Is Null Or shtb.alacak_id = prm_tb_id)
       Group By shtb.siparis_hareket_id, shtb.alacak_id, shtb.siparis_tahsilat_bilgi_tipi_id;

  Begin
    For r In c_tahsilat_bilgi Loop
      Begin
        Select ss.id, ss.sirket_id, sh.id, ssh.siparis_kosul_id, ssh.id
          Into l_siparis_sahibi_id, l_sirket_id, l_siparis_hareket_id, l_kosul_id, l_ssh_id
          From siparis_hareket sh, siparis s, siparis_sahibi_hareket_detay sshd, siparis_sahibi_hareket ssh
         Where sh.id = r.siparis_hareket_id And sh.siparis_id = s.id
           And sh.id = sshd.siparis_hareket_id And sshd.siparis_sahibi_hareket_id = ssh.id
           And ((r.siparis_tahsilat_bilgi_tipi_id = pck_def.c_tbt_munferit_siparisten_revize
                 And ssh.islem_id = pck_def.c_islem_munferit_avans_dgrln And ssh.ref_id Is Null)
                Or (r.siparis_tahsilat_bilgi_tipi_id <> pck_def.c_tbt_munferit_siparisten_revize
                    And ssh.islem_id = pck_def.c_islem_ilk_bilgilendirme))
           And rownum < 2;

        l_siparis_sahibi_har := Null;
        Select seq_siparis_sahibi_hareket.nextval Into l_siparis_sahibi_har.id From dual;
        l_siparis_sahibi_har.siparis_sahibi_id := l_siparis_sahibi_id;
        l_siparis_sahibi_har.islem_tarihi   := trunc(Sysdate);

        If r.siparis_tahsilat_bilgi_tipi_id = pck_def.c_tbt_munferit_siparisten_revize Then
          l_siparis_sahibi_har.islem_id := pck_def.c_islem_munferit_avans_dgrln;
        Else
          l_siparis_sahibi_har.islem_id := prm_islem_id;
        End If;

        l_siparis_sahibi_har.siparis_sahibi_hareket_durum_id := pck_def.c_sshd_degerlendirilecek;
        l_siparis_sahibi_har.siparis_toplam_tutari   := 0;
        l_siparis_sahibi_har.belge_mahsup_tutari     := 0;
        l_siparis_sahibi_har.avans_mahsup_tutari     := 0;
        l_siparis_sahibi_har.nakit_siparis_tutari     := 0;
        l_siparis_sahibi_har.kalan_alacak_tutari     := 0;
        l_siparis_sahibi_har.transfere_aktarilan_tutar := 0;
        l_siparis_sahibi_har.kesinti_yapilan_tutar     := 0;
        l_siparis_sahibi_har.siparis_kosul_id          := l_kosul_id;
        l_siparis_sahibi_har.ref_id                    := l_ssh_id;
        l_siparis_sahibi_har.sirket_id                 := l_sirket_id;
        l_siparis_sahibi_har.tanitim_zamani             := Sysdate;
        l_siparis_sahibi_har.tanitan_kullanici_id       := pck_def.c_inst_sistem_kullanici_id;
        l_siparis_sahibi_har.guncelleme_zamani          := systimestamp;
        l_siparis_sahibi_har.guncelleyen_kullanici_id   := Null;
        l_siparis_sahibi_har.kural_siparis_tipi_id      := Null;
        l_siparis_sahibi_har.grup_id                    := Null;

        if prm_islem_id = pck_def.C_AVANS_SIPARIS_TLB then
           l_siparis_sahibi_har.nakit_talep_tarihi := trunc(sysdate);
        end if;

        Insert Into siparis_sahibi_hareket Values l_siparis_sahibi_har;

        Insert Into siparis_sahibi_hareket_detay Values (
          seq_siparis_sahibi_hrk_detay.nextval, l_siparis_sahibi_har.id, l_siparis_hareket_id,
          Sysdate, pck_def.c_inst_sistem_kullanici_id, systimestamp, Null);

      End Loop;
    End;
  Exception
    When Others Then Null;
  End;

  -- =========================================================================
  -- teslimat_hareket_isle: Periyodik teslimat hareket islemleri
  -- =========================================================================
  Procedure teslimat_hareket_isle(prm_bas_zamani siparis_sahibi_hareket.islem_tarihi%Type,
                                  prm_bit_zamani siparis_sahibi_hareket.islem_tarihi%Type,
                                  prm_sirket_id  siparis_sahibi.sirket_id%Type) Is
    l_count Number;
    l_sonuc Number;
    l_cevap_kodu Varchar2(20);
    l_error_type database_exception.error_type%Type;
    l_error_num  database_exception.error_code%Type;
    l_error_str  database_exception.error_message%Type;

    Cursor c_bekleyen_hareketler Is
      Select ssh.id
        From siparis_sahibi_hareket ssh, siparis_sahibi ss
       Where ssh.siparis_sahibi_id = ss.id
         And ss.sirket_id = prm_sirket_id
         And ssh.siparis_sahibi_hareket_durum_id = pck_def.c_sshd_degerlendirilecek
         And ssh.islem_tarihi Between prm_bas_zamani And prm_bit_zamani
       Order By ssh.id;

  Begin
    For r In c_bekleyen_hareketler Loop
      Begin
        l_sonuc := stok_faaliyet(r.id, l_cevap_kodu, l_error_type, l_error_num, l_error_str);
        If Mod(c_bekleyen_hareketler%Rowcount, 50) = 0 Then Commit; End If;
      Exception When Others Then
        Null;
      End;
    End Loop;
    Commit;
  End;

  -- =========================================================================
  -- rutin_bilgilendirme: Rutin bilgilendirme islemi
  -- =========================================================================
  Function rutin_bilgilendirme(prm_baslangic_tarihi Date,
                               prm_bitis_tarihi     Date,
                               prm_sirket_id        sirket.id%Type,
                               prm_cevap_kodu       In Out Varchar2,
                               prm_error_str        In Out Nocopy database_exception.error_message%Type) Return Number Is
    l_count        Number;
    l_toplam_tutar Number := 0;
    l_bildirim_id  bildirim.id%Type;

    Cursor c_siparis_sahipleri Is
      Select ss.id, ss.hesap_id, ss.musteri_id, ss.sirket_id,
             nvl(ss.kalan_alacak_tutari, 0) kalan_alacak,
             h.hesap_no, m.musteri_no, m.ad || ' ' || m.soyad musteri_adi
        From siparis_sahibi ss, hesap h, musteri m
       Where ss.sirket_id = prm_sirket_id
         And ss.hesap_id = h.id And ss.musteri_id = m.id
         And ss.kalan_alacak_tutari > 0
         And Exists (Select 1 From siparis_sahibi_hareket ssh
                      Where ssh.siparis_sahibi_id = ss.id
                        And ssh.islem_tarihi Between prm_baslangic_tarihi And prm_bitis_tarihi
                        And ssh.siparis_sahibi_hareket_durum_id = pck_def.c_sshd_degerlendirildi)
       Order By ss.id;

  Begin
    For r In c_siparis_sahipleri Loop
      Begin
        Select seq_bildirim.nextval Into l_bildirim_id From dual;

        Insert Into bildirim Values (l_bildirim_id, r.hesap_id, r.musteri_id,
           pck_def.c_bildirim_tipi_siparis, pck_def.c_bildirim_aktif,
           'Siparis islem bilgilendirmesi. HesapNo:' || r.hesap_no ||
           ' MusteriNo:' || r.musteri_no || ' Tutar:' || r.kalan_alacak,
           Sysdate, pck_def.c_inst_sistem_kullanici_id, systimestamp, Null);

        l_toplam_tutar := l_toplam_tutar + r.kalan_alacak;

        If Mod(c_siparis_sahipleri%Rowcount, 100) = 0 Then Commit; End If;
      Exception When Others Then Null;
      End;
    End Loop;

    Commit;
    Return pck_def.c_success;
  Exception
    When Others Then
      prm_cevap_kodu := pck_def.c_rc_sql_error;
      prm_error_str := 'rutin_bilgilendirme:' || prm_sirket_id || ', ' || Sqlerrm;
      Return pck_def.c_fail;
  End;

  -- =========================================================================
  -- dagitim_detay_al: Dagitim detay bilgilerini alir
  -- =========================================================================
  Procedure dagitim_detay_al(prm_sh_id            In siparis_hareket.id%Type,
                             prm_siparis_tutari    In Out siparis_sahibi_finans.siparis_toplam_tutari%Type,
                             prm_belge_mah_tutar   In Out siparis_sahibi_finans.belge_mahsup_tutari%Type,
                             prm_avans_mah_tutar   In Out siparis_sahibi_finans.avans_mahsup_tutari%Type,
                             prm_nakit_tutar       In Out siparis_sahibi_finans.nakit_siparis_tutari%Type,
                             prm_transfer_tutar    In Out siparis_sahibi_finans.transfere_aktarilan_tutar%type,
                             prm_kesinti_tutar     In Out siparis_sahibi_finans.kesinti_yapilan_tutar%type,
                             prm_kalan_alacak      In Out siparis_sahibi_finans.kalan_alacak_tutari%Type,
                             prm_nakit_talep_tarihi In Out siparis_sahibi_finans.nakit_talep_tarihi%Type) is

    e_islem_tamamlandi Exception;

    l_mun_top         siparis_hareket.islem_tutari%Type;
    l_mun_fazla_odeme siparis_hareket.fazla_odeme_tutari%Type;
    l_belge_mahsup    siparis_hareket.islem_tutari%Type;
    l_avans_mah       tahakkuk_bildirim.islem_tutari%Type := 0;
    l_avans_nak       tahakkuk_bildirim.islem_tutari%Type := 0;
    l_avans_kalan     tahakkuk_bildirim.islem_tutari%Type := 0;
    l_transfer_tutar  tahakkuk_bildirim.islem_tutari%Type := 0;
    l_kesinti_tutar   tahakkuk_bildirim.islem_tutari%Type := 0;
    l_nakit_talep_tarihi tb_dagitim_detay.tanitim_zamani%type;

    Type munfrecord Is Record(
      siparis_hareket_id siparis_hareket.id%Type,
      munf_toplam        siparis_hareket_tahsilat_bilgi.siparis_tutari%Type,
      belge_mah_tutar    siparis_hareket_tahsilat_bilgi.siparis_tutari%Type,
      avans_mah_tutar    siparis_hareket_tahsilat_bilgi.siparis_tutari%Type,
      avans_nakden       siparis_hareket_tahsilat_bilgi.siparis_tutari%Type,
      kalan_alacak       siparis_hareket_tahsilat_bilgi.siparis_tutari%Type,
      transfer_tutar     siparis_hareket_tahsilat_bilgi.siparis_tutari%Type,
      kesinti_tutar      siparis_hareket_tahsilat_bilgi.siparis_tutari%Type,
      kalan_munf_toplam  siparis_hareket_tahsilat_bilgi.siparis_tutari%Type);

    Type munferitcollection Is Table Of munfrecord Index By Binary_Integer;
    l_muncolllist munferitcollection;

    lc_siparis_tutari    siparis_sahibi_finans.siparis_toplam_tutari%Type;
    lc_belge_mah_tutar   siparis_sahibi_finans.belge_mahsup_tutari%Type;
    lc_avans_mah_tutar   siparis_sahibi_finans.avans_mahsup_tutari%Type;
    lc_nakit_tutar       siparis_sahibi_finans.nakit_siparis_tutari%Type;
    lc_transfer_tutar    siparis_sahibi_finans.transfere_aktarilan_tutar%Type;
    lc_kesinti_tutar     siparis_sahibi_finans.kesinti_yapilan_tutar%Type;
    lc_kalan_alacak      siparis_sahibi_finans.kalan_alacak_tutari%Type;

    Cursor c_dagitim Is
      Select shtb.siparis_hareket_id, shtb.alacak_id
        From siparis_hareket_tahsilat_bilgi shtb
       Where shtb.siparis_hareket_id = prm_sh_id
         And shtb.siparis_tahsilat_bilgi_tipi_id = pck_def.c_tbt_munferit_siparisten_revize
       Group By shtb.siparis_hareket_id, shtb.alacak_id;

  Begin
    prm_siparis_tutari   := 0;
    prm_belge_mah_tutar  := 0;
    prm_avans_mah_tutar  := 0;
    prm_nakit_tutar      := 0;
    prm_transfer_tutar   := 0;
    prm_kesinti_tutar    := 0;
    prm_kalan_alacak     := 0;

    For r_dagitim In c_dagitim Loop
      lc_siparis_tutari  := 0;
      lc_belge_mah_tutar := 0;
      lc_avans_mah_tutar := 0;
      lc_nakit_tutar     := 0;
      lc_transfer_tutar  := 0;
      lc_kesinti_tutar   := 0;
      lc_kalan_alacak    := 0;

      Select sh.islem_tutari, nvl(sh.fazla_odeme_tutari, 0) Into l_mun_top, l_mun_fazla_odeme
        From siparis_hareket sh Where sh.id = r_dagitim.siparis_hareket_id;

      l_belge_mahsup := l_mun_top - l_mun_fazla_odeme;

      If l_mun_fazla_odeme > 0 And r_dagitim.alacak_id Is Not Null Then
        Select nvl(Sum(decode(tbd.islem_id, 11, tbd.islem_tutari, 404, tbd.islem_tutari, 0)), 0),
               nvl(Sum(decode(tbd.islem_id, 430, tbd.islem_tutari, 433, tbd.islem_tutari, 0)), 0),
               nvl(Sum(decode(tbd.islem_id, 429, tbd.islem_tutari, 0)), 0),
               nvl(Sum(decode(tbd.islem_id, 428, tbd.islem_tutari, 0)), 0),
               max(decode(tbd.islem_id, 430, tbd.tanitim_zamani, 433, tbd.tanitim_zamani, null))
          Into l_avans_mah, l_avans_nak, l_transfer_tutar, l_kesinti_tutar, l_nakit_talep_tarihi
          From tb_dagitim_detay tbd
         Where tbd.ana_kaynak_id = r_dagitim.alacak_id And tbd.islem_durumu = 1;

        l_avans_kalan := l_mun_fazla_odeme - l_avans_mah - l_avans_nak - l_transfer_tutar;
      End If;

      Select t.siparis_hareket_id, nvl(Sum(t.siparis_tutari), 0),
             nvl(Sum(decode(t.siparis_tahsilat_bilgi_tipi_id, 1, t.siparis_tutari, 0)), 0),
             nvl(Sum(decode(t.siparis_tahsilat_bilgi_tipi_id, 2, t.siparis_tutari, 0)), 0),
             nvl(Sum(decode(t.siparis_tahsilat_bilgi_tipi_id, 3, t.siparis_tutari, 0)), 0),
             0, 0, 0, 0
        Into l_muncolllist(c_dagitim%Rowcount).siparis_hareket_id,
             l_muncolllist(c_dagitim%Rowcount).munf_toplam,
             l_muncolllist(c_dagitim%Rowcount).belge_mah_tutar,
             l_muncolllist(c_dagitim%Rowcount).avans_mah_tutar,
             l_muncolllist(c_dagitim%Rowcount).avans_nakden,
             l_muncolllist(c_dagitim%Rowcount).kalan_alacak,
             l_muncolllist(c_dagitim%Rowcount).transfer_tutar,
             l_muncolllist(c_dagitim%Rowcount).kesinti_tutar,
             l_muncolllist(c_dagitim%Rowcount).kalan_munf_toplam
        From siparis_hareket_tahsilat_bilgi t
       Where t.siparis_hareket_id = prm_sh_id
         And t.alacak_id = r_dagitim.alacak_id
       Group By t.siparis_hareket_id;

      lc_siparis_tutari  := l_mun_top;
      lc_belge_mah_tutar := l_belge_mahsup;
      lc_avans_mah_tutar := l_avans_mah;
      lc_nakit_tutar     := l_avans_nak;
      lc_transfer_tutar  := l_transfer_tutar;
      lc_kesinti_tutar   := l_kesinti_tutar;
      lc_kalan_alacak    := l_avans_kalan;

      prm_siparis_tutari   := prm_siparis_tutari + lc_siparis_tutari;
      prm_belge_mah_tutar  := prm_belge_mah_tutar + lc_belge_mah_tutar;
      prm_avans_mah_tutar  := prm_avans_mah_tutar + lc_avans_mah_tutar;
      prm_nakit_tutar      := prm_nakit_tutar + lc_nakit_tutar;
      prm_transfer_tutar   := prm_transfer_tutar + lc_transfer_tutar;
      prm_kesinti_tutar    := prm_kesinti_tutar + lc_kesinti_tutar;
      prm_kalan_alacak     := prm_kalan_alacak + lc_kalan_alacak;
      prm_nakit_talep_tarihi := l_nakit_talep_tarihi;

    End Loop;

  End;

  -- =========================================================================
  -- dagitim_raporu_olustur: Dagitim raporu olusturur
  -- =========================================================================
  Function dagitim_raporu_olustur(prm_sirket_id  siparis_sahibi.sirket_id%Type,
                                  prm_cevap_kodu In Out Varchar2,
                                  prm_error_str  In Out Nocopy database_exception.error_message%Type) Return Number Is
    l_siparis_toplam      Number := 0;
    l_belge_mahsup        Number := 0;
    l_avans_mah           Number := 0;
    l_avans_nak           Number := 0;
    l_avans_kalan         Number := 0;
    l_transfer_tutar      Number := 0;
    l_kesinti_tutar       Number := 0;
    l_son_har_zamani      Date;
    l_adet                Number;
    l_dagitim_rapor_id    siparis_dagitim_rapor.id%Type;
    l_siparis_shb_list    t_number_dizi;
    l_hesap               hesap%Rowtype;

    Cursor c_stok_analiz Is
      Select sa.id as stok_analiz_id, sa.karar_no, sa.karar_tarihi, sa.teblig_tarihi,
             sa.tip, sa.kurum_id, sa.talep_kodu, sa.transfer_aktarim_durumu
        From stok_analiz sa
       Where sa.sirket_id = prm_sirket_id
         And sa.durum_id = pck_def.c_stok_analiz_durum_tamamlandi
       Order By sa.id;

    Cursor c_siparis_sah(p_stok_analiz_id stok_analiz.id%Type) Is
      Select ss.id as siparis_sahibi_id, h.hesap_no, m.musteri_no, m.tc_kimlik_no, m.vergi_no,
             m.ad || ' ' || m.soyad musteri_adi, h.hesap_durum as hesap_durumu_id,
             m.tuzel_gercek_id, ss.tanitim_zamani, h.id as hesap_id,
             ss.nakit_talep_tarihi, ss.munferit_sms_gonderim_tarihi
        From siparis_sahibi ss, hesap h, musteri m, stok_analiz_detay sad
       Where ss.stok_analiz_detay_id = sad.id And sad.stok_analiz_id = p_stok_analiz_id
         And ss.hesap_id = h.id And ss.musteri_id = m.id
         And ss.sirket_id = prm_sirket_id
       Order By ss.id;

  Begin
    For r_stok_analiz In c_stok_analiz Loop
      For r_siparis_sah In c_siparis_sah(r_stok_analiz.stok_analiz_id) Loop
        Begin
          l_siparis_toplam := 0; l_belge_mahsup := 0; l_avans_mah := 0;
          l_avans_nak := 0; l_avans_kalan := 0; l_transfer_tutar := 0; l_kesinti_tutar := 0;

          -- Toplam siparis bilgilerini al
          Select nvl(Sum(s.siparis_tutari), 0), Max(sh.guncelleme_zamani)
            Into l_siparis_toplam, l_son_har_zamani
            From siparis s, siparis_hareket sh, siparis_durum sd
           Where s.siparis_sahibi_id = r_siparis_sah.siparis_sahibi_id
             And s.siparis_durum_id = sd.id And sd.islem_durumu = pck_def.c_siparis_durum_islem_basarili
             And s.id = sh.siparis_id And sh.siparis_hareket_durum_id = pck_def.c_shd_degerlendirildi;

          Select Count(0) Into l_adet From siparis s
           Where s.siparis_sahibi_id = r_siparis_sah.siparis_sahibi_id
             And s.siparis_durum_id In (pck_def.c_id_basarili, pck_def.c_id_onaylandi);

          If l_adet = 0 Then continue; End If;

          -- Eski raporu sil
          Delete From siparis_dagitim_rapor_mahsup t
           Where t.siparis_dagitim_rapor_id In
                 (Select id From siparis_dagitim_rapor sdr
                   Where sdr.stok_analiz_id = r_stok_analiz.stok_analiz_id
                     And sdr.musteri_no = r_siparis_sah.musteri_no
                     And sdr.hesap_no = r_siparis_sah.hesap_no
                     And sdr.rapor_tarihi = trunc(Sysdate));

          Delete From siparis_dagitim_rapor sdr
           Where sdr.stok_analiz_id = r_stok_analiz.stok_analiz_id
             And sdr.musteri_no = r_siparis_sah.musteri_no
             And sdr.hesap_no = r_siparis_sah.hesap_no
             And sdr.rapor_tarihi = trunc(Sysdate);

          Select seq_siparis_dagitim_rapor.nextval Into l_dagitim_rapor_id From dual;

          select * into l_hesap from HESAP h where h.id = r_siparis_sah.HESAP_ID;

          Insert Into siparis_dagitim_rapor Values (
             l_dagitim_rapor_id, r_stok_analiz.stok_analiz_id, r_stok_analiz.karar_no,
             r_stok_analiz.karar_tarihi, r_stok_analiz.teblig_tarihi, r_stok_analiz.tip,
             r_stok_analiz.kurum_id, r_stok_analiz.talep_kodu, r_siparis_sah.tc_kimlik_no,
             r_siparis_sah.vergi_no, r_siparis_sah.musteri_adi, r_siparis_sah.musteri_no,
             r_siparis_sah.hesap_no, r_siparis_sah.tuzel_gercek_id, r_siparis_sah.hesap_durumu_id,
             l_siparis_toplam, (l_belge_mahsup + l_avans_mah), l_avans_nak, l_avans_kalan,
             trunc(Sysdate), l_son_har_zamani, trunc(r_siparis_sah.tanitim_zamani),
             Sysdate, 1, systimestamp, Null, l_transfer_tutar, l_kesinti_tutar,
             r_siparis_sah.nakit_talep_tarihi,
             (SELECT CASE
                  WHEN r_stok_analiz.TEBLIG_TARIHI is null or l_hesap.ACILIS_TARIHI is null THEN NULL
                  WHEN r_stok_analiz.TEBLIG_TARIHI is not null and l_hesap.ACILIS_TARIHI is not null
                       and (l_hesap.ACILIS_TARIHI < r_stok_analiz.TEBLIG_TARIHI
                            AND (l_hesap.IPTAL_TARIHI is null or l_hesap.IPTAL_TARIHI > r_stok_analiz.TEBLIG_TARIHI))
                  THEN 'AKTIF' ELSE 'PASIF'
              END AS TEBLIG_TARIHINDE_ABONE_DURUMU FROM dual),
             r_siparis_sah.MUNFERIT_SMS_GONDERIM_TARIHI);

          -- Belge Mahsup
          If l_belge_mahsup > 0 Then
            Insert Into siparis_dagitim_rapor_mahsup
              Select seq_siparis_dagitim_rapor_hrk.nextval, l_dagitim_rapor_id,
                     r_siparis_sah.hesap_no, l_belge_mahsup, 1, sysdate From dual;
          End If;

          Insert Into siparis_dagitim_rapor_mahsup
            Select seq_siparis_dagitim_rapor_hrk.nextval, l_dagitim_rapor_id, hesap_no, tutar, tip, sysdate
              From (Select h.hesap_no, Sum(x.islem_tutari) tutar,
                           decode(x.islem_id, 430, 3, 433, 3, 429, 4, 2) tip
                      From tb_dagitim_detay x, tahakkuk_bildirim tb, siparis_hareket sh, hesap h
                     Where x.ana_kaynak_id In
                           (Select ss.alacak_id From siparis_sahibi_hareket t,
                                   siparis_sahibi_hareket_detay sshd, siparis_hareket sh2, siparis ss
                             Where t.siparis_sahibi_id In (Select deger From Table(l_siparis_shb_list))
                               And t.islem_id = pck_def.c_islem_ilk_bilgilendirme
                               And t.id = sshd.siparis_sahibi_hareket_id And sshd.siparis_hareket_id = sh2.id
                               And sh2.siparis_id = ss.id And ss.alacak_id Is Not Null)
                       And x.islem_durumu = 1
                       And x.islem_id In (404, 11, 111, 430, 433, 429)
                       And x.kaynak2_id = tb.id(+) And x.sh_sonuc_id = sh.id(+)
                       And nvl(tb.hesap_id, sh.hesap_id) = h.id
                     Group By h.hesap_no, h.sanal_mi, h.id,
                              decode(x.islem_id, 430, 3, 433, 3, 429, 4, 2));

          If Mod(c_siparis_sah%Rowcount, 100) = 0 Then Commit; End If;

        End Loop;

        if r_stok_analiz.transfer_aktarim_durumu = 2 then
          update stok_analiz set transfer_aktarim_durumu = 3
           where id = r_stok_analiz.stok_analiz_id;
        end if;

        Commit;

      End Loop;
    End Loop;

    Return pck_def.c_success;
  Exception
    When Others Then
      prm_cevap_kodu := pck_def.c_rc_sql_error;
      prm_error_str  := 'dagitim_raporu_olustur:' || prm_sirket_id || ', ' || Sqlerrm;
      Return pck_def.c_fail;
  End;

  -- =========================================================================
  -- Yardimci private prosedurler
  -- =========================================================================

  -- siparis_guncelle: Siparis durumunu gunceller
  Procedure siparis_guncelle(prm_t_siparis_cevap_type In t_siparis_cevap_type,
                             prm_siparis_durum_id     In siparis.siparis_durum_id%Type,
                             prm_hareket_durum_id     In siparis_hareket.siparis_hareket_durum_id%Type,
                             prm_islem_id             In siparis_hareket.islem_id%Type,
                             prm_kullanici_id         In siparis.tanitan_kullanici_id%Type,
                             prm_cevap_kodu           In Out Varchar2,
                             prm_error_str            In Out Varchar2) Is
  Begin
    Update siparis Set siparis_durum_id = prm_siparis_durum_id,
           cevap_kodu = prm_cevap_kodu, cevap_mesaji = substr(prm_error_str, 1, 500),
           istek_deneme_sayisi = istek_deneme_sayisi + 1, guncelleme_zamani = systimestamp
     Where id = prm_t_siparis_cevap_type.siparis_id;
  End;

  -- siparis_sahibi_finans_guncelle: Finans kaydi olusturur/gunceller
  Procedure siparis_sahibi_finans_guncelle(prm_siparis_sahibi_id    In siparis_sahibi.id%Type,
                                           prm_finans_tipi          In Number,
                                           prm_siparis_toplam       In Number,
                                           prm_belge_mahsup         In Number,
                                           prm_avans_mahsup         In Number,
                                           prm_nakit                In Number,
                                           prm_transfer             In Number,
                                           prm_kesinti              In Number,
                                           prm_kalan_alacak         In Number,
                                           prm_nakit_talep_tarihi   In Date) Is
    l_count Number;
  Begin
    Select Count(0) Into l_count From siparis_sahibi_finans Where siparis_sahibi_id = prm_siparis_sahibi_id And finans_tipi = prm_finans_tipi;
    If l_count = 0 Then
      Insert Into siparis_sahibi_finans Values (
        seq_siparis_sahibi_finans.nextval, prm_siparis_sahibi_id, prm_finans_tipi,
        nvl(prm_siparis_toplam, 0), nvl(prm_belge_mahsup, 0), nvl(prm_avans_mahsup, 0),
        nvl(prm_nakit, 0), nvl(prm_kalan_alacak, 0), nvl(prm_transfer, 0), nvl(prm_kesinti, 0),
        prm_nakit_talep_tarihi, Sysdate, pck_def.c_inst_sistem_kullanici_id, systimestamp, Null);
    Else
      Update siparis_sahibi_finans Set
             siparis_toplam_tutari = nvl(prm_siparis_toplam, siparis_toplam_tutari),
             belge_mahsup_tutari = nvl(prm_belge_mahsup, belge_mahsup_tutari),
             avans_mahsup_tutari = nvl(prm_avans_mahsup, avans_mahsup_tutari),
             nakit_siparis_tutari = nvl(prm_nakit, nakit_siparis_tutari),
             kalan_alacak_tutari = nvl(prm_kalan_alacak, kalan_alacak_tutari),
             transfere_aktarilan_tutar = nvl(prm_transfer, transfere_aktarilan_tutar),
             kesinti_yapilan_tutar = nvl(prm_kesinti, kesinti_yapilan_tutar),
             nakit_talep_tarihi = prm_nakit_talep_tarihi,
             guncelleme_zamani = systimestamp, guncelleyen_kullanici_id = pck_def.c_inst_sistem_kullanici_id
       Where siparis_sahibi_id = prm_siparis_sahibi_id And finans_tipi = prm_finans_tipi;
    End If;
  End;

  -- opf_siparis_isle: OPF (ortak payli belge) icin siparis isleme
  Function opf_siparis_isle(prm_ana_sh_row         In siparis_hareket%Rowtype,
                            prm_ana_belge           In belge%Rowtype,
                            prm_sh_id               In siparis_hareket.id%Type,
                            prm_cevap_kodu          In Out Varchar2,
                            prm_error_type          In Out Nocopy database_exception.error_type%Type,
                            prm_error_num           In Out Nocopy database_exception.error_code%Type,
                            prm_error_str           In Out Nocopy database_exception.error_message%Type) Return Number Is

    l_tedarikci_belge       belge%Rowtype;
    l_pay_siparis_hareket   siparis_hareket%Rowtype;
    l_sh_id                 siparis_hareket.id%Type;

    Cursor c_tedarikci_belgeler Is
      Select b.* From belge b
       Where b.ana_belge_id = prm_ana_belge.id
         And b.belge_durum In (pck_def.c_unpaid, pck_def.c_partial)
       Order By b.id;

  Begin
    For r In c_tedarikci_belgeler Loop
      l_tedarikci_belge := r;

      Select seq_siparis_hareket.nextval Into l_sh_id From dual;

      Insert Into siparis_hareket Values (
         l_sh_id, prm_ana_sh_row.islem_tarihi, prm_ana_sh_row.kurum_id,
         pck_general.get_stan, prm_ana_sh_row.siparis_id, pck_def.c_shd_degerlendirildi,
         pck_def.c_id_basarili, prm_ana_sh_row.islem_id, prm_ana_sh_row.islem_tutari,
         prm_sh_id, Null, Null, Null, prm_ana_sh_row.business_id,
         prm_ana_sh_row.conversation_id, Sysdate, prm_ana_sh_row.tanitim_kullanici,
         systimestamp, Null, 0);

      Update belge b Set b.siparis_duzeltme_tutar = b.siparis_duzeltme_tutar - prm_ana_sh_row.islem_tutari,
             b.kalan_tutar = b.kalan_tutar - prm_ana_sh_row.islem_tutari,
             b.guncelleme_zaman = systimestamp,
             b.guncelleyen_kullanici = prm_ana_sh_row.tanitim_kullanici
       Where b.id = l_tedarikci_belge.id;

    End Loop;

    Return pck_def.c_success;
  Exception
    When Others Then Return pck_def.c_fail;
  End;

End pck_siparis;
/

