package com.hyj.command;

public class TakeMarchandise implements Command {

    private final int nbColis;

    public TakeMarchandise(int nbColis) {
        this.nbColis = nbColis;
    }

    public int getNbColis() {
        return nbColis;
    }
}
