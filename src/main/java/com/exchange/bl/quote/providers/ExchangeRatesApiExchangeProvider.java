package com.exchange.bl.quote.providers;

import com.exchange.bl.quote.IExchangeProvider;
import com.exchange.restclient.BaseRestClientImpl;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Qualifier("ExchangeRatesApi")
public class ExchangeRatesApiExchangeProvider extends BaseRestClientImpl implements IExchangeProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExchangeRatesApiExchangeProvider.class);

    private static final String URL = "https://api.exchangerate-api.com/v4/latest";

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public String getProviderName() {
        return "Exchange Rates Api";
    }

    @Override
    @Cacheable("ExchangeRatesApiRate")
    public Map<String, Double> getRates(String fromCurrency) {

        String url = URL + "/" + fromCurrency;

        LOGGER.info("Sending rates for {} request to {} ", fromCurrency, url);
        String response = callClient(url, HttpMethod.GET, null, String.class);
        ExchangeRatesApiResponse exchangeRatesApiResponse = gson.fromJson(response, ExchangeRatesApiResponse.class);

        return exchangeRatesApiResponse.getRates();
    }

    private class ExchangeRatesApiResponse {
        private String date;
        private String base;
        private long time_last_updated;
        private HashMap<String, Double> rates;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getBase() {
            return base;
        }

        public void setBase(String base) {
            this.base = base;
        }

        public long getTime_last_updated() {
            return time_last_updated;
        }

        public void setTime_last_updated(long time_last_updated) {
            this.time_last_updated = time_last_updated;
        }

        public HashMap<String, Double> getRates() {
            return rates;
        }

        public void setRates(HashMap<String, Double> rates) {
            this.rates = rates;
        }
    }

}
