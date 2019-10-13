package com.techelevator.view;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

public class Menu {

	private PrintWriter out;
	private Scanner in;
	private boolean clsAfterInput = false;

	public Menu(InputStream input, OutputStream output) {
		this.out = new PrintWriter(output);
		this.in = new Scanner(input);
	}
	public Menu(InputStream input, OutputStream output, boolean clsAfterInput) {
		this(input, output);
		this.clsAfterInput = clsAfterInput;
	}

	public Object getChoiceFromOptions(Object[] options) {
		return this.getChoiceFromOptions(options, null);
	}
	public Object getChoiceFromOptions(Object[] options, String displayUnder) {
		return getChoiceFromOptions(options, displayUnder, false);
	}
	public Object getChoiceFromOptions(Object[] options, boolean returnAnything) {
		return getChoiceFromOptions(options, null, returnAnything);
	}
	public Object getChoiceFromOptions(Object[] options, String displayUnder, boolean returnAnything) {
		Object choice = null;
		while (choice == null) {
			displayMenuOptions(options, displayUnder);
			choice = getChoiceFromUserInput(options, returnAnything);
		}
		return choice;
	}

	private Object getChoiceFromUserInput(Object[] options, boolean returnAnything) {
		Object choice = null;
		String userInput = in.nextLine();
		if( this.clsAfterInput ) {
			cls();
		}
		try {
			int selectedOption = Integer.valueOf(userInput);
			if (selectedOption > 0 && selectedOption <= options.length) {
				choice = options[selectedOption - 1];
			} else if( returnAnything ) {
				choice = selectedOption;
			}
		} catch (NumberFormatException e) {
			// eat the exception, an error message will be displayed below since choice will be null
		}
		if (choice == null) {
			if( returnAnything ) {
				choice = userInput;
			} else {
				displayInvalidSelection(userInput);
			}
		}
		return choice;
	}

	private void displayMenuOptions(Object[] options, String displayUnder) {
		out.println();
		for (int i = 0; i < options.length; i++) {
			int optionNum = i + 1;
			out.println("("+optionNum + ") " + options[i]);
		}
		if( displayUnder != null && !displayUnder.equals("") ) {
			out.format("\n%s\n", displayUnder);
		}
		out.print("\nPlease choose an option >>> ");
		out.flush();
	}
	
	public void cls() {
		for( int i=0; i<100; i++ ) {
			out.println();
		}
		out.flush();
	}
	
	public void displayInvalidSelection(String input) {
		out.println("\n*** " + input + " is not a valid option ***\n");
		out.flush();
	}
}
