package com.example.demo.services;

import com.example.demo.entities.TrafficReportEntry;

import java.util.Iterator;

public interface TrafficReportService {
	
	void saveTrafficReportEntry(TrafficReportEntry entry);
	
	Iterator<TrafficReportEntry> getAllEntries();
	
}
