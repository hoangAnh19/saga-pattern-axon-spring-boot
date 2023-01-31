package com.progressivecoder.ordermanagement.orderservice.sagas;

import com.progressivecoder.ecommerce.commands.CreateInvoiceCommand;
import com.progressivecoder.ecommerce.commands.CreateShippingCommand;
import com.progressivecoder.ecommerce.commands.UpdateOrderStatusCommand;
import com.progressivecoder.ecommerce.events.*;
import com.progressivecoder.ordermanagement.orderservice.aggregates.OrderStatus;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.SagaLifecycle;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;

import javax.inject.Inject;
import java.util.Objects;
import java.util.UUID;

@Saga
@Slf4j
public class OrderManagementSaga {

    @Inject
    private transient CommandGateway commandGateway;

    @StartSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderCreatedEvent orderCreatedEvent) {
        String paymentId = UUID.randomUUID().toString();
        System.out.println("Saga invoked");

        //associate Saga
        SagaLifecycle.associateWith("paymentId", paymentId);


        //send the commands
        try {
            log.info("create order: " + "order id " + orderCreatedEvent.getOrderId() + " order status " + orderCreatedEvent.getOrderStatus());
            if (orderCreatedEvent.getPrice() == 1000)
                commandGateway.send(new CreateInvoiceCommand(paymentId, orderCreatedEvent.orderId));
            else throw new Exception("EXCEPTION");
        } catch (Exception e) {
            log.error(e.getMessage());
            // Start the compensating transaction
            log.info("compensating transaction order id: " + orderCreatedEvent.getOrderId() + " " + String.valueOf(OrderStatus.REJECTED));
        }

    }

    @SagaEventHandler(associationProperty = "paymentId")
    public void handle(InvoiceCreatedEvent invoiceCreatedEvent) {
        String shippingId = UUID.randomUUID().toString();

        //associate Saga with shipping
        SagaLifecycle.associateWith("shipping", shippingId);

        //send the create shipping command
        try {
            commandGateway.send(new CreateShippingCommand(shippingId, invoiceCreatedEvent.orderId, invoiceCreatedEvent.paymentId));
            log.info("payment order :" + " order id " + invoiceCreatedEvent.getOrderId() + " payment id " + invoiceCreatedEvent.getPaymentId());
        } catch (Exception e) {
            log.error(e.getMessage());
            // Start the compensating transaction
            log.info("compensating transaction payment");
        }
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderShippedEvent orderShippedEvent) {
        try {
            commandGateway.send(new UpdateOrderStatusCommand(orderShippedEvent.orderId, String.valueOf(OrderStatus.SHIPPED)));
            log.info("ship order:" + "order id " + orderShippedEvent.orderId + " payment id " + orderShippedEvent.paymentId);
        } catch (Exception e) {
            log.error(e.getMessage());
            // Start the compensating transaction
            log.info("compensating transaction shipping", String.valueOf(OrderStatus.REJECTED));
        }
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderUpdatedEvent orderUpdatedEvent) {
        log.info("data order:" + " order id " + orderUpdatedEvent.getOrderId() + " order status " + orderUpdatedEvent.orderStatus);
        SagaLifecycle.end();
    }
}
