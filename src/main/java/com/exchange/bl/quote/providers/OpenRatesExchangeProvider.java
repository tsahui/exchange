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
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Service
@Qualifier("OpenRates")
public class OpenRatesExchangeProvider extends BaseRestClientImpl implements IExchangeProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(OpenRatesExchangeProvider.class);

    private static final String URL = "https://api.exchangeratesapi.io/latest";

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public String getProviderName() {
        return "Open Rates";
    }

    @Override
    @Cacheable("openRatesRate")
    public Map<String, Double> getRates(String fromCurrency) {

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(URL)
                .queryParam("base", fromCurrency);

        LOGGER.info("Sending rates for {} request to {} ", fromCurrency, URL);
        String response = callClient(uriBuilder.toUriString(), HttpMethod.GET, null, String.class);
        OpenRatesResponse openRatesResponse = gson.fromJson(response, OpenRatesResponse.class);

        return openRatesResponse.getRates();
    }

    private class OpenRatesResponse {
        private String date;
        private String base;
        private Map<String, Double> rates;

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

        public Map<String, Double> getRates() {
            return rates;
        }

        public void setRates(Map<String, Double> rates) {
            this.rates = rates;
        }
    }
}
