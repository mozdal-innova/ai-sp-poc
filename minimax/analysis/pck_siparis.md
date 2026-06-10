# pck_siparis Analizi

## Package Amaci
Siparis islemlerini yonetir: siparis olusturma, guncelleme, iptal, toplu siparis isleme, teslimat sonuc bildirimi.

## Fonksiyonlar

### 1. siparis_olustur_ws
- Web servis uzerinden siparis olusturur
- prm_t_siparis_istek_type_dizi: Siparis isttekleri dizisi
- Donus: 	_siparis_cevap_type

### 2. siparis_detay_kontrol_ekran
- Ekran uzerinden siparis detay kontrolu yapar
- Sadece validasyon, islem yapmaz (rollback)

### 3. siparis_kontrol_ekran
- Belge bazli siparis kontrolu
- siparis_isle cagrir ama commit yapmaz

### 4. siparis_isle (ANA FONKSIYON)
- Siparis sahibi, siparis ve siparis hareket kayitlarini olusturur
- Adimlar:
  1. Resend kontrolu
  2. Hizmet no al (sanal hesap kontrolu)
  3. Kalem kontrol (siparis_kalem_kontrol)
  4. Siparis sahibi bul/olustur
  5. Siparis kaydi olustur
  6. Siparis detay olustur/guncelle
  7. Siparis hareket olustur
  8. Kalem detaylari kaydet
  9. Belge guncelle
  10. Siparis durumu guncelle
- Tablolar: siparis_sahibi, siparis, siparis_detay_kayit, siparis_hareket, siparis_hareket_islem_sdy

### 5. siparis_yap
- Belge ID ile siparis yapar
- siparis_isle cagrir

### 6. siparis_yap_kanal
- Teslimat talep no ile siparis yapar
- Talep durumunu 'tamamlandi' olarak isaretler

### 7. siparis_isle_kanal
- Mevcut siparis hareketi uzerinden islem yapar

### 8. teslimat_sonuc_bildir
- Teslimat sonucunu bildirir (basarili/basarisiz)
- Yeni siparis hareketi olusturur
- Belge guncellemesi yapar

### 9. siparis_iptali / siparis_iptali_ws
- Siparis iptal islemi
- Iptal hareketi olusturur
- Belge durumunu geri alir
- Bildirim iptal eder

### 10. toplu_siparis_isle
- Toplu siparis isleme
- Kayit bazli isler, hata varsa atlar

### 11. toplu_siparis_iptali_isle
- Toplu siparis iptal isleme

## Tablolar (iliskili)
- siparis_sahibi: Siparis sahibi bilgileri
- siparis: Ana siparis tablosu
- siparis_detay_kayit: Siparis detay bilgileri
- siparis_hareket: Siparis hareketleri
- siparis_hareket_islem_sdy: Hareket detaylari
- siparis_hareket_detay: [???]
- siparis_hareket_tahsilat_bilgi: Tahsilat bilgileri
- elge: Belge tablosu
- hesap: Hesap tablosu
- 	oplu_siparis: Toplu siparis
- 	oplu_siparis_kayitlar: Toplu siparis kayitlari
- 	eslimat_talep: Teslimat talepleri
- stok_analiz: Stok analiz
- kalem_tipi: Kalem tipi
- paket_tipi: Paket tipi
- ildirim: Bildirim

## Cross-Package Bagimliliklar
- pck_def: Sabitler (c_success, c_fail, c_id_*, c_rc_*, vs.)
- pck_general: Genel yardimci fonksiyonlar
- pck_talep_yonetimi: Talep yonetimi (talep iptal/red)
- opf_siparis_isle: [bagimlilik]

## Sabitler (body icinde)
- c_tolerans_toplam_limit: 0.02
- c_kalem_kdv: 3 (KDV urun grubu)
- c_kalem_otv: 4 (OTV urun grubu)
- c_kalem_mahsup: 10 (Mahsup urun grubu)
- c_kalem_temlik: 26 (Temlik urun grubu)

## Custom Tipler (data_types/ klasorunde)
- 	_siparis_istek_type: Siparis istem tipi
- 	_siparis_istek_type_dizi: Istem dizisi
- 	_siparis_cevap_type: Cevap tipi
- 	_siparis_kalem_detayi: Kalem detay tipi
- 	_siparis_kalem_detayi_collection: Kalem collection
- 	_siparis_istek_fdy_type: FDY istem tipi
- 	_siparis_istek_fdy_type_dizi: FDY dizi
- 	_siparis_istek_detay_type: Detay tipi
- 	_siparis_munferit_type: [data_types'da var]
- 	_siparis_munferit_type_dizi: [data_types'da var]
- 	_number_dizi: [standart tip]

## Mimari Notlar
- Ana islem akisi: Belge -> Hesap -> Siparis -> Hareket
- Kalem tipi bazli vergi (KDV/OTV) ve mahsup islemleri
- Resend korumasi: Ayni islem_tarihi+kurum_id+stan+islem_id kontrolu
- Savepoint kullanimi: Hata durumunda geri alma
- Stok analiz entegrasyonu: Onay isleminde talep iptali

## Bellirsiz Noktalar
- siparis_hareket_detay tablosu tam yapisi [?]
- opf_siparis_isle fonksiyonu [pck_xxx?]
- 	aksitli_satis_id, 	aksit_no alanlari icin detayli logic
- siparis_sahibi_finans_guncelle proseduru [pck_xxx?]
- siparis_guncelle proseduru [pck_xxx?]
