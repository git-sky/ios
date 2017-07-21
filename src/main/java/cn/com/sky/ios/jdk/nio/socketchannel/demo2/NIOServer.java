package cn.com.sky.ios.jdk.nio.socketchannel.demo2;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * NIO服务端
 * 
 * Java NIO实际上就是多路复用IO。多路复用IO采用Reactor模式。
 * 
 * IO多路复用（IO Multiplexing）：即经典的Reactor设计模式，有时也称为异步阻塞IO，Java中的Selector和Linux中的epoll都是这种模型。
 * 
 */
public class NIOServer {

	private Selector selector;
	private ServerSocketChannel ssc;

	public void initServer(String host, int port) throws IOException {
		// 1.获得一个ServerSocketChannel通道
		ssc = ServerSocketChannel.open();
		// 设置通道为非阻塞
		ssc.configureBlocking(false);
		// 将该通道对应的ServerSocket绑定到port端口
		ssc.socket().bind(new InetSocketAddress(host, port));
		// 2.获得一个通道管理器
		selector = Selector.open();

		// 将通道管理器和该通道绑定，并为该通道注册SelectionKey.OP_ACCEPT事件,注册该事件后，
		// 当该事件到达时，selector.select()会返回，如果该事件没到达selector.select()会一直阻塞。
		// 3. 向Selector注册ServerSocketChannel
		ssc.register(selector, SelectionKey.OP_ACCEPT);

	}

	/**
	 * 采用轮询的方式监听selector上是否有需要处理的事件，如果有，则进行处理
	 */
	public void listen() throws IOException {
		System.out.println("服务端启动成功！");
		while (true) {
			// 当注册的事件到达时，方法返回；否则,该方法会一直阻塞
			System.out.println("----------阻塞开始-----------");
			selector.select();
			System.out.println("----------阻塞结束-----------");
			// 获得selector中选中的项的迭代器，选中的项为注册的事件
			Iterator<SelectionKey> ite = selector.selectedKeys().iterator();
			while (ite.hasNext()) {
				SelectionKey key = ite.next();
				// 删除已选的key,以防重复处理
				ite.remove();
				// 客户端请求连接事件
				if (key.isAcceptable()) {
					ssc = (ServerSocketChannel) key.channel();
					
					SocketChannel socketChannel = ssc.accept();// 获得和客户端连接的通道
					socketChannel.configureBlocking(false);// 设置成非阻塞
					socketChannel.write(ByteBuffer.wrap(new String("向客户端发送了一条信息").getBytes()));
					socketChannel.register(selector, SelectionKey.OP_READ);
					
				} else if (key.isReadable()) {// 获得了可读的事件
					read(key);
				}

			}
		}
	}

	/**
	 * 处理读取客户端发来的信息 的事件
	 */
	public void read(SelectionKey key) throws IOException {
		// 服务器可读取消息:得到事件发生的Socket通道
		SocketChannel channel = (SocketChannel) key.channel();
		// 创建读取的缓冲区
		ByteBuffer buffer = ByteBuffer.allocate(100);
		channel.read(buffer);
		byte[] data = buffer.array();
		String msg = new String(data).trim();
		System.out.println("服务端收到信息：" + msg);
		ByteBuffer outBuffer = ByteBuffer.wrap(msg.getBytes());
		channel.write(outBuffer);// 将消息回送给客户端
	}

	public static void main(String[] args) throws IOException {
		NIOServer server = new NIOServer();
		server.initServer("localhost", 8000);
		server.listen();
	}

}
