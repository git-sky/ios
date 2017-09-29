package cn.com.sky.ios.jdk.nio.channel.filechannel;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class TestFileChannel4 {
	public static void main(String args[]) throws Exception {
		System.out.println("begin");
		long start = System.currentTimeMillis();
		int _5M = 1024 * 1024 * 5;
		File fin = new File("e:\\a.log"); // �ļ���С200M
		File fout = new File("e:\\b.log");
		FileChannel fcin = new RandomAccessFile(fin, "r").getChannel();
		ByteBuffer rBuffer = ByteBuffer.allocate(_5M);
		FileChannel fcout = new RandomAccessFile(fout, "rws").getChannel();
		ByteBuffer wBuffer = ByteBuffer.allocateDirect(_5M);
		readFileByLine(_5M, fcin, rBuffer, fcout, wBuffer);
		System.out.println((System.currentTimeMillis() - start) +"ms");
		
	}

	public static void readFileByLine(int bufSize, FileChannel fcin,
			ByteBuffer rBuffer, FileChannel fcout, ByteBuffer wBuffer) {
		String enterStr = "\n";
		try {
			byte[] bs = new byte[bufSize];
			StringBuilder strBuf = new StringBuilder("");
			String tempString = null;
			while (fcin.read(rBuffer) != -1) {
				int rSize = rBuffer.position();
				rBuffer.rewind();
				rBuffer.get(bs);
				rBuffer.clear();
				tempString = new String(bs, 0, rSize);
				int fromIndex = 0;
				int endIndex = 0;
				while ((endIndex = tempString.indexOf(enterStr, fromIndex)) != -1) {
					String line = tempString.substring(fromIndex, endIndex);
					line = strBuf.toString() + line;
					writeFileByLine(fcout, wBuffer, line);
					strBuf.delete(0, strBuf.length());
					fromIndex = endIndex + 1;
				}

				if (rSize > tempString.length()) {
					strBuf.append(tempString.substring(fromIndex,
							tempString.length()));
				} else {
					strBuf.append(tempString.substring(fromIndex, rSize));
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void writeFileByLine(FileChannel fcout, ByteBuffer wBuffer,
			String line) {
		try {
			fcout.write(wBuffer.wrap(line.getBytes()), fcout.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
