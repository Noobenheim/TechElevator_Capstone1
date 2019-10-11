package com.techelevator;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

public class MoneyTest {
	Money money;
	
	@Before
	public void setup() {
		money = new Money();
	}
	
	@Test
	public void ensure_balance_correct() {
		money.addDime();
		Assert.assertEquals(10, money.getBalance());
		
		money.addDime();
		Assert.assertEquals(20, money.getBalance());
		
		money.addHundredDollars(2);
		Assert.assertEquals(20020, money.getBalance());
		
		money.subtractBalance(10000);
		Assert.assertEquals(10020, money.getBalance());
	}
	
	@Test
	public void ensure_coins_add_correct_amount() {
		int expected;
		
		money.addPenny();
		expected = 1;
		Assert.assertEquals(expected, money.getBalance());
		
		money.addPenny(4);
		expected += 1*4;
		Assert.assertEquals(expected, money.getBalance());
		
		money.addNickel();
		expected += 5;
		Assert.assertEquals(expected, money.getBalance());
		
		money.addNickel(3);
		expected += 5*3;
		Assert.assertEquals(expected, money.getBalance());
		
		money.addDime();
		expected += 10;
		Assert.assertEquals(expected, money.getBalance());
		
		money.addDime(4);
		expected += 10*4;
		Assert.assertEquals(expected, money.getBalance());
		
		money.addQuarter();
		expected += 25;
		Assert.assertEquals(expected, money.getBalance());
		
		money.addQuarter(3);
		expected += 25*3;
		Assert.assertEquals(expected, money.getBalance());
		
		money.addDollar();
		expected += 100;
		Assert.assertEquals(expected, money.getBalance());
		
		money.addDollar(3);
		expected += 3*100;
		Assert.assertEquals(expected, money.getBalance());
		
		money.addTwoDollars();
		expected += 200;
		Assert.assertEquals(expected, money.getBalance());
		
		money.addTwoDollars(3);
		expected += 200*3;
		Assert.assertEquals(expected, money.getBalance());
		
		money.addFiveDollars();
		expected += 500;
		Assert.assertEquals(expected, money.getBalance());
		
		money.addFiveDollars(3);
		expected += 500*3;
		Assert.assertEquals(expected, money.getBalance());
		
		money.addTenDollars();
		expected += 1000;
		Assert.assertEquals(expected, money.getBalance());
		
		money.addTenDollars(4);
		expected += 1000*4;
		Assert.assertEquals(expected, money.getBalance());
		
		money.addTwentyDollars();
		expected += 2000;
		Assert.assertEquals(expected, money.getBalance());
		
		money.addTwentyDollars(2);
		expected += 2000*2;
		Assert.assertEquals(expected, money.getBalance());
		
		money.addFiftyDollars();
		expected += 5000;
		Assert.assertEquals(expected, money.getBalance());
		
		money.addFiftyDollars(7);
		expected += 5000*7;
		Assert.assertEquals(expected, money.getBalance());
		
		money.addHundredDollars();
		expected += 10000;
		Assert.assertEquals(expected, money.getBalance());
		
		money.addHundredDollars(2);
		expected += 10000*2;
		Assert.assertEquals(expected, money.getBalance());
	}
	
	@Test
	public void convert_to_nickels() {
		Money expected = new Money();
		expected.addNickel(2000);
		
		money.addHundredDollars();
		
		Assert.assertNotEquals(expected, money);
		money.convertToDenomination(Money.NICKEL);
		Assert.assertEquals(expected, money);
		Assert.assertNotSame(expected, money);
	}
	
	@Test
	public void convert_to_hundreds() {
		Money expected = new Money();
		expected.addHundredDollars(2);
		
		money.addPenny(20000);
		
		Assert.assertNotEquals(expected, money);
		money.convertToDenomination(Money.HUNDRED_DOLLAR_BILL);
		Assert.assertEquals(expected, money);
		Assert.assertNotSame(expected, money);
	}
	
	@Test
	public void clearing_money() {
		money.addDime(100);
		money.addNickel(58);
		money.addHundredDollars();
		money.addFiftyDollars();
		
		int expected = 100*10 + 58*5 + 10000 + 5000;
		
		Assert.assertEquals(expected, money.getBalance());
		
		money.clear();
		
		Assert.assertEquals(0, money.getBalance());
		Assert.assertEquals(new Money(), money);
	}
	
	@Test
	public void copy_money_object() {
		money.addHundredDollars();
		money.addDollar(33);
		
		Money expected = Money.copy(money);
		
		Assert.assertEquals(expected, money);
		Assert.assertNotSame(expected, money);
	}
}
