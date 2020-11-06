package com.shubham.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.math.BigInteger;

@Data
@Builder
@Entity
@Table(name = "portfolio")
@NoArgsConstructor
@AllArgsConstructor
public class PortfolioDto {
    @Id
    @Column(name = "ticker_symbol")
    private String tickerSymbol;
    @Column(name = "average_buy_price")
    private BigDecimal avgBuyPrice;
    @Column(name = "shares")
    private BigInteger shares;
}
