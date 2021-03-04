package com.exchange.controller;

import com.exchange.bl.quote.IQuotesManager;
import com.exchange.controller.parameters.QuotesResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Currency;

import static com.exchange.config.AppConfig.BASE_URL;


@RestController(BASE_URL +"quote")
public class QuotesController {

    private static final Logger LOGGER = LoggerFactory.getLogger(QuotesController.class);

    @Autowired
    private IQuotesManager quotesManager;

    @GetMapping
    public QuotesResponse getExchangeQuote(
            @RequestParam Currency from_currency_code,
            @RequestParam int amount,
            @RequestParam Currency to_currency_code) throws Exception {

        LOGGER.info("Got exchange request. convert {} {} to {}", amount, from_currency_code, to_currency_code);
        if (from_currency_code.equals(to_currency_code)){
            LOGGER.info("Same currency requested. no convert neccesary.");
            return new QuotesResponse(1, from_currency_code.getCurrencyCode(), amount);
        }

        return quotesManager.getExchangeQuote(from_currency_code, amount, to_currency_code);
    }
}
