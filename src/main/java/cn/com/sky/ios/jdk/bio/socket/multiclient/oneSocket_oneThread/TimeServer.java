package cn.com.sky.ios.jdk.bio.socket.multiclient.oneSocket_oneThread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 一个连接 <=====> 一个线程
 */
public class TimeServer {

	public static final int port = 8888;

	public static void main(String[] args) throws IOException {
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(port);
			System.out.println("The time server is start in port : " + port);
			while (true) {
				Socket socket = serverSocket.accept();
				new Thread(new TimeServerHandler(socket)).start();
			}
		} finally {
			if (serverSocket != null) {
				System.out.println("The time server close");
				serverSocket.close();
				serverSocket = null;
			}
		}
	}
}
