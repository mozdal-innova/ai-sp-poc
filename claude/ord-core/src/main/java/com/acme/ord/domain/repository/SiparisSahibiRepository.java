package com.acme.ord.domain.repository;

import com.acme.ord.domain.entity.SiparisSahibi;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SiparisSahibiRepository extends JpaRepository<SiparisSahibi, Long> {

    Optional<SiparisSahibi> findByHesapIdAndMusteriId(Long hesapId, Long musteriId);
}
