package com.acme.ord.domain.repository;

import com.acme.ord.domain.entity.SiparisSahibiFinans;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SiparisSahibiFinansRepository extends JpaRepository<SiparisSahibiFinans, Long> {

    Optional<SiparisSahibiFinans> findBySiparisSahibiIdAndSiparisSahibiFinansTipiId(
            Long siparisSahibiId, Long siparisSahibiFinansTipiId);
}
