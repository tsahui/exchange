package com.exchange.bl.quote;

import java.util.Map;

public interface IExchangeProvider {

    String getProviderName();

    Map<String, Double> getRates(String fromCurrency);

}
