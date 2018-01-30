package cn.com.sky.ios.jdk.bio.socket.oneclient.demo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;

public class AncientServer {
	public static void main(String args[]) {

		try {
			ServerSocket server = null;
			try {
				server = new ServerSocket(4700); // 创建一个ServerSocket在端口4700监听客户请求
			} catch (Exception e) {
				System.out.println("can not listen to:" + e);// 出错，打印出错信息
			}

			Socket socket = null;
			try {
				System.out.println("Server已经启动，监听端口: " + server.getLocalPort() + "， 等待客户端注册。。。");
				socket = server.accept();// 使用accept()阻塞等待客户请求，有客户请求到来则产生一个Socket对象，并继续执行

				System.out.println("有一个客户端注册上来了。。。");
				System.out.println("Client:" + socket.getRemoteSocketAddress());
				System.out.println("Server IP:" + socket.getLocalAddress() + ", Port:" + socket.getLocalPort());
			} catch (Exception e) {
				System.out.println("Error." + e); // 出错，打印出错信息
			}

			String line;

			BufferedReader is = new BufferedReader(new InputStreamReader(socket.getInputStream()));// 由Socket对象得到输入流，并构造相应的BufferedReader对象
			PrintWriter os = new PrintWriter(socket.getOutputStream());// 由Socket对象得到输出流，并构造PrintWriter对象

			System.out.println("\n等待客户端输入。。。");
			line = is.readLine();// 从标准输入读入一字符串

			while (!Objects.equals(line, "bye")) {// 如果该字符串为 "bye"，则停止循环
				System.out.println("Client发来:" + line);
				os.println("Server已收到刚发送的:" + line); // 向客户端输出该
				os.flush();// 刷新输出流，使Client马上收到该字符串

				System.out.println("\n等待客户端输入。。。");
				line = is.readLine(); // 从系统标准输入读入一字符串
			} // 继续循环

			os.close(); // 关闭Socket输出流
			is.close(); // 关闭Socket输入流
			socket.close(); // 关闭Socket

			server.close(); // 关闭ServerSocket
		} catch (Exception e) {
			System.out.println("Error:" + e);// 出错，打印出错信息
		}
	}
}
