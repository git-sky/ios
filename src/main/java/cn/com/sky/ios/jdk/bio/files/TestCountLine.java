package cn.com.sky.ios.jdk.bio.files;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author Administrator
 */
public class TestCountLine {

	public static String fname = "e:\\b.log";

	public static void main(String args[]) {
		testBIO();
		testNIO();
	}

	public static void testNIO() {

		FileInputStream fis = null;
		try {
			long now;
			int lines = 1, readCount;
			fis = new FileInputStream(new File(fname));
			int size = 1024 * 512;
			ByteBuffer buffer = ByteBuffer.allocate(size);
			now = System.currentTimeMillis();
			FileChannel channel = fis.getChannel();
			while (0 != (readCount = channel.read(buffer))) {
				buffer.flip();
				for (int i = 0; i < readCount; i++) {
					if (((int) buffer.get(i)) == '\n') {
						lines++;
					}
				}
			}
			System.out.println("nio lines = " + lines);
			System.out.println(System.currentTimeMillis() - now);
			channel.close();
		} catch (FileNotFoundException ex) {
			Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			try {
				if (null != fis) {
					fis.close();
				}
			} catch (IOException ex) {
				Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null,
						ex);
			}
		}
	}

	public static void testBIO() {

		FileInputStream fis = null;
		try {
			long now;
			int lines = 1;
			fis = new FileInputStream(new File(fname));

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					fis));

			now = System.currentTimeMillis();
			while (reader.readLine() != null) {
				lines++;
			}
			System.out.println("bio lines = " + lines);
			System.out.println(System.currentTimeMillis() - now);
			reader.close();
		} catch (FileNotFoundException ex) {
			Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			try {
				if (null != fis) {
					fis.close();
				}
			} catch (IOException ex) {
				Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null,
						ex);
			}
		}

	}
}
