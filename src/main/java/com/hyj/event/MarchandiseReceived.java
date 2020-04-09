package com.hyj.event;

import com.hyj.projections.OrderId;

public class MarchandiseReceived implements Event {

    private final OrderId orderId;

    public MarchandiseReceived(OrderId orderId) {
        this.orderId = orderId;
    }

    public OrderId getOrderId() {
        return orderId;
    }
}
