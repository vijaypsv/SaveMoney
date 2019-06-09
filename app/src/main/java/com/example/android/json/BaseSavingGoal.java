package com.example.android.json;

public class BaseSavingGoal {

    public String getName() {
        return name;
    }

    public BalanceValue getTarget() {
        return target;
    }

    protected String name;
    protected BalanceValue target;
}
