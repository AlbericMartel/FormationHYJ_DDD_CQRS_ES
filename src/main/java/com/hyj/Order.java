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

    public Optional<Event> start(StartOrder command) {
        return start(List.of(), command);
    }

    public Optional<Event> start(List<Event> history, Command command) {
        DecisionProjection decisionProjection = new DecisionProjection(history);

        Event output = null;

        if (command instanceof StartOrder) {
            output = startOrder(decisionProjection, (StartOrder) command);
        } else if (command instanceof TakeMarchandise) {
            output = takeMarchandise(decisionProjection, (TakeMarchandise) command);
        }

        return Optional.ofNullable(output);
    }

    private Event startOrder(DecisionProjection decisionProjection, StartOrder startOrder) {
        return !decisionProjection.isOrderStarted() ? new OrderStarted(startOrder.getNbColis()) : null;
    }

    private Event takeMarchandise(DecisionProjection decisionProjection, TakeMarchandise takeMarchandise) {
        if (!decisionProjection.isOrderStarted())
            return null;

        if (decisionProjection.isMarchandiseFullyReceived())
            return null;

        int nbColisToTake = takeMarchandise.getNbColis();
        if (nbColisToTake < decisionProjection.getNbRemainingColis()) {
            return new MarchandisePartiallyReceived(nbColisToTake);
        } else {
            return new MarchandiseReceived();
        }
    }

}