package net.jaguargaming.backdoor.commandhandlers;

import net.jaguargaming.backdoor.utils.Stroboskop;

public class CMDstrobe {
	
	public static void handle(String[] args,  Stroboskop stroboskop) throws IllegalArgumentException, NumberFormatException {
		if (args.length != 2) {
			throw new IllegalArgumentException("Wrong argument length");
		}
		if (stroboskop.isDestroyed()) {
			throw new IllegalArgumentException("Stroboskop has been destroyed");
		}
		if (args[0].equalsIgnoreCase("state")) {
			boolean state;
			if (args[1].equalsIgnoreCase("on"))
				state = true;
			else if (args[1].equalsIgnoreCase("off"))
				state = false;
			else
				throw new IllegalArgumentException("State not found");
			stroboskop.setActive(state);
		} else if (args[0].equalsIgnoreCase("speed")) {
			try {
				stroboskop.setSpeed(Integer.parseInt(args[1]));
			} catch (NumberFormatException e) {
				throw e;
			}
		} else {
			throw new IllegalArgumentException("Action command not found");
		}
	}
	
}
