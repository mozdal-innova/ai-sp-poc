package com.acme.ord.domain.repository;

import com.acme.ord.domain.entity.SiparisDetayKayit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SiparisDetayKayitRepository extends JpaRepository<SiparisDetayKayit, Long> {

    Optional<SiparisDetayKayit> findBySiparisId(Long siparisId);

    boolean existsBySiparisBelgeNoAndIdNot(String siparisBelgeNo, Long id);

}
