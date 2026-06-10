package com.acme.ord.domain.repository;

import com.acme.ord.domain.entity.Siparis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SiparisRepository extends JpaRepository<Siparis, Long> {

    Optional<Siparis> findByBelgeId(Long belgeId);

    List<Siparis> findBySiparisSahibiId(Long siparisSahibiId);

}
