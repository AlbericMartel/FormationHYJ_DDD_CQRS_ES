package com.hyj.suivimarchandise;

import com.hyj.pubsub.EventStore;
import com.hyj.pubsub.InMemoryEventStore;
import com.hyj.pubsub.InMemoryPubSub;
import com.hyj.pubsub.PubSub;
import com.hyj.suivimarchandise.event.OrderStarted;
import com.hyj.suivimarchandise.projections.OrderId;
import com.hyj.suivimarchandise.projections.WaitingOrders;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.spy;

public class SuiviMarchandiseTest {

    @Test
    @DisplayName("Should store events when publish event")
    void shouldStoreEvent_WhenPublishEvent() {
        EventStore eventStore = new InMemoryEventStore();
        PubSub pubSub = new InMemoryPubSub(eventStore);

        OrderStarted event = new OrderStarted(new OrderId(1), 10);
        pubSub.publish(event);

        assertThat(eventStore.getEvents()).containsOnly(event);
    }

    @Test
    void shoudPersistEventsBeforeNotifyingHandlers() {
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
    void shoudDisplayNoWaitingOrders_WhenMarchandiseTaken() {
        WaitingOrders waitingOrders = new WaitingOrders();
        OrderId orderId = new OrderId(1);
        EventStore eventStore = new InMemoryEventStore(List.of(new OrderStarted(orderId, 10)));
        PubSub pubSub = new InMemoryPubSub(eventStore);
        pubSub.addListener(waitingOrders);

        SuiviMarchandise suiviMarchandise = new SuiviMarchandise(pubSub, eventStore);
        suiviMarchandise.takeMarchandise(orderId, 10);

        assertThat(waitingOrders.getWaitingOrders()).isEmpty();
    }

}
