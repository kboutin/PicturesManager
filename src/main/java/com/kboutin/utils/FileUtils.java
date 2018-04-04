package com.kboutin.utils;

import com.kboutin.core.Picture;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.stream.Stream;

public class FileUtils {

	private final static Logger logger = LogManager.getLogger(FileUtils.class);

	public final static String NEW_LINE = System.getProperty("line.separator");
	public final static String FILE_SEP = System.getProperty("file.separator");
	public final static String USER_HOME = System.getProperty("user.home");

	private final static long ONE_KB = 1024;
	public final static long ONE_MB = ONE_KB * ONE_KB;

	public static void scanDirectory(Path path) throws IOException {

		Files.newDirectoryStream(path)
				.forEach(System.out::println);
	}

	public static void scanDir(File f, List<Picture> lstPictures) {

		if (f.isDirectory()) {
			Stream.of(f.listFiles()).forEach(subDirectory -> scanDir(subDirectory, lstPictures));
		} else if (f.isFile()) {
			if(FileUtils.isPicture(f)) {
				Picture p = new Picture(f);
				lstPictures.add(p);
				//addPicture(p);
				//addMetadataForPicture(p);
			}
		}
	}

	public static String getFileMD5(File f) {

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

		} catch (NoSuchAlgorithmException | IOException e) {
			e.printStackTrace();
		}

		return md5;
	}

	public static String getMD5Hash(File f) {

		String md5Hash = null;
		try {
			md5Hash = DigestUtils.md5Hex(new FileInputStream(f));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return md5Hash;
	}

	public static String getSHA1Hash(File f) {

		String sha1Hash = null;
		try {
			sha1Hash = DigestUtils.sha1Hex(new FileInputStream(f));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return sha1Hash;
	}

	public static boolean isPicture(File f) {

		String fileName = f.getName().toLowerCase();

		return f.length() > ONE_KB &&
				(fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")
						|| fileName.endsWith(".png") || fileName.endsWith(".gif")
						|| fileName.endsWith(".tiff"));
	}

	public static boolean isMovie(File f) {

		String fileName = f.getName().toLowerCase();

		return f.length() > ONE_KB &&
				(fileName.endsWith(".avi") || fileName.endsWith(".mp4")
						|| fileName.endsWith(".mpg") || fileName.endsWith(".mpeg")
						|| fileName.endsWith(".mov"));
	}

	public static String getReadableFileSize(File f) {

		return getReadableFileSize(f.length());
	}

	public static String getReadableFileSize(long size) {

		String res;
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
