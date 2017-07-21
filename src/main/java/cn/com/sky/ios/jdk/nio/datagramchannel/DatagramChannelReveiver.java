package cn.com.sky.ios.jdk.nio.datagramchannel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.charset.Charset;

/**
 *
 */
public class DatagramChannelReveiver {

	public static void main(String[] args) {
		try {
			receive();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void receive() throws IOException {
		DatagramChannel channel = DatagramChannel.open(); //DatagramChannel的打开方式
		channel.socket().bind(new InetSocketAddress(10022));

		ByteBuffer buffer = ByteBuffer.allocate(60);
		while (channel.receive(buffer) == null) {//receive()方法会将接收到的数据包内容复制到指定的Buffer. 如果Buffer容不下收到的数据，多出的数据将被丢弃。
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		buffer.flip();
		String recStr = Charset.forName("utf-8").newDecoder().decode(buffer).toString();
		System.out.println(recStr);

		channel.close();

	}

}