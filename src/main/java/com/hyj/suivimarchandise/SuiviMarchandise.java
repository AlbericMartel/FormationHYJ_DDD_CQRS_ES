package com.hyj.suivimarchandise;

import com.hyj.pubsub.EventStore;
import com.hyj.pubsub.PubSub;
import com.hyj.suivimarchandise.command.StartOrder;
import com.hyj.suivimarchandise.command.TakeMarchandise;
import com.hyj.suivimarchandise.projections.OrderId;

public class SuiviMarchandise {

    private final PubSub pubSub;
    private final EventStore eventStore;

    public SuiviMarchandise(PubSub pubSub, EventStore eventStore) {
        this.pubSub = pubSub;
        this.eventStore = eventStore;
    }

    public void start(OrderId orderId, int nbColis) {
        new Order().start(new StartOrder(orderId, nbColis))
                .ifPresent(event -> pubSub.publish(event));
    }

    public void takeMarchandise(OrderId orderId, int nbColis) {
        new Order().decide(eventStore.getEvents(), new TakeMarchandise(orderId, nbColis))
                .ifPresent(event -> pubSub.publish(event));
    }
}
