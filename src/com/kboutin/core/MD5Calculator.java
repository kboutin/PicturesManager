package com.kboutin.core;

import java.io.File;

import com.kboutin.utils.FileUtils;

public class MD5Calculator implements Runnable {

	private File file = null;
	private String md5;

	public MD5Calculator(File file) {
		this.file = file;
	}

	public final String getMD5() {
		return md5;
	}

	@Override
	public void run() {

		md5 = FileUtils.getFileMD5(file);
	}
}
