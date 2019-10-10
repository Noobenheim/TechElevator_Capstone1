package com.techelevator;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Scanner;

import com.techelevator.items.Item;
import com.techelevator.view.Menu;

public class VendingMachineCLI {

	private static final String MAIN_MENU_OPTION_DISPLAY_ITEMS = "Display Vending Machine Items";
	private static final String MAIN_MENU_OPTION_PURCHASE = "Purchase";
	private static final String MAIN_MENU_OPTION_EXIT = "Exit";
	private static final String[] MAIN_MENU_OPTIONS = { MAIN_MENU_OPTION_DISPLAY_ITEMS, MAIN_MENU_OPTION_PURCHASE, MAIN_MENU_OPTION_EXIT };

	private Menu menu;
	private VendingMachine machine;

	public VendingMachineCLI(Menu menu) {
		this.menu = menu;
		machine = new VendingMachine();
		
		String fileName = "vendingmachine.csv";
		File file = new File(fileName);
		try(Scanner fileScanner = new Scanner(file)) {
			while( fileScanner.hasNextLine() ) {
				String line = fileScanner.nextLine();
				String[] split = line.split("\\|");
				
				if( split.length != 4 ) {
					System.out.println("Invalid format in file.");
					System.exit(1);
				}
				
				String slot = split[0];
				String name = split[1];
				double price = 0.00;
				try {
					price = Double.parseDouble(split[2]);
				} catch( NumberFormatException e ) {
					System.out.println("Price \""+split[2]+"\" is not a valid price.");
					System.exit(1);
				}
				String type = split[3];
				
				machine.loadItems(slot, name, price, type);
			}
		} catch (FileNotFoundException e) {
			System.out.println("File \""+fileName+"\" not found.");
			System.exit(1);
		}
	}

	public void run() {
		while (true) {
			String choice = (String) menu.getChoiceFromOptions(MAIN_MENU_OPTIONS);

			if (choice.equals(MAIN_MENU_OPTION_DISPLAY_ITEMS)) {
				Map<String,Slot> inventory = machine.getInventory();
				
				for( Map.Entry<String,Slot> entry : inventory.entrySet() ) {
					Slot slot = entry.getValue();
					
					// verify exists
					if( slot.getItemList().size() == 0 ) {
						continue;
					}
					Item slotItem = slot.getItemList().get(0);
					
					System.out.format("%4s %-20s $%.2f\n", entry.getKey(), slotItem.getName(), slotItem.getPrice());
				}
			} else if (choice.equals(MAIN_MENU_OPTION_PURCHASE)) {
				
			} else if (choice.contentEquals(MAIN_MENU_OPTION_EXIT)) {
				System.exit(0);
			}
		}
	}

	public static void main(String[] args) {
		Menu menu = new Menu(System.in, System.out);
		VendingMachineCLI cli = new VendingMachineCLI(menu);
		cli.run();
	}
}
