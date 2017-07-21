package cn.com.sky.ios.jdk.nio.filechannel;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

/**
 * 
 In Java NIO you can transfer data directly from one channel to another, if one of the channels is
 * a FileChannel. The FileChannel class has a transferTo() and a transferFrom() method which does
 * this for you.
 */
public class TestFileChannelTransfer {
	public static void main(String[] args) throws IOException {
		new TestFileChannelTransfer().test();
	}

	private void test() throws IOException {
		RandomAccessFile fromFile = new RandomAccessFile("fromFile.txt", "rw");
		FileChannel fromChannel = fromFile.getChannel();

		RandomAccessFile toFile = new RandomAccessFile("toFile.txt", "rw");
		FileChannel toChannel = toFile.getChannel();

		long position = 0;
		long count = fromChannel.size();

		// The FileChannel.transferFrom() method transfers data from a source channel into the
		// FileChannel.
		toChannel.transferFrom(fromChannel, position, count);

		// The transferTo() method transfer from a FileChannel into some other channel.
		fromChannel.transferTo(position, count, toChannel);

		fromFile.close();
		toFile.close();

	}
}