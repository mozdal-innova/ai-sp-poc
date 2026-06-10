# ORDER_MIGRATION - Oracle PL/SQL -> Spring Boot Migration

## Proje Ozeti
52 Oracle PL/SQL package'indaki business logic Java Spring Boot'a tasiniyor.
- DB semasi as-is kaliyor, JPA entity'ler birebir Oracle tablo/kolon adlarini kullaniyor
- Spring Boot 2.7.18, Java 17, single module Maven projesi
- PostgreSQL (dev), Oracle (prod) — Spring profiles ile gecis

## Dizin Yapisi

```
ORDER_MIGRATION/
├── CLAUDE.md              # Proje convention'lari (Turkce analiz, tablo adlari as-is, vb.)
├── AGENTS.md              # Agent davranis kurallari
├── instructions.md        # BU DOSYA — calisma talimatlari
├── analysis/              # Her package icin ayri analiz dosyasi
│   ├── pck_envanter.txt   # pck_envanter analizi
│   ├── pck_siparis.txt    # pck_siparis analizi
│   └── ...                # Her yeni package icin ayri dosya
├── plsql_sources/         # PL/SQL kaynak dosyalari (txt formatinda)
│   ├── pck_xxx_spec.txt   # Package spec dosyalari
│   ├── pck_xxx_body.txt   # Package body dosyalari
│   └── data_types/        # Oracle custom data type tanimlari (gerektiginde)
└── ord-core/              # Spring Boot projesi (single module, sifirdan olusturulacak)
    ├── pom.xml
    └── src/main/java/com/acme/ord/
        ├── common/        # Sabitler, util, fixed-width framework, exception
        ├── domain/        # JPA entity + repository + DTO
        ├── service/       # Package is mantigi (her pck_* icin alt paket)
        ├── batch/         # Scheduler, batch job
        └── app/           # Spring Boot main, config, REST API
```

## Is Akisi (Her Yeni Package Icin)

### Faz 1: Analiz
1. Kullanici PL/SQL kaynak kodunu `plsql_sources/` altina txt dosyasi olarak koyar
   - Spec: `plsql_sources/pck_xxx_spec.txt`
   - Body: `plsql_sources/pck_xxx_body.txt`
   - Data type gerekirse Claude talep eder → kullanici `plsql_sources/data_types/` altina koyar
2. **KRITIK — PL/SQL Custom TYPE Tanimlari:**
   - Spec veya body'de `TYPE ... IS RECORD`, `TYPE ... IS TABLE OF`, `TYPE ... IS REF CURSOR`,
     `SUBTYPE`, veya `%ROWTYPE` / `%TYPE` gibi bilinmeyen custom type referanslari goruldugunde
     **MUTLAKA kullanicidan tanimi istenmelidir**
   - Tahmin edilmez, varsayim yapilmaz — yanlis Java karsiligi uretmekten daha iyi sormak
   - Kullanici type tanimini `plsql_sources/data_types/` altina koyar veya dogrudan paylasir
3. `analysis/pck_xxx.txt` dosyasina analiz yazilir (her package ayri dosya):
   - Package amaci
   - Sabitler ve referans veriler
   - Prosedur/fonksiyon detaylari (is kurallari, validasyonlar, hesaplamalar)
   - Tablo iliskileri, cross-package bagimliliklari
   - Mimari gozlemler
4. Turkce yazilir, tablo/kolon/parametre adlari orijinal kalir

### Faz 2: Migrasyon
1. Gerekli yeni entity'ler `domain/entity/` altina eklenir
2. Repository'ler `domain/repository/` altina eklenir
3. Service siniflari `service/<paket_adi>/` altina eklenir
4. Mevcut service'lerle entegrasyon (cross-package cagrilar)
5. `mvn clean compile` ile derleme dogrulamasi
6. Gerekirse test eklenir

## Teknik Convention'lar

