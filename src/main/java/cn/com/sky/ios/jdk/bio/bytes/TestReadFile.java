package cn.com.sky.ios.jdk.bio.bytes;

import java.io.*;

import org.junit.Test;

/**
 */
public class TestReadFile {

	@Test
	public void readFile02() throws IOException {
		FileInputStream fis = new FileInputStream("f:/ip.txt");
		InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
		BufferedReader br = new BufferedReader(isr);
		String line = "";

		while ((line = br.readLine()) != null) {
			if (line.isEmpty()) {
				continue;
			}
			System.out.println(line);
			String[] arrs = line.split(",");
			if (arrs.length == 1) {
				System.out.println(line + " : " + arrs[0]);
			} else {
				System.out.println(line + " : " + arrs[0] + ":" + arrs[1]);
			}
			// System.out.println(arrs[0] + " : " + arrs[1] + " : " + arrs[2]);

		}
		br.close();
		isr.close();
		fis.close();
	}

	private long ipToInt(String ip) {
		String[] arr = ip.split("\\.");
		long ret = 0;
		for (int i = 0; i < arr.length; i++) {
			long l = 1;
			for (int j = 0; j < i; j++) {
				l *= 256;
			}
			try {
				ret += Long.parseLong(arr[arr.length - i - 1]) * l;
			} catch (Exception e) {
				ret += 0;
			}
		}
		return ret;
	}
}