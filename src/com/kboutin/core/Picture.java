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
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.kboutin.utils.FileUtils;
import com.kboutin.utils.StringUtils;

public class Picture implements Comparable<Picture> {

	private final static Logger logger = LogManager.getLogger(Picture.class);

	private String filePath = null;
	private String hash = null;
	private List<String> lstDuplicates = null;
	private Map<String, String> metadata = null;

	//private MetadataExtractor metadataExtractor = MetadataExtractor.getInstance();

	public Picture(File fPicture) {

		if (!FileUtils.isPicture(fPicture)) {
			return;
		}
		logger.debug("Building a picture from file " + fPicture.getName());
		this.filePath = fPicture.getPath();
		this.hash = FileUtils.getFileMD5(fPicture);
		this.lstDuplicates = new ArrayList<String>();
		//this.metadata = metadataExtractor.extractMetaData(this);
		this.metadata = extractMetaData();
	}

	private final Map<String, String> extractMetaData() {

		//System.out.println("Extracting metadata for file : " + filePath);
		logger.debug("Extracting metadata for file : " + filePath);
		Map<String, String> mapMetaData = new TreeMap<String, String>();

		Metadata metadata = null;
		try {
			File f = new File(filePath);
			metadata = ImageMetadataReader.readMetadata(f);
		} catch (ImageProcessingException e) {
			mapMetaData = null;
		} catch (IOException e) {
			mapMetaData = null;
		}
		for (Directory directory : metadata.getDirectories()) {
		    for (Tag tag : directory.getTags()) {
		    	mapMetaData.put(tag.getTagName(), tag.getDescription());
		        //System.out.format("[%s] - %s = %s\n", directory.getName(), tag.getTagName(), tag.getDescription());
		    }
		}

		return mapMetaData;
	}

	public final String getFilePath() {
		return filePath;
	}

	public final String getSize() {

		return FileUtils.getReadableFileSize(new File(filePath));
	}

	public final Map<String, String> getMetadata() {
		return metadata;
	}

	public final String getMetadataAsString() {

		StringBuilder s = new StringBuilder();

		for (String key :  metadata.keySet()) {
			s.append("[" + key + "] -> " + metadata.get(key) + StringUtils.NEW_LINE);
		}

		return s.toString();
	}

	public final List<String> getDuplicates() {

		return lstDuplicates;
	}

	public final long getWastedSpace() {

		long fileSize = new File(filePath).length();

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

	public final long removeDuplicate(String duplicate) {

		boolean deleted = false;
		long deletedSpace = 0;
		if (lstDuplicates.contains(duplicate)) {

			File tmp = new File(duplicate);
			long fileSize = tmp.length();
			deleted = tmp.delete();
			if (deleted) {

				deletedSpace = fileSize;
				lstDuplicates.remove(duplicate);
			}
		}

		return deletedSpace;
	}

	public final long removeAllDuplicates() {

		long deletedSpace = 0;

		for (Iterator<String> iter = lstDuplicates.iterator(); iter.hasNext(); ) {

			String duplicate = iter.next();
			File tmp = new File(duplicate);
			long fileSize = tmp.length();
			logger.debug("Removing " + tmp.getPath());
			boolean deleted = tmp.delete();
			if (deleted) {

				logger.info("Removed " + tmp.getPath());
				deletedSpace += fileSize;
				iter.remove();
			}
		}

		return deletedSpace;
	}

	public final void printMetaData() {

		for (String category : metadata.keySet()) {
			System.out.format("[%s] -> %s\n", category, metadata.get(category));
		}
	}

	public final void printInfos() {

		logger.debug("Picture with filePath : " + filePath);
		logger.debug("fileSize : " + new File(filePath).length());
		if (hasDuplicates()) {
			logger.debug("Duplicated Locations...");
		}
		for (String duplicatedLocation : lstDuplicates) {
			logger.debug("\t" + duplicatedLocation);
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
