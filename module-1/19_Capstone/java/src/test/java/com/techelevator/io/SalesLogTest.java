package com.techelevator.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import com.techelevator.items.Chip;
import com.techelevator.items.Slot;

public class SalesLogTest {
	@Rule
	public TemporaryFolder temp = new TemporaryFolder();
	
	@Before
	@After
	public void deleteAllFiles() {
		File[] files = temp.getRoot().listFiles();
		
		for( File file : files ) {
			file.delete();
		}
	}
	
	@Test
	public void test_simple_output() throws IOException {
		SalesLog.generateLog(temp.getRoot().getAbsolutePath(), null);
		
		File[] files = temp.getRoot().listFiles();
		
		Assert.assertTrue(files.length > 0);
	}
	
	@Test
	public void makes_recursive_directory() throws IOException {
		File test = new File(temp.getRoot().getAbsolutePath(), "hello/world");
		
		Assert.assertFalse(test.exists());
		SalesLog.generateLog(test.getAbsolutePath(), null);
		
		Assert.assertTrue(test.exists());
	}
	
	@Test
	public void reads_old_info() throws IOException {
		File test = writeNewLog("Fake Item", "3", "TOTAL SALES $5.00");
		
		List<Slot> inventory = new ArrayList<>();
		Slot s = new Slot();
		s.addItem(new Chip("Fake Item", 9.99), 5);
		inventory.add(s);
		
		SalesLog.generateLog(temp.getRoot().getAbsolutePath(), inventory);
		
		test.delete();
		// should be only one file left
		Assert.assertEquals(1, temp.getRoot().listFiles().length);
		File f = temp.getRoot().listFiles()[0];
		
		String actual = file_get_contents(f);
		
		String expected = "Fake Item|3\n\nTOTAL SALES $5.00";
		
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void bad_sales_total_line() throws IOException {
		writeNewLog("Fake Item", "3", "totAl sales 3 dolalrs");
		
		generateLog();
		
		Assert.assertEquals("Unknown previous total sales", SalesLog.getLastErrorMessage());
	}
	
	@Test
	public void bad_sales_total_amount() throws IOException {
		writeNewLog("Fake Item", "3", "TOTAL SALES $nothing");
		
		generateLog();
		
		Assert.assertEquals("Unknown total sales: TOTAL SALES $nothing", SalesLog.getLastErrorMessage());
	}
	
	@Test
	public void no_total_amount_after_space() throws IOException {
		writeNewLog("Fake Item|0\n\n");
		
		generateLog();
		
		Assert.assertEquals("Unknown total sales. No sales total line.", SalesLog.getLastErrorMessage());
	}
	
	@Test
	public void nothing_after_dollar() throws IOException {
		writeNewLog("Fake Item|0\n\nTOTAL SALES $");
		
		generateLog();
		
		Assert.assertEquals("Unknown previous total sales", SalesLog.getLastErrorMessage());
	}
	
	@Test
	public void bad_item_format() throws IOException {
		writeNewLog("Fake Item 0\n\nTOTAL SALES $0.00");
		
		generateLog();
		
		Assert.assertEquals("Unknown format: Fake Item 0", SalesLog.getLastErrorMessage());
	}
	
	@Test
	public void too_many_pipes() throws IOException {
		writeNewLog("Fake Item|0|hi\n\nTOTAL SALES $0.00");
		
		generateLog();
		
		Assert.assertEquals("Unknown format: Fake Item|0|hi", SalesLog.getLastErrorMessage());
	}
	
	@Test
	public void invalid_number_in_items() throws IOException {
		writeNewLog("Fake Item|notnumeric\n\nTOTAL SALES $0.00");
		
		generateLog();
		
		Assert.assertEquals("Unknown number: notnumeric", SalesLog.getLastErrorMessage());
	}
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Test
	public void avoid_filename_conflicts() throws IOException {
		thrown.expect(IllegalStateException.class);
		
		for( int i=0; i<20; i++ ) {
			generateLog();
		}
		
		Assert.fail();
	}
	
	@Test
	public void test_filter() {
		SalesLog.SalesLogFilenameFilter filter = new SalesLog.SalesLogFilenameFilter();
		
		Assert.assertTrue(filter.accept(null, "1999-10-02 05_44_13 PM Vending Machine Sales Report.txt"));
		Assert.assertFalse(filter.accept(null, "not a valid name,tar.gz"));
	}
	
	private int lastHour = 0;
	private int lastAM = 0;
	
	private void generateLog() {
		try {
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.HOUR, lastHour++%2==0?0:5);
			cal.set(Calendar.AM_PM, lastAM++%2==0?Calendar.AM:Calendar.PM);
			
			SalesLog.generateLog(temp.getRoot().getAbsolutePath(), getInventory(), cal);
		} catch( IOException e ) {
			e.printStackTrace();
		}
	}
	
	private List<Slot> getInventory() {
		List<Slot> inventory = new ArrayList<>();
		Slot s = new Slot();
		s.addItem(new Chip("Fake Item", 9.99), 5);
		inventory.add(s);
		return inventory;
	}
	
	private File writeNewLog(String log) throws IOException {
		File test = new File(temp.getRoot().getAbsoluteFile(), "2019-10-12 05_00_00 PM Vending Machine Sales Report.txt");
		PrintWriter writer = new PrintWriter(new FileWriter(test, true));
		writer.print(log);
		writer.close();
		
		return test;
	}
	private File writeNewLog(String item, String soldCount, String totalSalesLine) throws IOException {
		return writeNewLog(item+"|"+soldCount+"\n\n"+totalSalesLine);
	}
	
	private String file_get_contents(File f) throws FileNotFoundException, IOException {
		FileReader fr = new FileReader(f);
		StringBuilder returnString = new StringBuilder();
		
		int lastRead;
		while( (lastRead = fr.read()) > 0 ) {
			returnString.append((char)lastRead);
		}
		fr.close();
		return returnString.toString();
	}
}
