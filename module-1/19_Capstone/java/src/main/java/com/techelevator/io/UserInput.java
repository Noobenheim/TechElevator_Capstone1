package com.techelevator.io;

import java.io.InputStream;
import java.util.Scanner;

public class UserInput {
	Scanner scanner;
	
	public UserInput(InputStream in) {
		scanner = new Scanner(in);
	}
	
	public int getInt() {
		return getInt(0);
	}
	public int getInt(int defaultValue) {
		try {
			return Integer.parseInt(scanner.nextLine());
		} catch( NumberFormatException e ) {
			return defaultValue;
		}
	}
	public double getDouble() {
		return getDouble(0.0);
	}
	public double getDouble(double defaultValue) {
		try {
			return Double.parseDouble(scanner.nextLine());
		} catch( NumberFormatException e ) {
			return defaultValue;
		}
	}
	public String getString() {
		return scanner.nextLine();
	}
}
