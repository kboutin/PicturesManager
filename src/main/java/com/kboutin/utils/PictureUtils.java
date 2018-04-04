package com.kboutin.utils;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

public class PictureUtils {

	private PictureUtils() {
		// Hide public constructor to use static methods.
	}

	public static Map<String, String> extractMetaData(String filePath) {

		//logger.debug("Extracting metadata for file : " + filePath);
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
	}
}
