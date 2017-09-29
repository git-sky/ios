package cn.com.sky.ios.jdk.bio.socket.multiclient.oneSocket_oneThread;

import java.io.IOException;
import java.net.UnknownHostException;

public class TimeClientMul {

	public static final String host = "127.0.0.1";
	public static final int port = 8888;

	public static void main(String[] args) throws UnknownHostException, IOException {

		for (int i = 0; i < 10; i++) {
			System.out.println("i=" + i);

			new Thread(new TimeClientHandler()).start();
		}
	}
}
