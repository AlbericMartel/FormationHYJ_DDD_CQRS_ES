package com.hyj.suivimarchandise.command;

import com.hyj.suivimarchandise.projections.OrderId;

public class StartOrder implements Command {

    private final OrderId id;
    private final int nbColis;

    public StartOrder(OrderId id, int nbColis) {
        this.id = id;
        this.nbColis = nbColis;
    }

    public OrderId getId() {
        return id;
    }

    public int getNbColis() {
        return nbColis;
    }
}
