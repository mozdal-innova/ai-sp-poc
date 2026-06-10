package com.acme.ord.common;

/**
 * pck_def sabitlerinin Java karsiligi.
 * Tum sabitler Oracle'daki orijinal isimleriyle tanimlanir.
 */
public final class Constants {

    private Constants() {
    }

    // -- Genel donuş kodları --
    public static final int SUCCESS = 1;
    public static final int FAIL = 0;

    public static final String RC_SUCCESS = "00";

    // -- Evet / Hayir --
    public static final int C_EVET = 1;
    public static final int C_HAYIR = 0;

    // -- Sirket ID'leri (pck_def.c_company_*) --
    public static final long C_COMPANY_ANA = 100;
    public static final long C_COMPANY_DIJITAL = 110;
    public static final long C_COMPANY_SATIS = 155;
    public static final long C_COMPANY_PORTAL_DIJITAL = 160;
    public static final long C_COMPANY_DIJITAL3 = 165;

    // -- Siparis Durum ID'leri --
    public static final long C_ID_ONAY_BEKLIYOR = 1;

    // -- Islem Kodlari --
    public static final long C_AMOUNT_CORRECTION = 3;
    public static final long C_ISLEM_MUSTERI_MEMNUNIYETI = 4;
    public static final long C_ISLEM_MAKAM_ONAYI = 5;
    public static final long C_ONAY_ISLEMI = 6;
    public static final long C_ISLEMKODU_REVIZE = 3;

    // -- Islem Durum --
    public static final long C_NOTSEND = 0;

    // -- Siparis Tipi --
    public static final long C_SIPARIS_TIPI_MM = 3;
    public static final long C_SIPARIS_TIPI_MO = 4;

    // -- Siparis Durum (MM) --
    public static final long C_ID_MM_YENI_KAYIT = 1;
    public static final long C_ID_MM_ONAY_BEKLIYOR = 2;
    public static final long C_ID_MM_IPTAL_ONAY = 5;

    // -- Kalem tipleri --
    public static final int C_KALEM_KDV = 3;
    public static final int C_KALEM_OTV = 4;
    public static final int C_KALEM_MAHSUP = 10;
    public static final int C_KALEM_TEMLIK = 26;

    // -- Hesap Tipi --
    public static final long C_HESAP_TIPI_DIJITAL_PREPAID = 99; // placeholder

    // -- Sistem kullanici --
    public static final long C_INST_SISTEM_KULLANICI_ID = 0;

    // -- Tolerans --
    public static final double C_TOLERANS_TOPLAM_LIMIT = 0.02;

    // -- Cevap Kodlari (pck_def.c_rc_*) --
    public static final String RC_SIPARIS_ONAY_BEKLEYEN_VAR = "SIP001";
    public static final String RC_SIPARIS_MUNFERIT_DAHIL = "SIP002";
    public static final String RC_SIPARIS_MUNFERIT_REVIZE_VAR = "SIP003";
    public static final String RC_TEKRARLI_TOKEN = "SIP004";
    public static final String RC_EK_TAH_MAH_DETAY_YOK = "SIP005";
    public static final String RC_SIPARIS_KDV_DETAY_OLMALI = "SIP006";
    public static final String RC_SIPARIS_OTV_DETAY_OLMALI = "SIP007";
    public static final String RC_BIRDEN_FZLA_KDV_VAR = "SIP008";
    public static final String RC_BIRDEN_FZLA_OTV_VAR = "SIP009";
    public static final String RC_SIPARIS_TUTARSIZ = "SIP010";
    public static final String RC_SIPARIS_NEGATIF = "SIP011";
    public static final String RC_KALEM_CARPAN_NEG = "SIP012";
    public static final String RC_DETAY_DUZELTME_UYUMSUZ = "SIP013";
    public static final String RC_SIPARIS_DETAY_BOS = "SIP014";
    public static final String RC_SIPARIS_DETAY_YOK = "SIP015";
    public static final String RC_SIPARIS_COKLU_DETAY = "SIP016";
    public static final String RC_BELGE_SAHIBI_YOK = "SIP017";
    public static final String RC_SIPARIS_BLOKE_TUTAR = "SIP018";
    public static final String RC_PAKET_TIPI_HATALI = "SIP019";
    public static final String RC_OPF_TDK_KAPATMA = "SIP020";
    public static final String RC_OTOMATIK_TALEP_IPTAL = "SIP021";
    public static final String RC_SATIS_TEDARIKCI_OLAMAZ = "SIP022";
    public static final String RC_DIJITAL_PREPAID = "SIP023";
    public static final String RC_TAKSIT_TEDARIKCI_REVIZE = "SIP024";
    public static final String RC_KALEM_KAMPANYA_ZORUNLU = "SIP025";
    public static final String RC_SQL_ERROR = "SQL999";
    public static final String RC_RESEND = "SIP030";
    public static final String RC_BELGE_BULUNAMADI = "SIP031";
    public static final String RC_HESAP_BULUNAMADI = "SIP032";
    public static final String RC_SIPARIS_SAHIBI_OLUSMADI = "SIP033";
    public static final String RC_BIRDEN_FAZLA_SIP_SAHIBI = "SIP034";
    public static final String RC_KALEM_KONTROL = "SIP035";
    public static final String RC_BELGE_GUNCELLEME = "SIP036";
    public static final String RC_HAREKET_OLUSTURMA = "SIP037";
}
