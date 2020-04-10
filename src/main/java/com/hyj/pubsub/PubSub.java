package com.hyj.pubsub;

import com.hyj.suivimarchandise.event.Event;
import com.hyj.suivimarchandise.projections.Projection;

public interface PubSub {

    void publish(Event event);
    void addListener(Projection projection);
}
