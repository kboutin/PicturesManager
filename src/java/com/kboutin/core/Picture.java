package com.kboutin.core;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.kboutin.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static com.kboutin.utils.FileUtils.getReadableFileSize;
import static com.kboutin.utils.StringConstants.NEW_LINE;

public class Picture implements Comparable<Picture> {

	//private final static Logger logger = LogManager.getLogger(Picture.class);

	private String filePath = null;
	private String hash = null;
	private List<String> lstDuplicates = null;
	private Map<String, String> metadata = null;

	//private MetadataExtractor metadataExtractor = MetadataExtractor.getInstance();

	public Picture(File fPicture) {

		if (!FileUtils.isPicture(fPicture)) {
			return;
		}
		//logger.debug("Building a picture from file " + fPicture.getName());
		this.filePath = fPicture.getPath();
		this.hash = FileUtils.getFileMD5(fPicture);
		this.lstDuplicates = new ArrayList<>();
		this.metadata = extractMetaData();
	}

	private Map<String, String> extractMetaData() {

		//logger.debug("Extracting metadata for file : " + filePath);
		Map<String, String> mapMetaData = new TreeMap<>();

		Metadata metadata;
		try {
			File f = new File(filePath);
			metadata = ImageMetadataReader.readMetadata(f);
		} catch (ImageProcessingException | IOException e) {
			metadata = null;
		}
		if (metadata != null) {
			metadata.getDirectories()
					.forEach(directory -> directory.getTags()
							.forEach(tag -> mapMetaData.put(tag.getTagName(), tag.getDescription())));
		}

		return mapMetaData;
	}

	public final String getFilePath() {
		return filePath;
	}

	public final String getSize() {
		return getReadableFileSize(new File(filePath));
	}

	public final Map<String, String> getMetadata() {
		return metadata;
	}

	public final String getMetadataAsString() {

		StringBuilder s = new StringBuilder();

		metadata.keySet().forEach(key -> buildMetaDataKey(s, key));

		return s.toString();
	}

	private void buildMetaDataKey(StringBuilder s, String key) {
		s.append("[").append(key).append("] -> ").append(metadata.get(key)).append(NEW_LINE);
	}

	public final List<String> getDuplicates() {
		return lstDuplicates;
	}

	public final long getFileSize() {
		return new File(filePath).length();
	}

	public final long getWastedSpace() {
		return getFileSize() * lstDuplicates.size();
	}

	public final int countDuplicates() {
		return lstDuplicates.size();
	}

	public final boolean hasDuplicates() {
		return lstDuplicates.size() > 0;
	}

	public final void addDuplicate(String duplicate) {
		lstDuplicates.add(duplicate);
	}

	public final long deleteDuplicates() {

		long deletedSpace = 0;

		for (Iterator<String> iter = lstDuplicates.iterator(); iter.hasNext(); ) {

			String duplicate = iter.next();
			File tmp = new File(duplicate);
			long duplicateFileSize = tmp.length();
			//logger.debug("Deleting " + tmp.getPath());
			if (tmp.delete()) {
				//logger.info("Deleted " + tmp.getPath());
				deletedSpace += duplicateFileSize;
				iter.remove();
			}
		}

		return deletedSpace;
	}

	/*public final void printMetaData() {

		metadata.keySet().forEach(category -> System.out.format("[%s] -> %s\n", category, metadata.get(category)));
	}*/

	public final void printInfos() {

		//logger.debug("Picture with filePath : " + filePath);
		//logger.debug("fileSize : " + new File(filePath).length());
		if (hasDuplicates()) {
			//logger.debug("Duplicated Locations...");
			/*for (String duplicatedLocation : lstDuplicates) {
				logger.debug("\t" + duplicatedLocation);
			}*/
		}
	}

	@Override
	public int compareTo(Picture p) {
		return this.hash.compareTo(p.hash);
	}

	@Override
	public boolean equals(Object obj) {
		return this.hash.equals(((Picture) obj).hash);
	}

	@Override
	public final String toString() {
		return this.filePath;
	}
}
