package com.kboutin.core;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;

public class MetadataExtractor implements Runnable {

	private File file = null;
	private Map<String, String> metadata = null;

	public MetadataExtractor(File f) {
		this.file = f;
	}

	public final Map<String, String> getMetadata() {
		return metadata;
	}

	@Override
	public void run() {
		metadata = extractMetaData();
	}

	private Map<String, String> extractMetaData() {

		Map<String, String> mapMetaData = new TreeMap<>();

		Metadata metadata = null;
		try {
			metadata = ImageMetadataReader.readMetadata(file);
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
