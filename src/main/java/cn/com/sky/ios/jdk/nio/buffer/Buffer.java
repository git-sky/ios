package cn.com.sky.ios.jdk.nio.buffer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.MappedByteBuffer;
import java.nio.ShortBuffer;

import org.junit.Test;

/**
 * 所有的基本数据类型都有相应的缓冲区类（布尔型除外）。
 */
public class Buffer {

	@Test
	public void test1() {

		ByteBuffer byteBuffer = ByteBuffer.allocate(100);
		byteBuffer.put((byte) 'A').put((byte) 'B');
		// byteBuffer.flip();
		// byte[] dst = new byte[1];
		// byteBuffer.get(dst);// 数据方向：byteBuffer->dst,当dst.length> limit - position时,就会报错。
		// System.out.println(Arrays.toString(dst));

		byte[] src = new byte[10];
		byteBuffer.put(src);// 数据方向：src->byteBuffer,当src.length > limit - position时，会报错

		byte[] array = new byte[10];
		ByteBuffer bf = ByteBuffer.wrap(array);
		
		System.out.println(byteBuffer.order());
		
		System.out.println(ByteOrder.nativeOrder());

	}

//	@Test
	public void test() {

		ShortBuffer shortBuffer = ShortBuffer.allocate(100);

		IntBuffer intBuffer;
		LongBuffer longBuffer;

		FloatBuffer floatBuffer;
		DoubleBuffer doubleBuffer;

		CharBuffer charBuffer;

		MappedByteBuffer mappedByteBuffer;

		// byteBuffer.clear();
	}
}
