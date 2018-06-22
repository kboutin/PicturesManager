package com.kboutin.utils;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.kboutin.core.Picture;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import static com.kboutin.utils.FileUtils.NEW_LINE;

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

	public final void savePicturesInfo(Picture picture) throws IOException {

		File fPicsInfo = new File("PicsInfo.txt");
		BufferedWriter buffWriter = new BufferedWriter(new FileWriter(fPicsInfo, true));
		buffWriter.append("=========================================================" + NEW_LINE);
		buffWriter.append(picture.getMetadataAsString() + NEW_LINE);

		buffWriter.close();
	}
}
