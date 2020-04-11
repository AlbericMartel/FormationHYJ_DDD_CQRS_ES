package com.hyj.pubsub;

import com.hyj.EventId;
import com.hyj.suivimarchandise.event.Event;

import java.util.Objects;

public class EventWrapper {

    private EventId eventId;
    private Event event;

    private EventWrapper() {
    }

    public EventWrapper(EventId eventId, Event event) {
        this.eventId = eventId;
        this.event = event;
    }

    public EventId getEventId() {
        return eventId;
    }

    public Event getEvent() {
        return event;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventWrapper that = (EventWrapper) o;
        return Objects.equals(eventId, that.eventId) &&
                Objects.equals(event, that.event);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId, event);
    }
}
