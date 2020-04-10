package com.hyj.suivimarchandise;

import com.hyj.suivimarchandise.command.Command;
import com.hyj.suivimarchandise.command.StartOrder;
import com.hyj.suivimarchandise.command.TakeMarchandise;
import com.hyj.suivimarchandise.event.Event;
import com.hyj.suivimarchandise.event.MarchandisePartiallyReceived;
import com.hyj.suivimarchandise.event.MarchandiseReceived;
import com.hyj.suivimarchandise.event.OrderStarted;

import java.util.List;
import java.util.Optional;

public class Order {

    public Optional<Event> start(StartOrder command) {
        return decide(List.of(), command);
    }

    public Optional<Event> decide(List<Event> history, Command command) {
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
        return !decisionProjection.isOrderStarted() ? new OrderStarted(startOrder.getId(), startOrder.getNbColis()) : null;
    }

    private Event takeMarchandise(DecisionProjection decisionProjection, TakeMarchandise takeMarchandise) {
        if (!decisionProjection.isOrderStarted())
            return null;

        if (decisionProjection.isMarchandiseFullyReceived())
            return null;

        int nbColisToTake = takeMarchandise.getNbColis();
        if (nbColisToTake < decisionProjection.getNbRemainingColis()) {
            return new MarchandisePartiallyReceived(takeMarchandise.getOrderId(), nbColisToTake);
        } else {
            return new MarchandiseReceived(takeMarchandise.getOrderId());
        }
    }

}