# pck_siparis Analizi

## Package Amaci
pck_siparis, Order Migration sisteminin en kritik ve en kapsamli paketlerinden biridir. Belge (fatura) uzerinden siparis olusturma, siparis kalemlerinin kontrolu, siparis hareketlerinin yonetimi, teslimat sonuc bildirimi, siparis iptali, toplu siparis isleme, stok hazirlama ve dagitim faaliyetleri gibi siparis yasam dongusunun tamamini kapsar.

## Surum Gecmisi
- Ana surum: 3.34.0 -> 4.40.0
- Alt moduller: STOK_TAKIP, KAMPANYA, taksitli_satis, SATIS, PORTAL_FAZ2, DIJITAL_EKRAN3, KESINTI

## Sabitler
- `c_tolerans_toplam_limit`: 0.02 (tutar uyumsuzlugu toleransi)
- `c_kalem_kdv`: 3 (KDV urun grubu)
- `c_kalem_otv`: 4 (OTV urun grubu)
- `c_kalem_mahsup`: 10 (Mahsup urun grubu)
- `c_kalem_temlik`: 26 (Temlik urun grubu)

## Custom Type'lar
### Record (Java DTO karsiliklari)
- `toplusipcolrecord` -> TopluSiparisKayitRecord (toplama icin gecici)
- `toplusipcolcollection` -> List<TopluSiparisKayitRecord>
- `r_sd_knt_donus_rec` -> SiparisDetayKontrolRecord (kampanya kodu donus icin)
- `t_sd_knt_donus_col` -> List<SiparisDetayKontrolRecord>
- `munfrecord` -> MunferitRecord (dagitim detay icin)
- `munferitcollection` -> List<MunferitRecord>

### Oracle Object Type'lar (Root data_types/ altinda)
1. **T_SIPARIS_ISTEK_TYPE** -> SiparisIstekDto
   - islem_tarihi, kurum_id, stan, sirket_id, orig_*, kullanici_id, islem_sira_no
   - siparis_detay_bilgi (T_SIPARIS_ISTEK_DETAY_TYPE)
   - ref_islem_tarihi, ref_stan, ref_islem_sira_no, stok_analiz_id
   - toplam_kalem_sayisi, siparis_toplam_tutar, hesap_no, taksit_no, belge_no, donem_kodu
   - uygulama, islem_kaynagi, durum_id, islem_kodu, business_id, conversation_id
   - ws_detay_list (T_SIPARIS_ISTEK_FDY_TYPE_DIZI)
   - kalem_detay_list (T_SIPARIS_KALEM_DETAYI_COLLECTION)
   - tahsilat_kurum_kodu, taksitli_mi

2. **T_SIPARIS_ISTEK_DETAY_TYPE** -> SiparisIstekDetayDto
   - tdk_mi, vergi_haric, siparis_sebebi_id, aciklama, siparis_referans_no, teslimat_talep_id
   - siparis_belge_no, vergi_dairesi, vergi_no, sebep_bildirim_no, sebep_kullanici_sicil_no
   - sebep_bayi_kodu, sebep_cagri_merkezi, erp_gonderilecek_mi, erp_iban, erp_alici_ad_soyad
   - erp_alici_telefon, erp_aciklama, cid, islem_tarihi, temsilci_adi_soyadi, temsilci_sicil_no
   - portal_kullanici_kodu, portal_kullanici_ad_soyad

3. **T_SIPARIS_ISTEK_FDY_TYPE** -> SiparisIstekFdyDto (WS detay listesi)
   - urun_seri_no, kalem_tipi_id, tedarikci_id, siparis_tutari, kdv_orani, otv_orani
   - paket_tipi_id, bayi_kodu, kampanya_kodu, taksitliSatisID, taksitNo

4. **T_SIPARIS_KALEM_DETAYI** -> SiparisKalemDetayiDto
   - siparis_detay_id, kalem_tipi_id, urun_grubu_id, tedarikci_id, paket_tipi_id
   - orjinal_tutar, siparis_duzeltme_tutari, revize_tutari, kdv_orani, kdv_tutari
   - otv_orani, otv_tutari, duzeltme_yapilabilir_mi, carpan
   - kdv_siparis_detay_id, otv_siparis_detay_id, urun_seri_no, mahsup_siparis_detay_id
   - taksitliSatisID, taksitNo

