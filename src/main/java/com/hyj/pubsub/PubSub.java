package com.hyj.pubsub;

import com.hyj.suivimarchandise.projections.Projection;

public interface PubSub {

    void publish(EventWrapper event);
    void addListener(Projection projection);
}
