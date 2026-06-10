package com.acme.ord.domain.repository;

import com.acme.ord.domain.entity.Hesap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HesapRepository extends JpaRepository<Hesap, Long> {
    Optional<Hesap> findByHesapNo(String hesapNo);
}
