package com.techelevator.items;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.techelevator.items.Candy;
import com.techelevator.items.Chip;
import com.techelevator.items.Drink;
import com.techelevator.items.Gum;
import com.techelevator.items.Inventory;
import com.techelevator.items.Item;

public class InventoryTest {
	Inventory inventory;
	Item firstItem = new Drink("Soda", 1.85);
	Item secondItem = new Chip("Doritos", 0.50);
	Item thirdItem = new Gum("Ripple", 0.85);
	Item fourthItem = new Candy("Hershey", 1.00);
	
	@Before
	public void setup() {
		inventory = new Inventory();
	}
	
	@Test
	public void allows_multiple_items() {
		inventory.addItem(firstItem);
		inventory.addItem(secondItem);
		
		List<Item> expected = new ArrayList<Item>();
		expected.add(firstItem);
		expected.add(secondItem);
		
		Assert.assertEquals(expected, inventory.getItemList());

		inventory.addItem(thirdItem);
		expected.add(thirdItem);
		
		Assert.assertEquals(expected, inventory.getItemList());
		
		inventory.addItem(fourthItem);
		expected.add(fourthItem);
		
		Assert.assertEquals(expected, inventory.getItemList());
	}
	
	@Test
	public void keep_track_of_item_quantities() {
		inventory.addItem(firstItem);
		inventory.addItem(secondItem, 4);
		inventory.addItem(thirdItem, 3);
		inventory.addItem(fourthItem, 2);
		
		Assert.assertEquals(2, inventory.getQuantity(fourthItem));
		Assert.assertEquals(3, inventory.getQuantity(thirdItem));
		Assert.assertEquals(4, inventory.getQuantity(secondItem));
		Assert.assertEquals(1, inventory.getQuantity(firstItem));
		
		inventory.removeItem(thirdItem);
		
		Assert.assertEquals(2, inventory.getQuantity(fourthItem));
		Assert.assertEquals(2, inventory.getQuantity(thirdItem));
		Assert.assertEquals(4, inventory.getQuantity(secondItem));
		Assert.assertEquals(1, inventory.getQuantity(firstItem));
		
		inventory.removeItem(fourthItem, 2);
		
		Assert.assertEquals(0, inventory.getQuantity(fourthItem));
		Assert.assertEquals(2, inventory.getQuantity(thirdItem));
		Assert.assertEquals(4, inventory.getQuantity(secondItem));
		Assert.assertEquals(1, inventory.getQuantity(firstItem));
		
		inventory.removeItem(firstItem, 2); // won't remove any if it goes over
		
		Assert.assertEquals(0, inventory.getQuantity(fourthItem));
		Assert.assertEquals(2, inventory.getQuantity(thirdItem));
		Assert.assertEquals(4, inventory.getQuantity(secondItem));
		Assert.assertEquals(1, inventory.getQuantity(firstItem));
		
		Assert.assertEquals(0, inventory.getQuantity(new Drink("Mountain Fizz", 1.00)));
	}
	
	@Test
	public void ensure_boolean_return_from_remove() {
		Assert.assertFalse(inventory.removeItem(thirdItem));
		
		inventory.addItem(firstItem);
		inventory.addItem(secondItem);
		
		Assert.assertTrue(inventory.removeItem(firstItem));
		Assert.assertFalse(inventory.removeItem(firstItem));
	}
	
	@Test
	public void ensure_has_item() {
		inventory.addItem(firstItem);
		
		Assert.assertTrue(inventory.hasItem(firstItem));
		Assert.assertFalse(inventory.hasItem(secondItem));
		
		inventory.removeItem(firstItem);
		
		Assert.assertFalse(inventory.hasItem(firstItem));
		
		Assert.assertFalse(inventory.hasItem(secondItem));
	}
	
	@Test
	public void add_multiple_items_of_the_same() {
		inventory.addItem(firstItem, 3);
		Assert.assertEquals(3, inventory.getQuantity(firstItem));
		
		inventory.addItem(firstItem, 2);
		Assert.assertEquals(5, inventory.getQuantity(firstItem));
	}
}
