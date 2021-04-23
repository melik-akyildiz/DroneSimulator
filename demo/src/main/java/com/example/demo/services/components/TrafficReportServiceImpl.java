package com.example.demo.services.components;

import com.example.demo.entities.TrafficReportEntry;
import com.example.demo.repository.TrafficReportRepository;
import com.example.demo.services.TrafficReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Iterator;


@Component
@Transactional
public class TrafficReportServiceImpl implements TrafficReportService {
	
	private final TrafficReportRepository reportRepository;
	
	@Autowired
	public TrafficReportServiceImpl(TrafficReportRepository reportRepository){
		this.reportRepository = reportRepository;
	}

	@Override
	public void saveTrafficReportEntry(TrafficReportEntry entry) {
		reportRepository.save(entry);
	}

	@Override
	public Iterator<TrafficReportEntry> getAllEntries() {		
		return reportRepository.findAll().iterator();
	}	
}
