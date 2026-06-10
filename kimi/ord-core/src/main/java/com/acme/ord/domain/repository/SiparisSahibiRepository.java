package com.acme.ord.domain.repository;

import com.acme.ord.domain.entity.SiparisSahibi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SiparisSahibiRepository extends JpaRepository<SiparisSahibi, Long> {

    Optional<SiparisSahibi> findByHesapIdAndMusteriId(Long hesapId, Long musteriId);

    List<SiparisSahibi> findBySirketIdAndKalanAlacakTutariGreaterThan(Long sirketId, java.math.BigDecimal kalan);

}
