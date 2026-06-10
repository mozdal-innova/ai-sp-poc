package com.acme.ord.domain.repository;

import com.acme.ord.domain.entity.SiparisDetay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SiparisDetayRepository extends JpaRepository<SiparisDetay, Long> {
    List<SiparisDetay> findBySiparisId(Long siparisId);
}
