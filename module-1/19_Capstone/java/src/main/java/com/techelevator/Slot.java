package com.techelevator;

import com.techelevator.items.Item;

public class Slot extends Inventory {
	private int maximumItems = Integer.MAX_VALUE;

	@Override
	public void addItem(Item item, int quantity) {
		if( !hasItem(item) || inventory.get(item) + quantity < maximumItems ) {
			super.addItem(item, quantity);
		}
	}
	
	public int getMaximumItems() {
		return maximumItems;
	}
	public void setMaximumItems(int maximumItems) {
		this.maximumItems = maximumItems;
	}
	public Item getItem() {
		if( getItemList().size() > 0 ) {
			return getItemList().get(0);
		}
		return null;
	}
	
	public boolean hasItem() {
		return super.hasItem(getItem());
	}
	
	public int getQuantity() {
		return super.getQuantity(getItem());
	}
	
	@Override
	public boolean equals(Object o) {
		if( !(o instanceof Slot) ) {
			return false;
		}
		Slot test = (Slot)o;
		if( !test.getItem().equals(this.getItem()) ) {
			return false;
		}
		if( this.getMaximumItems() != test.getMaximumItems() ) {
			return false;
		}
		
		return true;
	}
}
