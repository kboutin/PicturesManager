package com.kboutin.core;

import com.drew.imaging.ImageProcessingException;
import com.kboutin.gui.GenFrame;
import com.kboutin.utils.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
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

	private List<Picture> lstPictures = new ArrayList<>();

	//private Map<String, Set<String>> mapValuesForMetadata = new TreeMap<>();

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

	private int selectedIndex = 0;

	/**
	 * @param args
	 * @throws IOException
	 * @throws ImageProcessingException
	 */
	public static void main(String[] args) throws ImageProcessingException, IOException {

		//PicturesManager manager = new PicturesManager();
		//manager.scanDir(new File(""));
		//System.out.println("Nombre de photos : " + manager.getLstPictures().size());

		// /Users/Kouikoui/Pictures/Bibliotheque iPhoto/Masters
		//File picture = new File("/Users/Kouikoui/Desktop/IMG_7855.JPG");
		//File iPhotoDir = new File("/Users/Kouikoui/Pictures/Bibliothèque iPhoto/Masters");
		//PicturesManager manager = new PicturesManager();
		//manager.scanDir(iPhotoDir);

		logger.info("Launching application");
		// Set the look and feel to the default of the system...
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}

		Path path = FileSystems.getDefault().getPath("/Users/kouikoui/Desktop/Kev/Photos/20171118_Anniversaire_Mounir");
		FileUtils.scanDirectory(path);
		//new GenFrame();
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

		if (f.isDirectory()) {
			Stream.of(f.listFiles()).forEach(subFile -> scanDir(subFile));
		} else if (f.isFile()) {
			if(FileUtils.isPicture(f)) {
				//Picture p = new Picture(f);
				addPicture(new Picture(f));
				//addMetadataForPicture(p);
			}
		}
	}

	public final List<Picture> getPictures() {

		return lstPictures;
	}

	public Picture getCurrentPicture() {

		if (!lstPictures.isEmpty() && selectedIndex >= 0 && selectedIndex < lstPictures.size()) {
			return lstPictures.get(selectedIndex);
		}

		return null;
	}

	public Picture nextPicture() {

		selectedIndex++;
		// Loop over the list...
		// If the end has been reached, start again from the beginning.
		if (selectedIndex >= lstPictures.size()) {
			selectedIndex = 0;
		}

		return getCurrentPicture();
	}

	public Picture previousPicture() {

		selectedIndex--;
		// Loop over the list...
		// If the beginning has been reached, start again from the end.
		if (selectedIndex < 0) {
			selectedIndex = lstPictures.size() - 1;
		}

		return getCurrentPicture();
	}

	public final int countPictures() {

		return lstPictures.size();
	}

	public final int countDuplicates() {

		// Sum the countDuplicates of each picture in the list.
		return lstPictures.stream().mapToInt(Picture::countDuplicates).sum();
	}

	public final long getTotalWastedSpace() {

		// Sum the getWastedSpace of each picture in the list.
		//return lstPictures.stream().mapToLong(Picture::getWastedSpace).sum();
		return getTotalWastedSpace(lstPictures);
	}

	public final long getTotalWastedSpace(List<Picture> lstPictures) {
		return lstPictures.stream().mapToLong(Picture::getWastedSpace).sum();
	}

	public final void addPicture(Picture p) {

		boolean found = false;
		for (Picture pic : lstPictures) {
			if (p.equals(pic)) {

				pic.addDuplicate(p.getFilePath());
				found = true;
				break;
			}
		}
		if (!found) {
			lstPictures.add(p);
		}
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
