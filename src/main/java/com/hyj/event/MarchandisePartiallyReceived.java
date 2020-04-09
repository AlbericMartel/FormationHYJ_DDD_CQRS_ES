package com.hyj.event;

import java.util.Objects;

public class MarchandisePartiallyReceived implements Event {

    private final int nbColis;

    public MarchandisePartiallyReceived(int nbColis) {
        this.nbColis = nbColis;
    }

    public int getNbColis() {
        return nbColis;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MarchandisePartiallyReceived that = (MarchandisePartiallyReceived) o;
        return nbColis == that.nbColis;
    }

    @Override
    public int hashCode() {
        return Objects.hash(nbColis);
    }
}
