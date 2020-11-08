package com.shubham.util;

import com.shubham.dto.SecurityDto;
import com.shubham.enums.TradeType;
import com.shubham.models.Security;
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
     * lambda function to check the basic validations on trade.
     * <p>
     * 1. checks share and price value must be greater than 0 iff user is buying.
     * </p>
     * <p>
     * 2. checks user shares must not less than 0 in case of selling the shares.
     * </p>
     *
     * @param trade: {@link Trade} trade that user made.
     * @param security: {@link SecurityDto} current state of security for the user
     * @return {@link TradeValidation} this is a model that contains if the trade made by the user
     * is valid or not and the Response if the trade is not a valid trade.
     */
    public static BiFunction<Trade, Optional<Security>, TradeValidation> validateTrade = (trade, optionalSecurity) -> {
        Assert.notNull(trade.getShares(), "Shares information not submitted");
        Assert.notNull(trade.getPrice(), "Price information  not submitted");
        //basic validations
        if (0 <= BigInteger.ZERO.compareTo(trade.getShares())) {
            return TradeValidation.builder().isValid(false)
                    .responseEntity(ResponseEntity.badRequest().body(Optional.of("Share value must be greater than 0")))
                    .build();
        } else if (0 <= BigDecimal.ZERO.compareTo(trade.getPrice())) {
            return TradeValidation.builder().isValid(false)
                    .responseEntity(ResponseEntity.badRequest().body(Optional.of("Price value must be greater than 0")))
                    .build();
        } else if (TradeType.SELL == trade.getTradeType()) {
            if (!optionalSecurity.isPresent()) {
                return TradeValidation.builder()
                        .isValid(false)
                        .responseEntity(ResponseEntity.badRequest().body(Optional.of("Please buy some shares to make a sell trade with us.")))
                        .build();
            }
            Security security = optionalSecurity.get();
            BigInteger remainingShares = security.getShares().subtract(trade.getShares());
            if (0 < BigInteger.ZERO.compareTo(remainingShares)) {
                return TradeValidation.builder().isValid(false)
                        .responseEntity(ResponseEntity.badRequest().body(Optional.of("You cannot sell more shares than you own")))
                        .build();
            }
        }
        return TradeValidation.builder().isValid(true).build();
    };
    public static Function<BigInteger, TradeValidation> validateShares = (shares) -> {
        if (0 < BigInteger.ZERO.compareTo(shares)) {
            return TradeValidation.builder().isValid(false)
                    .responseEntity(ResponseEntity.badRequest().body(Optional.of("Invalid trade making share count negative")))
                    .build();
        }
        return TradeValidation.builder().isValid(true).build();
    };
    public static Function<BigDecimal, TradeValidation> validatePrice = (price) -> {
        if (0 < BigDecimal.ZERO.compareTo(price)) {
            return TradeValidation.builder().isValid(false)
                    .responseEntity(ResponseEntity.badRequest().body(Optional.of("Invalid trade making price negative")))
                    .build();
        }
        return TradeValidation.builder().isValid(true).build();
    };

    private Util() {

    }

    /**
     * checks for basic validations that after making trade the avg buy price and share should not be less than 0.
     *
     * @param updatedSecurity: {@link Security} current state of security for the user
     * @return {@link TradeValidation} this is a model that contains if the trade made by the user
     * is valid or not and the Response if the trade is not a valid trade.
     */
    public static TradeValidation validateTradePriceAndShares(Security updatedSecurity) {

        TradeValidation priceValidation = validatePrice.apply(updatedSecurity.getTotalPrice());
        TradeValidation shareValidation = validateShares.apply(updatedSecurity.getShares());

        if (priceValidation.isValid() && shareValidation.isValid()) {
            return TradeValidation.builder().isValid(true).build();
        }
        return priceValidation.isValid() ? shareValidation : priceValidation;
    }

    /**
     * Utility method to get {@link SecurityDto} from the {@link Trade} model
     *
     * @param trade: {@link Trade}
     * @return {@link SecurityDto}
     */
    public static SecurityDto getSecurityDtoFromTrade(Trade trade) {
        Security security = Security.builder()
                .tickerSymbol(trade.getTicker())
                .shares(trade.getShares())
                .avgBuyPrice(trade.getPrice())
                .totalPrice(trade.getPrice().multiply(BigDecimal.valueOf(trade.getShares().longValue())))
                .build();
        return Security.getSecurityDto(security);
    }

}