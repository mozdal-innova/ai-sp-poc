package com.acme.ord.common.constants;

/**
 * pck_def sabitlerinin stub karsiligi.
 * Diger package'ler migrate edildikce zenginlestirilecek.
 */
public final class DefConstants {

    private DefConstants() {}

    // Genel donus kodlari
    public static final int RC_SUCCESS = 0;
    public static final int RC_FAIL = 1;

    // Sirket kodlari
    public static final long COMPANY_ANA = 100;
    public static final long COMPANY_DIJITAL = 110;
    public static final long COMPANY_SATIS = 120;
    public static final long COMPANY_PORTAL_DIJITAL = 130;
    public static final long COMPANY_DIJITAL3 = 140;

    // Evet/Hayir
    public static final int HAYIR = 0;
    public static final int EVET = 1;

    // Siparis durum ID'leri
    public static final long ID_ONAY_BEKLIYOR = 10;
    public static final long ID_ONAYLANDI = 15;
    public static final long ID_BELIRSIZ = 20;
    public static final long ID_BASARILI = 30;
    public static final long ID_TESLIMAT_BASARILI = 40;
    public static final long ID_TESLIMAT_BASARISIZ = 50;
    public static final long ID_IPTAL_EDILDI = 60;
    public static final long ID_MM_YENI_KAYIT = 70;
    public static final long ID_MM_ONAY_BEKLIYOR = 80;
    public static final long ID_MM_IPTAL_ONAY = 90;

    // Islem kodlari
    public static final long ISLEM_KODU_REVIZE = 100;
    public static final long ONAY_ISLEMI = 200;
    public static final long KANAL_TALEP_GIRISI = 300;
    public static final long STOK_ANALIZ_TESLIMAT_BASARILI = 400;
    public static final long SIPARIS_IPTAL_ISLEMI = 500;
    public static final long AMOUNT_CORRECTION = 600;
    public static final long ISLEM_MUSTERI_MEMNUNIYETI = 700;
    public static final long ISLEM_MAKAM_ONAYI = 800;

    // Siparis tip ID'leri
    public static final long SIPARIS_TIPI_MM = 1;
    public static final long SIPARIS_TIPI_MO = 2;
    public static final long SIPARIS_TIPI_MUNFERIT_REVIZE = 3;
    public static final long SIPARIS_TIPI_ONAY = 4;
    public static final long SIPARIS_TIPI_DIGER = 5;
    public static final long SIPARIS_TIPI_KANALDAN_MNFRT_REVIZE = 6;

    // Belge durumlari
    public static final String PARTIAL = "PARTIAL";
    public static final String REVIZE_ILE_KAPANMIS = "REVIZE_ILE_KAPANMIS";
    public static final String PAID = "PAID";
    public static final String UNPAID = "UNPAID";

    // Uygulama kodlari
    public static final int UYGULAMA_SIPARIS_TALEP = 1;
    public static final int UYGULAMA_TOPLU_SIPARIS = 2;

    // Siparis hareket durum ID'leri
    public static final long SHD_DEGERLENDIRILDI = 1000;
    public static final long SHD_DEGERLENDIRILECEK = 1010;
    public static final long SHD_DEGERLENDIRILMEYECEK = 1020;

    // Siparis sahibi hareket durum ID'leri
    public static final long SSHD_DEGERLENDIRILECEK = 2000;
    public static final long SSHD_DEGERLENDIRILDI = 2010;

    // Stok analiz
    public static final long STOK_ANALIZ_TIP_ONAY = 1;
    public static final long STOK_ANALIZ_TIP_DIGER = 2;
    public static final long STOK_ANALIZ_DURUM_ISLEMDE = 10;
    public static final long STOK_ANALIZ_DURUM_TAMAMLANDI = 20;

    // Siparis sahibi
    public static final long SIPARIS_SAHIBI_TIPI_NORMAL = 1;
    public static final long SIPARIS_SAHIBI_FINANS_NORMAL = 1;

