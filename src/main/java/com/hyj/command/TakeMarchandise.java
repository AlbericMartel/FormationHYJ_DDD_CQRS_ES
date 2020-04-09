package com.hyj.command;

import java.util.Objects;

public class TakeMarchandise implements Command {

    private final int nbColis;

    public TakeMarchandise(int nbColis) {
        this.nbColis = nbColis;
    }

    public int getNbColis() {
        return nbColis;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TakeMarchandise that = (TakeMarchandise) o;
        return nbColis == that.nbColis;
    }

    @Override
    public int hashCode() {
        return Objects.hash(nbColis);
    }
}
