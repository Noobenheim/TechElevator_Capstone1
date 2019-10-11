package com.techelevator;

import java.io.Closeable;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public abstract class Log implements Closeable {
	private File auditFile;
	protected PrintWriter fileWriter = null;
	private String format = "";
	
	public Log(String fileName, String format) {
		if( fileName == null || fileName.equals("") ) {
			throw new IllegalArgumentException("Filename must not be blank");
		}
		
		this.auditFile = new File(fileName);
		this.format = format;
		
		try {
			fileWriter = new PrintWriter(new FileWriter(auditFile, true));
		} catch( IOException e ) {
			e.printStackTrace();
		}
	}
	
	protected void log(Object...objects) {
		fileWriter.format(format, objects);
		fileWriter.flush();
	}
	public abstract void log(String message);

	@Override
	public void close() throws IOException {
		if( fileWriter != null ) {
			fileWriter.close();
		}
	}
}
