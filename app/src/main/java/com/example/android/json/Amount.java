package com.example.android.json;

public class Amount {

    public Amount(String currency, double minorUnits) {
        BalanceValue amount = new BalanceValue(currency,minorUnits);
        this.amount = amount;
    }

    public BalanceValue getAmount() {
        return amount;
    }

    private BalanceValue amount;
}
