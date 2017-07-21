package cn.com.sky.ios.jdk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Description EchoServer based on blocking IO
 * 
 *              One thread is needed for each new client connection that is served. You may argue
 *              that we could make use of a thread pool to get rid of the overhead of creating the
 *              threads, but that would only help for a while. The fundamental problem still
 *              remains: the number of concurrent clients that can be served is limited to the
 *              number of threads that you can have "alive" at the same time. When your application
 *              needs to handle thousands of concurrent clients, that's a big problem.
 * 
 * @author netty in action
 */
public class PlainEchoServer {

	public void serve(int port) throws IOException {
		final ServerSocket socket = new ServerSocket(port); // Bind server to port
		try {
			while (true) {
				System.out.println("Waiting connection from client...");
				final Socket clientSocket = socket.accept(); // Block until new client connection is
																// accepted
				System.out.println("Accepted connection from " + clientSocket);
				new Thread(new Runnable() { // Create new thread to handle client connection
							@Override
							public void run() {
								try {
									BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
									PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
									while (true) { // Read data from client and write it back
										writer.println(reader.readLine());
										writer.flush();
									}
								} catch (IOException e) {
									e.printStackTrace();
									try {
										clientSocket.close();
									} catch (IOException ex) {
										// ignore on close
									}
								}
							}
						}).start(); // Start thread
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		try {
			new PlainEchoServer().serve(8080);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}