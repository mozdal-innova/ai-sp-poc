package com.acme.ord.domain.repository;

import com.acme.ord.domain.entity.SiparisSahibiFinans;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SiparisSahibiFinansRepository extends JpaRepository<SiparisSahibiFinans, Long> {

    Optional<SiparisSahibiFinans> findBySiparisSahibiIdAndFinansTipi(Long siparisSahibiId, Long finansTipi);

}
