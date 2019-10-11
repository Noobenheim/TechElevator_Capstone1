package com.techelevator;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

import com.techelevator.items.Candy;
import com.techelevator.items.Chip;
import com.techelevator.items.Drink;
import com.techelevator.items.Gum;
import com.techelevator.items.Item;
import com.techelevator.view.Menu;

public class VendingMachine {
	public final static int MAX_ITEMS = 5;
	
	private static final String MAIN_MENU_OPTION_DISPLAY_ITEMS = "Display Vending Machine Items";
	private static final String MAIN_MENU_OPTION_PURCHASE = "Purchase";
	private static final String MAIN_MENU_OPTION_EXIT = "Exit";
	private static final String[] MAIN_MENU_OPTIONS = { MAIN_MENU_OPTION_DISPLAY_ITEMS, MAIN_MENU_OPTION_PURCHASE, MAIN_MENU_OPTION_EXIT };

	private static final String PURCHASE_MENU_OPTION_FEED_MONEY = "Feed Money";
	private static final String PURCHASE_MENU_OPTION_SELECT_PRODUCT = "Select Product";
	private static final String PURCHASE_MENU_OPTION_FINISH_TRANSACTION = "Finish Transaction";
	private static final String[] PURCHASE_MENU_OPTIONS = { PURCHASE_MENU_OPTION_FEED_MONEY, PURCHASE_MENU_OPTION_SELECT_PRODUCT, PURCHASE_MENU_OPTION_FINISH_TRANSACTION };
	
	private Map<String,Slot> inventory = new LinkedHashMap<>();
	private Money money = new Money();
	private Menu menu;
	private UserInput userInput = new UserInput();
	
