package com.techelevator;

import com.techelevator.view.Menu;

public class VendingMachineCLI {
	public static void main(String[] args) {
		Menu menu = new Menu(System.in, System.out);
		VendingMachine machine = new VendingMachine("vendingmachine.csv", menu);
		machine.run();
	}
}
