package com.techelevator.items;

public abstract class Item {
	private String name;
	private int price;
	private String sound;
	
	public Item(String name, double price) {
		this.name = name;
		this.price = (int)Math.round(price * 100.0);
	}

	public String getName() {
		return name;
	}
	public double getPrice() {
		return price / 100.0;
	}
	public int getCents() {
		return price;
	}
	
	public String getSound() {
		return sound;
	}
	protected void setSound(String sound) {
		this.sound = sound;
	}
	
	@Override
	public boolean equals(Object o) {
		if( !( o instanceof Item ) ) {
			return false;
		}
		Item test = (Item)o;
		
		if( test.getCents() != this.getCents() ) {
			return false;
		}
		if( !test.getName().equals(this.getName()) ) {
			return false;
		}
		if( !test.getSound().equals(this.getSound()) ) {
			return false;
		}
		
		return true;
	}
	
	@Override
	public String toString() {
		return String.format("%s (%.2f)", getName(), getPrice());
	}
}
