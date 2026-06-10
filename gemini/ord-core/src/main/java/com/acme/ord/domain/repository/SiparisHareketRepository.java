package com.acme.ord.domain.repository;

import com.acme.ord.domain.entity.SiparisHareket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SiparisHareketRepository extends JpaRepository<SiparisHareket, Long> {
    List<SiparisHareket> findBySiparisId(Long siparisId);
}
