package com.hyj.suivimarchandise;

import com.hyj.EventId;
import com.hyj.pubsub.EventWrapper;
import com.hyj.pubsub.PubSub;
import com.hyj.store.EventStore;
import com.hyj.suivimarchandise.command.StartOrder;
import com.hyj.suivimarchandise.command.TakeMarchandise;
import com.hyj.suivimarchandise.event.Event;
import com.hyj.suivimarchandise.projections.OrderId;

import java.util.List;

public class SuiviMarchandise {

    private final PubSub pubSub;
    private final EventStore eventStore;

    public SuiviMarchandise(PubSub pubSub, EventStore eventStore) {
        this.pubSub = pubSub;
        this.eventStore = eventStore;
    }

    public void start(OrderId orderId, int nbColis) {
        new Order().start(new StartOrder(orderId, nbColis))
                .ifPresent(event -> pubSub.publish(buildEventWrapper(orderId, event, 1)));
    }

    public void takeMarchandise(OrderId orderId, int nbColis) {
        List<Event> aggregateEvents = getAggregateEvents(orderId);
        new Order().decide(aggregateEvents, new TakeMarchandise(orderId, nbColis))
                .ifPresent(event -> pubSub.publish(buildEventWrapper(orderId, event, getNextSequence(aggregateEvents))));
    }

    private List<Event> getAggregateEvents(OrderId orderId) {
        return eventStore.getEvents(aggregateId(orderId));
    }

    private EventWrapper buildEventWrapper(OrderId orderId, Event event, int sequence) {
        return new EventWrapper(new EventId(aggregateId(orderId), sequence), event);
    }

    private int getNextSequence(List<Event> aggregateEvents) {
        return aggregateEvents.size() + 1;
    }

    private String aggregateId(OrderId orderId) {
        return "order-" + orderId.getValue();
    }
}
