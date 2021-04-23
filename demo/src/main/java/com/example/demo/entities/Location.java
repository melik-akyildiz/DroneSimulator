package com.example.demo.entities;

import lombok.Data;

@Data
public class Location {


	private double latitude;

	private double longitude;

	private boolean hasStationsNearby;

	public Location(){
		this(0,0);
	}

	public Location(double latitude,double longitude){
		this.latitude = latitude;
		this.longitude = longitude;
	}

	@Override
	public String toString() {
		return "Location [latitude=" + latitude + ", longitude=" + longitude
				+ ", hasStationsNearby=" + hasStationsNearby + "]";
	}
}
