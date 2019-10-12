package com.techelevator;

import java.util.Map;

import com.techelevator.io.SalesLog;
import com.techelevator.io.UserInput;
import com.techelevator.items.Item;
import com.techelevator.items.Slot;
import com.techelevator.vendingmachine.Money;
import com.techelevator.vendingmachine.VendingMachine;
import com.techelevator.vendingmachine.VendingMachine.BadFileException;
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
	
	private VendingMachine machine;
	private Menu menu;
	private UserInput userInput = new UserInput();
	
	public static void main(String[] args) {
		VendingMachineCLI program = new VendingMachineCLI();
		program.run();
	}
	
	public VendingMachineCLI() {
		menu = new Menu(System.in, System.out);
		try {
			machine = new VendingMachine("vendingmachine.csv");
		} catch (BadFileException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		menu.cls();
		while (true) {
			Object choiceReturn = menu.getChoiceFromOptions(MAIN_MENU_OPTIONS, true);
			String choice = "";
			if( choiceReturn instanceof String ) {
				choice = (String) choiceReturn;
			} else if( choiceReturn instanceof Integer ) {
				choice = Integer.toString((int)choiceReturn);
			} else {
				System.out.println("An unknown error has occurred with making a choice.");
				System.exit(0);
			}

			if (choice.equals(MAIN_MENU_OPTION_DISPLAY_ITEMS)) {
				displayItems();
			} else if ( choice.equals("4") ) {
				SalesLog.generateLog("sales reports", machine.getInventory().values());
				
				System.out.println("Report has been generated.\n\nThank you!\n\n");
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
								if( !machine.putMoneyInMachine(insertedMoney) ) {
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
						
						if( !machine.choiceExists(itemChoice) ) {
							System.out.println("Product code does not exist.");
						} else if( !machine.hasQuantity(itemChoice) ) {
							System.out.println("SOLD OUT");
						} else if( !machine.hasBalanceFor(itemChoice) ) {
							System.out.println("You don't have enough balance");
						} else {
							Item output = machine.getItem(itemChoice);
							
							if( output == null ) {
								System.out.println("An unknown error has occurred.");
							} else {
								System.out.format("Dispensing %s for $%.2f ($%.2f remaining balance)\n", output.getName(), output.getPrice(), machine.getMoneyInMachine());
								System.out.println(output.getSound());
							}
						}
					}
				} while( !choice.equals(PURCHASE_MENU_OPTION_FINISH_TRANSACTION) );
				
				// return change
				Money change = machine.getChange();
				int quartersReturned = change.getQuarters();
				int dimesReturned = change.getDimes();
				int nickelsReturned = change.getNickels();
				
				System.out.println("Dispensing Change: ");
				System.out.format("\t%d Quarters dispensed.\n", quartersReturned);
				System.out.format("\t%d Dimes dispensed.\n", dimesReturned);
				System.out.format("\t%d Nickels dispensed.\n", nickelsReturned);
			} else if (choice.contentEquals(MAIN_MENU_OPTION_EXIT)) {
				System.out.println("Thank you for using this vending machine!\n\n\nCome back again!");
				System.exit(0);
			} else {
				menu.displayInvalidSelection(choice);
			}
		}
	}
	
	private String getBalanceMessage() {
		return String.format("Current Money Provided: $%.2f", machine.getMoneyInMachine());
	}
	
	private void displayItems() {
		Map<String,Slot> inventory = machine.getInventory();
		
		int count = 0;
		for( Map.Entry<String,Slot> entry : inventory.entrySet() ) {
			if( count>0 && count%4 == 0 ) {
				System.out.println();
			}
			
			Slot slot = entry.getValue();
			
			Item slotItem = slot.getItem();
			
			if( slotItem == null ) {
				continue;
			}
			
			System.out.format("%3s %-20s $%.2f %-10s", entry.getKey(), slotItem.getName(), slotItem.getPrice(), slot.getQuantity()>0?"("+slot.getQuantity()+" left)":"SOLD OUT");
			count++;
		}
		System.out.println();
	}
}