5. **T_SIPARIS_CEVAP_TYPE** -> SiparisCevapDto
   - stok_analiz_id, stok_analiz_detay_id, hesap_id, musteri_id, siparis_sahibi_id
   - siparis_id, siparis_hareket_id, siparis_detay_id, islem_id, hareket_id, tahsilat_id
   - siparis_tutari, alacak_tutari, islenen_kayit_sayisi, islenen_kayit_tutari
   - kalan_kayit_sayisi, onay_bekleyen_adeti, dekont_grup_no, hesap_no

6. **T_SIPARIS_MUNFERIT_TYPE** -> SiparisMunferitDto
   - hareket_id, tahsilat_id, t_munferit_list (T_SIPARIS_KALEM_DETAYI_COLLECTION)

7. **T_SIPARIS_ISTEK_TYPE_DIZI** -> List<SiparisIstekDto>
8. **T_SIPARIS_ISTEK_FDY_TYPE_DIZI** -> List<SiparisIstekFdyDto>
9. **T_SIPARIS_KALEM_DETAYI_COLLECTION** -> List<SiparisKalemDetayiDto>
10. **T_SIPARIS_MUNFERIT_TYPE_DIZI** -> List<SiparisMunferitDto>

## Fonksiyon/Prosedur Listesi

### Public Fonksiyonlar
1. `siparis_olustur_ws` - Web servis uzerinden siparis olusturur. hesap_no + belge_no + donem_kodu ile bulur.
2. `siparis_detay_kontrol_ekran` - Ekran uzerinden sadece kalem kontrolu yapar (rollback).
3. `siparis_kontrol_ekran` - Ekran uzerinden belge+hesap ile tam siparis isleme yapar.
4. `siparis_isle` - **ANA MOTOR**: Belge ve hesap uzerinden siparis kaydi olusturur/gunceller. Resend kontrolu, kalem kontrolu, siparis sahibi olusturma, siparis olusturma, hareket kaydetme, belge guncelleme.
5. `siparis_yap` - belge_id ile siparis yapar. siparis_isle cagrisi wrapper'i.
6. `siparis_yap_kanal` - Kanal/talep_no ile siparis yapar. Teslimat talep durum gunceller.
7. `siparis_isle_kanal` - Mevcut hareket uzerinden kanal siparisi isler.
8. `teslimat_sonuc_bildir` - Teslimat sonucunu (basarili/basarisiz) bildirir. Yeni hareket olusturur.
9. `stok_hazirlik` - Periyodik stok hazirlama. siparis_sahibi_hareket kaydi olusturur.
10. `stok_faaliyet` - Siparis sahibi hareketini finansal olarak degerlendirir.
11. `siparis_iptali` - Siparis iptal islemi. Iptal hareketi olusturur, belge+detay geri alir.
12. `siparis_iptali_ws` - WS uzerinden referans stan ile iptal.
13. `toplu_siparis_isle` - Toplu siparis dosyasi isleme.
14. `toplu_siparis_iptali_isle` - Toplu siparis iptali.
15. `siparis_oncesi_degerlendir` - Toplu siparis oncesi hesap/belge validasyonu.
16. `teslimat_hareket_at` - Teslimat hareketi olusturur (siparis_sahibi_hareket).
17. `teslimat_hareket_isle` - Periyodik bekleyen hareketleri isler.
18. `rutin_bilgilendirme` - Rutin bildirim olusturur.
19. `siparis_onay` - Onay bekleyen siparisi onaylar. Belge no uretir.
20. `munferitle_siparis_yap` - Munferit tahsilatlari siparise donusturur.
21. `dagitim_detay_al` - Dagitim detaylarini hesaplar (mahsup, avans, nakit, transfer, kesinti).
22. `dagitim_raporu_olustur` - Dagitim raporu olusturur. siparis_dagitim_rapor ve mahsup tablolarina yazar.

### Private Fonksiyonlar
23. `siparis_belge_no_uret` - Onay bekleyen siparis icin belge no uretir (SIP + YYYY + 12 haneli id).
24. `sanal_hizmet_no_al` - Sanal hesaplar icin hizmet_no bulur.
25. `numberscale6tochar` / `numberscale2tochar` - Format fonksiyonlari.
26. `siparis_detay_urun_serino_kntrl` - Urun seri no format duzeltme.
27. `sd_kampanya_kodu_ekle` - Kampanya kodu kontrol collection'a ekler.
28. `siparis_kalem_kontrol` - **Kritik**: Belge kalemlerinin detayli validasyonu.
29. `siparis_guncelle` - Siparis durumu gunceller (private).
30. `siparis_sahibi_finans_guncelle` - Finans kaydi insert/update.
31. `opf_siparis_isle` - Ortak payli belge isleme.
32. `siparis_iptali_kontrol` - Iptal on kontrolu.

