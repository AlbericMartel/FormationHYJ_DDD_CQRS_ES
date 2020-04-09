package com.hyj.event;

import java.util.Objects;

public class OrderStarted implements Event {

    private final int nbColis;

    public OrderStarted(int nbColis) {
        this.nbColis = nbColis;
    }

    public int getNbColis() {
        return nbColis;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderStarted that = (OrderStarted) o;
        return nbColis == that.nbColis;
    }

    @Override
    public int hashCode() {
        return Objects.hash(nbColis);
    }
}