package com.acme.ord.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "teslimat_talep")
@Getter
@Setter
@NoArgsConstructor
public class TeslimatTalep {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_teslimat_talep")
    @SequenceGenerator(name = "seq_teslimat_talep", sequenceName = "seq_teslimat_talep", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "talep_no")
    private String talepNo;

    @Column(name = "hesap_id")
    private Long hesapId;

    @Column(name = "talep_durum_id")
    private Long talepDurumId;

    @Column(name = "guncelleme_zamani")
    private Instant guncellemeZamani;

    @Column(name = "guncelleyen_kullanici_id")
    private Long guncelleyenKullaniciId;

}
