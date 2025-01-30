package com.novab.unisaeat.data.model;

import java.io.Serializable;
import java.sql.Date;

public class Transaction implements Serializable {

    private String id;
    private String userId;
    private String amount;
    private Date date;
    private String mode;

    public Transaction(String id, String userId, String amount, Date date, String mode) {
        this.id = id;
        this.userId = userId;
        this.amount = amount;
        this.date = date;
        this.mode = mode;
    }

    public Transaction(String userId, String amount, Date date, String mode) {
        this.userId = userId;
        this.amount = amount;
        this.date = date;
        this.mode = mode;
    }

    public Transaction() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", amount='" + amount + '\'' +
                ", date=" + date +
                ", mode='" + mode + '\'' +
                '}';
    }
}
