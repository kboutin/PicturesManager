package com.kboutin.core;

import com.kboutin.gui.GenFrame;
import com.kboutin.utils.FileUtils;
import com.kboutin.utils.JSONUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/*
 * TODO KBO Simplify memory management for duplicates.
 * Once it has been detected as duplicate of a file, only store its path
 * Map<Picture, String> : firstObject is the first file found, others are only locations where dup files are ...
 */
public class PicturesManager {

	private final static Logger logger = LogManager.getLogger(PicturesManager.class);

	private static PicturesManager INSTANCE = null;

	private List<Picture> pictureList = new ArrayList<>();

	private int selectedIndex = 0;

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) {

		//File iPhotoDir = new File("/Users/Kouikoui/Pictures/Bibliothèque iPhoto/Masters");

		logger.info("Launching application");
		// Set the look and feel to the default of the system...
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}

		new GenFrame();
	}

	private PicturesManager() {

	}

	public static PicturesManager getInstance() {

		if (INSTANCE == null) {
			INSTANCE = new PicturesManager();
		}

		return INSTANCE;
	}

	public final void scanDir(File f) {

		if (f == null || !f.exists()) {
			return;
		}
		if (f.isDirectory()) {
			Stream.of(f.listFiles()).forEach(subFile -> scanDir(subFile));
		} else if (FileUtils.isPicture(f)) {
			addPicture(new Picture(f));
			//addMetadataForPicture(p);
		}
	}

	public final List<Picture> getPictures() {

		return pictureList;
	}

	public final void clearListPictures() {
		this.pictureList = new ArrayList<>();
	}

	/*public final Picture getPictureByName(String pictureName) {
		return pictureList.stream()
				.filter(picture -> picture.getFileName().equals(pictureName))
				.findFirst()
				.orElse(null);
	}*/

	public final Picture getPictureByHash(String hash) {
		return pictureList.stream()
				.filter(picture -> picture.getHash().equals(hash))
				.findFirst()
				.orElse(null);
	}

	public Picture currentPicture() {

		if (!pictureList.isEmpty()) {
			return pictureList.get(selectedIndex);
		}

		return null;
	}

	public Picture firstPicture() {

		selectedIndex = 0;
		return currentPicture();
	}

	public Picture lastPicture() {

		selectedIndex = pictureList.size() - 1;
		return currentPicture();
	}

	public Picture nextPicture() {

		selectedIndex++;
		// Loop over the list...
		// If the end has been reached, start again from the beginning.
		if (selectedIndex >= pictureList.size()) {
			return firstPicture();
		}

		return currentPicture();
	}

	public Picture previousPicture() {

		selectedIndex--;
		// Loop over the list...
		// If the beginning has been reached, start again from the end.
		if (selectedIndex < 0) {
			return lastPicture();
		}

		return currentPicture();
	}

	public final int countDuplicates() {

		// Sum the number of duplicates of each picture in the list.
		return pictureList.stream().mapToInt(p -> p.getDuplicates().size()).sum();
	}

	public final long getTotalWastedSpace() {

		// Sum the getWastedSpace of each picture in the list.
		return pictureList.stream().mapToLong(Picture::getWastedSpace).sum();
	}

	public final boolean addPicture(Picture p) {

		if (pictureList.contains(p)) {
			Picture tmpPicture = pictureList.get(pictureList.indexOf(p));
			if (tmpPicture.equals(p) && !tmpPicture.getFilePath().equals(p.getFilePath())) {
				tmpPicture.addDuplicate(p.getFilePath());
			}
			return false;
		} else {
			pictureList.add(p);
			return true;
		}
	}

	public void loadPicturesFromFile(String fileName) {
		pictureList.clear();
		pictureList = JSONUtils.loadPicturesFromFile(fileName);
		selectedIndex = 0;
	}

	/*public final void addMetadataForPicture(Picture p) {

		Map<String, String> metadataForPicture = p.getMetadata();
		for (String key : metadataForPicture.keySet()) {

			Set<String> lstValuesForMetadata = mapValuesForMetadata.get(key);
			if (lstValuesForMetadata == null) {
				lstValuesForMetadata = new TreeSet<>();
			}
			if (lstAcceptedMetadata.contains(key)) {
				lstValuesForMetadata.add(metadataForPicture.get(key));
				//mapValuesForMetadata.put(key, lstValuesForMetadata);
			}
		}
	}*/

	/*public final Set<String> getMetadataKeySet() {

		return mapValuesForMetadata.keySet();
	}

	public final List<String> getValuesForKey(String key) {

		return new ArrayList<>(mapValuesForMetadata.get(key));
	}*/

	/*public final void savePicturesInfo(Picture picture) throws IOException {

		File fPicsInfo = new File("PicsInfo.txt");
		BufferedWriter buffWriter = new BufferedWriter(new FileWriter(fPicsInfo, true));
		buffWriter.append("=========================================================" + StringUtils.NEW_LINE);
		buffWriter.append(picture.getMetadataAsString() + StringUtils.NEW_LINE);

		buffWriter.close();
	}*/
}
