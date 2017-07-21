package cn.com.sky.ios.jdk.nio.filechannel;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 
 * 缓冲区本质上是一块可以写入数据，然后可以从中读取数据的内存。这块内存被包装成NIO Buffer对象，并提供了一组方法，用来方便的访问该块内存。
 * 
 * 一、 Java NIO 有以下Buffer类型
 * 
 * ByteBuffer MappedByteBuffer CharBuffer DoubleBuffer FloatBuffer IntBuffer LongBuffer ShortBuffer
 * 
 * 二、 写数据到Buffer有两种方式：
 * 
 * 从Channel写到Buffer。 通过Buffer的put()方法写到Buffer里。
 * 
 * 从Channel写到Buffer的例子: int bytesRead = inChannel.read(buf); //read into buffer.
 * 
 * 通过put方法写Buffer的例子： buf.put(127);
 * 
 * 
 * 三、从Buffer中读取数据有两种方式：
 * 
 * 从Buffer读取数据到Channel。 使用get()方法从Buffer中读取数据。
 * 
 * 从Buffer读取数据到Channel的例子：int bytesWritten = inChannel.write(buf); //read from buffer into channel.
 * 
 * 使用get()方法从Buffer中读取数据的例子：byte aByte = buf.get();
 * 
 * 四、方法
 * 
 * rewind()方法
 * 
 * Buffer.rewind()将position设回0，所以你可以重读Buffer中的所有数据。limit保持不变，仍然表示能从Buffer中读取多少个元素（byte、char等）。
 * 
 * clear()与compact()方法
 * 
 * 一旦读完Buffer中的数据，需要让Buffer准备好再次被写入。可以通过clear()或compact()方法来完成。
 * 
 * 如果调用的是clear()方法，position将被设回0，limit被设置成 capacity的值。换句话说，Buffer
 * 被清空了。Buffer中的数据并未清除，只是这些标记告诉我们可以从哪里开始往Buffer里写数据。
 * 
 * 如果Buffer中有一些未读的数据，调用clear()方法，数据将“被遗忘”，意味着不再有任何标记会告诉你哪些数据被读过，哪些还没有。
 * 
 * 如果Buffer中仍有未读的数据，且后续还需要这些数据，但是此时想要先先写些数据，那么使用compact()方法。
 * 
 * compact()方法将所有未读的数据拷贝到Buffer起始处。然后将position设到最后一个未读元素正后面。limit属性依然像clear()方法一样，设置成capacity。
 * 现在Buffer准备好写数据了，但是不会覆盖未读的数据。
 * 
 * mark()与reset()方法
 * 
 * 通过调用Buffer.mark()方法，可以标记Buffer中的一个特定position。之后可以通过调用Buffer.reset()方法恢复到这个position。
 * 
 * equals()
 * 
 * 当满足下列条件时，表示两个Buffer相等：
 * 
 * 有相同的类型（byte、char、int等）。 Buffer中剩余的byte、char等的个数相等。 Buffer中所有剩余的byte、char等都相同。
 * 
 * 如你所见，equals只是比较Buffer的一部分，不是每一个在它里面的元素都比较。实际上，它只比较Buffer中的剩余元素。
 * 
 * compareTo()方法
 * 
 * 比较两个Buffer的剩余元素(byte、char等)， 如果满足下列条件，则认为一个Buffer“小于”另一个Buffer：
 * 
 * 第一个不相等的元素小于另一个Buffer中对应的元素 。 所有元素都相等，但第一个Buffer比另一个先耗尽(第一个Buffer的元素个数比另一个少)。
 * 
 * 
 * 五、Buffer的基本用法
 * 
 * 使用Buffer读写数据一般遵循以下四个步骤：
 * 
 * 1.写入数据到Buffer
 * 
 * 2.用flip()方法
 * 
 * 3.从Buffer中读取数据
 * 
 * 4.调用clear()方法或者compact()方法
 * 
 * 当向buffer写入数据时，buffer会记录下写了多少数据。一旦要读取数据，需要通过flip()方法将Buffer从写模式切换到读模式。在读模式下，可以读取之前写入到buffer的所有数据。
 * 
 * 一旦读完了所有的数据，就需要清空缓冲区，让它可以再次被写入。有两种方式能清空缓冲区：调用clear()或compact()方法。clear()方法会清空整个缓冲区。compact()
 * 方法只会清除已经读过的数据。任何未读的数据都被移到缓冲区的起始处，新写入的数据将放到缓冲区未读数据的后面。
 * 
 */
public class TestFileChannel2 {
	public static void main(String[] args) throws IOException {
		RandomAccessFile aFile = new RandomAccessFile("e:/nio-data.txt", "rw");
		FileChannel inChannel = aFile.getChannel();
		ByteBuffer buf = ByteBuffer.allocate(48);// 分配48字节capacity的ByteBuffer
		int bytesRead = inChannel.read(buf);// read into buffer.
		while (bytesRead != -1) {
			System.out.println("Read " + bytesRead);
			buf.flip();// make buffer ready for read
						// flip()方法将Buffer从写模式切换到读模式。调用flip()方法会将position设回0，并将limit设置成之前position的值。
			while (buf.hasRemaining()) {
				System.out.print((char) buf.get());// read 1 byte at a time
			}

			buf.clear();// make buffer ready for writing
			bytesRead = inChannel.read(buf);
		}
		aFile.close();
	}
}