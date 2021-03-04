package com.exchange.bl.quote;

import com.exchange.controller.parameters.QuotesResponse;

import java.util.Currency;

public interface IQuotesManager {

    QuotesResponse getExchangeQuote(Currency fromCode, int amount, Currency toCode) throws Exception;

}
