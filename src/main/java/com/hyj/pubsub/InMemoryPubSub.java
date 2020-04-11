package com.hyj.pubsub;

import com.hyj.store.EventStore;
import com.hyj.suivimarchandise.projections.Projection;

import java.util.ArrayList;
import java.util.List;

public class InMemoryPubSub implements PubSub {

    private final EventStore eventStore;
    private final List<Projection> listeners = new ArrayList<>();

    public InMemoryPubSub(EventStore eventStore) {
        this.eventStore = eventStore;
    }

    @Override
    public void publish(EventWrapper event) {
        eventStore.storeEvent(event);
        listeners.forEach(projection -> projection.handle(event.getEvent()));
    }

    @Override
    public void addListener(Projection projection) {
        listeners.add(projection);
    }
}
