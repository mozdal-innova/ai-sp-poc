package com.acme.ord.domain.repository;

import com.acme.ord.domain.entity.HesapTipi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HesapTipiRepository extends JpaRepository<HesapTipi, Long> {
}
