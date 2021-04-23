package com.example.demo.util;

import com.example.demo.entities.Location;
import com.example.demo.entities.Node;
import com.example.demo.entities.Time;
import com.opencsv.CSVReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class InputParser {

    private static final Integer RANGE = 350;

    public static List<Location> loadStations(String file) throws IOException {

        List<Location> stations = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream(file)))) {
            String[] nextLine;

            while ((nextLine = reader.readNext()) != null) {
                Location location = new Location(
                        Double.parseDouble(nextLine[1]),
                        Double.parseDouble(nextLine[2]));
                stations.add(location);
            }
        }

        return stations;
    }

    public static List<Node> loadNodes(String file) throws IOException {

        List<Node> nodes = new ArrayList<Node>();

        try (CSVReader reader = new CSVReader(new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream(file)))) {
            String[] nextLine;

            while ((nextLine = reader.readNext()) != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime dateTime = LocalDateTime.parse(nextLine[3], formatter);

                Node node = new Node(nextLine[0], new Location(
                        Double.parseDouble(nextLine[1]),
                        Double.parseDouble(nextLine[2])), new Time(
                        dateTime.getHour(),
                        dateTime.getMinute()));

                nodes.add(node);
            }
        }

        return nodes;
    }

    public static void updateNodeLocation(List<Node> nodes,
                                          List<Location> stations) {
        nodes.forEach(node -> {
            stations.forEach(location -> {
                if (isStationCloseToNode(location, node)) {
                    node.getLocation().setHasStationsNearby(true);
                }
            });
        });

    }

    private static boolean isStationCloseToNode(Location location, Node node) {

        return distance(location.getLatitude(), location.getLongitude(), node
                .getLocation().getLatitude(), node.getLocation().getLongitude()) < RANGE;
    }

    /**
     * calculates the distance between two points (given the latitude/longitude
     * of those points) South latitudes are negative, east longitudes are
     * positive
     * <p>
     * Source : http://www.geodatasource.com/developers/java
     *
     * @param lat1 - Latitude of point 1 (in decimal degrees)
     * @param lon1 - Longitude of point 1 (in decimal degrees)
     * @param lat2 - Latitude of point 2 (in decimal degrees)
     * @param lon2 - Longitude of point 2 (in decimal degrees)
     * @return - distance in meters
     */
    private static double distance(double lat1, double lon1, double lat2,
                                   double lon2) {

        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        dist = dist * 1.609344;

        return dist * 1000;
    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

}
