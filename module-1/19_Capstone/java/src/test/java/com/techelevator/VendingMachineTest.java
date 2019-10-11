package com.techelevator;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.techelevator.Money;
import com.techelevator.Slot;
import com.techelevator.VendingMachine;
import com.techelevator.items.*;

public class VendingMachineTest {
	VendingMachine machine;
	
	@Before
	public void setup() {
		this.machine = new VendingMachine("vendingmachine.csv");
	}
	
	@Test
	public void verify_items_loaded_from_file() {
		Map<String,Slot> expected = new HashMap<>();
		Slot a1 = new Slot();
		a1.addItem(new Chip("Potato Crisps", 3.05), 5);
		expected.put("A1", a1);
		Slot a2 = new Slot();
		a2.addItem(new Chip("Stackers", 1.45), 5);
		expected.put("A2", a2);
		Slot a3 = new Slot();
		a3.addItem(new Chip("Grain Waves", 2.75), 5);
		expected.put("A3", a3);
		Slot a4 = new Slot();
		a4.addItem(new Chip("Cloud Popcorn", 3.65), 5);
		expected.put("A4", a4);
		Slot b1 = new Slot();
		b1.addItem(new Candy("Moonpie", 1.80), 5);
		expected.put("B1", b1);
		Slot b2 = new Slot();
		b2.addItem(new Candy("Cowtales", 1.50), 5);
		expected.put("B2", b2);
		Slot b3 = new Slot();
		b3.addItem(new Candy("Wonka Bar", 1.50), 5);
		expected.put("B3", b3);
		Slot b4 = new Slot();
		b4.addItem(new Candy("Crunchie", 1.75), 5);
		expected.put("B4", b4);
		Slot c1 = new Slot();
		c1.addItem(new Drink("Cola", 1.25), 5);
		expected.put("C1", c1);
		Slot c2 = new Slot();
		c2.addItem(new Drink("Dr. Salt", 1.50), 5);
		expected.put("C2", c2);
		Slot c3 = new Slot();
		c3.addItem(new Drink("Mountain Melter", 1.50), 5);
		expected.put("C3", c3);
		Slot c4 = new Slot();
		c4.addItem(new Drink("Heavy", 1.50), 5);
		expected.put("C4", c4);
		Slot d1 = new Slot();
		d1.addItem(new Gum("U-Chews", 0.85), 5);
		expected.put("D1", d1);
		Slot d2 = new Slot();
		d2.addItem(new Gum("Little League Chew", 0.95), 5);
		expected.put("D2", d2);
		Slot d3 = new Slot();
		d3.addItem(new Gum("Chiclets", 0.75), 5);
		expected.put("D3", d3);
		Slot d4 = new Slot();
		d4.addItem(new Gum("Triplemint", 0.75), 5);
		expected.put("D4", d4);
		
		Assert.assertEquals(expected, machine.getInventory());
	}
	
	@Test
	public void can_get_a1() {
		machine.putMoneyInMachine(Money.HUNDRED_DOLLAR_BILL);
		
		Assert.assertNotNull(machine.getItem("A1"));
	}

	@Test
	public void can_get_a1_lowercase() {
		machine.putMoneyInMachine(Money.HUNDRED_DOLLAR_BILL);
		
		Assert.assertNotNull(machine.getItem("a1"));
	}
	
	@Test
	public void can_get_d4() {
		machine.putMoneyInMachine(Money.HUNDRED_DOLLAR_BILL);
		
		Assert.assertNotNull(machine.getItem("D4"));
	}
	
	@Test
	public void cant_get_e1() {
		machine.putMoneyInMachine(Money.HUNDRED_DOLLAR_BILL);
		
		Assert.assertNull(machine.getItem("E1"));
	}
	
	@Test
	public void cant_get_a1_without_money() {
		Assert.assertNull(machine.getItem("A1"));
	}
	
	@Test
	public void put_money_in_machine() {
		machine.putMoneyInMachine(Money.HUNDRED_DOLLAR_BILL);
		machine.putMoneyInMachine(Money.DOLLAR_BILL);
		
		double expected = 101.00;
		
		Assert.assertEquals(expected, machine.getMoneyInMachine(), 0.001);
	}
	
	@Test
	public void cant_put_coins_in_machine() {
		machine.putMoneyInMachine(Money.DIME);
		
		double expected = 0.00;
		
		Assert.assertEquals(expected, machine.getMoneyInMachine(), 0.001);
	}
	
	@Test
	public void correct_change_returned() {
		machine.putMoneyInMachine(Money.HUNDRED_DOLLAR_BILL);
		
		Money expected = new Money();
		expected.addQuarter(400);
		
		Assert.assertEquals(expected, machine.getChange());
	}
	
	@Test
	public void ensure_a1_has_quantity() {
		Assert.assertTrue(machine.hasQuantity("A1"));
		Assert.assertTrue(machine.hasQuantity("a1"));
	}
	
	@Test
	public void make_sure_a1_sold_out() {
		machine.putMoneyInMachine(Money.HUNDRED_DOLLAR_BILL);
		for( int i=0; i<5; i++ ) {
			machine.getItem("a1");
		}

		Assert.assertFalse(machine.hasQuantity("A1"));
		Assert.assertFalse(machine.hasQuantity("a1"));
	}
	
	@Test
	public void ensure_has_balance() {
		Assert.assertFalse(machine.hasBalanceFor("A1"));
		machine.putMoneyInMachine(Money.FIVE_DOLLAR_BILL);
		Assert.assertTrue(machine.hasBalanceFor("A1"));
	}
	
	@Test
	public void choice_exists() {
		Assert.assertTrue(machine.choiceExists("a1"));
		Assert.assertFalse(machine.choiceExists("e192"));
	}
}
