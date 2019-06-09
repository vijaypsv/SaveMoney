package com.example.android.json;

public class BalanceValue {

    public BalanceValue(String currency, double minorUnits) {
        this.currency = currency;
        this.minorUnits = minorUnits;
    }

    public String getCurrency() {
        return currency;
    }

    public double getMinorUnits() {
        return minorUnits;
    }

    public double getAmount() {
        return minorUnits/100;
    }

    private String currency;
    private double minorUnits;
}
