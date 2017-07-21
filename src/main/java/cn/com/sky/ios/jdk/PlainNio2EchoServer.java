package cn.com.sky.ios.jdk;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

/**
 * 
 * <pre>
 * 
 * EchoServer based on NIO.2
 * 
 * 
 * Unlike the original NIO implementation, NIO.2 allows you to issue IO operations and provide what
 * is called a completion handler (CompletionHandler class). This completion handler gets executed
 * after the operation completes. Therefore, execution of the completion handler is driven by the
 * underlying system and the implementation is hidden from the developer. It also guarantees that
 * only one CompletionHandler is executed for channel at the same time. This approach helps to
 * simplify the code because it removes the complexity that comes with multithreaded execution.
 * 
 * The major difference between the original NIO and NIO.2 is that you don't have to check whether
 * an event accours on the Channel and then trigger some action. In NIO.2 you can just trigger an IO
 * operation and register a completion handler with it, this handler will then get notified once the
 * operation complates. This removes the need to create your own application logic to check for
 * completion, which itself results in unnecessary processing.
 * 
 * Now let's see how the same asynchronous EchoServer would be implemented with NIO2,implementation
 * as shown in the next listing.
 * 
 * </pre>
 * 
 * @author netty in action
 */
public class PlainNio2EchoServer {

	public void serve(int port) throws IOException {
		System.out.println("Listening for connections on port " + port);
		final AsynchronousServerSocketChannel serverChannel = AsynchronousServerSocketChannel.open();
		InetSocketAddress address = new InetSocketAddress(port);
		serverChannel.bind(address);// Bind Server to port
		final CountDownLatch latch = new CountDownLatch(1);
		serverChannel.accept(null, new CompletionHandler<AsynchronousSocketChannel, Object>() {
			// Start
			// to
			// accept
			// new
			// Client
			// connections.
			// Once
			// one
			// is
			// accepted
			// the
			// CompletionHandler
			// will
			// get
			// called
			@Override
			public void completed(final AsynchronousSocketChannel channel, Object attachment) {

				serverChannel.accept(null, this); // Again accept new Client connections
				ByteBuffer buffer = ByteBuffer.allocate(100);
				channel.read(buffer, buffer, new EchoCompletionHandler(channel)); // Trigger
																					// a
																					// read
																					// operation
																					// on
																					// the
																					// Channel,
																					// the
																					// given
																					// CompletionHandler
																					// will
																					// be
																					// notified
																					// once
																					// something
																					// was
																					// read
			}

			@Override
			public void failed(Throwable throwable, Object attachment) {
				try {
					serverChannel.close(); // Close the socket on error
				} catch (IOException e) {
					// ingnore on close
				} finally {
					latch.countDown();
				}
			}
		});
		try {
			latch.await();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	private final class EchoCompletionHandler implements CompletionHandler<Integer, ByteBuffer> {
		private final AsynchronousSocketChannel channel;

		EchoCompletionHandler(AsynchronousSocketChannel channel) {
			this.channel = channel;
		}

		@Override
		public void completed(Integer result, ByteBuffer buffer) {
			buffer.flip();
			channel.write(buffer, buffer, new CompletionHandler<Integer, ByteBuffer>() { // Trigger
																							// a
																							// write
																							// operation
																							// on
																							// the
																							// Channel,
																							// the
																							// given
																							// CompletionHandler
																							// will
																							// be
																							// notified
																							// once
																							// something
																							// was
																							// written
						@Override
						public void completed(Integer result, ByteBuffer buffer) {
							if (buffer.hasRemaining()) {
								channel.write(buffer, buffer, this); // Trigger again a write
																		// operation if something is
																		// left in the ByteBuffer
							} else {
								buffer.compact();
								channel.read(buffer, buffer, EchoCompletionHandler.this); // Trigger
																							// a
																							// read
																							// operation
																							// on
																							// the
																							// Channel,
																							// the
																							// given
																							// CompletionHandler
																							// will
																							// be
																							// notified
																							// once
																							// something
																							// was
																							// read
							}
						}

						@Override
						public void failed(Throwable exc, ByteBuffer attachment) {
							try {
								channel.close();
							} catch (IOException e) {
								// ingnore on close
							}
						}
					});
		}

		@Override
		public void failed(Throwable exc, ByteBuffer attachment) {
			try {
				channel.close();
			} catch (IOException e) {
				// ingnore on cl
			}
		}
	}
}