	public VendingMachine(String inventoryInputFile, Menu menu) {
		this.menu = menu;
		menu.cls();
		
		File file = new File(inventoryInputFile);
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
				
				loadItems(slot, name, price, type);
			}
		} catch (FileNotFoundException e) {
			System.out.println("File \""+inventoryInputFile+"\" not found.");
			System.exit(1);
		}
	}
	
	public void loadItems(String slotName, String name, double price, String type) {
		loadItems(slotName, name, price, type, MAX_ITEMS);
	}
	public void loadItems(String slotName, String name, double price, String type, int quantity) {
		Item item;
		Slot slot = new Slot();
		
		type = type.toLowerCase();
		switch( type ) {
			case "candy":
				item = new Candy(name, price);
				break;
			case "drink":
				item = new Drink(name, price);
				break;
			case "chip":
				item = new Chip(name, price);
				break;
			case "gum":
				item = new Gum(name, price);
				break;
			default:
				return;
		}
		
		slot.addItem(item, quantity);
		
		inventory.put(slotName.toUpperCase(), slot);
	}
	
	public Map<String,Slot> getInventory() {
		return inventory;
	}
	
	public Item getItem(String choice) {
		Slot slot = getSlot(choice);
		
		if( slot == null ) {
			return null;
		}
		
		Item item = slot.getItem();
		if( money.getBalance() >= item.getPrice() && slot.removeItem(slot.getItem()) ) {
			money.subtractBalance(item.getCents());
			return item;
		}
		return null;
	}
	public boolean hasQuantity(String choice) {
		Slot slot = getSlot(choice);
		
		if( slot == null ) {
			return false;
		}
		
		return slot.hasItem();
	}
	public boolean hasBalanceFor(String choice) {
		Slot slot = getSlot(choice);
		
		if( slot == null ) {
			return false;
		}
		
		if( slot.getItem().getPrice() > money.getBalance() ) {
			return false;
		}
		
		return true;
	}
	public boolean choiceExists(String choice) {
		return getSlot(choice)!=null;
	}
	private Slot getSlot(String choice) {
		choice = choice.toUpperCase();
		
		if( !inventory.containsKey(choice) ) {
			return null;
		}
		
		return inventory.get(choice);
	}
	
	public double getMoneyInMachine() {
		return money.getBalance() / 100.0;
	}
	
	public boolean putMoneyInMachine(double amount) {
		int amountInCents = (int)(amount * 100.0);
		
		switch( amountInCents ) {
			case Money.DOLLAR_BILL:
				money.addDollar();
				return true;
			case Money.TWO_DOLLAR_BILL:
				money.addTwoDollars();
				return true;
			case Money.FIVE_DOLLAR_BILL:
				money.addFiveDollars();
				return true;
			case Money.TEN_DOLLAR_BILL:
				money.addTenDollars();
				return true;
			case Money.TWENTY_DOLLAR_BILL:
				money.addTwentyDollars();
				return true;
			case Money.FIFTY_DOLLAR_BILL:
				money.addFiftyDollars();
				return true;
			case Money.HUNDRED_DOLLAR_BILL:
				money.addHunredDollars();
				return true;
		}
		
		return false;
	}
	
	private int returnQuarters() {
		return returnMoney(Money.QUARTER);
	}
	private int returnDimes() {
		return returnMoney(Money.DIME);
	}
	private int returnNickels() {
		return returnMoney(Money.NICKEL);
	}
	
	private int returnMoney(int currency) {
		double balance = this.money.getBalance();
		
		int count = (int)(balance / currency);
		money.subtractBalance(count*currency);
		
		return count;
	}
	
	public void run() {
		while (true) {
			String choice = (String) menu.getChoiceFromOptions(MAIN_MENU_OPTIONS);

			if (choice.equals(MAIN_MENU_OPTION_DISPLAY_ITEMS)) {
				displayItems();
			} else if (choice.equals(MAIN_MENU_OPTION_PURCHASE)) {
				do {
					choice = (String) menu.getChoiceFromOptions(PURCHASE_MENU_OPTIONS, getBalanceMessage());
					
					if( choice.equals(PURCHASE_MENU_OPTION_FEED_MONEY) ) {
						double insertedMoney;
						do {
							System.out.println("Please insert currency (enter 0 to exit)");
							System.out.println();
							System.out.format(getBalanceMessage());
							System.out.println();
							insertedMoney = userInput.getDouble();
							menu.cls();
							
							if( insertedMoney > 0 ) {
								if( !putMoneyInMachine(insertedMoney) ) {
									System.out.println("Unknown currency inserted.\n");
								}
							}
						} while( insertedMoney > 0 );
					} else if( choice.equals(PURCHASE_MENU_OPTION_SELECT_PRODUCT) ) {
						displayItems();
						
						System.out.println(String.format("\n%s\n", getBalanceMessage()));
						
						System.out.println("Please enter product code.");
						String itemChoice = userInput.getString();
						menu.cls();
						
						if( !choiceExists(itemChoice) ) {
							System.out.println("Product code does not exist.");
						} else if( !hasQuantity(itemChoice) ) {
							System.out.println("SOLD OUT");
						} else if( !hasBalanceFor(itemChoice) ) {
							System.out.println("You don't have enough balance");
						} else {
							Item output = getItem(itemChoice);
							
							System.out.format("Dispensing %s for $%.2f ($%.2f remaining balance)\n", output.getName(), output.getPrice(), getMoneyInMachine());
							System.out.println(output.getSound());
						}
					}
				} while( !choice.equals(PURCHASE_MENU_OPTION_FINISH_TRANSACTION) );
				
				// return change
				int quartersReturned = returnQuarters();
				int dimesReturned = returnDimes();
				int nickelsReturned = returnNickels();
				
				System.out.println("Dispensing Change: ");
				System.out.format("\t%d Quarters dispensed.\n", quartersReturned);
				System.out.format("\t%d Dimes dispensed.\n", dimesReturned);
				System.out.format("\t%d Nickels dispensed.\n", nickelsReturned);
			} else if (choice.contentEquals(MAIN_MENU_OPTION_EXIT)) {
				System.exit(0);
			}
		}
	}
	
	private String getBalanceMessage() {
		return String.format("Current balance : $%.2f", getMoneyInMachine());
	}
	
	private void displayItems() {
		Map<String,Slot> inventory = getInventory();
		
		int count = 0;
		for( Map.Entry<String,Slot> entry : inventory.entrySet() ) {
			if( count>0 && count%4 == 0 ) {
				System.out.println();
			} else if( count>0 ){
				System.out.print("\t");
			}
			
			Slot slot = entry.getValue();
			
			Item slotItem = slot.getItem();
			
			if( slotItem == null ) {
				continue;
			}
			
			System.out.format("%3s %-20s $%.2f", entry.getKey(), slotItem.getName(), slotItem.getPrice());
			count++;
		}
		System.out.println();
	}
}
