package cn.com.sky.ios.jdk.bio.character;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.CharBuffer;

public class Print {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO自动生成的方法存根
		char[] buffer = new char[512]; // 一次取出的字节数大小,缓冲区大小
		int numberRead = 0;
		FileReader reader = null; // 读取字符文件的流
		PrintWriter writer = null; // 写字符到控制台的流

		try {
			reader = new FileReader("D:/David/Java/java 高级进阶/files/copy1.txt");
			writer = new PrintWriter(System.out); // PrintWriter可以输出字符到文件，也可以输出到控制台
			while ((numberRead = reader.read(buffer)) != -1) {
				writer.write(buffer, 0, numberRead);
			}
		} catch (IOException e) {
			// TODO自动生成的 catch 块
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				// TODO自动生成的 catch 块
				e.printStackTrace();
			}
			writer.close(); // 这个不用抛异常
		}

	}

}