package com.techelevator.io;

import java.util.Scanner;

public class UserInput {
	Scanner scanner = new Scanner(System.in);
	
	public int getInt() {
		try {
			return Integer.parseInt(scanner.nextLine());
		} catch( NumberFormatException e ) {
			return 0;
		}
	}
	public double getDouble() {
		try {
			return Double.parseDouble(scanner.nextLine());
		} catch( NumberFormatException e ) {
			return 0.0;
		}
	}
	public String getString() {
		return scanner.nextLine();
	}
}
