package cn.com.sky.ios.jdk.bio.socket.multiclient.demo2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.Hashtable;

/**
 * 阻塞式多用户聊天程序
 */
public class ChatServer {

	public static final int port = 9999;

	public static void main(String args[]) {
		Hashtable<String, DataOutputStream> userList = new Hashtable<String, DataOutputStream>();
		ServerSocket ss = null;
		try {
			ss = new ServerSocket(port);
			while (true) {
				Socket socket = ss.accept();
				new ReaderHandler(socket, userList).start();// 一个连接一个线程
			}
		} catch (IOException e) {
			try {
				ss.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}
}

class ReaderHandler extends Thread {

	private Socket socket;
	private Hashtable<String, DataOutputStream> userList;

	public ReaderHandler(Socket socket, Hashtable<String, DataOutputStream> userList) {
		this.socket = socket;
		this.userList = userList;
	}

	public void run() {
		try {
			DataInputStream dis = new DataInputStream(socket.getInputStream());
			DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
			String name = dis.readUTF();
			userList.put(name, dos);

			String info;
			transmitMessage(name + " in!", "--Server Info--");
			while (true) {
				info = dis.readUTF();
				if (info.equals("bye")) {
					Thread.sleep(1000);
					dos.close();
					dis.close();
					userList.remove(name);
					transmitMessage(name + " out!", "--Server Info--");
					break;
				} else if (info.length() > 0) {
					transmitMessage(info, name);
				}
			}
		} catch (IOException | InterruptedException e1) {
			e1.printStackTrace();
		}
	}

	public void transmitMessage(String msg, String name) {
		Collection<DataOutputStream> doses = userList.values();
		DataOutputStream dos;
		for (DataOutputStream o : doses) {
			dos = o;
			try {
				dos.writeUTF(name + ":" + msg);
			} catch (Exception e) {
			}
		}
	}
}