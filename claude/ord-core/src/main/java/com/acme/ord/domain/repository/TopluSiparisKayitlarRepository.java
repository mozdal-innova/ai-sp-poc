package com.acme.ord.domain.repository;

import com.acme.ord.domain.entity.TopluSiparisKayitlar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TopluSiparisKayitlarRepository extends JpaRepository<TopluSiparisKayitlar, Long> {

    List<TopluSiparisKayitlar> findByTopluSiparisId(Long topluSiparisId);
}
