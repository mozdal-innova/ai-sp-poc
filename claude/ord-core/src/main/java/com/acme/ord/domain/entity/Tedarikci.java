package com.acme.ord.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "tedarikci")
public class Tedarikci {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "tedarikci_tipi_id")
    private Long tedarikciTipiId;
}
