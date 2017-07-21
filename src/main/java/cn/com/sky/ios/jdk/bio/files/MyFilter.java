package cn.com.sky.ios.jdk.bio.files;

import java.io.File;
import java.io.FileFilter;

public class MyFilter implements FileFilter {
	private String extension;

	public MyFilter(String extension) {
		this.extension = extension;
	}

	public boolean accept(File file) {
		if (file.isDirectory())
			return false;

		String name = file.getName();
		int idx = name.lastIndexOf(".");
		if ((idx == -1) || (idx == (name.length() - 1))) {
			return false;
		} else {
			return name.substring(idx).equals(extension);
		}
	}
	/*
	 * public boolean accept(File file){ return (!file.isDirectory()) &&
	 * (file.getName().endsWith(extension)); }
	 */
}
