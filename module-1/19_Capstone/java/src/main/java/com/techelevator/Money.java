package com.techelevator;

import java.util.LinkedHashMap;
import java.util.Map;

public class Money {
	public final static int PENNY = 1;
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
	
	private Map<Integer,Integer> currentMoney = new LinkedHashMap<>();
	
	public Money() {
		currentMoney.put(HUNDRED_DOLLAR_BILL, 0);
		currentMoney.put(FIFTY_DOLLAR_BILL, 0);
		currentMoney.put(TWENTY_DOLLAR_BILL, 0);
		currentMoney.put(TEN_DOLLAR_BILL, 0);
		currentMoney.put(FIVE_DOLLAR_BILL, 0);
		currentMoney.put(TWO_DOLLAR_BILL, 0);
		currentMoney.put(DOLLAR_BILL, 0);
		currentMoney.put(QUARTER, 0);
		currentMoney.put(DIME, 0);
		currentMoney.put(NICKEL, 0);
		currentMoney.put(PENNY, 0);
	}
	
	private Money(Money copy) {
		this.copyValues(copy);
	}

	public int getBalance() {
		int sum = 0;
		
		for( Map.Entry<Integer,Integer> entry : currentMoney.entrySet() ) {
			sum += (entry.getKey() * entry.getValue());
		}
		
		return sum;
	}
	
	private void addBalance(int amount) {
		if( currentMoney.containsKey(amount) ) {
			currentMoney.put(amount, currentMoney.get(amount)+1);
		}
	}
	public void addPenny() {
		addBalance(PENNY);
	}
	public int getPennies() {
		return currentMoney.get(PENNY);
	}
	public void addNickel() {
		addBalance(NICKEL);
	}
	public int getNickels() {
		return currentMoney.get(NICKEL);
	}
	public void addDime() {
		addBalance(DIME);
	}
	public int getDimes() {
		return currentMoney.get(DIME);
	}
	public void addQuarter() {
		addBalance(QUARTER);
	}
	public int getQuarters() {
		return currentMoney.get(QUARTER);
	}
	public void addDollar() {
		addBalance(DOLLAR_BILL);
	}
	public int getDollarBills() {
		return currentMoney.get(DOLLAR_BILL);
	}
	public void addTwoDollars() {
		addBalance(TWO_DOLLAR_BILL);
	}
	public int getTwoDollarBills() {
		return currentMoney.get(TWO_DOLLAR_BILL);
	}
	public void addFiveDollars() {
		addBalance(FIVE_DOLLAR_BILL);
	}
	public int getFiveDollarBills() {
		return currentMoney.get(FIVE_DOLLAR_BILL);
	}
	public void addTenDollars() {
		addBalance(TEN_DOLLAR_BILL);
	}
	public int getTenDollarBills() {
		return currentMoney.get(TEN_DOLLAR_BILL);
	}
	public void addTwentyDollars() {
		addBalance(TWENTY_DOLLAR_BILL);
	}
	public int getTwentyDollarBills() {
		return currentMoney.get(TWENTY_DOLLAR_BILL);
	}
	public void addFiftyDollars() {
		addBalance(FIFTY_DOLLAR_BILL);
	}
	public int getFiftyDollarBills() {
		return currentMoney.get(FIFTY_DOLLAR_BILL);
	}
	public void addHunredDollars() {
		addBalance(HUNDRED_DOLLAR_BILL);
	}
	public int getHundredDollarBills() {
		return currentMoney.get(HUNDRED_DOLLAR_BILL);
	}
	
	public boolean subtractBalance(int amount) {
		if( getBalance() >= amount ) {
			for( int key : currentMoney.keySet() ) {
				while( key <= amount && currentMoney.get(key) > 0 ) {
					currentMoney.put(key, currentMoney.get(key)-1);
					amount -= key;
				}
			}
			
			return true;
		}
		return false;
	}
	
	public void convertToDenomination(int denomination) {
		if( !currentMoney.containsKey(denomination) ) {
			return;
		}
		int sum = getBalance();
		clear();
		
		for( int key : currentMoney.keySet() ) {
			if( key <= denomination ) {
				currentMoney.put(key, sum/key);
				sum -= (sum/key)*key;
			}
		}
	}
	
	public void clear() {
		for( int denomination : currentMoney.keySet() ) {
			currentMoney.put(denomination, 0);
		}
	}
	
	private void copyValues(Money copy) {
		currentMoney.put(HUNDRED_DOLLAR_BILL, copy.getHundredDollarBills());
		currentMoney.put(FIFTY_DOLLAR_BILL, copy.getFiftyDollarBills());
		currentMoney.put(TWENTY_DOLLAR_BILL, copy.getTwentyDollarBills());
		currentMoney.put(TEN_DOLLAR_BILL, copy.getTenDollarBills());
		currentMoney.put(FIVE_DOLLAR_BILL, copy.getFiveDollarBills());
		currentMoney.put(TWO_DOLLAR_BILL, copy.getTwoDollarBills());
		currentMoney.put(DOLLAR_BILL, copy.getDollarBills());
		currentMoney.put(QUARTER, copy.getQuarters());
		currentMoney.put(DIME, copy.getDimes());
		currentMoney.put(NICKEL, copy.getNickels());
		currentMoney.put(PENNY, copy.getPennies());
	}
	
	public static Money copy(Money copy) {
		return new Money(copy);
	}
}
