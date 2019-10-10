package com.techelevator;

import java.util.LinkedHashMap;
import java.util.Map;

import com.techelevator.items.Candy;
import com.techelevator.items.Chip;
import com.techelevator.items.Drink;
import com.techelevator.items.Gum;
import com.techelevator.items.Item;

public class VendingMachine {
	public final static int MAX_ITEMS = 5;
	
	private Map<String,Slot> inventory = new LinkedHashMap<>();
	private Money money = new Money();
	
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
				return true;
			case Money.TWO_DOLLAR_BILL:
				money.addTwoDollars();
				return true;
			case Money.FIVE_DOLLAR_BILL:
				money.addFiveDollars();
				return true;
			case Money.TEN_DOLLAR_BILL:
				money.addTenDollars();
				return true;
			case Money.TWENTY_DOLLAR_BILL:
				money.addTwentyDollars();
				return true;
			case Money.FIFTY_DOLLAR_BILL:
				money.addFiftyDollars();
				return true;
			case Money.HUNDRED_DOLLAR_BILL:
				money.addHunredDollars();
				return true;
		}
		
		return false;
	}
	
	public int returnQuarters() {
		return returnMoney(Money.QUARTER);
	}
	public int returnDimes() {
		return returnMoney(Money.DIME);
	}
	public int returnNickels() {
		return returnMoney(Money.NICKEL);
	}
	
	private int returnMoney(int currency) {
		double balance = this.money.getBalance();
		
		int count = (int)(balance / currency);
		money.subtractBalance(count*currency);
		
		return count;
	}
}
