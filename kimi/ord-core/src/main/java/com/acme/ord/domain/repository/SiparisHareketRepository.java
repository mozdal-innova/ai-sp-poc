package com.acme.ord.domain.repository;

import com.acme.ord.domain.entity.SiparisHareket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SiparisHareketRepository extends JpaRepository<SiparisHareket, Long> {

    List<SiparisHareket> findBySiparisId(Long siparisId);

    List<SiparisHareket> findByUstId(Long ustId);

    Optional<SiparisHareket> findByIslemTarihiAndKurumIdAndStanAndIslemId(LocalDate islemTarihi, Long kurumId, Integer stan, Long islemId);

    List<SiparisHareket> findByBelgeIdAndIslemIdInAndIslemDurumIdNotAndUstIdIsNull(Long belgeId, List<Long> islemIds, String islemDurumId);

    long countByUstIdAndIslemId(Long ustId, Long islemId);

}
