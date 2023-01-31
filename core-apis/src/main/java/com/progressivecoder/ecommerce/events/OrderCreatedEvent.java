package com.progressivecoder.ecommerce.events;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class OrderCreatedEvent {

    public final String orderId;

    public final String itemType;

    public final Long price;

    public final String currency;

    public final String orderStatus;

    public OrderCreatedEvent(String orderId, String itemType, Long price, String currency, String orderStatus) {
        this.orderId = orderId;
        this.itemType = itemType;
        this.price = price;
        this.currency = currency;
        this.orderStatus = orderStatus;
    }
}
