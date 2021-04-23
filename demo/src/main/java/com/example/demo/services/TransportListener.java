package com.example.demo.services;

import com.example.demo.entities.Message;

public interface TransportListener extends Destination {

    void onMessageReceived(Message message);
}
