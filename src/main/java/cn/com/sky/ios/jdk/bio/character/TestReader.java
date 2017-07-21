package cn.com.sky.ios.jdk.bio.character;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 * 掌握类的优化情况来彻底理解IO的机制！Java IO主要主要在java.io包下，分为四大块近80个类：
 * 
 * 1、基于字节操作的I/O接口：InputStream和OutputStream
 * 
 * 2、基于字符操作的I/O接口：Writer和Reader
 * 
 * 3、基于磁盘操作的I/O接口：File
 * 
 * 4、基于网络操作的I/O接口：Socket（不在java.io包下）
 * 
 * 影响IO性能的无非就是两大因素：数据的格式及存储的方式，前两类主要是数据格式方面的，后两个类是存储方式方面的：本地和网络。所以策划好这两个方面的活动，有助于我们合理使用IO。
 *
 **/
public class TestReader {

	public static String read(String filename) throws Exception {
		BufferedReader br = new BufferedReader(new FileReader(filename));
		String s;
		StringBuffer sb = new StringBuffer();
		while ((s = br.readLine()) != null) {
			sb.append(s + "\n");
		}
		br.close();
		return sb.toString();
	}

	public static void main(String[] args) throws Exception {
		System.out.println(read("e:/reader.java"));
	}
}