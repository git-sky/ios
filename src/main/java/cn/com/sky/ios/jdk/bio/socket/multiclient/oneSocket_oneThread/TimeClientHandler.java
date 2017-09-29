package cn.com.sky.ios.jdk.bio.socket.multiclient.oneSocket_oneThread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TimeClientHandler implements Runnable {

	public static final String host = "127.0.0.1";
	public static final int port = 8888;

	@Override
	public void run() {

		Socket socket = null;
		BufferedReader in = null;
		PrintWriter out = null;
		try {
			socket = new Socket(host, port);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);

			String name = Thread.currentThread().getName();
			int index = 0;
			String s = null;
			while ((s = in.readLine()) != null) {
				index++;
				out.println("name---> " + s + " index--> " + index);
				System.out.println("name---> " + s + " index--> " + index);
				String resp = in.readLine();
				System.out.println("name---> " + name + " index--> " + index + " resp is : " + resp);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.close();
				out = null;
			}

			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				in = null;
			}

			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				socket = null;
			}
		}
	}
}
