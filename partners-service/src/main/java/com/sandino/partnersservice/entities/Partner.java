package com.sandino.partnersservice.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonMultiPolygon;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

@Data
public class Partner {

    @Id
    private String id;
    private final String tradingName;
    private final String userId;
    private final GeoJsonMultiPolygon coverageArea;
    private final GeoJsonPoint address;
}
