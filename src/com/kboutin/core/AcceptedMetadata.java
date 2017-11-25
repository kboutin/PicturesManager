package com.kboutin.core;

// Set the list of acceptedMetadata in Picture
// Use them in filters to get a specific value.
public enum AcceptedMetadata {

	APERTURE("Aperture Value"),
	F_NUMBER("F-Number"),
	FOCAL_LENGTH("Focal Length"),
	ISO("ISO Speed Ratings"),
	MAKER("Make"),
	MODEL("Model"),
	HEIGHT("Image Height"),
	WIDTH("Image Width"),
	SHUTTER_SPEED("Shutter Speed Value");

	private String name;

	private AcceptedMetadata(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
