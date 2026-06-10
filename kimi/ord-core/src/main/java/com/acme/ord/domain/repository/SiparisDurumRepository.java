package com.acme.ord.domain.repository;

import com.acme.ord.domain.entity.SiparisDurum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SiparisDurumRepository extends JpaRepository<SiparisDurum, Long> {
}
