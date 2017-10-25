package cn.com.sky.ios.jdk.bio.socket.oneclient.oneSocketMultiThread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;

public class TimeClientHandler implements Runnable {

	Socket socket = null;
	PrintWriter os = null;
	BufferedReader is = null;
	CountDownLatch cdl = null;

	public TimeClientHandler(Socket socket, CountDownLatch cdl) throws IOException {
		os = new PrintWriter(socket.getOutputStream()); // 由Socket对象得到输出流，并构造PrintWriter对象
		is = new BufferedReader(new InputStreamReader(socket.getInputStream())); // 由Socket对象得到输入流，并构造相应的BufferedReader对象
		this.cdl = cdl;

	}

	@Override
	public void run() {

		for (int i = 0; i < 10; i++) {
			os.println(Thread.currentThread().getName());
			os.flush(); // 刷新输出流，使Server马上收到该字符串

			try {
				String resp = is.readLine();
				System.out.println(Thread.currentThread().getName() + "---> resp is : " + resp);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		cdl.countDown();
	}

}
