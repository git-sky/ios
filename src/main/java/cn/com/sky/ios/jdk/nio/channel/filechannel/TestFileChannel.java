package cn.com.sky.ios.jdk.nio.channel.filechannel;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * <pre>
 * 在使用FileChannel之前，必须先打开它。但是，我们无法直接打开一个FileChannel，
 * 需要通过使用一个InputStream、OutputStream或RandomAccessFile来获取一个FileChannel实例。
 * 
 */
public class TestFileChannel {
	public static void main(String[] args) throws Exception {
		// File file1 = new File("e:\\a.log");
		// File file2 = new File("e:\\b.log");

		// for (int i = 0; i < 100; i++) {
		// InputStream input = new FileInputStream(file1);
		// OutputStream output = new FileOutputStream(file2, true);
		// if ((input != null) && (output != null)) {
		// int temp = 0;
		// while ((temp = input.read()) != -1) {
		// output.write(temp);
		// }
		// }
		// input.close();
		// output.close();
		// }
		copyFile();
	}

	public static void copyFile() throws Exception {
		for (int i = 0; i < 100; i++) {

			FileInputStream fin = new FileInputStream("e:/a.log");
			FileOutputStream fout = new FileOutputStream("e:/b.log", true);

			FileChannel fcin = fin.getChannel();
			FileChannel fcout = fout.getChannel();

			ByteBuffer buffer = ByteBuffer.allocate(1000);

			while ((fcin.read(buffer)) != -1) {// read()方法返回的int值表示了有多少字节被读到了Buffer中。如果返回-1，表示到了文件末尾。
				buffer.flip();
				fcout.write(buffer);
				buffer.clear();
			}

			fcout.close();
			fcin.close();
			fout.close();
			fin.close();
		}
	}
}