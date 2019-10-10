package com.techelevator.items;

public abstract class Item {
	private String name;
	private int price;
	private String sound;
	
	public Item(String name, int price) {
		this.name = name;
		this.price = price;
	}
	public Item(String name, double price) {
		this.name = name;
		this.price = (int)(price * 100.0);
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
}
