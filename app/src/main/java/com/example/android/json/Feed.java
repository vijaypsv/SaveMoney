package com.example.android.json;

import java.util.Date;

public class Feed {

    public String getFeedItemUid() {
        return feedItemUid;
    }

    public String getCategoryUid() {
        return categoryUid;
    }

    public BalanceValue getAmount() {
        return amount;
    }

    public BalanceValue getSourceAmount() {
        return sourceAmount;
    }

    public String getDirection() {
        return direction;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public Date getTransactionTime() {
        return transactionTime;
    }

    public Date getSettlementTime() {
        return settlementTime;
    }

    public String getSource() {
        return source;
    }

    public String getStatus() {
        return status;
    }

    public String getCounterPartyType() {
        return counterPartyType;
    }

    public String getCounterPartyUid() {
        return counterPartyUid;
    }

    public String getCounterPartyName() {
        return counterPartyName;
    }

    public String getCountry() {
        return country;
    }

    public String getSpendingCategory() {
        return spendingCategory;
    }

    private String feedItemUid;
    private String categoryUid;
    private BalanceValue amount;
    private BalanceValue sourceAmount;
    private String direction;
    private Date updatedAt;
    private Date transactionTime;
    private Date settlementTime;
    private String source;
    private String status;
    private String counterPartyType;
    private String counterPartyUid;
    private String counterPartyName;
    private String country;
    private String spendingCategory;
}
