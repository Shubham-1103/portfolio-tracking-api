package com.shubham.models;


import com.shubham.dto.TradeDto;
import com.shubham.enums.TradeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Trade {
    private String ticker;
    @Enumerated(value = EnumType.STRING)
    private TradeType tradeType;
    private BigDecimal price;
    private BigInteger shares;

    public static TradeDto getTradeDto(Trade trade) {
        return TradeDto.builder()
                .price(trade.getPrice())
                .tradeType(trade.getTradeType())
                .ticker(trade.getTicker())
                .shares(trade.getShares())
                .lastUpdated(Instant.now())
                .build();
    }
    public static Trade getTradeModel(TradeDto trade){
        return Trade.builder()
                .price(trade.getPrice())
                .tradeType(trade.getTradeType())
                .ticker(trade.getTicker())
                .shares(trade.getShares())
                .build();
    }
}

