package com.hyj;

import com.hyj.event.Event;
import com.hyj.event.MarchandisePartiallyReceived;
import com.hyj.event.MarchandiseReceived;
import com.hyj.event.OrderStarted;

import java.util.List;

public class DecisionProjection {

    private boolean orderStarted;
    private int nbRemainingColis;

    public DecisionProjection(List<Event> events) {
        events.stream().forEach(this::hydrate);
    }

    public void hydrate(Event event) {
        if (event instanceof OrderStarted) {
            startOrder(((OrderStarted) event).getNbColis());
        }
        if (event instanceof MarchandisePartiallyReceived) {
            decreaseNbOfRemainingColis(((MarchandisePartiallyReceived) event).getNbColis());
        }
        if (event instanceof MarchandiseReceived) {
            setMarchandiseFullyReceived();
        }
    }

    public void startOrder(int nbColisInOrder) {
        this.orderStarted = true;
        this.nbRemainingColis = nbColisInOrder;
    }

    public void decreaseNbOfRemainingColis(int amount) {
        this.nbRemainingColis = this.nbRemainingColis - amount;
    }

    public void setMarchandiseFullyReceived() {
        this.nbRemainingColis = 0;
    }

    public boolean isMarchandiseFullyReceived() {
        return this.nbRemainingColis == 0;
    }

    public boolean isOrderStarted() {
        return this.orderStarted;
    }

    public int getNbRemainingColis() {
        return this.nbRemainingColis;
    }
}
