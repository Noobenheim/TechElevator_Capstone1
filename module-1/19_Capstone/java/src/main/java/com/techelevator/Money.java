package com.techelevator;

public class Money {
	public final static int NICKEL = 5;
	public final static int DIME = 10;
	public final static int QUARTER = 25;
	public final static int DOLLAR_BILL = 100;
	public final static int TWO_DOLLAR_BILL = 200;
	public final static int FIVE_DOLLAR_BILL = 500;
	public final static int TEN_DOLLAR_BILL = 1000;
	public static final int TWENTY_DOLLAR_BILL = 2000;
	public static final int FIFTY_DOLLAR_BILL = 5000;
	public static final int HUNDRED_DOLLAR_BILL = 10000;
	
	private int balance = 0;

	public int getBalance() {
		return balance;
	}
	
	private void addBalance(int amount) {
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
	
	public boolean subtractBalance(int amount) {
		if( balance >= amount ) {
			this.balance -= amount;
			return true;
		}
		return false;
	}
}
