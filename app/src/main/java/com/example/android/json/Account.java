package com.example.android.json;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Account {

    public String getAccountUid() {
        return accountUid;
    }

    public String getCategory() {
        return category;
    }

    public String getCurrency() {
        return currency;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    private String accountUid;
    @SerializedName("defaultCategory")
    private String category;
    private String currency;
    private Date createdAt;
}
