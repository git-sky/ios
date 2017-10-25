package cn.com.sky.ios.jdk.bio.socket.oneclient.oneSocketMultiThread;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.CountDownLatch;

/**
 * 单连接多线程
 */
public class TimeClientMul {

	public static void main(String[] args) throws UnknownHostException, IOException {

		try {
			Socket socket = new Socket("127.0.0.1", 4700); // 向本机的4700端口发出客户请求

			CountDownLatch cdl = new CountDownLatch(10);

			TimeClientHandler handler = new TimeClientHandler(socket, cdl);

			for (int i = 0; i < 10; i++) {
				new Thread(handler).start();
			}

			cdl.await();

			socket.close(); // 关闭Socket

		} catch (Exception e) {

		}
	}
}
