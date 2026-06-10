package com.acme.ord.domain.repository;

import com.acme.ord.domain.entity.TeslimatTalep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeslimatTalepRepository extends JpaRepository<TeslimatTalep, Long> {

    Optional<TeslimatTalep> findByTalepNo(String talepNo);

}
