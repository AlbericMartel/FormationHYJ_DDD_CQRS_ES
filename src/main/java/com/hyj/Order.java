package com.hyj;

import com.hyj.command.Command;
import com.hyj.command.StartOrder;
import com.hyj.command.TakeMarchandise;
import com.hyj.event.Event;
import com.hyj.event.MarchandisePartiallyReceived;
import com.hyj.event.MarchandiseReceived;
import com.hyj.event.OrderStarted;

import java.util.List;
import java.util.Optional;

class Order {

    private StateProjection stateProjection;

    public Order(List<Event> events) {
        stateProjection = new StateProjection(events);
        stateProjection.hydrateAll();
    }

    public Optional<Event> decide(Command command) {
        return execute(command).stream()
                .peek(stateProjection::addEvent)
                .peek(stateProjection::hydrate)
                .findFirst();
    }

    private Optional<Event> execute(Command command) {
        Event output = null;

        if (command instanceof StartOrder) {
            output = startOrder((StartOrder)command);
        } else if (command instanceof TakeMarchandise) {
            output = takeMarchandise((TakeMarchandise)command);
        }

        return Optional.ofNullable(output);
    }

    private Event startOrder(StartOrder startOrder) {
        return new OrderStarted(startOrder.getNbColis());
    }

    private Event takeMarchandise(TakeMarchandise takeMarchandise) {
        Event output = null;

        if (stateProjection.isOrderStarted() && !stateProjection.isMarchandiseFullyReceived()) {
            int nbColisToTake = takeMarchandise.getNbColis();

            if (nbColisToTake < stateProjection.getNbRemainingColis()) {
                output = new MarchandisePartiallyReceived(nbColisToTake);
            } else {
                output = new MarchandiseReceived();
            }
        }

        return output;
    }

}