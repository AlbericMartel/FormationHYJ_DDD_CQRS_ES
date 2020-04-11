package com.hyj;

import java.util.Objects;

public class EventId {

    private final String aggregateId;
    private final int sequence;

    public EventId(String aggregateId, int sequence) {
        this.aggregateId = aggregateId;
        this.sequence = sequence;
    }

    public String getAggregateId() {
        return aggregateId;
    }

    public int getSequence() {
        return sequence;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventId eventId = (EventId) o;
        return sequence == eventId.sequence &&
                Objects.equals(aggregateId, eventId.aggregateId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(aggregateId, sequence);
    }
}
