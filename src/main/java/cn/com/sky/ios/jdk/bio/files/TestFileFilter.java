package cn.com.sky.ios.jdk.bio.files;

import java.io.File;

public class TestFileFilter{
	public static void main (String[] args) {
		TestFileFilter tff = new TestFileFilter();
		tff.dir("D:\\ex\\",".java");
		System.out.println("------------------");			
		tff.dir("D:\\ex\\",".class");		
	}
	public void dir(String path,String extension){
		File directory = new File(path);
		MyFilter mf = new MyFilter(extension);		
		File[] files = directory.listFiles(mf);
		System.out.println("·��:\t" + path);	
		System.out.print("�ļ�:");	
		for(File file : files){
			System.out.println("\t" + file.getName());	
		}
	}
}