## Is Kurallari ve Validasyonlar (siparis_kalem_kontrol)

1. **Urun Seri No Kontrol**: 22.06.2014 - 29.02.2020 arasi belgelerde seri no numeric'e cevrilir.
2. **Paket Tipi Kontrol**: paket_tipi_id varsa gecerli oldugu kontrol edilir.
3. **Detay Eşleme**: ws_detay_list varsa siparis_detay tablosundan kalem_tipi_id + tedarikci_id + kdv_orani + otv_orani + urun_seri_no + paket_tipi + bayi_kodu + kampanya_id + taksit_no ile eslestirme yapilir.
4. **Temlik Kontrol**: urun_grubu_id = c_kalem_temlik (26) ise kampanya_kodu zorunlu.
5. **Dijital Prepaid Hesap**: hesap_tipi_id = C_HESAP_TIPI_DIJITAL_PREPAID ise hata.
6. **TDK Modu**: tdk_mi=1 ve detay yoksa siparis_duzeltme_tutar <> 0 olanlari auto topla.
7. **Kalem Validasyonlari**:
   - carpan < 0 -> revize_tutari negatif olmali
   - Tek KDV/OTV detayi kontrolu
   - duzeltme_yapilabilir_mi = 0 -> revize_tutari <= siparis_duzeltme_tutari
   - carpan > 0 ve revize_tutari < 0 -> negatif siparis hatasi
8. **Tutar Toleransi**: |siparis_toplam_tutar - detay_toplam| > 0.02 -> hata
9. **Belge Sahibi Kontrol**: belge_sahibi tablosunda hesap_id + belge_id kaydi olmali.
10. **Munferit Revize Kontrol**: Onay bekleyen munferit islem varsa otomatik iptal edilir (pck_talep_yonetimi).
11. **Munferit Islem Kontrol**: Tahsilat bilgisi tipi=4 olan kalem varsa hata.
12. **Tedarikci Toplam**: Toplam tedarikci duzeltme tutarindan fazla siparis yapilmamali.

## Tablo Iliskileri

### Dogrudan Kullanilan Tablolar (INSERT/UPDATE/SELECT)
- **siparis** (id, siparis_durum_id, siparis_sahibi_id, siparis_tipi_id, belge_id, alacak_id, ...)
- **siparis_hareket** (id, islem_tarihi, kurum_id, stan, siparis_id, siparis_hareket_durum_id, ...)
- **siparis_hareket_detay** (id, siparis_hareket_id, siparis_detay_id, islem_tutari, ...)
- **siparis_hareket_islem_sdy** (id, siparis_hareket_id, siparis_detay_id, kalem_tipi_id, tedarikci_id, revize_tutari, ...)
- **siparis_sahibi** (id, hesap_id, musteri_id, sirket_id, stok_analiz_detay_id, siparis_sahibi_tipi_id, ...)
- **siparis_sahibi_finans** (id, siparis_sahibi_id, finans_tipi, siparis_toplam_tutari, belge_mahsup_tutari, ...)
- **siparis_sahibi_hareket** (id, siparis_sahibi_id, islem_tarihi, islem_id, siparis_sahibi_hareket_durum_id, ...)
- **siparis_sahibi_hareket_detay** (id, siparis_sahibi_hareket_id, siparis_hareket_id, ...)
- **siparis_detay_kayit** (id, siparis_id, tdk_mi, vergi_haric, siparis_sebebi_id, aciklama, ...)
- **siparis_detay** (id, belge_id, kalem_tipi_id, tedarikci_id, orjinal_tutar, siparis_duzeltme_tutari, ...)
- **siparis_hareket_tahsilat_bilgi** (id, siparis_hareket_id, alacak_id, siparis_tutari, siparis_tahsilat_bilgi_tipi_id, ...)
- **siparis_dagitim_rapor** (id, stok_analiz_id, karar_no, ...)
- **siparis_dagitim_rapor_mahsup** (id, siparis_dagitim_rapor_id, hesap_no, tutar, tip, ...)

