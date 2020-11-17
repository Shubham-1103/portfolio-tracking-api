package com.shubham.client;


import com.shubham.models.QuoteList;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "restApiClient", url = "https://quotes-api.tickertape.in")
public interface RestApiClient {
    @GetMapping("/quotes")
    QuoteList getQuotes(@RequestParam(value = "sids") List<String> ids);
}
