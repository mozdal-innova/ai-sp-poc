package com.acme.ord.domain.repository;

import com.acme.ord.domain.entity.SiparisDetay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface SiparisDetayRepository extends JpaRepository<SiparisDetay, Long> {

    List<SiparisDetay> findByBelgeId(Long belgeId);

    @Query("SELECT sd FROM SiparisDetay sd JOIN KalemTipi kt ON sd.kalemTipiId = kt.id " +
           "WHERE sd.belgeId = :belgeId AND sd.siparisDuzeltmeTutar <> 0 " +
           "AND kt.urunGrubuId NOT IN (:excludeGrupIds)")
    List<SiparisDetay> findTdkKalemler(@Param("belgeId") Long belgeId,
                                        @Param("excludeGrupIds") List<Long> excludeGrupIds);

    @Query("SELECT SUM(sd.siparisDuzeltmeTutar * kt.carpan) FROM SiparisDetay sd " +
           "JOIN KalemTipi kt ON sd.kalemTipiId = kt.id " +
           "WHERE sd.belgeId = :belgeId AND sd.tedarikciId IN :tedarikciIds")
    BigDecimal sumTedarikciToplam(@Param("belgeId") Long belgeId,
                                  @Param("tedarikciIds") List<Long> tedarikciIds);

    @Modifying
    @Query("UPDATE SiparisDetay sd SET sd.kampanyaId = :kampanyaId, " +
           "sd.guncellemeZamani = :guncellemeZamani, sd.guncellleyenKullaniciId = :kullaniciId " +
           "WHERE sd.id = :id")
    void updateKampanyaId(@Param("id") Long id,
                          @Param("kampanyaId") Long kampanyaId,
                          @Param("guncellemeZamani") LocalDateTime guncellemeZamani,
                          @Param("kullaniciId") Long kullaniciId);
}
