package cn.com.sky.ios.jdk.aio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * 
 * <pre>
 * 
 * JDK对于IO支持基本上都是基于操作系统的封装，而IO操作的核心就是如何有效的管理Channel（数据通道），JDK7对AIO的支持主要提供如下的一些封装类：
 * AsynchronousChannel：所有AIO Channel的父类。
 * AsynchronousByteChannel：支持Byte读写的Channel
 * AsynchronousDatagramChannel：支持数据包（datagram）读写的Channel
 * AsynchronousFileChannel：支持文件读写的Channel
 * AsynchronousServerSocketChannel：支持数据流读写的服务器端Channel
 * AsynchronousSocketChannel：支持数据流读写的客户端Channel
 * AsynchronousChannelGroup：支持资源共享的Channel分组
 * 
 *    对于AIO的Channel，JDK定义了2种类型的操作，
 * 1、Future operation（....):即 通过Future判断是操作是否完成。
 * 2、void operation(Object attachment, CompletionHandler handler):即通过CompletionHandler来通知异步IO完成。
 * 
 * 下面就是一个AsynchronousFileChannel实现的完整异步读写文件的例子
 * 
 * </pre>
 */
public class AsynchronousFileChannelDemo {

	static Thread current;

	public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
		if (args == null || args.length == 0) {
			System.out.println("Please input file path");
			return;
		}
		Path filePath = Paths.get(args[0]);
		AsynchronousFileChannel afc = AsynchronousFileChannel.open(filePath);
		ByteBuffer byteBuffer = ByteBuffer.allocate(16 * 1024);
		// 使用FutureDemo时，请注释掉completionHandlerDemo，反之亦然
		futureDemo(afc, byteBuffer);
		completionHandlerDemo(afc, byteBuffer);
	}

	private static void completionHandlerDemo(AsynchronousFileChannel afc, ByteBuffer byteBuffer) throws IOException {
		current = Thread.currentThread();
		afc.read(byteBuffer, 0, null, new CompletionHandler<Integer, Object>() {
			@Override
			public void completed(Integer result, Object attachment) {
				System.out.println("Bytes Read = " + result);
				current.interrupt();
			}

			@Override
			public void failed(Throwable exc, Object attachment) {
				System.out.println(exc.getCause());
				current.interrupt();
			}
		});
		System.out.println("Waiting for completion...");
		try {
			current.join();
		} catch (InterruptedException e) {
		}
		System.out.println("End");
		afc.close();
	}

	private static void futureDemo(AsynchronousFileChannel afc, ByteBuffer byteBuffer) throws InterruptedException, ExecutionException, IOException {
		Future<Integer> result = afc.read(byteBuffer, 0);
		while (!result.isDone()) {
			System.out.println("Waiting file channel finished....");
			Thread.sleep(1);
		}
		System.out.println("Finished? = " + result.isDone());
		System.out.println("byteBuffer = " + result.get());
		System.out.println(byteBuffer);
		afc.close();
	}
}