### Referans Verilen Tablolar (SELECT)
- **belge** (id, hesap_id, belge_no, donem_kodu, sirket_id, kalan_tutar, siparis_duzeltme_tutar, belge_durum, ...)
- **hesap** (id, hesap_no, musteri_id, sanal_mi, hesap_tipi_id, hesap_durum, ...)
- **hesap_tipi** (id, duzeltme_yapilabilir_mi, carpan, urun_grubu_id, ortak_payli_belge, ...)
- **kalem_tipi** (id, urun_grubu_id, duzeltme_yapilabilir_mi, carpan, ...)
- **siparis_durum** (id, islem_durumu)
- **stok_analiz** (id, sirket_id, baslangic_tarihi, bitis_tarihi, durum_id, tip, ...)
- **stok_analiz_detay** (id, stok_analiz_id, ...)
- **teslimat_talep** (id, talep_no, hesap_id, talep_durum_id, ...)
- **toplu_siparis** (id, sirket_id, durum_id, ...)
- **toplu_siparis_kayitlar** (id, toplu_siparis_id, hesap_no, belge_no, donem_kodu, kayit_durum, ...)
- **toplu_siparis_kayitlar_tmp** (id, token, hesap_no, belge_no, donem_kodu, degerlendirme_sonucu, ...)
- **abone** (hesap_id, hizmet_no)
- **musteri** (id, musteri_no, ad, soyad, tc_kimlik_no, vergi_no, tuzel_gercek_id)
- **bildirim** (id, belge_id, bildirim_durum, ...)
- **tb_dagitim_detay** (ana_kaynak_id, islem_id, islem_tutari, islem_durumu, tanitim_zamani, ...)
- **tahakkuk_bildirim** (id, ...)
- **paket_tipi** (id, aciklama_json)
- **tedarikci** (id, tedarikci_tipi_id)

## Cross-Package Bagimliliklari
- **[bkz: pck_def]** - Tum sabitler (c_success, c_fail, c_company_ana, c_hesap_tipi_dijital_prepaid, c_siparis_tipi_*, c_id_*, c_rc_*, vb.)
- **[bkz: pck_general]** - set_out_variables, get_islem_cevap_ack, get_stan
- **[bkz: pck_talep_yonetimi]** - talep_iptal_veya_reddet (onay islemi sirasinda munferit talepleri iptal etmek icin)

## Mimari Gozlemler
- `siparis_isle` merkezi motor fonksiyondur. `siparis_yap`, `siparis_olustur_ws`, `siparis_yap_kanal`, `siparis_isle_kanal`, `siparis_kontrol_ekran` hepsi bunu cagirir.
- `siparis_kalem_kontrol` cok kapsamli bir validasyon fonksiyonudur. Tedarikci revize, KDV/OTV, mahsup, TDK modu, munferit kontrol gibi is kurallari icerir.
- Teslimat sonuc bildiriminde basarisiz ise savepoint rollback ile geri alinir; basarili ise belge tutari guncellenir.
- Iptal islemi `siparis_iptali_kontrol` ile onaylanir, basarili ise belge+detay geri alinir, iptal hareketi olusturulur.
- Stok hazirlama periyodik batch isidir. `siparis_sahibi_hareket` olusturur, `teslimat_hareket_isle` bunlari isler.
- Dagitim raporu olusturma `siparis_sahibi_finans` ve `tb_dagitim_detay` uzerinden karmasik hesaplamalar yapar.
- OPF (ortak payli belge) mantigi vardir - birden fazla belge/tedarikci durumu.
- Siparis hareket "chain" mantigi vardir: `ust_id` ile onceki harekete referans verilir (iptal ve teslimat sonuc)
- `tahsilat_bilgi` ayrintili tahsilat dagilimi bilgisi tasir (mahsup, avans, nakit, transfer, kesinti)

## [ ] Belirsiz Noktalar
- [ ] `pck_def` sabitlerinin tam listesi - mevcut POC'tan veya diger package'lerden cikarilacak
- [ ] `t_number_dizi` type tanimi - Oracle VARRAY/NESTED TABLE
- [ ] `database_exception` tanimi (error_type, error_code, error_message kolonlari)
- [ ] `belge_sahibi` tablo yapisi
- [ ] `siparis_hareket_tahsilat_bilgi` ve `siparis_hareket_detay` tablo kolonlari detayi
- [ ] `siparis_sahibi_hareket_detay` tablo yapisi

## Ilk Implemente Edilecek Kapsam (MVP)
1. Entity'ler: siparis, siparis_hareket, siparis_detay_kayit, siparis_sahibi, siparis_sahibi_finans, siparis_hareket_islem_sdy
2. DTO'lar: SiparisIstekDto, SiparisIstekDetayDto, SiparisIstekFdyDto, SiparisKalemDetayiDto, SiparisCevapDto
3. Repository'ler: Yukaridaki entity'ler icin
4. Service: SiparisService (siparis_isle, siparis_yap, siparis_olustur_ws, siparis_kalem_kontrol)
5. Cross-package stub'lari: DefConstants, GeneralUtil (sadece kullanilan sabitler)
