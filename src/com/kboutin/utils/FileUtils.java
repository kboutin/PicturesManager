package com.kboutin.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FileUtils {

	private final static Logger logger = LogManager.getLogger(FileUtils.class);

	public final static String getFileMD5(File f) {

		logger.debug("Calculating MD5 hash for " + f.getPath());
		String md5 = "";
		MessageDigest md5Algo = null;

		try {
			md5Algo = MessageDigest.getInstance("MD5");

			FileInputStream fis = new FileInputStream(f);
			int bytesRead = 0;
			byte[] buff = new byte[8192];

			while ((bytesRead = fis.read(buff)) != -1) {
				md5Algo.update(buff, 0, bytesRead);
			}

			fis.close();
			byte[] checksum = md5Algo.digest();

			for (int i = 0; i < checksum.length; i++) {
				md5 += Integer.toString((checksum[i] & 0xff) + 0x100, 16).substring(1);
			}

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return md5;
	}

	public final static boolean isPicture(File f) {

		String fileName = f.getName().toLowerCase();

		return f.length() > StringUtils.KB &&
				(fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")
						|| fileName.endsWith(".png") || fileName.endsWith(".gif")
						|| fileName.endsWith(".tiff"));
	}

	public final static boolean isMovie(File f) {

		String fileName = f.getName().toLowerCase();

		return f.length() > StringUtils.KB &&
				(fileName.endsWith(".avi") || fileName.endsWith(".mp4")
						|| fileName.endsWith(".mpg") || fileName.endsWith(".mpeg")
						|| fileName.endsWith(".mov"));
	}

	public final static String getReadableFileSize(File f) {

		return getReadableFileSize(f.length());
	}

	public final static String getReadableFileSize(long size) {

		String res = null;
		int unit = 0;
		double totalSize = size;

		while (totalSize > 1024) {
			totalSize /= 1024;
			unit++;
		}
		res = String.format("%.2f", totalSize);

		switch (unit) {
		case 0:
			res += " o";
			break;
		case 1:
			res += " ko";
			break;
		case 2:
			res += " Mo";
			break;
		case 3:
			res += " Go";
			break;
		}

		return res;
	}
}
