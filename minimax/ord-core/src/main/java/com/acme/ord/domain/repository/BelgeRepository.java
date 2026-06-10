package com.acme.ord.domain.repository;

import com.acme.ord.domain.entity.Belge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BelgeRepository extends JpaRepository<Belge, Long> {
    Optional<Belge> findByHesapIdAndBelgeNoAndDonemKodu(Long hesapId, java.math.BigDecimal belgeNo, java.math.BigDecimal donemKodu);
}
