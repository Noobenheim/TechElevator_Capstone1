package com.techelevator.io;

import java.util.Calendar;

public class AuditLog extends Log {
	private static final String format = "%02d/%02d/%02d %02d:%02d:%02d %s %s\n";
	
	public AuditLog(String fileName) {
		super(fileName, format);

		fileWriter.println("--------------Starting AuditLog for day.");
	}
	
	@Override
	public void log(String message) {
		Calendar date = Calendar.getInstance();
		
		super.log(
			date.get(Calendar.MONTH)+1, date.get(Calendar.DATE), date.get(Calendar.YEAR),
			date.get(Calendar.HOUR)==0?12:date.get(Calendar.HOUR), date.get(Calendar.MINUTE), date.get(Calendar.SECOND), date.get(Calendar.AM_PM)==Calendar.AM?"AM":"PM",
			message
		);
	}
}
