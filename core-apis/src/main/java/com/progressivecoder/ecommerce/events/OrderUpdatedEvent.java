package com.progressivecoder.ecommerce.events;

import lombok.Data;

@Data
public class OrderUpdatedEvent {

    public final String orderId;

    public final String orderStatus;

    public OrderUpdatedEvent(String orderId, String orderStatus) {
        this.orderId = orderId;
        this.orderStatus = orderStatus;
    }
}