    // Talep durumlari
    public static final long TALEP_DURUM_YENI = 1;
    public static final long TALEP_DURUM_ISLEMDE = 2;
    public static final long TALEP_DURUM_TAMAMLANDI = 3;

    // Toplu siparis durumlari
    public static final long TOPLU_DURUM_YUKLENDI = 1;
    public static final long TOPLU_DURUM_DEVAM = 2;
    public static final long TOPLU_DURUM_ISLEMDE = 3;
    public static final long TOPLU_DURUM_TAMAMLANDI = 4;
    public static final long TOPLU_DURUM_HATALI = 5;

    // Toplu siparis kayit durumlari
    public static final long TOPLU_KAYIT_BEKLIYOR = 1;
    public static final long TOPLU_KAYIT_BASARILI = 2;
    public static final long TOPLU_KAYIT_BASARISIZ = 3;
    public static final long TOPLU_KAYIT_IPTAL = 4;

    // Bildirim
    public static final long BILDIRIM_TIPI_SIPARIS = 1;
    public static final String BILDIRIM_AKTIF = "AKTIF";
    public static final String BILDIRIM_IPTAL = "IPTAL";

    // NotSend
    public static final String NOTSEND = "NOTSEND";

    // Tahsilat bilgi tipleri
    public static final long TBT_MUNFERIT_SIPARISTEN_REVIZE = 1;

    // Siparis sahibi hareket islem kodlari
    public static final long ISLEM_STOK_HAZIRLAMA = 100;
    public static final long ISLEM_ILK_BILGILENDIRME = 200;
    public static final long ISLEM_MUNFERIT_AVANS_DGRLN = 300;
    public static final long ISLEM_IPTAL_BILGILENDIRME = 400;
    public static final long AVANS_SIPARIS_TLB = 500;

    // Hesap tipi
    public static final long HESAP_TIPI_DIJITAL_PREPAID = 999;

    // Yetki
    public static final long YETKI_TAKSIT_IPTAL = 100;

    // Sistem kullanici ID
    public static final long INST_SISTEM_KULLANICI_ID = 0;

    // Siparis durum islem durumlari
    public static final long SIPARIS_DURUM_ISLEM_BASARISIZ = 1;
    public static final long SIPARIS_DURUM_ISLEM_BELIRSIZ = 2;
    public static final long SIPARIS_DURUM_ISLEM_BASARILI = 3;

