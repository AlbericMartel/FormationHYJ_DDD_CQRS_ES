package com.hyj;

import com.hyj.event.MarchandisePartiallyReceived;
import com.hyj.event.MarchandiseReceived;
import com.hyj.event.OrderStarted;
import com.hyj.projections.OrderId;
import com.hyj.projections.WaitingOrders;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class WaitingOrdersTest {


    @Test
    @DisplayName("When receive OrderCreated Then this order is added in waiting orders")
    void shouldAddWaitingOrder_WhenReceiveOrderCreated() {
        // GIVEN
        WaitingOrders waitingOrders = new WaitingOrders();

        // WHEN
        OrderId orderId = new OrderId(1);
        waitingOrders.listen(new OrderStarted(orderId, 2));

        // THEN
        assertThat(waitingOrders.getWaitingOrders()).contains(orderId);
    }

    @Test
    @DisplayName("When receive MarchandiseReceived Then this order is removed of waiting orders")
    void shouldRemoveWaitingOrder_WhenReceiveMarchandiseReceived() {
        // GIVEN
        WaitingOrders waitingOrders = new WaitingOrders();
        OrderId orderId = new OrderId(1);
        waitingOrders.listen(new OrderStarted(orderId, 1));

        // WHEN
        waitingOrders.listen(new MarchandiseReceived(orderId));

        // THEN
        assertThat(waitingOrders.getWaitingOrders()).isEmpty();
    }

    @Test
    @DisplayName("Given 2 waiting orders (A and B) When receive MarchandiseReceived of order B Then I have only order A in waiting orders")
    void shouldGet1WaitingOrder_When2WaitingOrdersAnd1HasMarchandiseReceived() {
        // GIVEN
        WaitingOrders waitingOrders = new WaitingOrders();

        OrderId orderIdOfCompletedOrder = new OrderId(1);
        waitingOrders.listen(new OrderStarted(orderIdOfCompletedOrder, 1));

        OrderId orderIdOfWaitingOrder = new OrderId(2);
        waitingOrders.listen(new OrderStarted(orderIdOfWaitingOrder, 2));

        // WHEN
        waitingOrders.listen(new MarchandiseReceived(orderIdOfCompletedOrder));

        // THEN
        assertThat(waitingOrders.getWaitingOrders()).containsOnly(orderIdOfWaitingOrder);
    }

    @Test
    @DisplayName("When receive MarchandisePartiallyReceived Then this order is still a waiting order")
    void shouldGetWaitingOrder_WhenMarchandisePartiallyReceived() {
        // GIVEN
        WaitingOrders waitingOrders = new WaitingOrders();

        OrderId orderId = new OrderId(1);
        waitingOrders.listen(new OrderStarted(orderId, 2));

        // WHEN
        waitingOrders.listen(new MarchandisePartiallyReceived(orderId, 1));

        // THEN
        assertThat(waitingOrders.getWaitingOrders()).contains(orderId);
    }
}
