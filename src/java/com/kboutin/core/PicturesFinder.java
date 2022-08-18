package com.kboutin.core;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PicturesFinder {

	//private final static Logger logger = LogManager.getLogger(PicturesFinder.class);

	private final List<Picture> lstPictures;

	public PicturesFinder(List<Picture> lstPictures) {
		this.lstPictures = lstPictures;
	}

	public final List<Picture> findPicturesWithCondition(String criteria, String value) {

		//logger.debug("Searching pictures having " + value + " for " + criteria);

		return lstPictures.stream()
				.filter(findPicturesMatchingCriteriaAndValue(criteria, value))
				.collect(Collectors.toList());
	}

	private static Predicate<Picture> findPicturesMatchingCriteriaAndValue(String criteria, String value) {
		return p -> {
			Map<String, String> metaData = p.getMetadata();
			return metaData.entrySet().stream()
					.anyMatch(e -> e.getKey().equals(criteria) && e.getValue().equals(value));
		};
	}
}
