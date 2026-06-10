# ORDER_MIGRATION - Oracle PL/SQL -> Spring Boot Migration

## Project Purpose
52 Oracle PL/SQL package'indaki business logic Java Spring Boot'a tasiniyor.
DB semasi as-is kaliyor, JPA entity'ler birebir Oracle tablo/kolon adlarini kullaniyor.

## Key Files
- `CLAUDE.md` — Bu dosya, proje convention'lari
- `instructions.md` — Detayli calisma talimatlari ve teknik convention'lar
- `analysis/` — Her package icin ayri analiz dosyasi (`analysis/pck_xxx.txt`)
- `ord-core/` — Spring Boot projesi (single module Maven, sifirdan olusturulacak)

## Tech Stack
- Java 17, Spring Boot 2.7.18, Maven single module
- `javax.persistence.*` (Spring Boot 2.7.x = Java EE, Jakarta DEGIL)
- PostgreSQL (dev, ddl-auto: update), Oracle (prod, ddl-auto: none)
- Lombok, Spring Data JPA, H2 (test)

## Workflow (Her Package Icin)
1. Kullanici PL/SQL spec + body verir
2. `analysis/pck_xxx.txt` dosyasina Turkce analiz yazilir (her package ayri dosya)
3. Java service/entity/repository implemente edilir
4. `mvn clean compile` ile dogrulanir

## Analysis Conventions
- Analiz ve iletisim dili: Turkce
- Tablo/kolon/parametre adlari orijinal Oracle naming ile kalir
- Cross-package bagimliliklari isaretlenir
- Belirsiz noktalar `[ ]` ile not edilir

## Code Conventions
- Entity: `@Getter @Setter @NoArgsConstructor` (`@Data` kullanilmaz)
- Entity: `@Table(name="oracle_tablo_adi")`, `@Column(name="oracle_kolon_adi")`
- Service: Her pck_* icin `service/<paket_adi>/` alt paketi
- Strategy Pattern: dosya tipi bazli IF/ELSIF zincirleri yerine
- Template Method: ortak akislar abstract base class'ta
- Fixed-width: `@FixedWidthRecord` + `@FixedWidthField` + `FixedWidthSerializer`
- Generic engine: `dosya_tipi.sorgu` SQL'i direkt string uretir, @FixedWidthRecord gerekmez

## Critical Rules
- Uygulama baslatma/durdurma YAPILMAZ — kullanici kendisi yapar
- `mvn spring-boot:run` CALISTIRILMAZ
- Her package analizi ayri dosyada: `analysis/pck_xxx.txt` (tek buyuyen dosya KULLANILMAZ)
- Proje sifirdan basliyor — onceki POC referans alinmaz, temiz baslangic yapilir

## Current State
- Proje sifirdan baslatiliyor
- ord-core Spring Boot projesi henuz olusturulmadi
- Ilk adim: Spring Boot proje iskeleti olusturma
