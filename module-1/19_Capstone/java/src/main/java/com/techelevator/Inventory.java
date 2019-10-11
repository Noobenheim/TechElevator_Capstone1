package com.techelevator;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.techelevator.items.Item;

public class Inventory {
	protected Map<Item,Integer> inventory = new LinkedHashMap<>();
	
	public boolean addItem(Item item) {
		return addItem(item, 1);
	}
	public boolean addItem(Item item, int quantity) {
		if( inventory.containsKey(item) ) {
			inventory.put(item, inventory.get(item) + quantity);
		} else {
			inventory.put(item, quantity);
		}
		return true;
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
	
	public int getQuantity(Item item) {
		if( !inventory.containsKey(item) ) {
			return 0;
		}
		
		return inventory.get(item);
	}
	
	public List<Item> getItemList() {
		List<Item> result = new ArrayList<>();
		
		for( Item key : inventory.keySet() ) {
			result.add(key);
		}
		
		return result;
	}
}
