package com.kboutin.core.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.kboutin.core.Picture;

public class PicturesCache {

	private static PicturesCache INSTANCE = null;
	private List<Picture> lstPictures = null;

	private PicturesCache() {

		lstPictures = new ArrayList<>();
	}

	public final static PicturesCache getInstance() {

		if (INSTANCE == null) {
			INSTANCE = new PicturesCache();
		}
		return INSTANCE;
	}

	/*public Optional<Picture> getPicture(Picture p) {

		if (p == null || !lstPictures.contains(p)) {
			return null;
		}
		return lstPictures.stream().findFirst();
	}*/

	public String getPictureHash(Picture p) {

		if (p == null) {
			return null;
		}
		if (!lstPictures.contains(p)) {
			lstPictures.add(p);
		}
		return p.getHash();
	}

	public Map<String, String> getPictureMetadata(Picture p) {

		if (p == null) {
			return null;
		}
		if (!lstPictures.contains(p)) {
			lstPictures.add(p);
		}
		return p.getMetadata();
	}
}
