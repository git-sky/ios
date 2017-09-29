package cn.com.sky.ios.jdk.nio.channel.socketchannel.demo1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 非阻塞式多用户聊天程序
 */
public class ChatServer {

	private Selector selector = null;
	private ServerSocketChannel ssc = null;

	private String host = "localhost";
	private int port = 8888;

	// 在线用户列表
	private ConcurrentHashMap<String, SocketChannel> onlineUsersList = null;

	public static void main(String args[]) {
		ChatServer server = new ChatServer();
		server.init();
		server.start();
	}

	public ChatServer() {
	}

	// 初始化服务器
	public void init() {
		try {
			// 1.创建选择器对象Selector
			selector = Selector.open();

			// 2.创建ServerSocketChannel
			ssc = ServerSocketChannel.open();
			// 设置ServerSocketChannel为非阻塞模式
			ssc.configureBlocking(false);
			InetSocketAddress isa = new InetSocketAddress(host, port);
			// 将与本通道相关的服务器套接字对象帮定到指定地址和端口
			ssc.socket().bind(isa);

			// 3.将ServerSocketChannel注册到Selector上，准备接收新连接请求
			ssc.register(selector, SelectionKey.OP_ACCEPT);

			// 创建在线用户列表
			onlineUsersList = new ConcurrentHashMap<String, SocketChannel>();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 启动服务器
	public void start() {
		try {

			SocketChannel sc;

			String name; // 用户名
			String msg; // 用户发言信息
			while (true) {
				// 选择当前所有处于就绪状态的通道所对应的选择键,并将这些键组成已选择键集
				int n = selector.select(); // n为已选择键集中键的个数(select()阻塞调用线程，直到有某个Channel的某个感兴趣的事件发生了)
				if (n > 0) {
					// 获取此选择器的已选择键集(这个方法返回一个包含SelectionKey对象的集合，分别对应各个准备好的Channel)
					Set<SelectionKey> readyKeys = selector.selectedKeys();
					Iterator<SelectionKey> it = readyKeys.iterator();

					while (it.hasNext()) { // 遍历当前已选择键集
						SelectionKey key = it.next();
						it.remove(); // 从当前已选择键集中移除当前键，避免重复处理

						if (key.isAcceptable()) { // 如果当前键对应的通道已准备好接受新的套接字连接
							ssc = (ServerSocketChannel) key.channel();// 获取当前键对应的可选择通道（ServerSocketChannel）
							// 接收新的套接字连接请求，返回新建的SocketChannel
							sc = (SocketChannel) ssc.accept();// 通过 ServerSocketChannel.accept()
																// 方法监听新进来的连接。当accept()方法返回的时候,它返回一个包含新进来的连接的
																// SocketChannel。因此,
																// accept()方法会一直阻塞到有新连接到达。在非阻塞模式下，accept()
																// 方法会立刻返回，如果还没有新进来的连接,返回的将是null。
							if (sc != null) {// 如果有新用户接入
								name = readMessage(sc);// 接收新上线用户姓名
								sc.configureBlocking(false);// 设置新建的SocketChannel为非阻塞模式
								sc.register(selector, SelectionKey.OP_WRITE, name); // 将当前用户名以附件的方式附带记录到新建的选择键上。
								onlineUsersList.put(name, sc);// 将新上线用户信息加入到在线用户列表
								transmitMessage(name + " in!", "--Server Info--");// 发送"新用户上线"通知
							}
						} else if (key.isWritable()) {// 否则，如果当前键对应的通道已准备好进行"写"操作
							sc = (SocketChannel) key.channel();// 获取当前键对应的可选择通道（SocketChannel）
							msg = readMessage(sc);// 接收该通道相应用户的发言信息
							name = key.attachment().toString();// 获取选择键上附带记录的当前用户名
							if (msg.equals("bye")) {// 如果用户提出要下线
								onlineUsersList.remove(name);// 从在线用户列表中移除当前用户
								key.cancel();// 注销当前选择键对应的注册关系
								sc.close(); // 关闭当前可选择通道
								transmitMessage(name + " out!", "--Server Info--");// 发送"用户下线"通知
							} else if (msg.length() > 0) {// 否则，如果接收到的用户发言信息非空（""）
								// 转发用户发言信息
								transmitMessage(msg, name);
							}
						}
					}
				}
				Thread.sleep(500);// 延时循环，降低服务器端处理负荷
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 转发用户发言信息
	public void transmitMessage(String msg, String name) {
		try {
			ByteBuffer buffer = ByteBuffer.wrap((name + ":" + msg).getBytes("UTF-8"));
			Collection<SocketChannel> channels = onlineUsersList.values();
			SocketChannel sc;
			for (SocketChannel c : channels) {
				sc = c;
				sc.write(buffer);
				buffer.flip();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String readMessage(SocketChannel sc) {
		String result = "";
		ByteBuffer buf = ByteBuffer.allocate(1024);
		try {
			sc.read(buf);
			buf.flip();
			Charset charset = Charset.forName("UTF-8");
			CharsetDecoder decoder = charset.newDecoder();
			CharBuffer charBuffer = decoder.decode(buf);
			result = charBuffer.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

}