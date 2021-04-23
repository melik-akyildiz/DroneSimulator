package com.example.demo.entities;


import lombok.Data;

@Data
public class Message {


    private String droneId;

    private MessageType type;

    private byte[] payload;

    public Message(MessageType type) {
        this(type, null);
    }

    public Message(MessageType type, byte[] payload) {
        this.type = type;
        this.payload = payload;
    }

    public Message(String id, MessageType type, byte[] payload) {
        this.type = type;
        this.droneId = id;
        this.payload = payload;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        if (droneId != null) {
            str.append("Drone id:" + droneId + " ");
        }

        str.append("[type=" + type + ", payload="
                + (payload != null ? new String(payload) : "") + "]");

        return str.toString();
    }
}
