package com.kboutin.core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.drew.imaging.ImageProcessingException;
import com.kboutin.gui.GenFrame;
import com.kboutin.utils.FileUtils;

/*
 * TODO KBO Simplify memory management for duplicates.
 * Once it has been detected as duplicate of a file, only store its path
 * Map<String, List<String>> : firstObject is the first file found, others are only locations where dup files are ...
 * The key is the hash of the picture.
 */

public class PicturesManager {

	private final static Logger logger = LogManager.getLogger(PicturesManager.class);

	private static PicturesManager INSTANCE = null;

	private Map<Picture, List<String>> mapPictures = new TreeMap<>();
	//private List<Picture> lstPictures = new ArrayList<Picture>();

	private Map<String, Set<String>> mapValuesForMetadata = new TreeMap<String, Set<String>>();
	private static List<String> lstAcceptedMetadata = new ArrayList<String>();

	private File dirToScan = null;

	private int selectedIndex = 0;

	static {

		lstAcceptedMetadata.add("Aperture Value");
		lstAcceptedMetadata.add("F-Number");
		lstAcceptedMetadata.add("Focal Length");
		lstAcceptedMetadata.add("ISO Speed Ratings");
		lstAcceptedMetadata.add("Make");
		lstAcceptedMetadata.add("Model");
		lstAcceptedMetadata.add("Image Height");
		lstAcceptedMetadata.add("Image Width");
		lstAcceptedMetadata.add("Shutter Speed Value");
	}

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
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		new GenFrame();
	}

	private PicturesManager() {

	}

	public final static PicturesManager getInstance() {

		if (INSTANCE == null) {
			INSTANCE = new PicturesManager();
		}

		return INSTANCE;
	}

	public final File getDirToScan() {
		return dirToScan;
	}

	public final void setDirToScan(File dirToScan) {
		this.dirToScan = dirToScan;
	}

	public final Map<Picture, List<String>> getMapPictures() {
		return mapPictures;
	}

	public final void scanDir(File f) {

		if (f.isDirectory()) {
			for (File subFile : f.listFiles()) {
				scanDir(subFile);
			}
		} else if (f.isFile()) {
			if(FileUtils.isPicture(f)) {
				Picture p = new Picture(f);
				addPicture(p);
				addMetadataForPicture(p);
			}
		}
	}

	public final List<Picture> getPictures() {

		return new ArrayList<Picture>(mapPictures.keySet());
		//return lstPictures;
	}

	public Picture getCurrentPicture() {

		if (!mapPictures.isEmpty() && selectedIndex >= 0 && selectedIndex < mapPictures.keySet().size()) {

			return getPictures().get(selectedIndex);
		}

		return null;
	}

	public Picture nextPicture() {

		selectedIndex++;
		// Loop over the list...
		// If the end has been reached, start again from the beginning.
		if (selectedIndex >= getPictures().size()) {
			selectedIndex = 0;
		}

		return getCurrentPicture();
	}

	public Picture previousPicture() {

		selectedIndex--;
		// Loop over the list...
		// If the beginning has been reached, start again from the end.
		if (selectedIndex < 0) {
			selectedIndex = getPictures().size() - 1;
		}

		return getCurrentPicture();
	}

	public final int countPictures() {

		return getPictures().size();
	}

	public final boolean hasDuplicates(Picture p) {

		return mapPictures.get(p).size() > 1;
	}

	public final int countDuplicates() {

		int totalDuplicates = 0;

		for (Picture p : getPictures()) {

			totalDuplicates += (mapPictures.get(p).size() - 1);
			//totalDuplicates += p.countDuplicates();
		}

		return totalDuplicates;
	}

	public final long getWastedSpaceForPicture(Picture p) {

		long fileSize = new File(p.getFilePath()).length();

		return fileSize * (mapPictures.get(p).size() - 1);
	}

	public final long getTotalWastedSpace() {

		long totalSpace = 0;
		for (Picture p : mapPictures.keySet()) {

			totalSpace += getWastedSpaceForPicture(p);
		}

		return totalSpace;
	}

	/*public final long getTotalWastedSpace() {

		long totalSpace = 0;
		for (Picture p : lstPictures) {

			totalSpace += p.getWastedSpace();
		}

		return totalSpace;
	}*/

	public final void addPicture(Picture p) {

		boolean found = false;
		for (Picture pic : mapPictures.keySet()) {
			if (p.equals(pic)) {

				List<String> lstPaths = mapPictures.get(pic);
				lstPaths.add(p.getFilePath());
				mapPictures.put(pic, lstPaths);
				//pic.addDuplicate(p.getFilePath());
				found = true;
				break;
			}
		}
		if (!found) {
			List<String> lstPaths = new ArrayList<>();
			lstPaths.add(p.getFilePath());
			mapPictures.put(p, lstPaths);
			//lstPictures.add(p);
		}
	}

	public final void addMetadataForPicture(Picture p) {

		Map<String, String> metadataForPicture = p.getMetadata();
		for (String key : metadataForPicture.keySet()) {

			Set<String> lstValuesForMetadata = mapValuesForMetadata.get(key);
			if (lstValuesForMetadata == null) {
				lstValuesForMetadata = new TreeSet<String>();
			}
			if (lstAcceptedMetadata.contains(key)) {
				lstValuesForMetadata.add(metadataForPicture.get(key));
				mapValuesForMetadata.put(key, lstValuesForMetadata);
			}
		}
	}

	public final long removeDuplicates(Picture p) {

		boolean deleted = false;
		long deletedSpace = 0;
		if (mapPictures.keySet().contains(p)) {

			List<String> lstPaths = mapPictures.get(p);
			Iterator<String> iter = lstPaths.iterator();
			// Skip first element
			if (iter.hasNext()) {
				iter.next();
			}
			while (iter.hasNext()) {

				String s = iter.next();
				File f = new File(s);
				long fileSize = f.length();
				deleted = f.delete();
				if (deleted) {

					deletedSpace = fileSize;
					iter.remove();
				}
			}
		}

		return deletedSpace;
	}

	public final Set<String> getMetadataKeySet() {

		return mapValuesForMetadata.keySet();
	}

	public final List<String> getMetadataKeys() {

		return new ArrayList<String>(mapValuesForMetadata.keySet());
	}

	public final List<String> getValuesForKey(String key) {

		return new ArrayList<String>(mapValuesForMetadata.get(key));
	}

	/*public final void savePicturesInfo(Picture picture) throws IOException {

		File fPicsInfo = new File("PicsInfo.txt");
		BufferedWriter buffWriter = new BufferedWriter(new FileWriter(fPicsInfo, true));
		buffWriter.append("=========================================================" + StringUtils.NEW_LINE);
		buffWriter.append(picture.getMetadataAsString() + StringUtils.NEW_LINE);

		buffWriter.close();
	}*/
}
