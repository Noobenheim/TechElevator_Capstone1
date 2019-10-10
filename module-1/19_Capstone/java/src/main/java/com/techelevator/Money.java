package com.techelevator;

public class Money {
	public final static double NICKEL = 0.05;
	public final static double DIME = 0.10;
	public final static double QUARTER = 0.25;
	public final static double DOLLAR_BILL = 1.00;
	public final static double TWO_DOLLAR_BILL = 2.00;
	public final static double FIVE_DOLLAR_BILL = 5.00;
	public final static double TEN_DOLLAR_BILL = 10.00;
	public static final double TWENTY_DOLLAR_BILL = 20.00;
	public static final double FIFTY_DOLLAR_BILL = 50.00;
	public static final double HUNDRED_DOLLAR_BILL = 100.00;
	
	private double balance = 0.00;

	public double getBalance() {
		return balance;
	}
	
	private void addBalance(double amount) {
		balance += amount;
	}
	public void addNickel() {
		addBalance(NICKEL);
	}
	public void addDime() {
		addBalance(DIME);
	}
	public void addQuarter() {
		addBalance(QUARTER);
	}
	public void addDollar() {
		addBalance(DOLLAR_BILL);
	}
	public void addTwoDollars() {
		addBalance(TWO_DOLLAR_BILL);
	}
	public void addFiveDollars() {
		addBalance(FIVE_DOLLAR_BILL);
	}
	public void addTenDollars() {
		addBalance(TEN_DOLLAR_BILL);
	}
	public void addTwentyDollars() {
		addBalance(TWENTY_DOLLAR_BILL);
	}
	public void addFiftyDollars() {
		addBalance(FIFTY_DOLLAR_BILL);
	}
	public void addHunredDollars() {
		addBalance(HUNDRED_DOLLAR_BILL);
	}
	
	public boolean subtractBalance(double amount) {
		if( balance >= amount ) {
			this.balance -= amount;
			return true;
		}
		return false;
	}
	
	// TODO: give specific change?
	public void giveChange() {
		this.balance = 0.0;
	}
}
