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

	private static final String PURCHASE_MENU_OPTION_FEED_MONEY = "Feed Money";
	private static final String PURCHASE_MENU_OPTION_SELECT_PRODUCT = "Select Product";
	private static final String PURCHASE_MENU_OPTION_FINISH_TRANSACTION = "Finish Transaction";
	private static final String[] PURCHASE_MENU_OPTIONS = { PURCHASE_MENU_OPTION_FEED_MONEY, PURCHASE_MENU_OPTION_SELECT_PRODUCT, PURCHASE_MENU_OPTION_FINISH_TRANSACTION };
	
	private Menu menu;
	private VendingMachine machine;
	private UserInput userInput = new UserInput();

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
				displayItems();
			} else if (choice.equals(MAIN_MENU_OPTION_PURCHASE)) {
				do {
					choice = (String) menu.getChoiceFromOptions(PURCHASE_MENU_OPTIONS);
					
					if( choice.equals(PURCHASE_MENU_OPTION_FEED_MONEY) ) {
						double insertedMoney;
						do {
							System.out.println("Please insert currency (enter 0 to exit)");
							System.out.println("");
							System.out.format("Current Money Provided: $%.2f\n", machine.getMoneyInMachine());
							insertedMoney = userInput.getDouble();
							
							if( insertedMoney > 0 ) {
								if( !machine.putMoneyInMachine(insertedMoney) ) {
									System.out.println("Unknown currency inserted.");
								}
							}
						} while( insertedMoney > 0 );
					} else if( choice.equals(PURCHASE_MENU_OPTION_SELECT_PRODUCT) ) {
						displayItems();
						
						System.out.println("Please enter product code.");
						String itemChoice = userInput.getString();
						
						if( !machine.choiceExists(itemChoice) ) {
							System.out.println("Product code does not exist.");
						} else if( !machine.hasQuantity(itemChoice) ) {
							System.out.println("SOLD OUT");
						} else if( !machine.hasBalanceFor(itemChoice) ) {
							System.out.println("You don't have enough balance");
						} else {
							Item output = machine.getItem(itemChoice);
							
							System.out.format("Dispensing %s for $%.2f ($%.2f remaining balance)\n", output.getName(), output.getPrice(), machine.getMoneyInMachine());
							System.out.println(output.getSound());
						}
					}
				} while( !choice.equals(PURCHASE_MENU_OPTION_FINISH_TRANSACTION) );
				
				// return change
				int quartersReturned = machine.returnQuarters();
				int dimesReturned = machine.returnDimes();
				int nickelsReturned = machine.returnNickels();
				
				System.out.println("Dispensing Change: ");
				System.out.format("\t%d Quarters dispensed.\n", quartersReturned);
				System.out.format("\t%d Dimes dispensed.\n", dimesReturned);
				System.out.format("\t%d Nickels dispensed.\n", nickelsReturned);
			} else if (choice.contentEquals(MAIN_MENU_OPTION_EXIT)) {
				System.exit(0);
			}
		}
	}
	
	private void displayItems() {
		Map<String,Slot> inventory = machine.getInventory();
		
		for( Map.Entry<String,Slot> entry : inventory.entrySet() ) {
			Slot slot = entry.getValue();
			
			Item slotItem = slot.getItem();
			
			if( slotItem == null ) {
				continue;
			}
			
			System.out.format("%4s %-20s $%.2f\n", entry.getKey(), slotItem.getName(), slotItem.getPrice());
		}
	}

	public static void main(String[] args) {
		Menu menu = new Menu(System.in, System.out);
		VendingMachineCLI cli = new VendingMachineCLI(menu);
		cli.run();
	}
}
