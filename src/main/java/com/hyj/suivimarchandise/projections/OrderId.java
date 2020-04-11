package com.hyj.suivimarchandise.projections;

import java.util.Objects;

public class OrderId {
    private int value;

    private OrderId() {
    }

    public OrderId(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderId orderId = (OrderId) o;
        return value == orderId.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "OrderId{" +
                "value=" + value +
                '}';
    }
}