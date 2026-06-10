package com.acme.ord.domain.repository;

import com.acme.ord.domain.entity.SiparisHareketIslemSdy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SiparisHareketIslemSdyRepository extends JpaRepository<SiparisHareketIslemSdy, Long> {

    List<SiparisHareketIslemSdy> findBySiparisHareketId(Long siparisHareketId);

}
