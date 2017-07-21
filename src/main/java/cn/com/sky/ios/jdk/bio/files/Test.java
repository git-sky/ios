package cn.com.sky.ios.jdk.bio.files;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class Test {

	public static void main(String[] args) {
		try {
			FileInputStream fis = new FileInputStream(
					"/home/tobacco/test/res.txt");
			int sum = 0;
			int n;
			long t1 = System.currentTimeMillis();
			try {
				while ((n = fis.read()) >= 0) {
					sum += n;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			long t = System.currentTimeMillis() - t1;
			System.out.println("sum:" + sum + "  time:" + t);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			FileInputStream fis = new FileInputStream(
					"/home/tobacco/test/res.txt");
			BufferedInputStream bis = new BufferedInputStream(fis);
			int sum = 0;
			int n;
			long t1 = System.currentTimeMillis();
			try {
				while ((n = bis.read()) >= 0) {
					sum += n;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			long t = System.currentTimeMillis() - t1;
			System.out.println("sum:" + sum + "  time:" + t);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		MappedByteBuffer buffer = null;
		try {
			buffer = new RandomAccessFile("/home/tobacco/test/res.txt", "rw")
					.getChannel().map(FileChannel.MapMode.READ_WRITE, 0,
							1253244);
			int sum = 0;
			int n;
			long t1 = System.currentTimeMillis();
			for (int i = 0; i < 1253244; i++) {
				n = 0x000000ff & buffer.get(i);
				sum += n;
			}
			long t = System.currentTimeMillis() - t1;
			System.out.println("sum:" + sum + "  time:" + t);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
