package cn.com.sky.ios.jdk.nio.pipe;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe;

/**
 * A Java NIO Pipe is a one-way data connection between two threads. A Pipe has a source channel and
 * a sink channel. You write data to the sink channel. This data can then be read from the source
 * channel.
 * 
 */
public class TestPipe {

	public static void main(String[] args) throws IOException {

		Pipe pipe = Pipe.open();// 通过Pipe.open()方法打开管道

		Pipe.SinkChannel sinkChannel = pipe.sink();// 要向管道写数据，需要访问sink通道。

		String newData = "New String to write to file..." + System.currentTimeMillis();
		ByteBuffer buf = ByteBuffer.allocate(48);
		buf.clear();
		buf.put(newData.getBytes());

		buf.flip();

		while (buf.hasRemaining()) {
			sinkChannel.write(buf);// 通过调用SinkChannel的write()方法，将数据写入SinkChannel
		}

		System.out.println(newData.length());

		Pipe.SourceChannel sourceChannel = pipe.source();// 从管道读取数据，需要访问source通道

		ByteBuffer bufRead = ByteBuffer.allocate(48);

		int bytesRead = sourceChannel.read(bufRead);// 调用source通道的read()方法来读取数据
		
		System.out.println(bytesRead);

	}
}
