package com.techelevator;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

import com.techelevator.items.Candy;
import com.techelevator.items.Chip;
import com.techelevator.items.Drink;
import com.techelevator.items.Gum;
import com.techelevator.items.Item;

public class VendingMachine {
	public final static int MAX_ITEMS = 5;
	
	private Map<String,Slot> inventory = new LinkedHashMap<>();
	private Money money = new Money();
	
	public VendingMachine(String inventoryInputFile) {
		
		File file = new File(inventoryInputFile);
		try(Scanner fileScanner = new Scanner(file)) {
			while( fileScanner.hasNextLine() ) {
				String line = fileScanner.nextLine();
				String[] split = line.split("\\|");
				
				if( split.length != 4 ) {
					System.out.println("Invalid format in file.");
					System.exit(1);
				}
				
				String slot = split[0];
				String name = split[1];
				double price = 0.00;
				try {
					price = Double.parseDouble(split[2]);
				} catch( NumberFormatException e ) {
					System.out.println("Price \""+split[2]+"\" is not a valid price.");
					System.exit(1);
				}
				String type = split[3];
				
				loadItems(slot, name, price, type);
			}
		} catch (FileNotFoundException e) {
			System.out.println("File \""+inventoryInputFile+"\" not found.");
			System.exit(1);
		}
	}
	
	public void loadItems(String slotName, String name, double price, String type) {
		loadItems(slotName, name, price, type, MAX_ITEMS);
	}
	public void loadItems(String slotName, String name, double price, String type, int quantity) {
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
		if( money.getBalance() >= item.getPrice() && slot.removeItem(slot.getItem()) ) {
			money.subtractBalance(item.getCents());
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
		
		if( slot.getItem().getPrice() > money.getBalance() ) {
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
				money.addHunredDollars();
				break;
			default:
				return false;
		}
		
		money.convertToDenomination(Money.NICKEL);
		return true;
	}
	
	public Money getChange() {
		money.convertToDenomination(Money.QUARTER);
		Money copy = Money.copy(money);
		money.clear();
		return copy;
	}
}
