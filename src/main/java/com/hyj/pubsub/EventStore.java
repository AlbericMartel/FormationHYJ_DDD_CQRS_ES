package com.hyj.pubsub;

import com.hyj.suivimarchandise.event.Event;

import java.util.List;

public interface EventStore {
    void storeEvent(Event event);
    List<Event> getEvents();
}
