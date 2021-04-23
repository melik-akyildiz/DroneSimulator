package com.example.demo.services.components;

import com.example.demo.entities.Message;
import com.example.demo.services.Destination;
import com.example.demo.services.Transport;
import com.example.demo.services.TransportListener;

import java.util.ArrayList;
import java.util.List;


public class TransportImpl implements Transport {

    private Destination dispatcherDestination;

    private List<TransportListener> listeners;

    public TransportImpl(Destination dispatcherDestination) {
        this.listeners = new ArrayList<>();
        this.dispatcherDestination = dispatcherDestination;
    }

    @Override
    public void sendMessage(Destination destination, Message message) {

        listeners.forEach(listener -> {
            if (listener.getAddress().equals(destination.getAddress())) {
                listener.onMessageReceived(message);
            }
        });
    }

    @Override
    public Destination getDispatcherDestination() {
        return dispatcherDestination;
    }

    @Override
    public void registerListener(TransportListener listener) {

        if (!this.listeners.contains(listener)) {
            this.listeners.add(listener);
        }

    }

}
