package com.shubham.service;

import com.shubham.dto.PortfolioDto;
import com.shubham.dto.TradeDto;
import com.shubham.enums.TradeType;
import com.shubham.models.Portfolio;
import com.shubham.models.Trade;
import com.shubham.models.TradeValidation;
import com.shubham.repo.PortfolioRepo;
import com.shubham.repo.TradeRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.shubham.util.Util.validatePrice;
import static com.shubham.util.Util.validateShares;

@Slf4j
@Service
public class PortfolioService {
    @Autowired
    private PortfolioRepo portfolioRepo;
    @Autowired
    private TradeRepo tradeRepo;

    public Optional<PortfolioDto> getSecurityByTicker(String ticker) {
        return portfolioRepo.findById(ticker);
    }

    public Optional<BigDecimal> getReturns() {
        return portfolioRepo.findAll().stream()
                .map(this::calculateReturn)
                .reduce(BigDecimal::add);
    }

    private BigDecimal calculateReturn(PortfolioDto portfolioDto) {
        return BigDecimal.valueOf(100) // Default current price value
                .subtract(portfolioDto.getAvgBuyPrice())
                .multiply(BigDecimal.valueOf(portfolioDto.getShares().longValue()));
    }

    public Optional<TradeDto> getTradeById(Long id) {
        return tradeRepo.findById(id);
    }

    public List<Trade> getAllTrades() {
        return tradeRepo.findAll().stream()
                .map(Trade::getTradeModel)
                .collect(Collectors.toList());
    }

    public List<Portfolio> getPortfolioOfUser() {
        return portfolioRepo.findAll()
                .stream()
                .map(Portfolio::getPortfolioModel)
                .collect(Collectors.toList());
    }

    //first valid trade.
    public TradeValidation insertToTradeAndPortfolio(Trade trade) {
        PortfolioDto portfolioDto = getPortfolioDto(trade);
        TradeDto tradeDto = Trade.getTradeDto(trade);
        tradeRepo.save(tradeDto);
        portfolioRepo.save(portfolioDto);
        return TradeValidation.builder().isValid(true)
                .responseEntity(new ResponseEntity<>(trade, HttpStatus.OK))
                .build();
    }


    private PortfolioDto getPortfolioDto(Trade trade) {
        Portfolio portfolio = Portfolio.builder()
                .tickerSymbol(trade.getTicker())
                .shares(trade.getShares())
                .avgBuyPrice(trade.getPrice())
                .build();
        return Portfolio.getPortfolioDto(portfolio);
    }

    public TradeValidation updateTradeAndPortfolio(Trade trade, PortfolioDto portfolio) {
        TradeDto tradeDto = Trade.getTradeDto(trade);
        PortfolioDto portfolioDto = getFinalPortfolio(trade, portfolio);
        TradeValidation tradeValidation = validatePrice.apply(portfolioDto.getAvgBuyPrice()).isValid() ?
                validateShares.apply(portfolio.getShares()).isValid() ? TradeValidation.builder().isValid(true).build()
                        : validateShares.apply(portfolio.getShares()) : validatePrice.apply(portfolioDto.getAvgBuyPrice());
        if (tradeValidation.isValid()) {
            tradeRepo.save(tradeDto);
            portfolioRepo.save(portfolioDto);
        }
        tradeValidation.setResponseEntity(new ResponseEntity<>(trade, HttpStatus.OK));
        return tradeValidation;
    }

    private PortfolioDto getFinalPortfolio(Trade trade, PortfolioDto oldPortfolioDetails) {
        Portfolio newPortfolio = Portfolio.builder().tickerSymbol(trade.getTicker()).build();
        // in case of Selling we need not to update the price only shares will be updated
        // remember basic validation on share is already applied that we can not sell more share than we own.
        if (trade.getTradeType() == TradeType.SELL) {
            BigInteger totalRemainingShares = oldPortfolioDetails.getShares().subtract(trade.getShares());
            newPortfolio.setShares(totalRemainingShares);
            newPortfolio.setAvgBuyPrice(oldPortfolioDetails.getAvgBuyPrice());
        } else {
            BigInteger totalShares = oldPortfolioDetails.getShares().add(trade.getShares());
            newPortfolio.setShares(totalShares);
            BigDecimal averagePrice = getWeightedAveragePrice(trade, oldPortfolioDetails);
            newPortfolio.setAvgBuyPrice(averagePrice);
        }
        return Portfolio.getPortfolioDto(newPortfolio);
    }

    private BigDecimal getWeightedAveragePrice(Trade trade, PortfolioDto oldPortfolioDetails) {
        return oldPortfolioDetails.getAvgBuyPrice()
                .multiply(BigDecimal.valueOf(oldPortfolioDetails.getShares().longValue()))
                .add(trade.getPrice()
                        .multiply(BigDecimal.valueOf(trade.getShares().longValue())))
                .divide(BigDecimal.valueOf(trade.getShares()
                        .add(oldPortfolioDetails.getShares()).longValue()), MathContext.DECIMAL32);
    }
}
