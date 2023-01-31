package com.progressivecoder.ecommerce.events;

import lombok.Data;

@Data
public class InvoiceCreatedEvent  {

    public final String paymentId;

    public final String orderId;

    public InvoiceCreatedEvent(String paymentId, String orderId) {
        this.paymentId = paymentId;
        this.orderId = orderId;
    }
}
