package com.techelevator;

import com.techelevator.items.Item;

// Slot is an inventory type that only allows one item
public class Slot extends Inventory {
	private int maximumItems = Integer.MAX_VALUE;
	private Item itemCompare = null;

	public boolean addItem() {
		return addItem(1);
	}
	public boolean addItem(int quantity) {
		return addItem(itemCompare, quantity);
	}
	@Override
	public boolean addItem(Item item, int quantity) {
		if( itemCompare != null && !itemCompare.equals(item) ) {
			return false;
		}
		if( quantity > maximumItems ) {
			return false;
		}
		if( !hasItem(item) || inventory.get(item) + quantity <= maximumItems ) {
			if( itemCompare == null ) {
				itemCompare = item;
			}
			return super.addItem(item, quantity);
		}
		return false;
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
		return super.hasItem(itemCompare);
	}
	
	public int getQuantity() {
		return super.getQuantity(itemCompare);
	}
	
	public boolean removeItem() {
		return super.removeItem(itemCompare);
	}
	
	@Override
	public boolean equals(Object o) {
		if( !(o instanceof Slot) ) {
			return false;
		}
		Slot test = (Slot)o;
		if( test.itemCompare == null && this.itemCompare != null ) {
			return false;
		}
		if( test.itemCompare != null && this.itemCompare == null ) {
			return false;
		}
		if( test.itemCompare != null && !test.itemCompare.equals(this.getItem()) ) {
			return false;
		}
		if( this.getMaximumItems() != test.getMaximumItems() ) {
			return false;
		}
		
		return true;
	}
}
