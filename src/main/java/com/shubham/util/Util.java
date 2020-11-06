package com.shubham.util;

import com.shubham.dto.PortfolioDto;
import com.shubham.enums.TradeType;
import com.shubham.models.Trade;
import com.shubham.models.TradeValidation;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Util {

    /**
     * lambda function to check the basic validations on trade
     */
    public static BiFunction<Trade, Optional<PortfolioDto>, TradeValidation> validateTrade = (trade, optionalPortfolio) -> {
        Assert.notNull(trade.getShares(), "Shares information not submitted");
        Assert.notNull(trade.getPrice(), "Price information  not submitted");
        //basic validations
        if (0 <= BigInteger.ZERO.compareTo(trade.getShares())) {
            return TradeValidation.builder().isValid(false)
                    .responseEntity(ResponseEntity.of(Optional.of("Share value must be greater than 0")))
                    .build();
        } else if (0 <= BigDecimal.ZERO.compareTo(trade.getPrice())) {
            return TradeValidation.builder().isValid(false)
                    .responseEntity(ResponseEntity.of(Optional.of("Price value must be greater than 0")))
                    .build();
        } else if (TradeType.SELL == trade.getTradeType()) {
            if (!optionalPortfolio.isPresent()) {
                return TradeValidation.builder()
                        .isValid(false)
                        .responseEntity(ResponseEntity.of(Optional.of("Please buy some shares to make a sell trade with us.")))
                        .build();
            }
            PortfolioDto portfolio = optionalPortfolio.get();
            BigInteger remainingShares = portfolio.getShares().subtract(trade.getShares());
            if (0 <= BigInteger.ZERO.compareTo(remainingShares)) {
                return TradeValidation.builder().isValid(false)
                        .responseEntity(ResponseEntity.of(Optional.of("You cannot sell more shares than you own")))
                        .build();
            }
        }
        return TradeValidation.builder().isValid(true).build();
    };

    public static Function<BigInteger, TradeValidation> validateShares = (shares) -> {
        if (0 <= BigInteger.ZERO.compareTo(shares)) {
            return TradeValidation.builder().isValid(false)
                    .responseEntity(ResponseEntity.of(Optional.of("Invalid trade making share count to negative")))
                    .build();
        }
        return TradeValidation.builder().isValid(true).build();
    };
    public static Function<BigDecimal, TradeValidation> validatePrice = (price) -> {
        if (0 <= BigDecimal.ZERO.compareTo(price)) {
            return TradeValidation.builder().isValid(false)
                    .responseEntity(ResponseEntity.of(Optional.of("Invalid trade making price to negative")))
                    .build();
        }
        return TradeValidation.builder().isValid(true).build();
    };

}