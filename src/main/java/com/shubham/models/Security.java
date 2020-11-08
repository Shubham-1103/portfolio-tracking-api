package com.shubham.models;

import com.shubham.dto.SecurityDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(description = "All details about the security")
public class Security {
    private String tickerSymbol;
    @ApiModelProperty(notes = "Average price per ticker")
    private BigDecimal avgBuyPrice;
    @ApiModelProperty(notes = "Total shares per ticker")
    private BigInteger shares;
    @ApiModelProperty(notes = "Total price per ticker")
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
