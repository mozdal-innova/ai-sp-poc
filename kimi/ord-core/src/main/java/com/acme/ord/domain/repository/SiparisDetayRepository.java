package com.acme.ord.domain.repository;

import com.acme.ord.domain.entity.SiparisDetay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SiparisDetayRepository extends JpaRepository<SiparisDetay, Long> {

    List<SiparisDetay> findByBelgeId(Long belgeId);

    @Query("SELECT sd FROM SiparisDetay sd WHERE sd.belgeId = :belgeId AND sd.kalemTipiId = :kalemTipiId AND sd.tedarikciId = :tedarikciId " +
           "AND sd.kdvOrani = :kdvOrani AND sd.otvOrani = :otvOrani AND sd.urunSeriNo = :urunSeriNo " +
           "AND (:paketTipiId IS NULL OR sd.paketTipi = :paketTipiId) " +
           "AND (:bayiKodu IS NULL OR sd.bayiKodu = :bayiKodu) " +
           "AND (:kampanyaId IS NULL OR sd.kampanyaId = :kampanyaId) " +
           "AND (:taksitliSatisId IS NULL OR sd.taksitliSatisId = :taksitliSatisId) " +
           "AND (:taksitNo IS NULL OR sd.taksitNo = :taksitNo)")
    Optional<SiparisDetay> findMatchingDetail(@Param("belgeId") Long belgeId,
                                              @Param("kalemTipiId") Long kalemTipiId,
                                              @Param("tedarikciId") Long tedarikciId,
                                              @Param("kdvOrani") java.math.BigDecimal kdvOrani,
                                              @Param("otvOrani") java.math.BigDecimal otvOrani,
                                              @Param("urunSeriNo") String urunSeriNo,
                                              @Param("paketTipiId") Long paketTipiId,
                                              @Param("bayiKodu") Long bayiKodu,
                                              @Param("kampanyaId") Long kampanyaId,
                                              @Param("taksitliSatisId") Long taksitliSatisId,
                                              @Param("taksitNo") Integer taksitNo);

}
