package com.example.demo.services;


import com.example.demo.entities.Location;

public interface Drone extends Destination {

    String getId();

    Location getLocation();

}
