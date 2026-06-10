package com.acme.ord.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "siparis_hareket_tahsilat_bilgi")
public class SiparisHareketTahsilatBilgi {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "siparis_hareket_id")
    private Long siparisHareketId;

    @Column(name = "siparis_detay_id")
    private Long siparisDetayId;

    @Column(name = "siparis_tahsilat_bilgi_tipi_id")
    private Long siparisTahsilatBilgiTipiId;

    @Column(name = "alacak_id")
    private Long alacakId;

    @Column(name = "tutar", precision = 24, scale = 6)
    private BigDecimal tutar;

    @Column(name = "tanitim_zamani")
    private LocalDateTime tanitimZamani;

    @Column(name = "guncelleme_zamani")
    private LocalDateTime guncellemeZamani;
}
