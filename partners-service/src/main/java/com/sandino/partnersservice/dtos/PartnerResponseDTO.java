package com.sandino.partnersservice.dtos;

import com.sandino.partnersservice.entities.Partner;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.geo.GeoJsonMultiPolygon;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

@Data
@NoArgsConstructor
public class PartnerResponseDTO {
    private String id;
    private String tradingName;
    private PersonResponseDTO person;
    private GeoJsonMultiPolygon coverageArea;
    private GeoJsonPoint address;

    public PartnerResponseDTO(Partner partner, PersonResponseDTO person) {
        this.id = partner.getId();
        this.tradingName = partner.getTradingName();
        this.person = person;
        this.coverageArea = partner.getCoverageArea();
        this.address = partner.getAddress();
    }
}
