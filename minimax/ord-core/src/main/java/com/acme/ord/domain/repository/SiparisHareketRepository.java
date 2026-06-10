package com.acme.ord.domain.repository;

import com.acme.ord.domain.entity.SiparisHareket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SiparisHareketRepository extends JpaRepository<SiparisHareket, Long> {
    Optional<SiparisHareket> findByIslemTarihiAndKurumIdAndStanAndIslemId(
            LocalDate islemTarihi, Long kurumId, Long stan, Long islemId);

    List<SiparisHareket> findBySiparisId(Long siparisId);

    Long countBySiparisId(Long siparisId);
}
