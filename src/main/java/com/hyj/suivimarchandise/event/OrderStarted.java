package com.hyj.suivimarchandise.event;

import com.hyj.suivimarchandise.projections.OrderId;

import java.util.Objects;

public class OrderStarted implements Event {

    private OrderId id;
    private int nbColis;

    private OrderStarted() {
    }

    public OrderStarted(OrderId id, int nbColis) {
        this.id = id;
        this.nbColis = nbColis;
    }

    public OrderId getId() {
        return id;
    }

    public int getNbColis() {
        return nbColis;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderStarted that = (OrderStarted) o;
        return nbColis == that.nbColis &&
                Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nbColis);
    }

    @Override
    public String toString() {
        return "OrderStarted{" +
                "id=" + id +
                ", nbColis=" + nbColis +
                '}';
    }
}