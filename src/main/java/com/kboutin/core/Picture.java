package com.kboutin.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kboutin.utils.FileUtils;
import com.kboutin.utils.PictureUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Picture implements Comparable<Picture> {

	private final static Logger logger = LogManager.getLogger(Picture.class);

	private String filePath = null;
	private String hash = null;
	private String size;
	private List<String> duplicates = null;
	private Map<String, String> metadata = null;
	private Orientation orientation = null;

	public Picture() {
		// Default Constructor needed for jaxb
	}

	public Picture(File fPicture) {

		if (!FileUtils.isPicture(fPicture)) {
			return;
		}
		logger.debug("Building a picture from file " + fPicture.getName());
		this.filePath = fPicture.getPath();
		this.hash = FileUtils.getSHA1Hash(fPicture);
		this.duplicates = new ArrayList<>();
		this.metadata = PictureUtils.extractMetaData(this.filePath);
		this.orientation = this.getHeight() > this.getWidth() ? Orientation.PORTRAIT : Orientation.LANDSCAPE;
	}

	/*private Map<String, String> extractMetaData() {

		logger.debug("Extracting metadata for file : " + filePath);
		Map<String, String> mapMetaData = new TreeMap<>();

		Metadata metadata = null;
		try {
			File f = new File(filePath);
			metadata = ImageMetadataReader.readMetadata(f);
		} catch (ImageProcessingException | IOException e) {
			metadata = null;
		}
		if (metadata != null) {
			metadata.getDirectories().forEach(directory -> {
				directory.getTags().forEach(tag -> mapMetaData.put(tag.getTagName(), tag.getDescription()));
			});
		}

		return mapMetaData;
	}*/

	public final String getFilePath() {
		return filePath;
	}

	@JsonIgnore
	public final String getFileName() {
		return filePath.substring(filePath.lastIndexOf(FileUtils.FILE_SEP) + 1);
	}

	public final String getSize() {
		return FileUtils.getReadableFileSize(new File(filePath));
	}

	private int getHeight() {
		String height = metadata.get("Exif Image Height");
		height = height.substring(0, height.indexOf(" ")).trim();
		return Integer.parseInt(height);
	}

	private int getWidth() {
		String width = metadata.get("Exif Image Width");
		width = width.substring(0, width.indexOf(" ")).trim();
		return Integer.parseInt(width);
	}

	public final String getHash() {
		return this.hash;
	}

	public final Map<String, String> getMetadata() {
		return metadata;
	}

	@JsonIgnore
	public final String getMetadataAsString() {

		StringBuilder s = new StringBuilder();

		metadata.keySet().forEach(key -> s.append("[" + key + "] -> " + metadata.get(key) + FileUtils.NEW_LINE));

		return s.toString();
	}

	public Orientation getOrientation() {
		return orientation;
	}

	public final List<String> getDuplicates() {

		return duplicates;
	}

	@JsonIgnore
	public final long getWastedSpace() {

		long fileSize = new File(filePath).length();

		return fileSize * duplicates.size();
	}

	@JsonIgnore
	public final int countDuplicates() {

		return duplicates.size();
	}

	@JsonIgnore
	public final boolean hasDuplicates() {

		return duplicates.size() > 0;
	}

	public final void addDuplicate(String duplicate) {

		duplicates.add(duplicate);
	}

	public final long deleteDuplicates() {

		long deletedSpace = 0;

		for (Iterator<String> iter = duplicates.iterator(); iter.hasNext(); ) {

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

		metadata.keySet().forEach(category -> System.out.format("[%s] -> %s\n", category, metadata.get(category)));
	}

	public final void printInfos() {

		logger.debug("Picture with filePath : " + filePath);
		logger.debug("fileSize : " + new File(filePath).length());
		if (hasDuplicates()) {
			logger.debug("Duplicated Locations...");
			duplicates.forEach(duplicatedLocation -> logger.debug("\t" + duplicatedLocation));
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
	public int hashCode() {
		return hash.hashCode();
	}

	@Override
	public final String toString() {

		return this.filePath;
	}

	public enum Orientation {
		LANDSCAPE,
		PORTRAIT
	}
}
