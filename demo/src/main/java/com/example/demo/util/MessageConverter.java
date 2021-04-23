package com.example.demo.util;

import java.io.ByteArrayOutputStream;


import com.example.demo.services.Drone;
import com.example.demo.entities.Location;
import com.example.demo.entities.Message;
import com.example.demo.entities.MessageType;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MessageConverter {
	
	public static Location messageToLocation(Message message){
		
		if( message.getType() == MessageType.MOVE_TO_LOCATION ){
			return jsonBytesArrayToObject(message.getPayload(),Location.class);
		}
		else{
			throw new IllegalArgumentException("The message must be MOVE_TO_LOCATION type.Input type :"+message.getType());
		}
	}
	
	public static Message locationToMessage(Drone drone, Location location){
		return new Message(drone.getId(), MessageType.MOVE_TO_LOCATION, objectToJsonBytesArray(location));
	}
	
	public static byte[] objectToJsonBytesArray(Object obj){
		try {
			ObjectMapper mapper = new ObjectMapper();
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			mapper.writeValue(bos, obj);			
			return bos.toByteArray();
		} catch (Exception e) {			
			e.printStackTrace();
			return null;
		}
	}
	
	public static <T> T jsonBytesArrayToObject( byte[] data,  Class<T> objectClass ){
		ObjectMapper mapper = new ObjectMapper();
		
		try {
			return mapper.readValue(data, objectClass);
		} catch (Exception e) {			
			e.printStackTrace();
			return null;
		} 
	}
}