package com.example.android.json;

public class NewSavingGoal extends BaseSavingGoal {

    public NewSavingGoal(String name, String currency,String targetCurrency, double targetMinorUnits,String base64EncodedPhoto) {

        BalanceValue target = new BalanceValue(targetCurrency,targetMinorUnits);

        this.name = name;
        this.currency = currency;
        this.target = target;
        this.base64EncodedPhoto = base64EncodedPhoto;

    }

    public String getCurrency() {
        return currency;
    }

    public String getBase64EncodedPhoto() {
        return base64EncodedPhoto;
    }

    private String currency;
    private String base64EncodedPhoto;
}