### Entity
- `@Table(name = "xxx")` — Oracle tablo adi birebir
- `@Column(name = "xxx")` — Oracle kolon adi birebir
- `@SequenceGenerator(allocationSize = 1)` — PL/SQL .nextval davranisi
- `ddl-auto: update` (dev), `ddl-auto: none` (prod)
- `@Getter @Setter @NoArgsConstructor` — `@Data` kullanilmaz (entity'lerde)
- `javax.persistence.*` import (Spring Boot 2.7.x = Java EE, Jakarta degil)

### Service
- Her pck_* icin `service/` altinda ayri alt paket
- Strategy Pattern: dosya tipi bazli IF/ELSIF zincirleri yerine
- Template Method: ortak akislar abstract base class'ta
- `@Transactional` service katmaninda

### Fixed-Width Dosya
- `@FixedWidthRecord(length = N)` + `@FixedWidthField(position, length, pad, scale)`
- `FixedWidthSerializer` ile serialize/deserialize
- Atomik yazma: `.pre` -> `.dat` rename
- Header: `H` + dosya_adi + kurum + tarih + kayit_sayisi
- Footer: `F` + dosya_adi

### Dosya Uretim Mimarisi
- **Generic Engine**: `dosya_tipi.sorgu` alanindaki SQL ile uretilen dosyalar (~40+ tip)
- **Ozel Strategy**: Post-generation update veya ozel logic gereken tipler (~8-10 tip)
  - STK, TDR, SRB, KRG, TES, DGT/DGU, BLT/KLT, SVD, BTO, OST
  - M-STK, M-TDR, M-SRB, M-KRG, M-DGT, M-BLR
- Yeni dosya tipi = yeni `@Component` class (Spring auto-discovery)

### Sabitler
- `CompanyConstants`: sirket kodlari (ACME=100, ACME_NET=110, ACME_MOB=155)
- `FileGroupConstants`: dosya grup ID'leri
- `SystemConstants`: durum kodlari, islem kodlari, tutar carpanlari

## Test

### Dev Ortami
- Uygulama: `mvn spring-boot:run -Dspring-boot.run.profiles=dev`
- Seed data: `POST /api/test/seed`
- Status: `GET /api/test/status`
- Ornek BLG dosya: `POST /api/test/create-sample-blg`
- Giden dosya uret: `POST /api/files/outgoing?fileTypeCode=STK&kurumId=2001`
- Gelen dosya isle: `POST /api/files/incoming?filePath=./files/incoming/I-BLG...-01.dat`

### Build
- Derleme: `mvn clean compile`
- Test: `mvn test`
- Full build: `mvn clean install`

## PL/SQL Kaynak Dosyalarini Okuma

PL/SQL kaynak kodlari `plsql_sources/` dizini altinda txt dosyasi olarak bulunur.
Yeni bir package analiz edilecekse kullanici kaynak kodunu bu dizine koyar.

### Dosya Yapisi
```
plsql_sources/
├── pck_xxx_spec.txt     # Package spec dosyalari
├── pck_xxx_body.txt     # Package body dosyalari
└── data_types/          # Oracle custom data type tanimlari (gerektiginde)
```

### Okuma Yontemi
- Kaynak dosyalar dogrudan `Read` tool ile okunur
- Dosya buyukse `offset` ve `limit` parametreleri ile parca parca okunabilir
- Data type gerekirse Claude talep eder → kullanici `plsql_sources/data_types/` altina koyar

## Dokumantasyon Convention'lari

### Analiz Dosyalari
Her pck_* paketi icin `analysis/` klasorunde AYRI dosya olusturulur:

```
analysis/
├── pck_envanter.txt       # pck_envanter analizi
├── pck_siparis.txt        # pck_siparis analizi
├── pck_taksit.txt         # pck_taksit analizi (gelecek)
└── pck_tahakkuk.txt       # pck_tahakkuk analizi (gelecek)
```

### Analiz Dosyasi Kurallari
- Her pck_* icin ayri `analysis/pck_xxx.txt` dosyasi (tek buyuyen dosya KULLANILMAZ)
- Icerik: PL/SQL analizi, fonksiyon listesi, tablo referanslari, cross-pkg bagimliliklari
- Turkce yazilir, tablo/kolon/parametre adlari orijinal Oracle naming
- Cross-package referanslar `[bkz: pck_xxx]` seklinde isaretlenir

## Notlar
- Kullanici uygulamayi kendisi baslatir/durdurur — agent baslatmaya calismaz
- Belirsiz kalan noktalar (dosya tipi acilimlari, parametre degerleri) analiz icinde `[ ]` ile isaretlenir
