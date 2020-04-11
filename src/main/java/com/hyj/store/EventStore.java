package com.hyj.store;

import com.hyj.pubsub.EventWrapper;
import com.hyj.suivimarchandise.event.Event;

import java.util.List;

public interface EventStore {
    void storeEvent(EventWrapper event) throws SequenceAlreadyExistsException;
    List<Event> getEvents(String aggregateId);
}
