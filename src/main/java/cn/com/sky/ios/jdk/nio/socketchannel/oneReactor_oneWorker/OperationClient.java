package cn.com.sky.ios.jdk.nio.socketchannel.oneReactor_oneWorker;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class OperationClient {

	// Direct byte buffer for reading
	private static ByteBuffer dbuf = ByteBuffer.allocateDirect(1024);

	private static void query(String host, int port) throws IOException {
		byte inBuffer[] = new byte[100];
		InetSocketAddress isa = new InetSocketAddress(host, port);
		SocketChannel sc = null;
		while (true) {
			try {
				System.in.read(inBuffer);
				sc = SocketChannel.open();
				sc.connect(isa);
				dbuf.clear();
				dbuf.put(inBuffer);
				dbuf.flip();
				sc.write(dbuf);
				dbuf.clear();

			} finally {
				if (sc != null)
					sc.close();
			}
		}
	}

	public static void main(String[] args) throws IOException {
		query("localhost", 8090);// A+B
		// query("localhost", 9090);//A*B
	}
}
