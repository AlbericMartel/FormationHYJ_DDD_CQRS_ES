package com.hyj.event;

public class OrderStarted implements Event {

    private final int nbColis;

    public OrderStarted(int nbColis) {
        this.nbColis = nbColis;
    }

    public int getNbColis() {
        return nbColis;
    }
}