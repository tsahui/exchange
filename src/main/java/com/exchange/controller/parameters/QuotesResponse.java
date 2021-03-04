package com.exchange.controller.parameters;

public class QuotesResponse {

    private double exchange_rate;
    private String currency_code;
    private int amount;

    public QuotesResponse(double exchange_rate, String currency_code, int amount) {
        this.exchange_rate = exchange_rate;
        this.currency_code = currency_code;
        this.amount = amount;
    }

    public double getExchange_rate() {
        return exchange_rate;
    }

    public void setExchange_rate(double exchange_rate) {
        this.exchange_rate = exchange_rate;
    }

    public String getCurrency_code() {
        return currency_code;
    }

    public void setCurrency_code(String currency_code) {
        this.currency_code = currency_code;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
