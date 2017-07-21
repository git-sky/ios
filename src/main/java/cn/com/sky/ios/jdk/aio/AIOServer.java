package cn.com.sky.ios.jdk.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * AsynchronousChannel：支持异步通道，包括服务端AsynchronousServerSocketChannel和普通AsynchronousSocketChannel等实现。
 * CompletionHandler：用户处理器。定义了一个用户处理就绪事件的接口，由用户自己实现，异步io的数据就绪后回调该处理器消费或处理数据。
 * AsynchronousChannelGroup：一个用于资源共享的异步通道集合。处理IO事件和分配给CompletionHandler。
 * 
 * <pre>
 * 以一个简单监听服务端为例，基本过程是：
 * 1.    启动一个服务端通道
 * 2.    定义一个事件处理器，用户事件完成的时候处理，如消费数据。
 * 3.    向系统注册一个感兴趣的事件，如接受数据，并把事件完成的处理器传递给系统。
 * 4.    都已经交待完毕，可以只管继续做自己的事情了，操作系统在完成事件后通过其他的线程会自动调用处理器完成事件处理。
 * 
 * 以下用一个例子来简单实现，一个服务端和客户端。服务端监听客户端的消息，并打印出来。
 * 
 * 服务端写了两种处理实现方式，startWithCompletionHandler是通过Handler来处理，startWithFuture是通过Future方式来处理。
 * startWithCompletionHandler方法里可以看到调用accept()完成异步注册后，线程就可以继续自己的处理了，完全不被这个io所中断。
 */
public class AIOServer {

	public final static int PORT = 9888;
	private AsynchronousServerSocketChannel server;

	public AIOServer() throws IOException {
		server = AsynchronousServerSocketChannel.open().bind(new InetSocketAddress(PORT));
	}

	public void startWithFuture() throws InterruptedException, ExecutionException, TimeoutException {
		System.out.println("Server listen on " + PORT);
		Future<AsynchronousSocketChannel> future = server.accept();
		AsynchronousSocketChannel socket = future.get();
		ByteBuffer readBuf = ByteBuffer.allocate(1024);
		readBuf.clear();
		socket.read(readBuf).get(100, TimeUnit.SECONDS);
		readBuf.flip();
		System.out.printf("received message:" + new String(readBuf.array()));
		System.out.println(Thread.currentThread().getName());

	}

	public void startWithCompletionHandler() throws InterruptedException, ExecutionException, TimeoutException {
		System.out.println("Server listen on " + PORT);
		// 注册事件和事件完成后的处理器
		server.accept(null, new CompletionHandler<AsynchronousSocketChannel, Object>() {
			final ByteBuffer buffer = ByteBuffer.allocate(1024);

			public void completed(AsynchronousSocketChannel result, Object attachment) {
				System.out.println(Thread.currentThread().getName());
				System.out.println("start");
				try {
					buffer.clear();
					result.read(buffer).get(100, TimeUnit.SECONDS);
					buffer.flip();
					System.out.println("received message: " + new String(buffer.array()));
				} catch (InterruptedException | ExecutionException e) {
					System.out.println(e.toString());
				} catch (TimeoutException e) {
					e.printStackTrace();
				} finally {
					try {
						result.close();
						server.accept(null, this);
					} catch (Exception e) {
						System.out.println(e.toString());
					}
				}

				System.out.println("end");
			}

			@Override
			public void failed(Throwable exc, Object attachment) {
				System.out.println("failed: " + exc);
			}
		});
		// 主线程继续自己的行为
		while (true) {
			System.out.println("main thread");
			Thread.sleep(1000);
		}

	}

	public static void main(String[] args) throws Exception {
		new AIOServer().startWithCompletionHandler();
		// new AIOServer().startWithFuture();
	}
}