package com.shubham.dto;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;


@Getter
@Builder
@Entity
@Table(name = "securities")
@NoArgsConstructor
@AllArgsConstructor
public class SecurityDto {
    @Id
    @Column(name = "ticker_symbol")
    private String tickerSymbol;
    @Column(name = "average_buy_price")
    private BigDecimal avgBuyPrice;
    @Column(name = "shares")
    private BigInteger shares;
    @Column(name = "last_updated")
    private Instant lastUpdated;
    @Column(name = "total_price")
    private BigDecimal totalPrice;
}
