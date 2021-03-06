package com.example.demo.services.components;

import com.example.demo.entities.*;
import com.example.demo.services.Drone;
import com.example.demo.services.Transport;
import com.example.demo.services.TransportListener;
import com.example.demo.util.MessageConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;


public class DroneImpl implements Drone, Runnable, TransportListener {

    private static final Logger LOG = LoggerFactory.getLogger(DroneImpl.class);

    private String id;

    private Location location;

    private Transport transport;

    private Queue<Message> messagesQueue;

    private static final Object LOCK = new Object();

    private static final Long SPEED = 1000L; // speed 1 s/location

    public DroneImpl(String id, Transport transport) {
        this.id = id;
        this.transport = transport;
        messagesQueue = new LinkedBlockingQueue<Message>(100);
        this.transport.registerListener(this);
        new Thread(this, "Drone " + id).start();
    }

    @Override
    public String getId() {
        return this.id;
    }

    ;

    @Override
    public void run() {
        LOG.info("Ready to fly.");

        loop:
        while (true) {

            Message message = null;

            while ((message = this.messagesQueue.poll()) != null) {
                LOG.info(message.toString());
                switch (message.getType()) {

                    case SHUTDOWN: {
                        LOG.warn("Shutdown signal Received Aborting...");
                        break loop;
                    }

                    case MOVE_TO_LOCATION: {

                        try {
                            Thread.sleep(SPEED);
                            this.location = MessageConverter.messageToLocation(message);

                            if (this.location.isHasStationsNearby()) {
                                this.transport.sendMessage(transport.getDispatcherDestination(), new Message(MessageType.TRAFFIC_REPORT, newTrafficReportEntry(this.location)));
                            }

                        } catch (InterruptedException e) {
                            LOG.warn(
                                    "Moving state is interrupted Aborting.", e);

                            break loop;
                        }
                    }
                    break;

                    default:
                        LOG.info("Invalid message! Ignoring...");
                }
            }

            this.transport.sendMessage(transport.getDispatcherDestination(), new Message(MessageType.ASK_FOR_LOCATION, this.id.getBytes()));

            synchronized (LOCK) {

                try {
                    LOG.info("Waiting for locations...");
                    LOCK.wait();
                } catch (InterruptedException e) {
                    LOG.warn(
                            "Waiting state interrupted.Aborting!", e);
                    break;
                }
            }
        }


        LOG.info("Returning to base .", id);
    }

    @Override
    public void onMessageReceived(Message message) {

        try {
            this.messagesQueue.add(message);

            synchronized (LOCK) {
                LOCK.notify();
            }

        } catch (IllegalStateException ex) {
            LOG.error("Drone memory is full! Ignoring the message...", ex);
        }
    }

    @Override
    public String getAddress() {
        return this.id;
    }

    @Override
    public Location getLocation() {
        return this.location;
    }

    private byte[] newTrafficReportEntry(Location location) {
        TrafficReportEntry entry = new TrafficReportEntry();
        entry.setDroneId(this.id);
        entry.setSpeed(new Random().nextInt(200));
        entry.setTime(new Date());
        entry.setTrafficCondition(TrafficCondition.MODERATE);

        return MessageConverter.objectToJsonBytesArray(entry);
    }
}
