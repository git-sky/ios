package cn.com.sky.ios.jdk.bio.socket.multiclient.demo1;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatServer {

	List<Client> clients = new ArrayList<Client>();

	public static void main(String[] args) {
		new ChatServer().start();
	}

	public void start() {
		ServerSocket ss = null;
		try {
			ss = new ServerSocket(8888); // 启动服务器。
		} catch (BindException e) {
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			while (true) {
				Socket s = ss.accept(); // 等待客户端接入
				Client c = new Client(s); // 每当有一个客户端接入，就建立一个Client对象。
				new Thread(c).start(); // 并建立一个线程。
				clients.add(c);// 把每一个Client对象加入clients中。

			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				ss.close(); // 关闭服务器。
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	class Client implements Runnable {

		private DataInputStream dis = null;
		private DataOutputStream dos = null;

		public Client(Socket s) {
			try {
				dis = new DataInputStream(s.getInputStream());
				dos = new DataOutputStream(s.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("A client connected");
		}

		public void run() {
			try {
				while (true) {
					String str = dis.readUTF();
					for (int i = 0; i < clients.size(); i++) {
						Client c = clients.get(i);
						c.send(str);
					}
					System.out.println(str);
				}
			} catch (IOException e) {
				System.out.println("A Client Closed!");
			} finally {
				try {
					if (dis != null)
						dis.close();
					if (dos != null)
						dos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		public void send(String str) {
			try {
				dos.writeUTF(str);
			} catch (IOException e) {
				clients.remove(this);
				System.out.println("我管理的进程退出了，我被从clients中移除了！");
			}
		}

	}
}
