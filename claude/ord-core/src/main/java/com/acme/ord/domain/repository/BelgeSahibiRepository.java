package com.acme.ord.domain.repository;

import com.acme.ord.domain.entity.BelgeSahibi;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BelgeSahibiRepository extends JpaRepository<BelgeSahibi, Long> {

    long countByBelgeIdAndHesapId(Long belgeId, Long hesapId);
}
