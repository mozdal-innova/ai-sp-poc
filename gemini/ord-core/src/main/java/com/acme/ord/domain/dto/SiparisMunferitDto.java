package com.acme.ord.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SiparisMunferitDto {
    private Long hareketId;
    private Long tahsilatId;
    private List<SiparisKalemDetayiDto> tMunferitList;
}
