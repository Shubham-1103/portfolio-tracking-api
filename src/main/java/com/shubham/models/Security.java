package com.shubham.models;

import com.shubham.dto.SecurityDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Security {
    private String tickerSymbol;
    private BigDecimal avgBuyPrice;
    private BigInteger shares;
    private BigDecimal totalPrice;

    public static SecurityDto getSecurityDto(Security security) {
        return SecurityDto.builder()
                .avgBuyPrice(security.getAvgBuyPrice())
                .shares(security.getShares())
                .tickerSymbol(security.getTickerSymbol())
                .totalPrice(security.getTotalPrice())
                .lastUpdated(Instant.now())
                .build();
    }

    public static Security getSecurityModel(SecurityDto securityDto) {
        return Security.builder()
                .avgBuyPrice(securityDto.getAvgBuyPrice())
                .shares(securityDto.getShares())
                .tickerSymbol(securityDto.getTickerSymbol())
                .totalPrice(securityDto.getTotalPrice())
                .build();
    }
}
