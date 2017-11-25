package com.kboutin.core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.kboutin.utils.FileUtils;
import com.kboutin.utils.StringUtils;

public class Picture implements Comparable<Picture> {

	private final static Logger logger = LogManager.getLogger(Picture.class);

	private String filePath = null;
	private String hash = null;
	private long fileSize = 0;
	private List<String> lstDuplicates = null;
	private Map<String, String> metadata = null;

	/*private static List<String> lstAcceptedMetadata = new ArrayList<>(
			Arrays.asList(
					"Aperture Value",
					"F-Number",
					"Focal Length",
					"ISO Speed Ratings",
					"Make",
					"Model",
					"Image Height",
					"Image Width",
					"Shutter Speed Value")
			);*/

	/*
	 * "Aperture Value",
		"F-Number",
		"Focal Length",
		"ISO Speed Ratings",
		"Make",
		"Model",
		"Image Height",
		"Image Width",
		"Shutter Speed Value"
	 */
	/*private String apertureValue = null;
	private String fValue = null;
	private String focalLength = null;
	private String iso = null;
	private String maker = null;
	private String model = null;
	private String height = null;
	private String width = null;
	private String shutterSpeed = null;*/

	public Picture(File fPicture) {

		if (!FileUtils.isPicture(fPicture)) {
			return;
		}
		logger.debug("Building a picture from file " + fPicture.getName());
		this.filePath = fPicture.getPath();
		this.fileSize = fPicture.length();
		this.hash = FileUtils.getFileMD5(fPicture);
		this.lstDuplicates = new ArrayList<>();
		this.metadata = extractMetaData();

		/*MD5Calculator md5Calculator = new MD5Calculator(fPicture);
		Thread tMD5Calculator = new Thread(md5Calculator);
		tMD5Calculator.start();
		try {
			tMD5Calculator.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		this.hash = md5Calculator.getMD5();

		MetadataExtractor metadataExtractor = new MetadataExtractor(fPicture);
		Thread tMetadataExtractor = new Thread(metadataExtractor);
		tMetadataExtractor.start();
		try {
			tMetadataExtractor.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		this.metadata = metadataExtractor.getMetadata();*/
	}

	private Map<String, String> extractMetaData() {

		logger.debug("Extracting metadata for file : " + filePath);
		Map<String, String> mapMetaData = new TreeMap<>();

		Metadata metadata = null;
		try {
			metadata = ImageMetadataReader.readMetadata(new File(filePath));
		} catch (ImageProcessingException | IOException e) {
			metadata = null;
		}
		if (metadata != null) {
			metadata.getDirectories().forEach(directory -> {
				directory.getTags().forEach(tag -> mapMetaData.put(tag.getTagName(), tag.getDescription()));
			});
		}

		return mapMetaData;
	}

	public final String getFileName() {
		return filePath.substring(filePath.lastIndexOf(StringUtils.FILE_SEP) + 1);
	}

	public final String getFilePath() {
		return filePath;
	}

	public final String getHash() {
		return hash;
	}

	public final String getSize() {
		return FileUtils.getReadableFileSize(new File(filePath));
	}

	public final Map<String, String> getMetadata() {
		return metadata;
	}

	public final String getMetadataAsString() {

		StringBuilder s = new StringBuilder();
		metadata.keySet().forEach(key -> s.append("[" + key + "] -> " + metadata.get(key) + StringUtils.NEW_LINE));
		return s.toString();
	}

	/*public final String getApertureValue() {
		return apertureValue;
	}

	public final void setApertureValue(String apertureValue) {
		this.apertureValue = apertureValue;
	}

	public final String getfValue() {
		return fValue;
	}

	public final void setfValue(String fValue) {
		this.fValue = fValue;
	}

	public final String getFocalLength() {
		return focalLength;
	}

	public final void setFocalLength(String focalLength) {
		this.focalLength = focalLength;
	}

	public String getIso() {
		return iso;
	}

	public void setIso(String iso) {
		this.iso = iso;
	}

	public final String getMaker() {
		return maker;
	}

	public final void setMaker(String maker) {
		this.maker = maker;
	}

	public final String getModel() {
		return model;
	}

	public final void setModel(String model) {
		this.model = model;
	}

	public final String getHeight() {
		return height;
	}

	public final void setHeight(String height) {
		this.height = height;
	}

	public final String getWidth() {
		return width;
	}

	public final void setWidth(String width) {
		this.width = width;
	}

	public final String getShutterSpeed() {
		return shutterSpeed;
	}

	public final void setShutterSpeed(String shutterSpeed) {
		this.shutterSpeed = shutterSpeed;
	}*/

	public final void setMetadata(Map<String, String> metadata) {
		this.metadata = metadata;
	}

	public final List<String> getDuplicates() {
		return lstDuplicates;
	}

	public final long getWastedSpace() {
		return fileSize * lstDuplicates.size();
	}

	public final int countDuplicates() {
		return lstDuplicates.size();
	}

	public final boolean hasDuplicates() {
		return lstDuplicates.size() > 0;
	}

	public final void addDuplicate(String duplicate) {
		lstDuplicates.add(duplicate);
	}

	public final long deleteDuplicates() {

		long deletedSpace = 0;
		for (Iterator<String> iter = lstDuplicates.iterator(); iter.hasNext(); ) {

			String duplicate = iter.next();
			File tmp = new File(duplicate);
			long fileSize = tmp.length();
			logger.debug("Deleting " + tmp.getPath());
			if (tmp.delete()) {

				logger.info("Deleted " + tmp.getPath());
				deletedSpace += fileSize;
				iter.remove();
			}
		}
		return deletedSpace;
	}

	public final void printMetaData() {
		metadata.keySet().stream().forEach(category -> System.out.format("[%s] -> %s\n", category, metadata.get(category)));
	}

	public final void printInfos() {

		logger.debug("Picture with filePath : " + filePath);
		logger.debug("fileSize : " + fileSize);
		if (hasDuplicates()) {
			logger.debug("Duplicated Locations...");
			for (String duplicatedLocation : lstDuplicates) {
				logger.debug("\t" + duplicatedLocation);
			}
		}
	}

	@Override
	public int compareTo(Picture p) {
		return this.hash.compareTo(p.hash);
	}

	@Override
	public boolean equals(Object obj) {
		return this.hash.equals(((Picture) obj).hash);
	}

	@Override
	public final String toString() {
		return this.filePath;
	}
}
