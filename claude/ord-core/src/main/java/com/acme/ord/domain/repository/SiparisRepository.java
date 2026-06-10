package com.acme.ord.domain.repository;

import com.acme.ord.domain.entity.Siparis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SiparisRepository extends JpaRepository<Siparis, Long> {

    @Query("SELECT s FROM Siparis s JOIN SiparisDurum sd ON s.siparisDurumId = sd.id " +
           "WHERE s.siparisSahibiId = :siparisSahibiId AND s.belgeId = :belgeId " +
           "AND sd.islemDurumu IN (:islemDurumlari)")
    Optional<Siparis> findBySiparisSahibiIdAndBelgeIdAndIslemDurumu(
            @Param("siparisSahibiId") Long siparisSahibiId,
            @Param("belgeId") Long belgeId,
            @Param("islemDurumlari") List<Integer> islemDurumlari);

    @Query("SELECT s FROM Siparis s WHERE s.belgeId = :belgeId " +
           "AND s.siparisTipiId IN (:siparisTipleri) " +
           "AND s.siparisDurumId IN (:siparisDurumlari)")
    List<Siparis> findByBelgeIdAndTipAndDurum(
            @Param("belgeId") Long belgeId,
            @Param("siparisTipleri") List<Long> siparisTipleri,
            @Param("siparisDurumlari") List<Long> siparisDurumlari);
}
