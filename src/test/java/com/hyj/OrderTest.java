package com.hyj;

import com.hyj.command.StartOrder;
import com.hyj.command.TakeMarchandise;
import com.hyj.event.Event;
import com.hyj.event.MarchandisePartiallyReceived;
import com.hyj.event.MarchandiseReceived;
import com.hyj.event.OrderStarted;
import com.hyj.projections.OrderId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderTest {

    @Test
    @DisplayName("When start order Then raise OrderStarted")
    void shouldRaiseOrderStarted_WhenStartOrder() {
        // WHEN
        Order order = new Order();
        Optional<Event> decision = order.start(new StartOrder(new OrderId(1), 1));

        // THEN
        assertThat(decision).contains(new OrderStarted(new OrderId(1), 1));
    }

    @Test
    @DisplayName("When start order twice Then raise nothing")
    void shouldRaiseNothing_WhenStartOrderTwice() {
        // WHEN
        Order order = new Order();
        Optional<Event> decision = order.start(
                List.of(new OrderStarted(new OrderId(1), 3)),
                new StartOrder(new OrderId(1), 1)
        );

        // THEN
        assertThat(decision).isEmpty();
    }

    @Test
    @DisplayName("Given order started When send take marchandise Then raise MarchandiseReceived")
    void shouldRaiseMarchandiseReceived_WhenTakeMarchandise_ForAStartedOrder() {
        // GIVEN
        Order order = new Order();

        // WHEN
        OrderId orderId = new OrderId(1);
        Optional<Event> decision = order.start(
                List.of(new OrderStarted(orderId, 1)),
                new TakeMarchandise(orderId, 1)
        );

        // THEN
        assertThat(decision).containsInstanceOf(MarchandiseReceived.class);
    }

    @Test
    @DisplayName("Given Order with marchandise received When take marchandise Then raise nothing")
    void shouldRaiseNothing_WhenTakeMarchandise_ForOrderWithMarchandiseReceived() {
        // GIVEN
        Order order = new Order();

        // WHEN
        OrderId orderId = new OrderId(1);
        Optional<Event> decision = order.start(
                List.of(new OrderStarted(orderId, 1), new MarchandiseReceived(orderId)),
                new TakeMarchandise(orderId, 1)
        );

        // THEN
        assertThat(decision).isEmpty();
    }

    @Test
    @DisplayName("Given Order started of 7 colis When take marchandise with 5 colis Then raise MarchandisePartiallyReceived")
    void shouldRaiseMarchandisePartiallyReceived_WhenTakeMarchandiseWith5Colis_ForStartedOrderOf7Colis() {
        // GIVEN
        Order order = new Order();

        // WHEN
        OrderId orderId = new OrderId(1);
        Optional<Event> decision = order.start(
                List.of(new OrderStarted(orderId, 7)),
                new TakeMarchandise(orderId, 5)
        );

        // THEN
        assertThat(decision).contains(new MarchandisePartiallyReceived(orderId, 5));
    }

    @Test
    @DisplayName("Given Order of 7 colis with 5 colis received When take marchandise with 2 colis Then raise MarchandiseReceived")
    void shouldRaiseMarchandiseReceived_WhenTakeMarchandiseWith2Colis_ForStartedOrderOf7ColisWith5ColisReceived() {
        // GIVEN
        Order order = new Order();

        // WHEN
        OrderId orderId = new OrderId(1);
        Optional<Event> decision = order.start(
                List.of(new OrderStarted(orderId, 7), new MarchandisePartiallyReceived(orderId, 5)),
                new TakeMarchandise(orderId, 2));

        // THEN
        assertThat(decision).containsInstanceOf(MarchandiseReceived.class);
    }

    @Test
    @DisplayName("Given Order started of 7 colis with 3 colis received When take marchandise with 2 colis Then raise MarchandisePartiallyReceived")
    void shouldRaiseMarchandisePartiallyReceived_WhenTakeMarchandiseWith2Colis_ForStartedOrderOf7ColisWith3ColisReceived() {
        // GIVEN
        Order order = new Order();

        // WHEN
        OrderId orderId = new OrderId(1);
        Optional<Event> decision = order.start(
                List.of(new OrderStarted(orderId, 7), new MarchandisePartiallyReceived(orderId, 3)),
                new TakeMarchandise(orderId, 2));

        // THEN
        assertThat(decision).contains(new MarchandisePartiallyReceived(orderId, 2));
    }
}
