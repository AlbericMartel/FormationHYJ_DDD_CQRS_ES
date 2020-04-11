package com.hyj.store;

import com.hyj.EventId;
import com.hyj.pubsub.EventWrapper;
import com.hyj.suivimarchandise.event.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InMemoryEventStore implements EventStore {

    private List<EventWrapper> events = new ArrayList<>();

    public InMemoryEventStore() {
    }

    public InMemoryEventStore(List<EventWrapper> events) {
        this.events.addAll(events);
    }

    @Override
    public void storeEvent(EventWrapper event) throws SequenceAlreadyExistsException {
        if (isSequenceAlreadyExisting(event.getEventId())) {
            throw new SequenceAlreadyExistsException();
        }

        events.add(event);
    }

    @Override
    public List<Event> getEvents(String aggregateId) {
        return events.stream()
                .filter(eventWrapper -> eventWrapper.getEventId().getAggregateId().equals(aggregateId))
                .map(eventWrapper -> eventWrapper.getEvent())
                .collect(Collectors.toList());
    }

    private boolean isSequenceAlreadyExisting(EventId eventId) {
        return getEvents(eventId.getAggregateId()).size() + 1 > eventId.getSequence();
    }
}
