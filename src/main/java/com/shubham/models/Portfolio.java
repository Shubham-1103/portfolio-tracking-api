package com.shubham.models;

import com.shubham.dto.PortfolioDto;
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
public class Portfolio {
    private String tickerSymbol;
    private BigDecimal avgBuyPrice;
    private BigInteger shares;

    public static PortfolioDto getPortfolioDto(Portfolio portfolio) {
        return PortfolioDto.builder()
                .avgBuyPrice(portfolio.getAvgBuyPrice())
                .shares(portfolio.getShares())
                .tickerSymbol(portfolio.getTickerSymbol())
                .build();
    }

    public static Portfolio getPortfolioModel(PortfolioDto portfolio) {
        return Portfolio.builder()
                .avgBuyPrice(portfolio.getAvgBuyPrice())
                .shares(portfolio.getShares())
                .tickerSymbol(portfolio.getTickerSymbol())
                .build();
    }
}
