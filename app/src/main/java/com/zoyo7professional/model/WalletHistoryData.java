package com.zoyo7professional.model;

public class WalletHistoryData {

    private String name;
    private String trnDate;
    private String trnTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTrnDate() {
        return trnDate;
    }

    public void setTrnDate(String trnDate) {
        this.trnDate = trnDate;
    }

    public String getTrnTime() {
        return trnTime;
    }

    public void setTrnTime(String trnTime) {
        this.trnTime = trnTime;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    private String amount;
}
