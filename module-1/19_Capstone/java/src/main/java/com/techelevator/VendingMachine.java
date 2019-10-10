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
		
		inventory.put(slotName, slot);
	}
	
	public Map<String,Slot> getInventory() {
		return inventory;
	}
}
