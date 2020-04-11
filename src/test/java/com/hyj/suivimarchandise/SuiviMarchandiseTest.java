package com.hyj.suivimarchandise;

import com.hyj.EventId;
import com.hyj.pubsub.*;
import com.hyj.store.EventStore;
import com.hyj.store.InMemoryEventStore;
import com.hyj.store.SequenceAlreadyExistsException;
import com.hyj.suivimarchandise.event.MarchandiseReceived;
import com.hyj.suivimarchandise.event.OrderStarted;
import com.hyj.suivimarchandise.projections.OrderId;
import com.hyj.suivimarchandise.projections.WaitingOrders;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.spy;

public class SuiviMarchandiseTest {

    @Test
    @DisplayName("Should store events when publish event")
    void shouldStoreEvent_WhenPublishEvent() {
        EventStore eventStore = new InMemoryEventStore();
        PubSub pubSub = new InMemoryPubSub(eventStore);

        EventWrapper event = new EventWrapper(new EventId("order-1", 1), new OrderStarted(new OrderId(1), 10));
        pubSub.publish(event);

        assertThat(eventStore.getEvents(event.getEventId().getAggregateId())).containsOnly(event.getEvent());
    }

    @Test
    void shouldPersistEventsBeforeNotifyingHandlers() {
        EventStore eventStore = spy(new InMemoryEventStore());
        PubSub pubSub = new InMemoryPubSub(eventStore);
        WaitingOrders waitingOrders = spy(new WaitingOrders());
        pubSub.addListener(waitingOrders);

        InOrder inOrder = inOrder(eventStore, waitingOrders);

        SuiviMarchandise suiviMarchandise = new SuiviMarchandise(pubSub, eventStore);
        suiviMarchandise.start(new OrderId(1), 10);

        inOrder.verify(eventStore).storeEvent(any());
        inOrder.verify(waitingOrders).handle(any());
    }

    @Test
    @DisplayName("Should call handlers when publish event")
    void shouldCallHandlersWhenPublishEvent() {
        EventStore eventStore = new InMemoryEventStore();
        PubSub pubSub = new InMemoryPubSub(eventStore);
        WaitingOrders waitingOrders = new WaitingOrders();
        pubSub.addListener(waitingOrders);

        OrderId orderId = new OrderId(1);
        SuiviMarchandise suiviMarchandise = new SuiviMarchandise(pubSub, eventStore);
        suiviMarchandise.start(orderId, 10);

        assertThat(waitingOrders.getWaitingOrders()).containsOnly(orderId);
    }

    @Test
    @DisplayName("Should display updated projection, when start order")
    void shouldDisplayUpdatedProjection_WhenStartOrder() {
        EventStore eventStore = new InMemoryEventStore();
        PubSub pubSub = new InMemoryPubSub(eventStore);
        WaitingOrders waitingOrders = new WaitingOrders();
        pubSub.addListener(waitingOrders);

        OrderId orderId = new OrderId(1);
        SuiviMarchandise suiviMarchandise = new SuiviMarchandise(pubSub, eventStore);
        suiviMarchandise.start(orderId, 10);

        assertThat(waitingOrders.getWaitingOrders()).containsOnly(orderId);
    }

    @Test
    @DisplayName("Should update projection to no waiting orders, when marchandise taken")
    void shouldDisplayNoWaitingOrders_WhenMarchandiseTaken() {
        WaitingOrders waitingOrders = new WaitingOrders();
        OrderId orderId = new OrderId(1);
        EventStore eventStore = new InMemoryEventStore(List.of(new EventWrapper(new EventId("order-1", 1), new OrderStarted(orderId, 10))));
        PubSub pubSub = new InMemoryPubSub(eventStore);
        pubSub.addListener(waitingOrders);

        SuiviMarchandise suiviMarchandise = new SuiviMarchandise(pubSub, eventStore);
        suiviMarchandise.takeMarchandise(orderId, 10);

        assertThat(waitingOrders.getWaitingOrders()).isEmpty();
    }

    @Test
    @DisplayName("Should return all events, when get all events of aggregate instance, after store events of an aggregate instance")
    void shouldReturnAllEvents_WhenGetEventsOfAggregate_AfterStoreEventsOfAggregate() {
        EventStore eventStore = new InMemoryEventStore();

        EventWrapper event = new EventWrapper(new EventId("order-1", 1), new OrderStarted(new OrderId(1), 10));
        eventStore.storeEvent(event);

        assertThat(eventStore.getEvents(event.getEventId().getAggregateId())).containsOnly(event.getEvent());
    }

    @Test
    @DisplayName("Should return only events of aggregate instance, when get all events of aggregate instance, after store events of several aggregate instances")
    void shouldReturnEventsOfAggregateInstance_WhenGetAllEventsOfAggregate_AfterStoreEventsOfSeveralAggregateInstances() {
        EventStore eventStore = new InMemoryEventStore();

        EventWrapper eventAggregate1 = new EventWrapper(new EventId("order-1", 1), new OrderStarted(new OrderId(1), 10));
        eventStore.storeEvent(eventAggregate1);
        EventWrapper eventAggregate2 = new EventWrapper(new EventId("order-2", 1), new OrderStarted(new OrderId(2), 10));
        eventStore.storeEvent(eventAggregate2);

        assertThat(eventStore.getEvents(eventAggregate1.getEventId().getAggregateId())).containsOnly(eventAggregate1.getEvent());
    }

    @Test
    @DisplayName("Should throw, when store event with sequence event already stored")
    void shouldThrow_WhenStoreEventWithSequenceAlreadyStored() {
        assertThatThrownBy(() -> {
            EventStore eventStore = new InMemoryEventStore();

            String aggregateId = "order-1";
            EventWrapper event = new EventWrapper(new EventId(aggregateId, 1), new OrderStarted(new OrderId(1), 10));
            eventStore.storeEvent(event);
            EventWrapper eventWithSameSequence = new EventWrapper(new EventId(aggregateId, 1), new MarchandiseReceived(new OrderId(1)));
            eventStore.storeEvent(eventWithSameSequence);
        })
                .isInstanceOf(SequenceAlreadyExistsException.class);
    }
}
