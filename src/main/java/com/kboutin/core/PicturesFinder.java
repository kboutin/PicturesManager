package com.kboutin.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PicturesFinder {

	private final static Logger logger = LogManager.getLogger(PicturesFinder.class);

	private List<Picture> lstPictures = null;

	public PicturesFinder(List<Picture> lstPictures) {

		this.lstPictures = lstPictures;
	}

	public final List<Picture> findPicturesWithCondition(String criteria, String value) {

		logger.debug("Searching pictures having " + value + " for " + criteria);
		List<Picture> filteredPictures = new ArrayList<>();

		lstPictures.stream().forEach(picture -> {
			Map<String, String> metaData = picture.getMetadata();
			for (String key : metaData.keySet()) {

				if (key.equals(criteria) && metaData.get(key).equals(value)) {
					logger.debug("Found picture " + picture.getFilePath());
					filteredPictures.add(picture);
				}
			}
		});

		return filteredPictures;
	}
}
