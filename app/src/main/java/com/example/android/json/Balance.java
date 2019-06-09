package com.example.android.json;

public class Balance {

    public BalanceValue getClearedBalance() {
        return clearedBalance;
    }

    public BalanceValue getEffectiveBalance() {
        return effectiveBalance;
    }

    public BalanceValue getPendingTransactions() {
        return pendingTransactions;
    }

    public BalanceValue getAvailableToSpend() {
        return availableToSpend;
    }

    public BalanceValue getAcceptedOverdraft() {
        return acceptedOverdraft;
    }

    public BalanceValue getAmount() {
        return amount;
    }

    private BalanceValue clearedBalance;
    private BalanceValue effectiveBalance;
    private BalanceValue pendingTransactions;
    private BalanceValue availableToSpend;
    private BalanceValue acceptedOverdraft;
    private BalanceValue amount;
}
