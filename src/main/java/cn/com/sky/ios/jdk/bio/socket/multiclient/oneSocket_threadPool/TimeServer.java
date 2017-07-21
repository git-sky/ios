package cn.com.sky.ios.jdk.bio.socket.multiclient.oneSocket_threadPool;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 一个连接 <=====> 一个线程，使用线程池
 */
public class TimeServer {

	public static final int port = 8888;

	public static void main(String[] args) throws IOException {

		ServerSocket server = null;
		try {
			server = new ServerSocket(port);
			System.out.println("The time server is start in port : " + port);
			TimeServerHandlerThreadPool executor = new TimeServerHandlerThreadPool(50, 10000);// 创建IO任务线程池
			while (true) {
				Socket socket = server.accept();
				executor.execute(new TimeServerHandler(socket));
			}
		} finally {
			if (server != null) {
				System.out.println("The time server close");
				server.close();
				server = null;
			}
		}
	}
}
