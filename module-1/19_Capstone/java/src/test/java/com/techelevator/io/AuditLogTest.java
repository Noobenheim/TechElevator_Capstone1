package com.techelevator.io;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

public class AuditLogTest {
	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	@Test
	public void test_single_output_midnight() throws IOException {
		File f = folder.newFile("test.txt");
		
		AuditLog log = new AuditLog(f.getAbsolutePath());
		
		Calendar date = Calendar.getInstance();
		
		date.set(Calendar.HOUR, 0);
		
		String test = "Hello, World!";
		
		log.log(test, date);
		
		String expected = String.format("%02d/%02d/%02d %02d:%02d:%02d %s %s\n",
				date.get(Calendar.MONTH)+1, date.get(Calendar.DATE), date.get(Calendar.YEAR),
				date.get(Calendar.HOUR)==0?12:date.get(Calendar.HOUR), date.get(Calendar.MINUTE), date.get(Calendar.SECOND), date.get(Calendar.AM_PM)==Calendar.AM?"AM":"PM",
				test);
		
		String actual = file_get_contents(f);
		
		Assert.assertEquals(expected, actual);
		
		log.close();
	}
	
	@Test
	public void test_single_output_noon() throws IOException {
		File f = folder.newFile("test.txt");
		
		AuditLog log = new AuditLog(f.getAbsolutePath());
		
		Calendar date = Calendar.getInstance();
		
		date.set(Calendar.HOUR, 12);
		
		String test = "Hello, World!";
		
		log.log(test, date);
		
		String expected = String.format("%02d/%02d/%02d %02d:%02d:%02d %s %s\n",
				date.get(Calendar.MONTH)+1, date.get(Calendar.DATE), date.get(Calendar.YEAR),
				date.get(Calendar.HOUR)==0?12:date.get(Calendar.HOUR), date.get(Calendar.MINUTE), date.get(Calendar.SECOND), date.get(Calendar.AM_PM)==Calendar.AM?"AM":"PM",
				test);
		
		String actual = file_get_contents(f);
		
		Assert.assertEquals(expected, actual);
		
		log.close();
	}
	
	@Test
	public void test_multiple_output() throws IOException {
		File f = folder.newFile("test.txt");
		
		AuditLog log = new AuditLog(f.getAbsolutePath());
		
		Calendar date = Calendar.getInstance();
		
		date.set(Calendar.HOUR, 0);
		
		String test = "Hello, World!";
		
		log.log(test, date);
		
		String expected = String.format("%02d/%02d/%02d %02d:%02d:%02d %s %s\n",
				date.get(Calendar.MONTH)+1, date.get(Calendar.DATE), date.get(Calendar.YEAR),
				date.get(Calendar.HOUR)==0?12:date.get(Calendar.HOUR), date.get(Calendar.MINUTE), date.get(Calendar.SECOND), date.get(Calendar.AM_PM)==Calendar.AM?"AM":"PM",
				test);
		
		date.set(Calendar.HOUR, 10);
		log.log(test, date);
		
		expected += String.format("%02d/%02d/%02d %02d:%02d:%02d %s %s\n",
				date.get(Calendar.MONTH)+1, date.get(Calendar.DATE), date.get(Calendar.YEAR),
				date.get(Calendar.HOUR)==0?12:date.get(Calendar.HOUR), date.get(Calendar.MINUTE), date.get(Calendar.SECOND), date.get(Calendar.AM_PM)==Calendar.AM?"AM":"PM",
				test);
		
		String actual = file_get_contents(f);
		
		Assert.assertEquals(expected, actual);
		
		log.close();
	}
	
	@Test
	public void test_regex() throws IOException {
		File f = folder.newFile("test.txt");
		AuditLog log = new AuditLog(f.getAbsolutePath());
		
		String test = "This is a test message";
		
		log.log(test);
		
		Calendar date = Calendar.getInstance();
		
		String expectedRegex = String.format("%02d/%02d/%02d %02d:[0-5][0-9]:[0-5][0-9] %s %s\n",
				date.get(Calendar.MONTH)+1, date.get(Calendar.DATE), date.get(Calendar.YEAR),
				date.get(Calendar.HOUR)==0?12:date.get(Calendar.HOUR), date.get(Calendar.AM_PM)==Calendar.AM?"AM":"PM",
				test);
		
		String actual = file_get_contents(f);
		
		Assert.assertTrue(actual.matches(expectedRegex));
	}
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Test
	public void null_file_not_allowed() throws IOException {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Filename must not be blank");
		AuditLog log;
		log = new AuditLog(null);
	}
	
	@Test
	public void blank_file_not_allowed() throws IOException {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Filename must not be blank");
		AuditLog log;
		log = new AuditLog("");
	}
	
	private String file_get_contents(File f) throws IOException {
		FileReader fr = new FileReader(f);
		StringBuilder returnString = new StringBuilder();
		
		int lastRead;
		while( (lastRead = fr.read()) > 0 ) {
			returnString.append((char)lastRead);
		}
		return returnString.toString();
	}
}
