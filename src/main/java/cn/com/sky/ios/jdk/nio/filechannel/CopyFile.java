package cn.com.sky.ios.jdk.nio.filechannel;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * FileChannel无法设置为非阻塞模式，它总是运行在阻塞模式下。
 */

public class CopyFile {
	public static void main(String[] args) throws Exception {
		String infile = "f:\\copy.sql";
		String outfile = "f:\\copy.txt";
		// 获取源文件和目标文件的输入输出流
		FileInputStream fin = new FileInputStream(infile);
		FileOutputStream fout = new FileOutputStream(outfile);
		// 获取输入输出通道
		FileChannel fcin = fin.getChannel();
		FileChannel fcout = fout.getChannel();

		// long fileSize = fcin.size();
		// System.out.println("fileSize="+fileSize);
		//
		// long fileOutSize = fcout.size();
		// System.out.println("fileOutSize="+fileOutSize);

		// 创建缓冲区
		ByteBuffer buffer = ByteBuffer.allocate(1);// allocate()
													// 方法分配一个具有指定大小的底层数组，并将它包装到一个缓冲区对象中
		while (true) {
			// clear方法重设缓冲区，使它可以接受读入的数据
			buffer.clear();
			// 从输入通道中将数据读到缓冲区
			int r = fcin.read(buffer);
			// read方法返回读取的字节数，可能为零，如果该通道已到达流的末尾，则返回-1
			if (r == -1) {
				break;
			}
			// flip方法让缓冲区可以将新读入的数据写入另一个通道
			buffer.flip();
			// 从缓冲区将数据写入输出通道中
			fcout.write(buffer);

			// FileChannel实例的size()方法将返回该实例所关联文件的大小
			long fileSize = fcin.size();
			System.out.println("fileSize=" + fileSize);

			long fileOutSize = fcout.size();
			System.out.println("fileOutSize=" + fileOutSize);

			// 调用position()方法获取FileChannel的当前位置
			long pos = fcin.position();
			System.out.println(pos);

			// 调用position(long pos)方法设置FileChannel的当前位置
			// fcin.position(1);
			// long posin = fcin.position();
			// System.out.println(posin);

			long pos2 = fcout.position();
			System.out.println(pos2);
		}
	}
}