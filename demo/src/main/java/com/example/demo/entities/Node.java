package com.example.demo.entities;

import lombok.Data;

@Data
public class Node {
	
	private String droneId;
	
	private Location location;
	
	private Time time;
	
	public Node(String droneId,Location location,Time time){
		this.droneId = droneId;
		this.location = location;
		this.time = time;
	}
	@Override
	public String toString() {
		return "Node [droneId=" + droneId + ", location=" + location
				+ ", time=" + time + "]";
	}
}
