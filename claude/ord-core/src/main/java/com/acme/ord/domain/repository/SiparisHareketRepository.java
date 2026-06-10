package com.acme.ord.domain.repository;

import com.acme.ord.domain.entity.SiparisHareket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface SiparisHareketRepository extends JpaRepository<SiparisHareket, Long> {

    @Query("SELECT COUNT(sh) FROM SiparisHareket sh WHERE sh.islemTarihi = :islemTarihi " +
           "AND sh.kurumId = :kurumId AND sh.stan = :stan AND sh.islemId = :islemId")
    long countByIslemTarihiAndKurumIdAndStanAndIslemId(
            @Param("islemTarihi") LocalDate islemTarihi,
            @Param("kurumId") Long kurumId,
            @Param("stan") Long stan,
            @Param("islemId") Long islemId);

    @Query("SELECT sh.id FROM SiparisHareket sh " +
           "JOIN SiparisHareketIslemSdy shd ON sh.id = shd.siparisHareketId " +
           "WHERE sh.belgeId = :belgeId " +
           "AND sh.islemKodu IN :islemKodlari " +
           "AND sh.islemDurumId <> :islemDurumId " +
           "AND sh.ustId IS NULL " +
           "AND shd.siparisDetayId IN :siparisDetayIds")
    List<Long> findHareketIdsByBelgeAndIslemKodlari(
            @Param("belgeId") Long belgeId,
            @Param("islemKodlari") List<Long> islemKodlari,
            @Param("islemDurumId") Long islemDurumId,
            @Param("siparisDetayIds") List<Long> siparisDetayIds);

    @Modifying
    @Query("UPDATE SiparisHareket sh SET sh.siparisHareketDurumId = :hareketDurumId, " +
           "sh.siparisDurumId = :siparisDurumId, sh.cevapKodu = :cevapKodu, " +
           "sh.guncellemeZamani = :guncellemeZamani, sh.guncellleyenKullaniciId = :kullaniciId " +
           "WHERE sh.id = :hareketId")
    void updateHareketDurum(@Param("hareketId") Long hareketId,
                            @Param("hareketDurumId") Long hareketDurumId,
                            @Param("siparisDurumId") Long siparisDurumId,
                            @Param("cevapKodu") String cevapKodu,
                            @Param("guncellemeZamani") LocalDateTime guncellemeZamani,
                            @Param("kullaniciId") Long kullaniciId);
}
