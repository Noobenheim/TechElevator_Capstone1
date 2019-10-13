package com.techelevator.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

import com.techelevator.items.Slot;
import com.techelevator.vendingmachine.VendingMachine;

// 2019-10-11 12:45:00 PM Vending Machine Sales Report.txt

public class SalesLog {
	private final static String TOTAL_SALES_STRING = "TOTAL SALES $";
	
	private static Map<String,Integer> lastInventoryLogged = new HashMap<>();
	
	private static String lastErrorMessage = null;
	
	private SalesLog() {}
	
	public static void generateLog(String logDirectory, Collection<Slot> inventory) throws IOException {
		generateLog(logDirectory, inventory, Calendar.getInstance());
	}
	public static void generateLog(String logDirectory, Collection<Slot> inventory, Calendar cal) throws IOException {
		// find the latest log file
		PreviousInformation previous = readLatestLog(new File(logDirectory));
		// write new information
		writeNewLog(logDirectory, previous, inventory, cal);
	}
	
	private static PreviousInformation readLatestLog(File directory) throws FileNotFoundException {
		Map<String,Integer> totals = new HashMap<>();
		double totalSales = 0.0;
		
		if( !directory.exists() ) {
			directory.mkdirs();
			// no totals to add for empty directory
		} else {
			// get totals from last file
			FilenameFilter filter = new SalesLogFilenameFilter();
			File[] files = directory.listFiles(filter);
			
			Arrays.sort(files);
			
			if( files.length == 0 ) {
				return new PreviousInformation();
			}
			
			File latestFile = files[files.length-1];
			Scanner fileScanner = new Scanner(latestFile);
			while( fileScanner.hasNextLine() ) {
				String line = fileScanner.nextLine();
				if( line.trim().equals("") ) {
					// next line should be total sales
					if( fileScanner.hasNextLine() ) {
						line = fileScanner.nextLine();
						if( line.length() <= TOTAL_SALES_STRING.length() || line.indexOf(TOTAL_SALES_STRING) != 0 ) {
							lastErrorMessage = "Unknown previous total sales";
							break;
						}
						try {
							totalSales = Double.parseDouble(line.substring(13));
							break;
						} catch( NumberFormatException e ) {
							lastErrorMessage = "Unknown total sales: "+line;
							break;
						}
					} else {
						lastErrorMessage = "Unknown total sales. No sales total line.";
						break;
					}
				} else if( !line.contains("|") ) {
					lastErrorMessage = "Unknown format: "+line;
					break;
				} else {
					String[] split = line.split("\\|");
					if( split.length != 2 ) {
						lastErrorMessage = "Unknown format: "+line;
						break;
					}
					String name = split[0];
					int quantity = 0;
					try {
						quantity = Integer.parseInt(split[1]);
					} catch( NumberFormatException e ) {
						lastErrorMessage = "Unknown number: "+split[1];
						break;
					}
					totals.put(name, quantity);
				}
			}
			fileScanner.close();
		}
		
		return new PreviousInformation(totals, totalSales);
	}
	
	private static void writeNewLog(String directory, PreviousInformation previous, Collection<Slot> current, Calendar date) throws IOException {
		// add the current inventory and the previous inventory
		Map<String,Integer> inventory = new HashMap<>();
		
		// loop through previous
		for( Map.Entry<String,Integer> entry : previous.getItems().entrySet() ) {
			inventory.put(entry.getKey(), entry.getValue());
		}
		// loop through current that hasn't been logged
		double currentTotal = 0.0;
		if( current != null ) {
			for( Slot slot : current ) {
				String name = slot.getItem().getName();
				int lastQuantity = lastInventoryLogged.containsKey(name)?lastInventoryLogged.get(name):VendingMachine.MAX_ITEMS;
				int quantity = lastQuantity - slot.getQuantity();
				double price = slot.getItem().getPrice();
				
				if( inventory.containsKey(name) ) {
					inventory.put(name, inventory.get(name)+quantity);
				} else {
					inventory.put(name, quantity);
				}
				
				currentTotal += quantity * price;
			}
		}
		
		String filename = String.format("%04d-%02d-%02d %02d_%02d_%02d %s %s", 
				date.get(Calendar.YEAR), date.get(Calendar.MONTH)+1, date.get(Calendar.DATE),
			date.get(Calendar.HOUR)==0?12:date.get(Calendar.HOUR), date.get(Calendar.MINUTE), date.get(Calendar.SECOND), date.get(Calendar.AM_PM)==Calendar.AM?"AM":"PM",
			"Vending Machine Sales Report.txt"
		);
		
		File output = new File(directory, filename);
		// ensure it doesn't exist
		if( output.exists() ) {
			throw new IllegalStateException("Log file \""+filename+"\" already exists.");
		}
		PrintWriter writer = new PrintWriter(new FileWriter(output));
		for( Map.Entry<String,Integer> entry : inventory.entrySet() ) {
			writer.format("%s|%d\n", entry.getKey(), entry.getValue());
		}
		writer.println();
		writer.format("%s%.2f", TOTAL_SALES_STRING, (currentTotal + previous.getSales()));
		writer.flush();
		writer.close();
		
		if( current != null ) {
			for( Slot slot : current ) {
				lastInventoryLogged.put(slot.getItem().getName(), slot.getQuantity());
			}
		}
	}
	
	public static String getLastErrorMessage() {
		return lastErrorMessage;
	}
	
	static class SalesLogFilenameFilter implements FilenameFilter {
		@Override
		public boolean accept(File dir, String name) {
			if( Pattern.matches("^[0-9]{4}-[0-1][0-9]-[0-3][0-9] [0-1][0-9]_[0-5][0-9]_[0-5][0-9] [A|P]M Vending Machine Sales Report\\.txt$", name) ) {
				return true;
			}
			return false;
		}
	}
	
	static class PreviousInformation {
		private Map<String,Integer> previousItems;
		private double previousSales;
		
		public PreviousInformation() {
			this.previousItems = new HashMap<>();
			this.previousSales = 0.0;
		}
		public PreviousInformation(Map<String,Integer> items, double sales) {
			this.previousItems = items;
			this.previousSales = sales;
		}
		
		public Map<String,Integer> getItems() {
			return this.previousItems;
		}
		public double getSales() {
			return this.previousSales;
		}
	}
}
