package com.example.panier;

public class PaymentIntentRequest {
    private int amount;

    public PaymentIntentRequest(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }
}
