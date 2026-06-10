package com.acme.ord.domain.repository;

import com.acme.ord.domain.entity.KalemTipi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KalemTipiRepository extends JpaRepository<KalemTipi, Long> {
}
