package com.acme.ord.domain.repository;

import com.acme.ord.domain.entity.Siparis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SiparisRepository extends JpaRepository<Siparis, Long> {
}
