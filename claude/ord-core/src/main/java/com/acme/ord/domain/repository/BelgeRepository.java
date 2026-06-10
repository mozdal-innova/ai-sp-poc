package com.acme.ord.domain.repository;

import com.acme.ord.domain.entity.Belge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

public interface BelgeRepository extends JpaRepository<Belge, Long> {

    @Query("SELECT b FROM Belge b WHERE b.hesapId = :hesapId AND b.belgeNo = :belgeNo AND b.donemKodu = :donemKodu")
    Optional<Belge> findByHesapIdAndBelgeNoAndDonemKodu(
            @Param("hesapId") Long hesapId,
            @Param("belgeNo") Long belgeNo,
            @Param("donemKodu") Long donemKodu);

    @Modifying
    @Query("UPDATE Belge b SET b.siparisDuzeltmeTutar = b.siparisDuzeltmeTutar - :tutar, " +
           "b.kalanTutar = b.kalanTutar - :tutar, " +
           "b.belgeDurum = :belgeDurum, " +
           "b.guncellemeZaman = :guncellemeZaman, " +
           "b.guncellleyenKullanici = :kullaniciId " +
           "WHERE b.id = :belgeId")
    void updateSiparisTutar(@Param("belgeId") Long belgeId,
                            @Param("tutar") BigDecimal tutar,
                            @Param("belgeDurum") String belgeDurum,
                            @Param("guncellemeZaman") LocalDateTime guncellemeZaman,
                            @Param("kullaniciId") Long kullaniciId);
}
