===========================================================================
PCK_SIPARIS ANALIZI
===========================================================================

1. PACKAGE AMACI
   Siparis yonetimi package'i. Belge uzerindeki kalemlerin siparis (revize)
   islemlerini gerceklestirir. Siparis olusturma, iptal, teslimat sonuc
   bildirimi, toplu siparis, stok hazirlik ve dagitim raporu gibi ana
   islemleri kapsar.

2. SABITLER VE REFERANS VERILER
   - c_tolerans_toplam_limit = 0.02 (toplam tutar toleransi)
   - c_kalem_kdv = 3
   - c_kalem_otv = 4
   - c_kalem_mahsup = 10
   - c_kalem_temlik = 26
   - toplusipcolrecord: Toplu siparis kayit record tipi (body-level)
   - r_sd_knt_donus_rec / t_sd_knt_donus_col: Kalem kontrol donus tipleri (body-level)

3. CUSTOM TYPE'LAR (plsql_sources/data_types/)
   - T_SIPARIS_ISTEK_TYPE: Ana istek parametresi (islem bilgileri, ws_detay_list, kalem_detay_list)
   - T_SIPARIS_ISTEK_TYPE_DIZI: TABLE OF T_SIPARIS_ISTEK_TYPE
   - T_SIPARIS_ISTEK_DETAY_TYPE: Siparis detay bilgileri (tdk, vergi, sebep, erp, temsilci)
   - T_SIPARIS_ISTEK_FDY_TYPE: Kalem bazli detay (urun, tedarikci, tutar, kdv, otv, paket)
   - T_SIPARIS_ISTEK_FDY_TYPE_DIZI: TABLE OF T_SIPARIS_ISTEK_FDY_TYPE
   - T_SIPARIS_KALEM_DETAYI: Kalem detay bilgisi (orjinal/revize/duzeltme tutarlar)
   - T_SIPARIS_KALEM_DETAYI_COLLECTION: TABLE OF T_SIPARIS_KALEM_DETAYI
   - T_SIPARIS_CEVAP_TYPE: Donus parametresi (siparis/hareket/detay ID'leri, tutarlar)
   - T_SIPARIS_MUNFERIT_TYPE / DIZI: Munferit siparis istek tipi

4. FONKSIYON/PROSEDUR DETAYLARI

   4.1 PRIVATE YARDIMCI FONKSIYONLAR
       - siparis_belge_no_uret: Onay bekleyen siparis icin belge no uretir
         'SIP' + yil + lpad(hareket_id, 12, '0')
       - sanal_hizmet_no_al: Sanal hesap ise abone tablosundan hizmet_no alir
       - numberscale6tochar / numberscale2tochar: Sayi formatlamasi
       - siparis_detay_urun_serino_kntrl: 22.06.2014-29.02.2020 tarih araligi
         icindeki belgelerde urun seri no'yu numerik olarak temizler
       - sd_kampanya_kodu_ekle: Kalem kontrol sirasinda kampanya kodu ekleme
       - siparis_guncelle: Siparis durum guncellemesi (satir 2704)
       - siparis_sahibi_finans_guncelle: Finans kaydi olusturma/guncelleme (satir 2719)
       - opf_siparis_isle: Ortak payli belge siparis isleme (satir 2754)

   4.2 siparis_kalem_kontrol (satir 138-440)
       IS KURALLARI:
       - ws_detay_list varsa: her kalem icin siparis_detay+kalem_tipi join ile eslesme arar
       - Sirket bazli paket_tipi kontrolu (Ana=1=1, Dijital=paket_tipi eslesmeli)
       - Temlik kalem (urun_grubu=26) icin kampanya_kodu zorunlu
       - Dijital prepaid hesap kontrolu (hesap_tipi_id = C_HESAP_TIPI_DIJITAL_PREPAID)
       - TDK ise ve kalem listesi bossa: siparis_duzeltme_tutar <> 0 olan kalemleri alir
       - Carpan < 0 ise revize_tutari negatif olmali
       - Birden fazla KDV/OTV detayi olamaz
       - Toplam tutar tolerans kontrolu (|istek - detay_toplam| > 0.02 ise hata)
       - Belge sahibi kontrolu
       - Munferit revize kontrolu (TDK degilse)
       - Tedarikci toplam kontrolu

   4.3 siparis_isle (satir 447-794) - ANA FONKSIYON
       IS KURALLARI:
       - Resend kontrolu (ayni islem_tarihi+kurum+stan+islem_id)
       - siparis_kalem_kontrol cagrisi
       - Siparis sahibi bul veya olustur (hesap_id + musteri_id ile)
       - Stok analiz durumuna gore siparis tipi belirleme
       - Siparis olusturma (seq_siparis)
       - Siparis detay kayit olusturma veya guncelleme
       - Siparis hareket olusturma (seq_siparis_hareket)
       - Siparis hareket islem detaylari olusturma (loop ile kalem bazli)
       - Kampanya kodu guncelleme
       - Belge tutar guncelleme (kalan_tutar - siparis_tutari)
       - Belge durum belirleme: kalan <= 0 ise REVIZE_ILE_KAPANMIS, degilse PARTIAL

   4.4 siparis_yap (satir 800-850)
       Belge ID ile siparis yapar. Belge ve hesap bulur, siparis_isle cagirir.

   4.5 siparis_olustur_ws (satir 855-922)
       Web servis giris noktasi. Hesap_no ile hesap bulur,
       hesap_id + belge_no + donem_kodu ile belge bulur, siparis_isle cagirir.

   4.6 siparis_yap_kanal (satir 927-1005)
       Kanal uzerinden siparis. Talep_no ile teslimat_talep bulur,
       talep durumu kontrol eder, siparis_isle cagirir,
       basariliysa talep durumunu tamamlandi yapar.

   4.7 siparis_isle_kanal (satir 1010-1079)
       Mevcut hareket uzerinden kanal siparis isleme. Hareket ID ile mevcut
       siparis/belge/hesap bulur, siparis_isle cagirir.

   4.8 teslimat_sonuc_bildir (satir 1085-1242)
       Teslimat sonucunu bildirir. Islem tarihi = bugun kontrolu.
       Basarili ise belge tutar guncellenir, basarisiz ise sadece durum guncellenir.
       stok_analiz_detay ile iliskilendirilir.

   4.9 siparis_detay_kontrol_ekran (satir 1247-1291)
       Ekrandan kalem kontrolu. siparis_kalem_kontrol cagirir,
       ROLLBACK yapar (sadece kontrol, commit yok).

   4.10 siparis_kontrol_ekran (satir 1296-1336)
        Ekrandan siparis kontrolu. siparis_isle cagirir.

   4.11 siparis_iptali_kontrol (satir 1342-1414)
        Iptal on kontrolu: hareket bulma, resend, iptal edilebilirlik,
        taksit iptal yetkisi, OPF kontrol.

   4.12 siparis_iptali (satir 1419-1563)
        Siparis iptal islemi. Kontrol sonrasi belge tutar geri eklenir,
        iptal hareket olusturulur. siparis_hareket_detay cursor ile detaylar islenir.
        Belge tipi kontrolu (ek_tahsilat_belgesi ozel durum).

   4.13 siparis_iptali_ws (satir 1565-1611)
        WS uzerinden iptal. t_siparis_istek_type'dan parametreleri cekip
        siparis_iptali cagirir.

   4.14 toplu_siparis_isle (satir 1612-1763)
        Toplu siparis dosyasi isleme. toplu_siparis_kayitlar tablosundaki
        kayitlari okur, her biri icin hesap/belge bulur, siparis_isle cagirir.

   4.15 toplu_siparis_iptali_isle (satir 1764-1847)
        Toplu siparis iptal isleme. kayit_id_list ile filtreleyerek
        her kayit icin siparis_iptali cagirir.

   4.16 siparis_oncesi_degerlendir (satir 1848-1928)
        Token bazli on degerlendirme. toplu_siparis_kayitlar_tmp tablosu kullanilir.

   4.17 stok_hazirlik (satir 1929-2002)
        Sirket ve gun parametresiyle stok analiz hazirlik.

   4.18 stok_faaliyet (satir 2003-2080)
        Siparis sahibi hareket bazinda stok faaliyet islemi.

   4.19 siparis_onay (satir 2081-2164)
        Siparis onay islemi. vergi_no, vergi_dairesi, siparis_belge_no,
        taksitli_mi alanlari guncellenir.

   4.20 munferitle_siparis_yap (satir 2165-2260)
        Munferit siparis islemi. Mevcut hareket uzerinden munferit revize.

   4.21 teslimat_hareket_at (satir 2261-2339)
        Teslimat hareket firlat (procedure, batch icin).

   4.22 teslimat_hareket_isle (satir 2340-2373)
        Zaman araligindaki teslimat hareketlerini toplu isler.

   4.23 rutin_bilgilendirme (satir 2374-2426)
        Periyodik bilgilendirme raporu.

   4.24 dagitim_detay_al (satir 2427-2556)
        Siparis hareket icin finans dagitim detaylarini getirir.
        siparis_sahibi_finans tablosundan tutarlari okur.

   4.25 dagitim_raporu_olustur (satir 2557-2703)
        Sirket bazinda dagitim raporu olusturur.

   4.26 siparis_guncelle (satir 2704-2718)
        Siparis ve siparis_hareket durum guncellemesi.

   4.27 siparis_sahibi_finans_guncelle (satir 2719-2753)
        Siparis sahibi finans kaydi olusturma/guncelleme.

   4.28 opf_siparis_isle (satir 2754+)
        Ortak payli belge siparis isleme ozel fonksiyonu.

5. TABLO ILISKILERI

   ANA TABLOLAR (READ + WRITE):
   - belge (R/U): Belge bilgileri, tutar, durum, kilitli
   - hesap (R): Hesap bilgileri, musteri
   - siparis (RW): Siparis ana kayit
   - siparis_sahibi (RW): Siparis sahibi (hesap+musteri bazli)
   - siparis_hareket (RW): Siparis islem hareketleri
   - siparis_detay (R/U): Belge uzerindeki kalem detaylari
   - siparis_detay_kayit (RW/U): Siparis bazli detay kaydi
   - siparis_hareket_islem_sdy (RW): Hareket bazli kalem detaylari
   - siparis_hareket_detay (R/W): Siparis hareket detaylari (iptal isleminde)
   - siparis_hareket_tahsilat_bilgi (R/W): Tahsilat bilgi kayitlari
   - siparis_sahibi_finans (RW/U): Finans dagitim bilgileri
   - siparis_sahibi_hareket (R/W): Hareket bazli siparis sahibi islemleri
   - siparis_sahibi_hareket_detay (R/W): Siparis sahibi hareket detaylari
   - siparis_dagitim_rapor (RW/D): Dagitim rapor tablosu
   - siparis_dagitim_rapor_mahsup (RW/D): Dagitim rapor mahsup detaylari
   - stok_analiz (R/W/U): Stok analiz tablosu
   - bildirim (R/W/U): Bildirim tablosu
   - toplu_siparis (R/U): Toplu siparis dosya bilgileri
   - toplu_siparis_kayitlar (R/U): Toplu siparis satir kayitlari
   - toplu_siparis_kayitlar_tmp (R/U): Gecici degerlendirme tablosu
   - teslimat_talep (R/U): Teslimat talep bilgileri

   REFERANS TABLOLAR (READ ONLY):
   - kalem_tipi (R): Kalem tipi tanimlari (urun_grubu, carpan, duzeltme)
   - paket_tipi (R): Paket tipi tanimlari
   - abone (R): Hizmet no icin
   - belge_sahibi (R): Belge-hesap iliskisi
   - tedarikci (R): Tedarikci bilgileri
   - hesap_tipi (R): Hesap tipi tanimlari (ortak_payli_belge)
   - stok_analiz_detay (R): Stok analiz detay bilgileri
   - siparis_durum (R): Siparis durum tanimlari
   - musteri (R): Musteri bilgileri
   - tahakkuk_bildirim (R): Tahakkuk bildirim bilgileri
   - tb_dagitim_detay (R): Dagitim detay bilgileri
   - kullanici_yetki (R): Kullanici yetki kontrolu
   - database_exception (R): Hata tip referansi

6. SEQUENCE'LAR
   - seq_siparis_sahibi
   - seq_siparis
   - seq_siparis_detay_kayit
   - seq_siparis_hareket
   - seq_siparis_hareket_detay
   - seq_siparis_hareket_islem_sdy
   - seq_siparis_hrkt_tahsilat_bilgi
   - seq_siparis_sahibi_hareket
   - seq_siparis_sahibi_hrk_detay
   - seq_siparis_sahibi_finans
   - seq_stok_analiz
   - seq_bildirim
   - seq_siparis_dagitim_rapor
   - seq_siparis_dagitim_rapor_hrk

7. CROSS-PACKAGE BAGIMLILIKLARI
   - [bkz: pck_def]: Tum sabitler (c_company_*, c_id_*, c_rc_*, c_siparis_tipi_*, vb.)
   - [bkz: pck_general]: set_out_variables, get_islem_cevap_ack, get_stan
   - [bkz: pck_talep_yonetimi]: talep_iptal_veya_reddet (onay islemi icinde)
   - [bkz: pck_stok]: [ stok islemleri icin olasi bagimlilik ]

8. FONKSIYON CAGIRIM HIYERARSISI

   siparis_yap  ---------> siparis_isle
   siparis_olustur_ws ---> siparis_isle
   siparis_yap_kanal ----> siparis_isle
   siparis_isle_kanal ---> siparis_isle
   siparis_kontrol_ekran -> siparis_isle
   toplu_siparis_isle ---> siparis_isle
   munferitle_siparis_yap -> siparis_isle

   siparis_detay_kontrol_ekran -> siparis_kalem_kontrol

   siparis_isle -> siparis_kalem_kontrol
               -> siparis_sahibi_finans_guncelle
               -> siparis_guncelle

   siparis_iptali_ws -> siparis_iptali -> siparis_iptali_kontrol
                                       -> opf_siparis_isle
                                       -> teslimat_hareket_at

   teslimat_sonuc_bildir -> siparis_guncelle
   teslimat_hareket_isle -> stok_faaliyet -> siparis_sahibi_finans_guncelle
   dagitim_raporu_olustur -> dagitim_detay_al

   stok_hazirlik (bagimsiz)
   siparis_oncesi_degerlendir (bagimsiz)
   rutin_bilgilendirme (bagimsiz)
   siparis_onay (bagimsiz)

9. ANAHTAR IS KURALLARI
   - Resend kontrolu: ayni islem_tarihi+kurum_id+stan+islem_id varsa reddedilir
   - Tutar toleransi: |istek_toplam - detay_toplam| > 0.02 ise hata
   - Belge durum: kalan_tutar <= 0 ise c_revize_ile_kapanmis, yoksa c_partial
   - Iptal: belge kalan_tutar geri yazilir, negatif tutar hareket olusturulur
   - OPF (Ortak Payli Fatura): tedarikci alt belgelerine de hareket olusturulur
   - Taksitli satis iptali: kullanici_yetki tablosundan yetki kontrolu
   - Munferit siparis: toplam munferit tutar, ana siparis tutarini asamaz
   - Toplu siparis: her kayit SAVEPOINT ile bagimsiz islenir
   - Teslimat basarili: belge kalan_tutar duser; basarisiz: sadece durum guncellenir
   - Dijital prepaid hesaplarda siparis reddedilir
   - Belge kilitleme: Dijital hesaplarda belge_durum PAID degilse kilitli=1
   - Siparis belge no: "SIP" + yil + lpad(hareket_id, 12, '0')
   - Dagitim raporu: her gun yeniden olusturulur (onceki silinir)
   - Batch commit: toplu islemlerde 50-100 kayitta bir COMMIT

10. MIMARI GOZLEMLER
    - siparis_isle merkezi fonksiyon: 7 farkli giris noktasi buna delege eder
    - siparis_kalem_kontrol kapsamli validasyon: ~25 farkli hata senaryosu
    - Sirket bazli ozel davranislar (Ana, Dijital, Satis, Portal, Dijital3)
    - Toplu siparis islemleri batch pattern'i takip ediyor
    - Savepoint kullanimiyla parcali rollback destegi
    - Toplam 33 fonksiyon/prosedur (9 private + 24 public)
    - 14 sequence, ~35 tablo referansi

11. BELIRSIZ NOKTALAR
    - [ pck_def sabit degerleri tam olarak bilinmiyor - placeholder kullanildi ]
    - [ pck_general.get_islem_cevap_ack fonksiyonu hata mesaji tablosu okuyor olabilir ]
    - [ t_number_dizi tipi tanimlanmamis - genel NUMBER collection ]
    - [ siparis_hareket_detay tablosu kolon yapisi bilinmiyor ]
    - [ bildirim tablosu kolon yapisi bilinmiyor ]
    - [ kullanici_yetki tablosu kolon yapisi bilinmiyor ]
    - [ tb_dagitim_detay tablosu kolon yapisi bilinmiyor ]
    - [ siparis_dagitim_rapor / siparis_dagitim_rapor_mahsup kolon yapisi bilinmiyor ]
    - [ stok_analiz_detay kolon yapisi bilinmiyor ]
    - [ musteri tablosu kolon yapisi bilinmiyor ]
    - [ tahakkuk_bildirim tablosu kolon yapisi bilinmiyor ]
