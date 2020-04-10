package com.hyj.suivimarchandise.projections;

import com.hyj.suivimarchandise.event.Event;

public interface Projection {
    void handle(Event event);
}
