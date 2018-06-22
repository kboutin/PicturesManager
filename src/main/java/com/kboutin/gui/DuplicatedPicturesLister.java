package com.kboutin.gui;

import com.kboutin.core.Picture;
import com.kboutin.core.PicturesManager;

import javax.swing.DefaultListModel;
import javax.swing.SwingWorker;
import java.io.File;
import java.io.FileFilter;
import java.util.List;
import java.util.stream.Stream;

public class DuplicatedPicturesLister extends SwingWorker<List<Picture>, Picture> {

	private PicturesManager picManager = PicturesManager.getInstance();

	private File dirToScan = null;
	private FileFilter fileFilter = null;
	private DefaultListModel<Picture> listModelPictures = null;

	public DuplicatedPicturesLister(File dirToScan, DefaultListModel<Picture> listModelPictures) {

		this.dirToScan = dirToScan;
		this.listModelPictures = listModelPictures;
	}

	public final void setFileFilter(FileFilter fileFilter) {
		this.fileFilter = fileFilter;
	}

	@Override
	protected List<Picture> doInBackground() {

		if (fileFilter == null) {
			scanDir(dirToScan);
		} else {
			scanDir(dirToScan, fileFilter);
		}

		return picManager.getPictures();
	}

	@Override
	protected void done() {

		//String lostSize = FileUtils.getReadableFileSize(picManager.getTotalWastedSpace());
		//pnlForDuplicatedLocations.setBorder(GUIUtils.createEtchedTitledBorder("Doublons (" + lostSize + ")"));
	}

	@Override
	protected void process(List<Picture> chunks) {

		for (Picture p : chunks) {

			listModelPictures.addElement(p);
		}
	}

	private List<Picture> scanDir(File f) {

		if (f.isDirectory()) {
			Stream.of(f.listFiles()).forEach(subFile -> scanDir(subFile));
		} else if (f.isFile() && !f.getName().startsWith(".")) {
			addAndPublishPicture(f);
		}

		return picManager.getPictures();
	}

	private List<Picture> scanDir(File f, FileFilter fileFilter) {

		if (f.isDirectory()) {
			Stream.of(f.listFiles(fileFilter)).forEach(pic -> scanDir(pic, fileFilter));
		} else if (f.isFile()) {
			if (fileFilter.accept(f)) {
				addAndPublishPicture(f);
			}
		}

		return picManager.getPictures();
	}

	private void addAndPublishPicture(File f) {

		Picture p = new Picture(f);
		if (picManager.addPicture(p)) {
			// Publish name only if picture has been added to the list ...
			publish(p);
		}
	}
}
