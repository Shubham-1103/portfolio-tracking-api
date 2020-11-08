package com.shubham.models;


import com.shubham.dto.TradeDto;
import com.shubham.enums.TradeType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(description = "All details about the Trade")
public class Trade {
    @ApiModelProperty(notes = "A unique id automatically created when there is insertion")
    private Long tradeId;
    @ApiModelProperty(notes = "Ticker for which the trade was executed")
    private String ticker;
    @ApiModelProperty(notes = "tradeType can only be BUY/SELL")
    @Enumerated(value = EnumType.STRING)
    private TradeType tradeType;
    @ApiModelProperty(notes = "Price of a share. It should be greater than 0")
    private BigDecimal price;
    @ApiModelProperty(notes = "Amount of shares. It should be greater than 0")
    private BigInteger shares;

    public static TradeDto getTradeDto(Trade trade) {
        return TradeDto.builder()
                .tradeId(trade.getTradeId())
                .price(trade.getPrice())
                .tradeType(trade.getTradeType())
                .ticker(trade.getTicker())
                .shares(trade.getShares())
                .lastUpdated(Instant.now())
                .build();
    }

    public static Trade getTradeModel(TradeDto trade) {
        return Trade.builder()
                .tradeId(trade.getTradeId())
                .price(trade.getPrice())
                .tradeType(trade.getTradeType())
                .ticker(trade.getTicker())
                .shares(trade.getShares())
                .build();
    }
}

