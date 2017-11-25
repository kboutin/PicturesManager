package com.kboutin.core;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PicturesFinder {

	private final static Logger logger = LogManager.getLogger(PicturesFinder.class);

	private List<Picture> lstPictures = null;

	public PicturesFinder(List<Picture> lstPictures) {

		this.lstPictures = lstPictures;
	}

	public final List<Picture> findPicturesWithCondition(String criteria, String value) {

		logger.debug("Searching pictures having " + value + " for " + criteria);
		List<Picture> filteredPictures = new ArrayList<Picture>();

		lstPictures.stream().forEach(pic -> {
			if (pic.getMetadata().containsKey(criteria) && pic.getMetadata().get(criteria).equals(value)) {
				filteredPictures.add(pic);
			}
		});

		return filteredPictures;
	}
}
