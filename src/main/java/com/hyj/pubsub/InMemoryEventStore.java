package com.hyj.pubsub;

import com.hyj.suivimarchandise.event.Event;

import java.util.ArrayList;
import java.util.List;

public class InMemoryEventStore implements EventStore {

    private List<Event> events = new ArrayList<>();

    public InMemoryEventStore() {
    }

    public InMemoryEventStore(List<Event> events) {
        this.events.addAll(events);
    }

    @Override
    public void storeEvent(Event event) {
        events.add(event);
    }

    @Override
    public List<Event> getEvents() {
        return events;
    }
}
