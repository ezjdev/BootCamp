package com.colvir.bootcamp.homework17.producer.client;

import com.colvir.bootcamp.homework17.dto.ExchangeRateDto;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

import java.util.List;

@Component
public interface BelarusBankClient {

    @GetExchange("/api/kursExchange")
    List<ExchangeRateDto> getExchangeRate(@RequestParam String city);

}
