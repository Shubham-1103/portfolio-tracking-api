package com.shubham.service;

import com.shubham.repo.SecuritiesRepo;
import com.shubham.repo.TradeRepo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class PortfolioTrackerServiceTest {
    @Mock
    SecuritiesRepo securitiesRepo;
    @Mock
    TradeRepo tradeRepo;
    @InjectMocks
    PortfolioTrackerService portfolioTrackerService;


    @Test
    public void test(){

    }
}