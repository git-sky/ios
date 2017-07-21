package cn.com.sky.ios.jdk.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

/*
 * <pre>
 * 
 * accept()： 接受一个连接，返回一个Future，可通过Future获取到Socket的状态和数据。
 * accept(A attachment, CompletionHandler<AsynchronousSocketChannel,? super A> handler)：接受连接，并为连接绑定一个CompletionHandler处理Socket连接
 * 
 * bind(SocketAddress local)：把ServerSocket绑定到本地端口上，等待连接。
 * bind(SocketAddress local, int backlog)：功能和上面一个方法一样，添加了backlog参数指定队列中挂起的连接的最大个数
 * 
 * open()：开启一个异步Socket通道，
 * open(AsynchronousChannelGroup group)：开启一个异步Socket通道，并把通道加入指定的组做资源管理
 * 
 * provider()：返回这个Channel的创建者
 * 
 * setOption(SocketOption<T> name, T value)：配置Socket参数的方法。
 * 
 * 
 * 下面就是通过AsynchronousServerSocketChannel实现的一个简单的EchoServer，服务器会打印出收到的客户端输入，并把输入写回客户端。（注：代码只有在JDK1.7下才能编译通过）
 * 
 * 在客户端，可以通过telnet localhost 6025验证服务 
 * </pre>
 * 
 */
public class AIOEchoServer {
	private AsynchronousServerSocketChannel server;

	public static void main(String[] args) throws IOException {
		AIOEchoServer aioServer = new AIOEchoServer();
		aioServer.init("localhost", 6025);
	}

	private void init(String host, int port) throws IOException {
		// ChannelGroup用来管理共享资源
		AsynchronousChannelGroup group = AsynchronousChannelGroup.withCachedThreadPool(Executors.newCachedThreadPool(), 10);
		server = AsynchronousServerSocketChannel.open(group);
		// 通过setOption配置Socket
		server.setOption(StandardSocketOptions.SO_REUSEADDR, true);
		server.setOption(StandardSocketOptions.SO_RCVBUF, 16 * 1024);
		// 绑定到指定的主机，端口
		server.bind(new InetSocketAddress(host, port));
		System.out.println("Listening on " + host + ":" + port);
		// 输出provider
		System.out.println("Channel Provider : " + server.provider());
		// 等待连接，并注册CompletionHandler处理内核完成后的操作。
		server.accept(null, new CompletionHandler<AsynchronousSocketChannel, Object>() {
			final ByteBuffer buffer = ByteBuffer.allocate(1024);

			@Override
			public void completed(AsynchronousSocketChannel result, Object attachment) {
				System.out.println("waiting....");
				buffer.clear();
				try {
					// 把socket中的数据读取到buffer中
					result.read(buffer).get();
					buffer.flip();
					System.out.println("Echo " + new String(buffer.array()).trim() + " to " + result);

					// 把收到的直接返回给客户端
					result.write(buffer);
					buffer.flip();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				} finally {
					try {
						// 关闭处理完的socket，并重新调用accept等待新的连接
						result.close();
						server.accept(null, this);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

			@Override
			public void failed(Throwable exc, Object attachment) {
				System.out.print("Server failed...." + exc.getCause());
			}
		});

		// 因为AIO不会阻塞调用进程，因此必须在主进程阻塞，才能保持进程存活。
		try {
			Thread.sleep(Integer.MAX_VALUE);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}