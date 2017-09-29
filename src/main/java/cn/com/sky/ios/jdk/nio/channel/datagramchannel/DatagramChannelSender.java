package cn.com.sky.ios.jdk.nio.channel.datagramchannel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

/**
 *
 */
public class DatagramChannelSender {

	public static void main(String[] args) {
		try {
			send();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void send() throws IOException {
		DatagramChannel channel = DatagramChannel.open();// DatagramChannel的打开方式
		ByteBuffer buffer = ByteBuffer.wrap("下雨的夜晚很安静".getBytes("utf-8"));
		channel.send(buffer, new InetSocketAddress("localhost", 10022));// 通过send()方法从DatagramChannel发送数据

		channel.close();
	}

}