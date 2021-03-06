package com.example.demo.entities;

import lombok.Data;

@Data
public class Time implements Comparable<Time> {
	
	public int hour;
	
	public int minute;
	
	public Time(int hour,int minute){
		this.hour = hour;
		this.minute = minute;
	}


	@Override
	public String toString() {
		return fill(hour)+":"+fill(minute);
	}
	
	private String fill(int value){
		if(value < 10){
			return "0"+value;
		}
		else{
			return String.valueOf(value);
		}
	}

	@Override
	public int compareTo(Time t) {
		
		if( this.hour < t.getHour() ){
			return -1;
		}
		else
		if( this.hour > t.getHour() ){
			return 1;
		}
		else
		if( this.minute < t.getMinute() ){
			return -1;
		}	
		else
		if( this.minute > t.getMinute() ){
			return 1;
		}
		else{
			return 0;
		}
	}
}
