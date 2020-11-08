package com.shubham.controller;


import com.shubham.models.Security;
import com.shubham.models.Trade;
import com.shubham.models.TradeValidation;
import com.shubham.service.PortfolioTrackerService;
import io.swagger.annotations.ApiOperation;
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

    @ApiOperation(value = "Insert/update the trade and security of the user",
            notes = "This is the core api which is used to update and create trades for the user." +
                    "The trade RequestBody is validated and then trade and securities of the user is updated. ")
    @PostMapping("trade")
    public ResponseEntity<?> insertOrUpdateTrade(@RequestBody Trade trade) {
        log.info("User requested to make a trade");
        Optional<Security> optionalSecurity = portfolioTrackerService.getSecurityByTicker(trade.getTicker());
        TradeValidation tradeValidation = validateTrade.apply(trade, optionalSecurity);
        if (!tradeValidation.isValid()) {
            return tradeValidation.getResponseEntity();
        }
        if (!optionalSecurity.isPresent()) {
            return portfolioTrackerService.insertToTradeAndSecurity(trade).getResponseEntity();
        }
        return portfolioTrackerService.insertToTradeAndUpdateSecurity(trade, optionalSecurity.get()).getResponseEntity();
    }

    @ApiOperation(value = "fetches Portfolio for of the user",
            notes = "API that fetches all the securities of the user if the portfolio details not found returns 404")
    @GetMapping("portfolio")
    public ResponseEntity<?> getPortfolio() {
        log.info("User requested the portfolio details");
        List<Security> securities = portfolioTrackerService.getPortfolioOfUser();
        return securities.isEmpty()
                ? new ResponseEntity<>("No securities found. Please make a trade first!", HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(securities, HttpStatus.OK);
    }

    @ApiOperation(value = "fetches the security by ticket",
            notes = "Fetches the Security bu the ticker provided in path variable if no security found returns 404")
    @GetMapping("security/{ticker}")
    public ResponseEntity<?> getSecurity(@PathVariable String ticker) {
        log.info("User requested for security info for ticker : {}", ticker);
        Optional<Security> optionalSecurity = portfolioTrackerService.getSecurityByTicker(ticker);
        if (!optionalSecurity.isPresent()) {
            return new ResponseEntity<>(String.format("No securities found with the ticker : %s", ticker), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(optionalSecurity.get(), HttpStatus.OK);
    }

    @ApiOperation(value = "gets all the trade for the user",
            notes = "fetches all of the trades details of the user if no trade found returns 404")
    @GetMapping("trades")
    public ResponseEntity<?> getAllTrades() {
        log.info("User Requested to fetch all the trade information");
        List<Trade> trades = portfolioTrackerService.getAllTrades();
        return trades.isEmpty()
                ? new ResponseEntity<>("No trade found!", HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(trades, HttpStatus.OK);
    }

    @ApiOperation(value = "get trade info using trade id",
            notes = "fetches the trade information using trade id provided in the path params if not trade is found with the provided id returns 404")
    @GetMapping("trade/{id}")
    public ResponseEntity<?> getTradeById(@PathVariable String id) {
        log.info("User Requested to fetch trade with Id : {}", id);
        Optional<Trade> optionalTrade;
        try {
            optionalTrade = portfolioTrackerService.getTradeById(Long.parseLong(id));
        } catch (NumberFormatException exception) {
            log.error("Trade id should be in digits");
            return ResponseEntity.badRequest().body(Optional.of("Please enter a trade Id in valid format"));
        }
        if (!optionalTrade.isPresent()) {
            return new ResponseEntity<>(String.format("No trade found with the trade Id : %s", id), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(optionalTrade.get(), HttpStatus.OK);
    }

    @ApiOperation(value = "Return the portfolio of the user",
            notes = "This API basically returns all the securities for the user if no security found it returns 404")
    @GetMapping("returns")
    public ResponseEntity<?> getReturns() {
        log.info("User Requested for returns calculation");
        Optional<BigDecimal> optionalBigDecimal = portfolioTrackerService.getReturns();
        if (!optionalBigDecimal.isPresent()) {
            return new ResponseEntity<>("No securities found. Please make a trade first!", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(optionalBigDecimal.get(), HttpStatus.OK);
    }

    @ApiOperation(value = "Deletes a trade corresponding to trade id", notes = "this api removes the specified trade and updates the portfolio details " +
            "of the user correspond to the ticker")
    @DeleteMapping("trade/{id}")
    public ResponseEntity<?> deleteTrade(@PathVariable String id) {
        log.info("User Requested for deletion of trade with trade id: {}", id);
        TradeValidation tradeValidation;
        try {
            tradeValidation = portfolioTrackerService.deleteTrade(Long.parseLong(id));
        } catch (NumberFormatException exception) {
            log.error("Trade id should be in digits");
            return ResponseEntity.badRequest().body(Optional.of("Please enter a trade Id in valid format"));
        }
        return tradeValidation.getResponseEntity();
    }


}
