package com.acme.ord.domain.repository;

import com.acme.ord.domain.entity.Abone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AboneRepository extends JpaRepository<Abone, Long> {

    Optional<Abone> findFirstByHesapId(Long hesapId);
}
