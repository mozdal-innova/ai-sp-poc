package com.acme.ord.domain.repository;

import com.acme.ord.domain.entity.TeslimatTalep;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeslimatTalepRepository extends JpaRepository<TeslimatTalep, Long> {

    Optional<TeslimatTalep> findByTalepNo(String talepNo);
}
