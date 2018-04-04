package com.kboutin.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kboutin.core.Picture;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class JSONUtils {

	private static ObjectMapper objectMapper = new ObjectMapper();

	private JSONUtils() {
		// Hide public constructor to use static methods.
	}

	public static void savePictures(List<Picture> lstPictures, String fileName) {

		File fPicture = new File(fileName);
		//lstPictures.forEach(picture -> {
		try {
			objectMapper.writeValue(fPicture, lstPictures);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static List<Picture> readFile(String fileName) {

		File fPicture = new File(fileName);
		try {
			return Arrays.asList(objectMapper.readValue(fPicture, Picture[].class));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
