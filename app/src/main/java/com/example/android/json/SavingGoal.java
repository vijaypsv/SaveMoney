package com.example.android.json;

public class SavingGoal extends BaseSavingGoal{

    public String getSavingsGoalUid() {
        return savingsGoalUid;
    }

    public BalanceValue getTotalSaved() {
        return totalSaved;
    }

    public int getSavedPercentage() {
        return savedPercentage;
    }

    private String savingsGoalUid;
    private BalanceValue totalSaved;
    private int savedPercentage;
}
