package com.shubham.dto;

import com.shubham.enums.TradeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;

@Data
@Entity
@Builder
@Table(name = "trade_info")
@NoArgsConstructor
@AllArgsConstructor
public class TradeDto {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "trade_id")
    private Long tradeId;
    @Column(name = "ticker")
    private String ticker;
    @Column(name = "trade_type")
    @Enumerated(value = EnumType.STRING)
    private TradeType tradeType;
    @Column(name = "last_updated")
    private Instant lastUpdated;
    @Column(name = "price")
    private BigDecimal price;
    @Column(name = "shares")
    private BigInteger shares;
}
