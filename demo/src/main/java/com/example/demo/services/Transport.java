package com.example.demo.services;


import com.example.demo.entities.Message;


public interface Transport {

    void sendMessage(Destination destination, Message message);

    Destination getDispatcherDestination();

    void registerListener(TransportListener listener);


}
