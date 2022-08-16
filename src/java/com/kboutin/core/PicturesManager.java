package com.kboutin.core;

import com.kboutin.gui.GenFrame;
import com.kboutin.utils.FileUtils;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Stream;

import static com.kboutin.utils.StringConstants.APERTURE_VALUE;
import static com.kboutin.utils.StringConstants.EXPOSURE_TIME;
import static com.kboutin.utils.StringConstants.FOCAL_LENGTH;
import static com.kboutin.utils.StringConstants.F_NUMBER;
import static com.kboutin.utils.StringConstants.IMAGE_HEIGHT;
import static com.kboutin.utils.StringConstants.IMAGE_WIDTH;
import static com.kboutin.utils.StringConstants.ISO_SPEED;
import static com.kboutin.utils.StringConstants.MAKE;
import static com.kboutin.utils.StringConstants.MODEL;
import static com.kboutin.utils.StringConstants.SHUTTER_SPEED;

/*
 * TODO KBO Simplify memory management for duplicates.
 * Once it has been detected as duplicate of a file, only store its path
 * Map<Picture, String> : firstObject is the first file found, others are only locations where dup files are ...
 */

public class PicturesManager {

	//private final static Logger logger = LogManager.getLogger(PicturesManager.class);

	private static final List<String> lstAcceptedMetadata = Arrays.asList(
			APERTURE_VALUE,
			EXPOSURE_TIME,
			F_NUMBER,
			FOCAL_LENGTH,
			ISO_SPEED,
			MAKE,
			MODEL,
			IMAGE_HEIGHT,
			IMAGE_WIDTH,
			SHUTTER_SPEED);

	private static PicturesManager INSTANCE = null;

	private final List<Picture> lstPictures;
	private final Map<String, Set<String>> mapValuesForMetadata;

	private int selectedIndex = 0;

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		//logger.info("Launching application");
		// Set the look and feel to the default of the system...
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException |
				 IllegalAccessException e) {
			e.printStackTrace();
		}

		new GenFrame();
	}

	private PicturesManager() {
		this.lstPictures = new ArrayList<>();
		this.mapValuesForMetadata = new TreeMap<>();
	}

	public static PicturesManager getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new PicturesManager();
		}
		return INSTANCE;
	}

	public final void scanDir(File f) {

		if (f.isDirectory()) {
			Stream.of(f.listFiles()).forEach(this::scanDir);
		} else if (f.isFile()) {
			if (FileUtils.isPicture(f)) {
				//Picture p = new Picture(f);
				addPicture(new Picture(f));
				//addMetadataForPicture(p);
			}
		}
	}

	public final void clear() {
		if (!lstPictures.isEmpty()) {
			lstPictures.clear();
			mapValuesForMetadata.clear();
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
		if (lstPictures.isEmpty()) {
			return 0;
		}
		// Sum the countDuplicates of each picture in the list.
		return lstPictures.stream()
				.mapToInt(Picture::countDuplicates)
				.sum();
	}

	public final long getTotalWastedSpace() {
		if (lstPictures.isEmpty()) {
			return 0;
		}
		// Sum the getWastedSpace of each picture in the list.
		return lstPictures.stream()
				.mapToLong(Picture::getWastedSpace)
				.sum();
	}

	public final void addPicture(Picture p) {
		lstPictures.stream()
				.filter(pic -> pic.equals(p))
				.findAny()
				.ifPresentOrElse(pic -> pic.addDuplicate(p.getFilePath()), () -> lstPictures.add(p));
	}

	public final void addMetadataForPicture(Picture p) {

		Map<String, String> metadataForPicture = p.getMetadata();
		metadataForPicture.entrySet().stream()
				.filter(metaData -> lstAcceptedMetadata.contains(metaData.getKey()))
				.forEach(this::extractValues);
		/*for (String key : metadataForPicture.keySet()) {
			Set<String> lstValuesForMetadata = mapValuesForMetadata.get(key);
			if (lstValuesForMetadata == null) {
				lstValuesForMetadata = new TreeSet<>();
			}
			//if (lstAcceptedMetadata.contains(key)) {
			lstValuesForMetadata.add(metadataForPicture.get(key));
			mapValuesForMetadata.put(key, lstValuesForMetadata);
			//}
		}*/
	}

	private void extractValues(Map.Entry<String, String> metaData) {
		String key = metaData.getKey();
		String value = metaData.getValue();
		Set<String> lstValuesForMetadata = Optional.ofNullable(mapValuesForMetadata.get(key))
				.orElse(new TreeSet<>());
		lstValuesForMetadata.add(value);
		mapValuesForMetadata.put(key, lstValuesForMetadata);
	}

	public final Set<String> getMetaDataKeySet() {
		return mapValuesForMetadata.keySet();
	}

	public final List<String> getValuesForKey(String key) {
		return new ArrayList<>(mapValuesForMetadata.get(key));
	}

	/*public final void savePicturesInfo(Picture picture) throws IOException {

		File fPicsInfo = new File("PicsInfo.txt");
		BufferedWriter buffWriter = new BufferedWriter(new FileWriter(fPicsInfo, true));
		buffWriter.append("=========================================================" + StringUtils.NEW_LINE);
		buffWriter.append(picture.getMetadataAsString() + StringUtils.NEW_LINE);

		buffWriter.close();
	}*/
}
