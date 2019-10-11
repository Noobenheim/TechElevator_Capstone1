package com.techelevator;

import java.io.Closeable;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;

public class AuditLog implements Closeable {
	private File auditFile;
	private PrintWriter fileWriter = null;
	private String format = "%02d/%02d/%02d %02d:%02d:%02d %s %s\n";
	
	public AuditLog(String fileName) {
		this.auditFile = new File(fileName);
		
		try {
			fileWriter = new PrintWriter(new FileWriter(auditFile, true));
		} catch( IOException e ) {
			e.printStackTrace();
		}
		fileWriter.println("--------------Starting AuditLog for day.");
	}
	
	public void log(String message) {
		Calendar date = Calendar.getInstance();
		
		fileWriter.format(format, 
				date.get(Calendar.MONTH)+1, date.get(Calendar.DATE), date.get(Calendar.YEAR),
				date.get(Calendar.HOUR), date.get(Calendar.MINUTE), date.get(Calendar.SECOND), date.get(Calendar.AM_PM)==Calendar.AM?"AM":"PM",
				message
		);
		fileWriter.flush();
	}

	@Override
	public void close() throws IOException {
		if( fileWriter != null ) {
			fileWriter.close();
		}
	}
}
