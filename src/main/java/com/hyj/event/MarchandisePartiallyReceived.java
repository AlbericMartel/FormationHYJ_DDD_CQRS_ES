package com.hyj.event;

public class MarchandisePartiallyReceived implements Event {

    private final int nbColis;

    public MarchandisePartiallyReceived(int nbColis) {
        this.nbColis = nbColis;
    }

    public int getNbColis() {
        return nbColis;
    }
}
