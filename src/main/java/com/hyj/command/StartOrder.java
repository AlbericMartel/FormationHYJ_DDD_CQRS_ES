package com.hyj.command;

public class StartOrder implements Command {

    private final int nbColis;

    public StartOrder(int nbColis) {
        this.nbColis = nbColis;
    }

    public int getNbColis() {
        return nbColis;
    }
}
