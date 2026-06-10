package com.acme.ord.domain.repository;

import com.acme.ord.domain.entity.SiparisDetayKayit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SiparisDetayKayitRepository extends JpaRepository<SiparisDetayKayit, Long> {

    Optional<SiparisDetayKayit> findBySiparisId(Long siparisId);
}
