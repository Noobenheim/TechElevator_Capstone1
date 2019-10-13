package com.techelevator.io;

import java.io.ByteArrayInputStream;

import org.junit.Test;
import org.junit.Assert;

public class UserInputTest {
	@Test
	public void gets_integer() {
		Assert.assertEquals(1, getUserInput("1").getInt());
		Assert.assertEquals(0, getUserInput("hello").getInt());
		Assert.assertEquals(-1, getUserInput("hello").getInt(-1));
	}
	
	@Test
	public void gets_double() {
		Assert.assertEquals(1.9, getUserInput("1.9").getDouble(), 0.001);
		Assert.assertEquals(0.0, getUserInput("hello").getDouble(), 0.001);
		Assert.assertEquals(-1.9, getUserInput("hello").getDouble(-1.9), 0.001);
	}
	
	@Test
	public void get_string() {
		Assert.assertEquals("Hello, World!", getUserInput("Hello, World!").getString());
	}
	
	private UserInput getUserInput(String input) {
		ByteArrayInputStream bais = new ByteArrayInputStream(String.valueOf(input).getBytes());
		
		return new UserInput(bais);
	}
}
