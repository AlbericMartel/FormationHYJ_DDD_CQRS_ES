package com.hyj.suivimarchandise.event;

import com.hyj.suivimarchandise.projections.OrderId;

import java.util.Objects;

public class MarchandisePartiallyReceived implements Event {

    private final OrderId orderId;
    private final int nbColis;

    public MarchandisePartiallyReceived(OrderId orderId, int nbColis) {
        this.orderId = orderId;
        this.nbColis = nbColis;
    }

    public OrderId getOrderId() {
        return orderId;
    }

    public int getNbColis() {
        return nbColis;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MarchandisePartiallyReceived that = (MarchandisePartiallyReceived) o;
        return nbColis == that.nbColis &&
                Objects.equals(orderId, that.orderId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, nbColis);
    }
}
