package com.techelevator.vendingmachine;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

import com.techelevator.io.AuditLog;
import com.techelevator.items.Candy;
import com.techelevator.items.Chip;
import com.techelevator.items.Drink;
import com.techelevator.items.Gum;
import com.techelevator.items.Item;
import com.techelevator.items.Slot;

public class VendingMachine {
	public final static int MAX_ITEMS = 5;
	
	private Map<String,Slot> inventory = new LinkedHashMap<>();
	private Money money = new Money();
	
	private AuditLog log = new AuditLog("Log.txt");
	
	public VendingMachine(String inventoryInputFile) throws BadFileException {
		if( inventoryInputFile == null || inventoryInputFile.equals("") ) {
			return;
		}
		File file = new File(inventoryInputFile);
		try(Scanner fileScanner = new Scanner(file)) {
			while( fileScanner.hasNextLine() ) {
				String line = fileScanner.nextLine();
				String[] split = line.split("\\|");
				
				if( split.length != 4 ) {
					throw new BadFileException("Invalid format in file.");
				}
				
				String slot = split[0];
				String name = split[1];
				double price = 0.00;
				try {
					price = Double.parseDouble(split[2]);
				} catch( NumberFormatException e ) {
					throw new BadFileException(String.format("Price \"%s\" is not a valid price.", split[2]));
				}
				String type = split[3];
				
				loadItems(slot, name, price, type);
			}
		} catch (FileNotFoundException e) {
			throw new BadFileException(String.format("File \"%s\" not found.", inventoryInputFile));
		}
	}
	
	private void loadItems(String slotName, String name, double price, String type) {
		loadItems(slotName, name, price, type, MAX_ITEMS);
	}
	private void loadItems(String slotName, String name, double price, String type, int quantity) {
		Item item;
		Slot slot = new Slot();
		
		type = type.toLowerCase();
		switch( type ) {
			case "candy":
				item = new Candy(name, price);
				break;
			case "drink":
				item = new Drink(name, price);
				break;
			case "chip":
				item = new Chip(name, price);
				break;
			case "gum":
				item = new Gum(name, price);
				break;
			default:
				return;
		}
		
		slot.addItem(item, quantity);
		
		inventory.put(slotName.toUpperCase(), slot);
	}
	
	public Map<String,Slot> getInventory() {
		return inventory;
	}
	
	public Item getItem(String choice) {
		Slot slot = getSlot(choice);
		
		if( slot == null ) {
			return null;
		}
		
		Item item = slot.getItem();
		if( money.getBalance() >= item.getCents() && slot.removeItem(slot.getItem()) ) {
			money.subtractBalance(item.getCents());
			
			log.log(String.format("%s %s $%.2f, $%.2f", item.getName(), choice.trim().toUpperCase(), item.getPrice(), this.getMoneyInMachine()));
			
			return item;
		}
		return null;
	}
	public boolean hasQuantity(String choice) {
		Slot slot = getSlot(choice);
		
		if( slot == null ) {
			return false;
		}
		
		return slot.hasItem();
	}
	public boolean hasBalanceFor(String choice) {
		Slot slot = getSlot(choice);
		
		if( slot == null ) {
			return false;
		}
		
		if( slot.getItem().getCents() > money.getBalance() ) {
			return false;
		}
		
		return true;
	}
	public boolean choiceExists(String choice) {
		return getSlot(choice)!=null;
	}
	private Slot getSlot(String choice) {
		choice = choice.toUpperCase();
		
		if( !inventory.containsKey(choice) ) {
			return null;
		}
		
		return inventory.get(choice);
	}
	
	public double getMoneyInMachine() {
		return money.getBalance() / 100.0;
	}
	
	public boolean putMoneyInMachine(int centsAmount) {
		return putMoneyInMachine(centsAmount/100.0);
	}
	public boolean putMoneyInMachine(double amount) {
		int amountInCents = (int)(amount * 100.0);
		
		switch( amountInCents ) {
			case Money.DOLLAR_BILL:
				money.addDollar();
				break;
			case Money.TWO_DOLLAR_BILL:
				money.addTwoDollars();
				break;
			case Money.FIVE_DOLLAR_BILL:
				money.addFiveDollars();
				break;
			case Money.TEN_DOLLAR_BILL:
				money.addTenDollars();
				break;
			case Money.TWENTY_DOLLAR_BILL:
				money.addTwentyDollars();
				break;
			case Money.FIFTY_DOLLAR_BILL:
				money.addFiftyDollars();
				break;
			case Money.HUNDRED_DOLLAR_BILL:
				money.addHundredDollars();
				break;
			default:
				return false;
		}
		
		log.log(String.format("FEED MONEY: $%.2f $%.2f", amount, getMoneyInMachine()));
		
		money.convertToDenomination(Money.NICKEL);
		return true;
	}
	
	public Money getChange() {
		money.convertToDenomination(Money.QUARTER);
		Money copy = Money.copy(money);
		money.clear();
		
		log.log(String.format("GIVE CHANGE: $%.2f $%.2f", (copy.getBalance()/100.0), this.getMoneyInMachine()));
		
		return copy;
	}
	
	public class BadFileException extends Exception {
		private static final long serialVersionUID = -35013279513318912L;
		
		public BadFileException(String message) {
			super(message);
		}
	}
}
