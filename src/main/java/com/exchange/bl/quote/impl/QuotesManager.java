package com.exchange.bl.quote.impl;

import com.exchange.bl.quote.IExchangeProvider;
import com.exchange.bl.quote.IQuotesManager;
import com.exchange.controller.parameters.QuotesResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Currency;
import java.util.List;
import java.util.Map;

@Service
public class QuotesManager implements IQuotesManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(QuotesManager.class);

    private final List<IExchangeProvider> ratesProviders;

    @Autowired
    public QuotesManager(List<IExchangeProvider> ratesProviders) {
        this.ratesProviders = ratesProviders;
    }

    @Override
    public QuotesResponse getExchangeQuote(Currency fromCode, int amount, Currency toCode) throws Exception {

        LOGGER.info("Start Exchange quote");
        double rate = getExchangeRate(fromCode, toCode);

        LOGGER.info("Best rate for exchange found as [{}]", rate);
        double toAmount = rate * amount;

        LOGGER.info("Return exchange quote at {} {} = {} {}", amount, fromCode, (int)toAmount, toCode);
        return new QuotesResponse(rate, toCode.getCurrencyCode(), (int) toAmount);
    }

    private double getExchangeRate(Currency fromCurrency, Currency toCurrency) throws Exception {

        return getBestRate(fromCurrency.getCurrencyCode(), toCurrency.getCurrencyCode());
    }

    private double getBestRate(String fromCurrency, String toCurrency) throws Exception {

        LOGGER.info("Getting rates from providers.");
        double chosenRate = 0;
        String providerName = "";

        for (IExchangeProvider provider : ratesProviders) {

            Map<String, Double> rates = provider.getRates(fromCurrency);

            Double toRate = rates.getOrDefault(toCurrency, 0.0);
            if(toRate > chosenRate){
                chosenRate = toRate;
                providerName = provider.getProviderName();
            }
        }

        if (chosenRate == 0){
            LOGGER.info("Requested currency {} isn't available on all providers", toCurrency);
            throw new Exception("Requested currency [" + toCurrency + "] isn't available");
        }

        LOGGER.info("\"{}\" provided better a rate: {} and was chosen", providerName, chosenRate);
        return chosenRate;
    }
}
