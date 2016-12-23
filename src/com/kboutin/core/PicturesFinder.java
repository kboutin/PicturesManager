package com.kboutin.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PicturesFinder {

	public final static Logger logger = LogManager.getLogger(PicturesFinder.class);

	private List<Picture> lstPictures = null;

	public PicturesFinder(List<Picture> lstPictures) {

		this.lstPictures = lstPictures;
	}

	public final List<Picture> findPicturesWithCondition(String criteria, String value) {

		logger.debug("Searching pictures having " + value + " for " + criteria);
		List<Picture> filteredPictures = new ArrayList<Picture>();

		for (Picture p : lstPictures) {

			Map<String, String> metaData = p.getMetadata();
			for (String key : metaData.keySet()) {

				if (key.equals(criteria) && metaData.get(key).equals(value)) {
					logger.debug("Found picture " + p.getFilePath());
					filteredPictures.add(p);
				}
			}
		}

		return filteredPictures;
	}
}
