package com.techelevator.items;

public abstract class Item {
	private String name;
	private double price;
	private String sound;
	
	public Item(String name, double price) {
		this.name = name;
		this.price = price;
	}

	public String getName() {
		return name;
	}
	public double getPrice() {
		return price;
	}
	
	public String getSound() {
		return sound;
	}
	protected void setSound(String sound) {
		this.sound = sound;
	}
}
