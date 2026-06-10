package com.acme.ord.domain.repository;

import com.acme.ord.domain.entity.SiparisHareketTahsilatBilgi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SiparisHareketTahsilatBilgiRepository extends JpaRepository<SiparisHareketTahsilatBilgi, Long> {

    @Query("SELECT COUNT(shtb) FROM SiparisHareketTahsilatBilgi shtb " +
           "WHERE shtb.siparisHareketId IN :hareketIds " +
           "AND shtb.siparisTahsilatBilgiTipiId = :tipiId " +
           "AND shtb.siparisDetayId IN :siparisDetayIds")
    long countByHareketIdsAndTipiAndDetayIds(
            @Param("hareketIds") List<Long> hareketIds,
            @Param("tipiId") Long tipiId,
            @Param("siparisDetayIds") List<Long> siparisDetayIds);
}
