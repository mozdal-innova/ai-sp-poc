package com.acme.ord.domain.repository;

import com.acme.ord.domain.entity.Hesap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HesapRepository extends JpaRepository<Hesap, Long> {

    Optional<Hesap> findFirstByHesapNo(String hesapNo);
}
