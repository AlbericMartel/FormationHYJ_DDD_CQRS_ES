package com.hyj;

import com.hyj.event.Event;
import com.hyj.event.MarchandisePartiallyReceived;
import com.hyj.event.MarchandiseReceived;
import com.hyj.event.OrderStarted;

import java.util.ArrayList;
import java.util.List;

public class StateProjection {

    private List<Event> events;
    private boolean orderStarted;
    private int nbRemainingColis;

    public StateProjection(List<Event> events) {
        this.events = new ArrayList<>(events);
    }

    public void addEvent(Event event) {
        this.events.add(event);
    }

    public void hydrateAll() {
        for (Event event : events) {
            hydrate(event);
        }
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
