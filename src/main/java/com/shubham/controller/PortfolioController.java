package com.shubham.controller;


import com.shubham.dto.SecurityDto;
import com.shubham.dto.TradeDto;
import com.shubham.models.Security;
import com.shubham.models.Trade;
import com.shubham.models.TradeValidation;
import com.shubham.service.PortfolioTrackerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static com.shubham.util.Util.validateTrade;

@Slf4j
@RestController
public class PortfolioController {
    @Autowired
    private PortfolioTrackerService portfolioTrackerService;

    @PostMapping("trade")
    public ResponseEntity<?> insertOrUpdateTrade(@RequestBody Trade trade) {
        log.info("User requested to make a trade");
        Optional<SecurityDto> optionalSecurityDto = portfolioTrackerService.getSecurityByTicker(trade.getTicker());
        TradeValidation tradeValidation = validateTrade.apply(trade, optionalSecurityDto);
        if (!tradeValidation.isValid()) {
            return tradeValidation.getResponseEntity();
        }
        if (!optionalSecurityDto.isPresent()) {
            return portfolioTrackerService.insertToTradeAndSecurity(trade).getResponseEntity();
        }
        return portfolioTrackerService.insertToTradeAndUpdateSecurity(trade, optionalSecurityDto.get()).getResponseEntity();
    }

    @GetMapping("portfolio")
    public ResponseEntity<?> getPortfolio() {
        log.info("User requested the portfolio details");
        List<Security> securities = portfolioTrackerService.getPortfolioOfUser();
        return securities.isEmpty()
                ? ResponseEntity.of(Optional.of("No securities found. Please make a trade first!"))
                : new ResponseEntity<>(securities, HttpStatus.OK);
    }

    @GetMapping("security/{ticker}")
    public ResponseEntity<?> getSecurity(@PathVariable String ticker) {
        log.info("User requested for security info for ticker : {}", ticker);
        Optional<SecurityDto> optionalSecurity = portfolioTrackerService.getSecurityByTicker(ticker);
        if (!optionalSecurity.isPresent()) {
            return ResponseEntity.of(Optional.of(String.format("No securities found with the ticker : %s", ticker)));
        }
        Security security = Security.getSecurityModel(optionalSecurity.get());
        return new ResponseEntity<>(security, HttpStatus.OK);
    }

    @GetMapping("trades")
    public ResponseEntity<?> getAllTrades() {
        log.info("User Requested to fetch all the trade information");
        List<Trade> trades = portfolioTrackerService.getAllTrades();
        return trades.isEmpty()
                ? ResponseEntity.of(Optional.of("No trade found!"))
                : new ResponseEntity<>(trades, HttpStatus.OK);
    }

    @GetMapping("trade/{id}")
    public ResponseEntity<?> getTradesById(@PathVariable String id) {
        log.info("User Requested to fetch trade with Id : {}", id);
        Optional<TradeDto> tradeDto;
        try {
            tradeDto = portfolioTrackerService.getTradeById(Long.parseLong(id));
        } catch (NumberFormatException exception) {
            log.error("Trade id should be in digits");
            return ResponseEntity.of(Optional.of("Please enter a trade Id in valid format"));
        }
        if (!tradeDto.isPresent()) {
            return ResponseEntity.of(Optional.of(String.format("No trade found with the trade Id : %s", id)));
        }
        Trade trade = Trade.getTradeModel(tradeDto.get());
        return new ResponseEntity<>(trade, HttpStatus.OK);
    }

    @GetMapping("returns")
    public ResponseEntity<?> getReturns() {
        log.info("User Requested for returns calculation");
        Optional<BigDecimal> optionalBigDecimal = portfolioTrackerService.getReturns();
        if (!optionalBigDecimal.isPresent()) {
            return ResponseEntity.of(Optional.of("No securities found. Please make a trade first!"));
        }
        return new ResponseEntity<>(optionalBigDecimal.get(), HttpStatus.OK);
    }


}