    // Return code sabitleri (hata kodlari)
    public static final String RC_SQL_ERROR = "SQL_ERROR";
    public static final String RC_SIPARIS_ONAY_BEKLEYEN_VAR = "SIPARIS_ONAY_BEKLEYEN_VAR";
    public static final String RC_SIPARIS_MUNFERIT_DAHIL = "SIPARIS_MUNFERIT_DAHIL";
    public static final String RC_SIPARIS_MUNFERIT_REVIZE_VAR = "SIPARIS_MUNFERIT_REVIZE_VAR";
    public static final String RC_TEKRARLI_TOKEN = "TEKRARLI_TOKEN";
    public static final String RC_EK_TAH_MAH_DETAY_YOK = "EK_TAH_MAH_DETAY_YOK";
    public static final String RC_SIPARIS_KDV_DETAY_OLMALI = "SIPARIS_KDV_DETAY_OLMALI";
    public static final String RC_SIPARIS_OTV_DETAY_OLMALI = "SIPARIS_OTV_DETAY_OLMALI";
    public static final String RC_BIRDEN_FZLA_KDV_VAR = "BIRDEN_FZLA_KDV_VAR";
    public static final String RC_BIRDEN_FZLA_OTV_VAR = "BIRDEN_FZLA_OTV_VAR";
    public static final String RC_SIPARIS_TUTARSIZ = "SIPARIS_TUTARSIZ";
    public static final String RC_SIPARIS_NEGATIF = "SIPARIS_NEGATIF";
    public static final String RC_KALEM_CARPAN_NEG = "KALEM_CARPAN_NEG";
    public static final String RC_DETAY_DUZELTME_UYUMSUZ = "DETAY_DUZELTME_UYUMSUZ";
    public static final String RC_SIPARIS_DETAY_BOS = "SIPARIS_DETAY_BOS";
    public static final String RC_SIPARIS_DETAY_YOK = "SIPARIS_DETAY_YOK";
    public static final String RC_SIPARIS_COKLU_DETAY = "SIPARIS_COKLU_DETAY";
    public static final String RC_BELGE_SAHIBI_YOK = "BELGE_SAHIBI_YOK";
    public static final String RC_SIPARIS_BLOKE_TUTAR = "SIPARIS_BLOKE_TUTAR";
    public static final String RC_PAKET_TIPI_HATALI = "PAKET_TIPI_HATALI";
    public static final String RC_OPF_TDK_KAPATMA = "OPF_TDK_KAPATMA";
    public static final String RC_OTOMATIK_TALEP_IPTAL = "OTOMATIK_TALEP_IPTAL";
    public static final String RC_SATIS_TEDARIKCI_OLAMAZ = "SATIS_TEDARIKCI_OLAMAZ";
    public static final String RC_DIJITAL_PREPAID = "DIJITAL_PREPAID";
    public static final String RC_TAKSIT_TEDARIKCI_REVIZE = "TAKSIT_TEDARIKCI_REVIZE";
    public static final String RC_KALEM_KAMPANYA_ZORUNLU = "KALEM_KAMPANYA_ZORUNLU";
    public static final String RC_RESEND = "RESEND";
    public static final String RC_ISTEK_BOS = "ISTEK_BOS";
    public static final String RC_HESAP_NO_BOS = "HESAP_NO_BOS";
    public static final String RC_HESAP_BULUNAMADI = "HESAP_BULUNAMADI";
    public static final String RC_BELGE_BULUNAMADI = "BELGE_BULUNAMADI";
    public static final String RC_COKLU_BELGE = "COKLU_BELGE";
    public static final String RC_TALEP_BULUNAMADI = "TALEP_BULUNAMADI";
    public static final String RC_TALEP_DURUMU_UYUMSUZ = "TALEP_DURUMU_UYUMSUZ";
    public static final String RC_HAREKET_BULUNAMADI = "HAREKET_BULUNAMADI";
    public static final String RC_SIPARIS_DURUMU_UYUMSUZ = "SIPARIS_DURUMU_UYUMSUZ";
    public static final String RC_TOPLU_BULUNAMADI = "TOPLU_BULUNAMADI";
    public static final String RC_TOPLU_DURUM_HATA = "TOPLU_DURUM_HATA";
    public static final String RC_IPTAL_YAPILAMAZ = "IPTAL_YAPILAMAZ";
    public static final String RC_TAKSIT_IPTAL_YETKI_YOK = "TAKSIT_IPTAL_YETKI_YOK";
    public static final String RC_REF_STAN_BOS = "REF_STAN_BOS";
    public static final String RC_SIPARIS_BULUNAMADI = "SIPARIS_BULUNAMADI";
    public static final String RC_MUNFERIT_BOS = "MUNFERIT_BOS";
    public static final String RC_TUTAR_ASIMI = "TUTAR_ASIMI";
    public static final String RC_BIRDEN_FAZLA_SIP_SAHIBI = "BIRDEN_FAZLA_SIP_SAHIBI";
    public static final String RC_BELGE_NO_MEVCUT = "BELGE_NO_MEVCUT";
    public static final String RC_HAREKET_DURUMU_HATA = "HAREKET_DURUMU_HATA";
    public static final String RC_ISLEM_TARIHI_HATALI = "ISLEM_TARIHI_HATALI";

}
