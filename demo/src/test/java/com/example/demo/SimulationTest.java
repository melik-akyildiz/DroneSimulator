package com.example.demo;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.example.demo.entities.*;
import com.example.demo.services.TrafficReportService;
import com.example.demo.services.TransportListener;
import com.example.demo.services.components.DispatcherImpl;
import com.example.demo.util.InputParser;
import com.example.demo.util.MessageConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.powermock.modules.testng.PowerMockTestCase;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertTrue;

public class SimulationTest extends PowerMockTestCase {

    @Test
    public void isStationCloseToNode_ShouldTrue() {
        //Given station location "Amersham",51.674129,-0.606514
        List<Location> stationLocation = new ArrayList<>(Arrays.asList(new Location(51.674129, -0.606514)));

        //When drone location 51.674129,-0.606514
        Location droneLocation = new Location(51.674129, -0.606514);

        Time time = new Time(5, 35);
        List<Node> nodes = new ArrayList<>(Arrays.asList(new Node("Drone1", droneLocation, time)));

        //When drone location nearby station
        InputParser.updateNodeLocation(nodes, stationLocation);

        //Then drone must be nearby
        assertTrue(nodes.get(0).getLocation().isHasStationsNearby());
    }

    @Test
    public void isStationCloseToNode_ShouldFalse() {

        //Given station location "Amersham",51.674129,-0.606514
        List<Location> stationLocation = new ArrayList<>(Arrays.asList(new Location(51.674129, -0.606514)));

        //When drone location 52.674129,-0.606514
        Location droneLocation = new Location(52.674129, -0.606514);

        Time time = new Time(5, 35);
        List<Node> nodes = new ArrayList<>(Arrays.asList(new Node("Drone1", droneLocation, time)));

        //When drone location away more than 350 m to station
        InputParser.updateNodeLocation(nodes, stationLocation);

        //Then drone must be not near by stations
        assertFalse(nodes.get(0).getLocation().isHasStationsNearby());

    }

    @Test
    public void onMessageReceived_TRAFFIC_REPORT() throws JsonProcessingException {

        TransportListener listener = new DispatcherImpl(mock(TrafficReportService.class));

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY, true);

        //Given traffic report
        TrafficReportEntry trafficReportEntry = mapper.readValue("{\"id\":null,\"droneId\":\"6043\",\"time\":1619166382692,\"speed\":155,\"trafficCondition\":\"MODERATE\"}",
                TrafficReportEntry.class);

        //Given message type as Traffic report
        Message message = new Message(MessageType.TRAFFIC_REPORT, MessageConverter.objectToJsonBytesArray(trafficReportEntry));

        Logger dispatcherLogger = (Logger) LoggerFactory.getLogger(DispatcherImpl.class);
        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();
        dispatcherLogger.addAppender(listAppender);

        //When message received
        listener.onMessageReceived(message);

        List<ILoggingEvent> logsList = listAppender.list;

        //Then message should be same with log entry 1
        assertEquals(message.toString(), logsList.get(0).getMessage());

        //AND log info should be same as expected
        assertEquals(logsList.get(1).getMessage(), "Report Traffic");

    }
}
