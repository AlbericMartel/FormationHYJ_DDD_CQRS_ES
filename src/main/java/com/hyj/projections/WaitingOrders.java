package com.hyj.projections;

import com.hyj.event.Event;
import com.hyj.event.MarchandisePartiallyReceived;
import com.hyj.event.MarchandiseReceived;
import com.hyj.event.OrderStarted;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class WaitingOrders {

    private Map<OrderId, Integer> waitingOrders = new HashMap<>();

    public void listen(Event event) {
        if (event instanceof OrderStarted) {
            OrderStarted orderStarted = (OrderStarted) event;
            waitingOrders.put(orderStarted.getId(), orderStarted.getNbColis());
        }

        if (event instanceof MarchandiseReceived) {
            MarchandiseReceived marchandiseReceived = (MarchandiseReceived) event;
            if (waitingOrders.containsKey(marchandiseReceived.getOrderId())) {
                waitingOrders.remove(marchandiseReceived.getOrderId());
            }
        }

        if (event instanceof MarchandisePartiallyReceived) {
            MarchandisePartiallyReceived marchandisePartiallyReceived = (MarchandisePartiallyReceived) event;
            OrderId orderId = marchandisePartiallyReceived.getOrderId();

            Integer nbColisRemaining = waitingOrders.get(orderId);//Better to add the remaining colis in the event than recalculating here
            if (nbColisRemaining != null) {
                nbColisRemaining = nbColisRemaining - marchandisePartiallyReceived.getNbColis();
                if (nbColisRemaining > 0) {
                    waitingOrders.put(orderId, nbColisRemaining);
                } else {
                    waitingOrders.remove(orderId);
                }
            }
        }
    }

    public Set<OrderId> getWaitingOrders() {
        return waitingOrders.keySet();
    }
}
