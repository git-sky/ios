package cn.com.sky.ios.jdk.bio.socket.multiclient.demo2;

import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;

public class ChatClient {
	private String name;
	private Socket s;
	private DataInputStream dis;
	private DataOutputStream dos;
	private Frame f;
	private TextArea ta;
	private TextField tf;
	private boolean runnable = true;

	public static void main(String args[]) {
		ChatClient cc = new ChatClient();
		cc.createUI();
		cc.inputName();
		cc.connect();
		cc.createThread();
	}

	public void createUI() {
		f = new Frame("Client");
		ta = new TextArea();
		ta.setEditable(false);
		tf = new TextField();
		Button send = new Button("Send");
		Panel p = new Panel();
		p.setLayout(new BorderLayout());
		p.add(tf, "Center");
		p.add(send, "East");
		f.add(ta, "Center");
		f.add(p, "South");
		MyClientListener listener = new MyClientListener(this);
		send.addActionListener(listener);
		tf.addActionListener(listener);
		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				ChatClient.this.shutDown();
			}
		});
		f.setSize(400, 400);
		f.setLocation(600, 0);
		f.setVisible(true);
		tf.requestFocus();
	}

	public void inputName() {
		String name = javax.swing.JOptionPane
				.showInputDialog("Input Your Name:");
		this.setName(name);
		f.setTitle(name);
	}

	public void connect() {
		try {
			s = new Socket("127.0.0.1", 9999);
			dos = new DataOutputStream(s.getOutputStream());
			dis = new DataInputStream(s.getInputStream());
			dos.writeUTF(name);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void createThread() {
		MyClientReader reader = new MyClientReader(this);
		reader.start();
	}

	public void stop() {
		runnable = false;
	}

	public void shutDown() {
		try {
			dos.writeUTF("bye");
			ta.append("Exit in 5 seconds!");
			this.stop();
			Thread.sleep(5000);
			dis.close();
			dos.close();
			s.close();
		} catch (Exception e) {
		}
		System.exit(0);
	}

	public boolean getRunnable() {
		return runnable;
	}

	public void setName(String name) {
		this.name = name;
	}

	public DataInputStream getDataInputStream() {
		return dis;
	}

	public DataOutputStream getDataOutputStream() {
		return dos;
	}

	public TextArea getTextArea() {
		return ta;
	}

	public TextField getTextField() {
		return tf;
	}
}

class MyClientListener implements ActionListener {

	private ChatClient client;

	public MyClientListener(ChatClient client) {
		this.client = client;
	}

	public void actionPerformed(ActionEvent e) {
		TextField tf = client.getTextField();
		String info = tf.getText();
		try {
			client.getDataOutputStream().writeUTF(info);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		if (info.equals("bye")) {
			client.shutDown();
		}
		tf.setText("");
		tf.requestFocus();
	}
}

class MyClientReader extends Thread {
	private ChatClient client;

	public MyClientReader(ChatClient client) {
		this.client = client;
	}

	public void run() {
		String info;
		DataInputStream dis = client.getDataInputStream();
		TextArea ta = client.getTextArea();
		try {
			while (client.getRunnable()) {
				info = dis.readUTF();
				ta.append(info + "\n");
			}
		} catch (IOException e) {
		}
	}
}