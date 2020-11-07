package com.shubham.models;

import com.shubham.dto.SecurityDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Security {
    private String tickerSymbol;
    private BigDecimal avgBuyPrice;
    private BigInteger shares;

    public static SecurityDto getSecurityDto(Security security) {
        return SecurityDto.builder()
                .avgBuyPrice(security.getAvgBuyPrice())
                .shares(security.getShares())
                .tickerSymbol(security.getTickerSymbol())
                .build();
    }

    public static Security getSecurityModel(SecurityDto securityDto) {
        return Security.builder()
                .avgBuyPrice(securityDto.getAvgBuyPrice())
                .shares(securityDto.getShares())
                .tickerSymbol(securityDto.getTickerSymbol())
                .build();
    }
}
