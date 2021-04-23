package com.example.demo.services.components;

import com.example.demo.entities.*;
import com.example.demo.services.*;
import com.example.demo.util.InputParser;
import com.example.demo.util.MessageConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Component
public class DispatcherImpl implements Dispatcher, TransportListener {

    private static final Logger LOG = LoggerFactory
            .getLogger(DispatcherImpl.class);

    private Transport transport;

    private Drone drone6043;

    private Drone drone5937;

    private DispatcherThread drone6043DispatcherThread;

    private DispatcherThread drone5937DispatcherThread;

    private Time finishTime;


    private List<Node> nodes = new ArrayList<>();

    private final TrafficReportService reportService;

    @Autowired
    public DispatcherImpl(TrafficReportService reportService) {
        this.reportService = reportService;
        this.transport = new TransportImpl(this);
        this.transport.registerListener(this);
        this.finishTime = new Time(8, 10);
    }

    public void setTransport(Transport transport) {
        this.transport = transport;
    }

    @Override
    public String getAddress() {
        return "dispatcher";
    }

    @Override
    public void init() throws IOException {
        nodes.addAll(InputParser.loadNodes("6043.csv"));
        nodes.addAll(InputParser.loadNodes("5937.csv"));
        List<Location> stations = InputParser
                .loadStations("tube.csv");
        InputParser.updateNodeLocation(nodes, stations);
    }

    @Override
    public void start() {
        this.drone5937 = new DroneImpl("5937", transport);
        this.drone6043 = new DroneImpl("6043", transport);
    }

    @Override
    public void onMessageReceived(Message message) {
        LOG.info(message.toString());

        switch (message.getType()) {
            case ASK_FOR_LOCATION:

                String droneId = new String(message.getPayload());

                if (droneId.equals(this.drone5937.getId())) {

                    if (this.drone5937DispatcherThread != null) {
                        this.drone5937DispatcherThread.resumeSendingLocation();
                    } else {
                        this.drone5937DispatcherThread = new DispatcherThread(drone5937);
                        this.drone5937DispatcherThread.start();
                    }

                } else if (droneId.equals(this.drone6043.getId())) {

                    if (this.drone6043DispatcherThread != null) {
                        this.drone6043DispatcherThread.resumeSendingLocation();
                    } else {
                        this.drone6043DispatcherThread = new DispatcherThread(drone6043);
                        this.drone6043DispatcherThread.start();
                    }
                }

                break;
            case TRAFFIC_REPORT: {
                LOG.info("Report Traffic",MessageConverter.jsonBytesArrayToObject(message.getPayload(),TrafficReportEntry.class));
                this.reportService.saveTrafficReportEntry(MessageConverter.jsonBytesArrayToObject(message.getPayload(),
                        TrafficReportEntry.class));
            }
            break;

            default:
                LOG.info("Nothing to be done here!");
                break;
        }
    }


    class DispatcherThread extends Thread {

        private Drone drone;

        public final Object LOCK = new Object();

        public DispatcherThread(Drone drone) {
            super("Dispatcher " + drone.getId());
            this.drone = drone;
        }

        public void resumeSendingLocation() {
            synchronized (LOCK) {
                LOCK.notify();
            }
        }

        @Override
        public void run() {
            int count = 0;
            for (Node node : nodes) {
                if (node.getTime().compareTo(finishTime) < 0) {

                    if (node.getDroneId().equals(drone.getId())) {
                        count++;

                        if (count <= 10) {
                            transport.sendMessage(drone, MessageConverter
                                    .locationToMessage(drone, node.getLocation()));
                        } else {
                            synchronized (LOCK) {
                                try {
                                    LOG.info("Waiting for drone to ask for location.");
                                    LOCK.wait();
                                    count = 0;
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                } else {
                    // Shutdown drone if the time is 08.10
                    transport.sendMessage(drone, new Message(
                            MessageType.SHUTDOWN));
                }
            }

            // Shutdown drone if there is no more location to go
            transport.sendMessage(drone, new Message(
                    MessageType.SHUTDOWN));
        }
    }
}
