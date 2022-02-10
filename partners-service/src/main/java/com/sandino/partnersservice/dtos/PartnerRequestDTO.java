package com.sandino.partnersservice.dtos;

import com.sandino.partnersservice.entities.Partner;
import lombok.Data;
import org.springframework.data.mongodb.core.geo.GeoJsonMultiPolygon;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
public class PartnerRequestDTO {
    private final String tradingName;
    private final String userId;
    private final double[][][][] coverageArea;
    private final double[] address;

    public Partner toPartner() {
        return new Partner(
                tradingName,
                userId,
                new GeoJsonMultiPolygon(Arrays.stream(coverageArea).flatMap(Stream::of)
                        .map(polygon -> new GeoJsonPolygon(Arrays.stream(polygon)
                                .map(point -> new GeoJsonPoint(point[0], point[1]))
                                .collect(Collectors.toList())))
                        .collect(Collectors.toList())),
                new GeoJsonPoint(address[0], address[1])
        );
    }
}
