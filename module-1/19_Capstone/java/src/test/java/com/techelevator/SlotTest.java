package com.techelevator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.techelevator.items.Candy;
import com.techelevator.items.Gum;
import com.techelevator.items.Item;

public class SlotTest {
	Slot actual;
	Item item = new Candy("Reese's", 1.89);
	
	@Before
	public void setup() {
		actual = new Slot();
	}
	
	@Test
	public void add_only_unique_items() {
		 actual.addItem(item);
		 Assert.assertEquals(item, actual.getItem());
		 Assert.assertEquals(1, actual.getQuantity());
		 Assert.assertEquals(1, actual.getQuantity(item));
		 
		 Item secondItem = new Gum("Bubblemint", 0.85);
		 actual.addItem(secondItem);
		 Assert.assertNotEquals(secondItem, actual.getItem());
		 Assert.assertEquals(0, actual.getQuantity(secondItem));
	}
	
	@Test
	public void maximum_quantity() {
		Assert.assertEquals(Integer.MAX_VALUE, actual.getMaximumItems());
		actual.setMaximumItems(5);
		Assert.assertEquals(5, actual.getMaximumItems());
		Assert.assertFalse(actual.addItem(item, 10));
		Assert.assertTrue(actual.addItem(item, 5));
		Assert.assertEquals(5, actual.getQuantity());
		Assert.assertFalse(actual.addItem(item, 5));
		Assert.assertEquals(5, actual.getQuantity());
		Assert.assertFalse(actual.addItem(item));
		Assert.assertEquals(5, actual.getQuantity());
		Assert.assertTrue(actual.removeItem());
		Assert.assertEquals(4, actual.getQuantity());
		Assert.assertFalse(actual.addItem(item, 5));
		Assert.assertFalse(actual.addItem(3));
		Assert.assertEquals(4, actual.getQuantity());
		Assert.assertTrue(actual.addItem());
		Assert.assertEquals(5, actual.getQuantity());
	}
	
	@Test
	public void empty_item() {
		Assert.assertNull(actual.getItem());
		Assert.assertFalse(actual.hasItem());
	}
	
	@Test
	public void equals_test() {
		Slot expected = new Slot();
		
		Assert.assertNotEquals(actual, new String(""));
		Assert.assertNotEquals(new Integer(81), actual);
		
		expected.setMaximumItems(4);
		Assert.assertNotEquals(expected, actual);
		expected.setMaximumItems(actual.getMaximumItems());
		Assert.assertEquals(expected, actual);
		expected.addItem(new Gum("Bubble", 0.60));
		Assert.assertNotEquals(expected, actual);
		expected = new Slot();
		Assert.assertEquals(expected, actual);
		actual.addItem(new Gum("Grape", 0.50));
		Assert.assertNotEquals(expected, actual);
		expected.addItem(new Candy("KitKat", 1.35));
		Assert.assertNotEquals(expected, actual);
	}
}
