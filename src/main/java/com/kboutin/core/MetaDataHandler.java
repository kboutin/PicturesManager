package com.kboutin.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class MetaDataHandler {

	private static List<String> lstAcceptedMetadata = new ArrayList<>(
			Arrays.asList(
					"Aperture Value",
					"F-Number",
					"Focal Length",
					"ISO Speed Ratings",
					"Make",
					"Model",
					"Image Height",
					"Image Width",
					"Shutter Speed Value")
	);

	private Map<String, Set<String>> mapMetaData = new HashMap<>();

	public final void addMetadataForPicture(Picture p) {

		Map<String, String> metadataForPicture = p.getMetadata();
		for (String key : metadataForPicture.keySet()) {

			Set<String> lstValuesForMetadata = mapMetaData.get(key);
			if (lstValuesForMetadata == null) {
				lstValuesForMetadata = new TreeSet<>();
			}
			if (lstAcceptedMetadata.contains(key)) {
				lstValuesForMetadata.add(metadataForPicture.get(key));
			}
			mapMetaData.put(key, lstValuesForMetadata);
		}
	}

	public final boolean isMetaDataAccepted(String metaData) {
		return lstAcceptedMetadata.contains(metaData);
	}

	public final Set<String> getMetadataKeySet() {

		return mapMetaData.keySet();
	}

	public final Set<String> getValuesForKey(String key) {

		return mapMetaData.get(key);
	}
}
