# pck_siparis Analiz Raporu

## 1. Package Amacı
`pck_siparis`, sistemdeki sipariş süreçlerinin uçtan uca yönetiminden (oluşturma, onaylama, revize, iptal, toplu sipariş işleme) sorumlu temel iş mantığı paketidir. Siparişlerin kalem kontrolleri, vergi (KDV/ÖTV) hesaplama tutarlılık kontrolleri, kampanya ve stok işlemlerine yönelik validasyonlar ile sipariş hareketlerinin (tarihçe/log) oluşturulması bu paket üzerinden yürütülür. Ayrıca kanal entegrasyonları için Web Servis (`_ws`) metodları içerir.

## 2. Kullanılan Veri Tipleri (Custom Types)
Paket giriş ve çıkış parametrelerinde Oracle tarafında tanımlanmış aşağıdaki Type'ları yoğun olarak kullanmaktadır. Java tarafında bu tipler DTO'lar (Data Transfer Object) olarak modellenecektir:
- `T_SIPARIS_ISTEK_TYPE`: Ana sipariş istek nesnesi (hesap bilgileri, tutar, kanal no, belge no vb.)
- `T_SIPARIS_CEVAP_TYPE`: Ana sipariş cevap nesnesi (oluşan id'ler, işlenen kayıt sayısı, hata durumu vb.)
- `T_SIPARIS_ISTEK_DETAY_TYPE`: İstek içindeki sipariş ekstra detay bilgileri (vergi no, bayi kodu, portal kullanıcısı, ERP bilgileri)
- `T_SIPARIS_ISTEK_FDY_TYPE`: Sipariş kalemlerine ait ürün detayları (ürün seri no, tedarikçi, KDV/ÖTV oranı, taksit bilgisi)
- `T_SIPARIS_KALEM_DETAYI`: Veritabanı ile etkileşimli çalışan, revize ve hesaplanmış tutarları barındıran kalem detayı.
- `T_SIPARIS_MUNFERIT_TYPE`: Münferit sipariş işlemleri için kalem koleksiyonu barındıran tip.

## 3. Tablo Etkileşimleri (Domain Entities)
Paket içindeki sorgulardan aşağıdaki tabloların kullanıldığı tespit edilmiştir (Java'da karşılıkları Entity olacaktır):
- `siparis`, `siparis_detay`, `siparis_hareket` (Sipariş ana yapı ve logları)
- `belge`, `hesap`, `abone` (Müşteri ve belge kök bilgileri)
- `siparis_sahibi`, `siparis_sahibi_hareket`, `siparis_sahibi_finans` (Sipariş sahipliği ve finansal dağılım)
- `teslimat_talep` (Teslimat bağlantıları)
- `toplu_siparis`, `toplu_siparis_kayitlar`, `toplu_siparis_kayitlar_tmp` (Toplu sipariş (batch) işlemleri)

## 4. Cross-Package Bağımlılıkları (Diğer Paketlere Referanslar)
- `[bkz: pck_def]`: Sistemsel sabitler (durum id'leri, şirket id'leri, uygulama tipleri `c_id_onay_bekliyor`, `c_success`, `c_fail` vb.)
- `[bkz: pck_general]`: Ortak output atama metotları (`set_out_variables`, mesaj getirme) ve genel exception handling.
- `[bkz: pck_talep_yonetimi]`: Sipariş içinden otomatik talep iptalleri (`talep_iptal_veya_reddet`).

## 5. Fonksiyon ve Prosedür Detayları / İş Kuralları
- **`siparis_olustur_ws` / `siparis_yap` / `siparis_isle` vb.**: Siparişlerin çeşitli kanallardan oluşturulmasını sağlar. `siparis_kalem_kontrol` adlı özel prosedür üzerinden sipariş kalemleri tutarlılık kontrolünden (negatif değerler, kampanya zorunlulukları, blokeli tutar uyumsuzlukları) geçer.
- **`siparis_kalem_kontrol` (Internal)**: Belge üzerindeki vergi, tedarikçi, kampanya kodları ve tutar doğrulamasını yapar. Hatalı durumlarda `database_exception` üzerinden hata fırlatır ve işlemi iptal eder.
- **`siparis_iptali` / `siparis_iptali_ws`**: Gelen sipariş hareket id'sine göre mevcut siparişi bulur, iptal hareketini kaydeder ve finansal ters hareketleri tetiklemek üzere işler.
- **`siparis_onay`**: Belirsiz veya onay bekleyen durumdaki bir siparişi alıp işlem kabulünü yapar, vergi no vs güncellenir.
- **`toplu_siparis_isle` / `toplu_siparis_iptali_isle`**: Token / dosya numarası ile geçici (`tmp`) tabloda bekleyen toplu kayıtları döngüye alıp tek tek siparişe dönüştürür.
- **`dagitim_detay_al`**: Sipariş sahibi üzerinde sipariş tutarı, nakit, belge, avans mahsuplaştırmalarını hesaplar.

## 6. Mimari Gözlemler & Spring Boot Karşılıkları
1.  Oracle PL/SQL'deki dışarı açılan "Function"lar, Java'da `SiparisService` arabiriminde birer metod olacaktır.
2.  `T_SIPARIS_*` Type'ları, DTO sınıflarına çevrilecektir (`SiparisIstekDto`, `SiparisCevapDto` vb.).
3.  Fonksiyonların geneli `OUT` parametrelerle (`prm_cevap_kodu`, `prm_error_str` vb.) hata/başarı durumunu dönmektedir. Java'da bu yapı, geriye dönen ana objenin (`SiparisCevapDto`) içinde bir `ResultStatus` objesi barındırması veya uygulamanın geneli bir `@RestControllerAdvice` ve özel Exception (`SiparisBusinessException`) sınıfları üzerinden yürütülecektir.
4.  Geliştirme süresince toplu işlem döngüleri (`toplu_siparis_isle`), performansı korumak için Spring Batch veya toplu JPA kayıt (`saveAll`) mimarisine uyarlanabilir. Şimdilik `Service` metodu olarak standart döngü içinde kalacaktır.
