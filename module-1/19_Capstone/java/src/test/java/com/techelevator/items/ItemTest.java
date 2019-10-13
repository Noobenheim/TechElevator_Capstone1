package com.techelevator.items;

import org.junit.Test;
import org.junit.Assert;

public class ItemTest {
	Candy candy = new Candy("candy", 1.15);
	Chip chip = new Chip("chip", 1.50);
	Drink drink = new Drink("drink", 2);
	Gum gum = new Gum("gum", 0.85);
	
	@Test
	public void polymorphism_test() {
		Assert.assertTrue(candy instanceof Item);
		Assert.assertTrue(chip instanceof Item);
		Assert.assertTrue(drink instanceof Item);
		Assert.assertTrue(gum instanceof Item);
	}
	
	@Test
	public void price_test() {
		Assert.assertEquals(1.15, candy.getPrice(), 0.001);
		Assert.assertEquals(115, candy.getCents());
		Assert.assertEquals(1.50, chip.getPrice(), 0.001);
		Assert.assertEquals(150, chip.getCents());
		Assert.assertEquals(2.0, drink.getPrice(), 0.001);
		Assert.assertEquals(200, drink.getCents());
		Assert.assertEquals(0.85, gum.getPrice(), 0.001);
		Assert.assertEquals(85, gum.getCents());
	}
	
	@Test
	public void name_test() {
		Assert.assertEquals("candy", candy.getName());
		Assert.assertEquals("chip", chip.getName());
		Assert.assertEquals("drink", drink.getName());
		Assert.assertEquals("gum", gum.getName());
	}
	
	@Test
	public void sound_test() {
		Assert.assertEquals("Munch Munch, Yum!", candy.getSound());
		Assert.assertEquals("Crunch Crunch, Yum!", chip.getSound());
		Assert.assertEquals("Glug Glug, Yum!", drink.getSound());
		Assert.assertEquals("Chew Chew, Yum!", gum.getSound());
	}
	
	@Test
	public void equals_test() {
		Candy candy2 = new Candy("candy", 1.15);
		Chip chip2 = new Chip("chip", 1.50);
		Drink drink2 = new Drink("drink", 2.00);
		Gum gum2 = new Gum("gum", .85);
		
		Assert.assertNotEquals("candy", candy);
		Assert.assertNotEquals(candy, "candy");
		
		Assert.assertEquals(candy, candy2);
		Assert.assertEquals(chip, chip2);
		Assert.assertEquals(drink, drink2);
		Assert.assertEquals(gum, gum2);
		
		candy2 = new Candy("candy", 1.16);
		
		Assert.assertNotEquals(candy, candy2);
		
		candy2 = new Candy("candy2", 1.15);
		
		Assert.assertNotEquals(candy, candy2);
		
		chip2 = new Chip("candy", 1.15);
		
		Assert.assertNotEquals(chip2, candy);
	}
	
	@Test
	public void string_test() {
		Assert.assertEquals("candy (1.15)", candy.toString());
		Assert.assertEquals("chip (1.50)", chip.toString());
		Assert.assertEquals("drink (2.00)", drink.toString());
		Assert.assertEquals("gum (0.85)", gum.toString());
	}
}
