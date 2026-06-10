package com.acme.ord.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "teslimat_talep")
public class TeslimatTalep {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "talep_no")
    private String talepNo;

    @Column(name = "hesap_id")
    private Long hesapId;

    @Column(name = "talep_durum_id")
    private Long talepDurumId;

    @Column(name = "guncelleme_zamani")
    private LocalDateTime guncellemeZamani;

    @Column(name = "guncelleyen_kullanici_id")
    private Long guncellleyenKullaniciId;
}
