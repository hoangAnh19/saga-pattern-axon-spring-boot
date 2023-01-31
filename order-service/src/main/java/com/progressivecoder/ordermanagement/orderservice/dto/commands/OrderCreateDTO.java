package com.progressivecoder.ordermanagement.orderservice.dto.commands;

import java.math.BigDecimal;

public class OrderCreateDTO {

    private String itemType;

    private Long price;

    private String currency;

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
