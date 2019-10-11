package com.techelevator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.techelevator.items.Item;

public class Inventory {
	Map<Item,Integer> inventory = new HashMap<>();
	
	public void addItem(Item item) {
		addItem(item, 1);
	}
	public void addItem(Item item, int quantity) {
		if( inventory.containsKey(item) ) {
			inventory.put(item, inventory.get(item) + quantity);
		} else {
			inventory.put(item, quantity);
		}
	}
	
	public boolean removeItem(Item item) {
		return removeItem(item, 1);
	}
	public boolean removeItem(Item item, int quantity) {
		if( !inventory.containsKey(item) ) {
			return false;
		}
		if( inventory.get(item) - quantity < 0 ) {
			return false;
		}
		inventory.put(item, inventory.get(item) - quantity);
		return true;
	}
	
	public boolean hasItem(Item item) {
		return inventory.containsKey(item) && inventory.get(item) > 0;
	}
	
	public List<Item> getItemList() {
		List<Item> result = new ArrayList<>();
		
		for( Item key : inventory.keySet() ) {
			result.add(key);
		}
		
		return result;
	}
}
