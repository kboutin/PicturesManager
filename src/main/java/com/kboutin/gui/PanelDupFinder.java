package com.kboutin.gui;

import com.kboutin.core.Picture;
import com.kboutin.core.PicturesManager;
import com.kboutin.gui.filefilters.MoviesFileFilter;
import com.kboutin.gui.filefilters.PicturesFileFilter;
import com.kboutin.utils.FileUtils;
import com.kboutin.utils.GUIUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingWorker;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileFilter;
import java.util.List;
import java.util.stream.Stream;

public class PanelDupFinder extends JPanel implements ActionListener, ListSelectionListener {

	private final static Logger logger = LogManager.getLogger(PanelDupFinder.class);

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private JLabel lblDirToScan = new JLabel();
	private JButton btnChooseDir = new JButton("...");

	private DefaultListModel<Picture> listModelPictures = new DefaultListModel<>();
	private JList<Picture> listPictures = new JList<>(listModelPictures);

	private DefaultListModel<String> listModelLocations = new DefaultListModel<>();

	private JButton btnDeleteDuplicates = new JButton("Supprimer les doublons de ce fichier");
	private JButton btnDeleteAllDuplicates = new JButton("Supprimer tous les doublons");

	private JPanel pnlForDuplicatedLocations = new JPanel(new BorderLayout());

	private PanelPicture pnlPicture = new PanelPicture();

	private PicturesManager picManager = PicturesManager.getInstance();

	public PanelDupFinder() {

		super(new BorderLayout());

		btnChooseDir.addActionListener(this);

		listPictures.addListSelectionListener(this);
		listPictures.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JList<String> listDupLocations = new JList<>(listModelLocations);
		listDupLocations.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		listDupLocations.addListSelectionListener(this);

		JPanel pnlDirToScan = new JPanel(new BorderLayout());
		pnlDirToScan.add(lblDirToScan, BorderLayout.CENTER);
		pnlDirToScan.add(btnChooseDir, BorderLayout.EAST);
		pnlDirToScan.setBorder(GUIUtils.createEtchedTitledBorder("Repertoire a analyser"));

		JScrollPane scrollDupLocations = new JScrollPane(listDupLocations);
		scrollDupLocations.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollDupLocations.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

		btnDeleteDuplicates.setEnabled(false);
		btnDeleteDuplicates.addActionListener(this);
		btnDeleteAllDuplicates.addActionListener(this);
		JPanel pnlForDeleteButtons = new JPanel();
		pnlForDeleteButtons.add(btnDeleteDuplicates);
		pnlForDeleteButtons.add(btnDeleteAllDuplicates);

		JLabel lblLostSize = new JLabel();
		pnlForDuplicatedLocations.add(lblLostSize, BorderLayout.NORTH);
		pnlForDuplicatedLocations.add(scrollDupLocations, BorderLayout.CENTER);
		pnlForDuplicatedLocations.add(pnlForDeleteButtons, BorderLayout.SOUTH);
		pnlForDuplicatedLocations.setBorder(GUIUtils.createEtchedTitledBorder("Doublons"));

		JPanel pnlLeft = new JPanel(new GridLayout(0, 1));
		JPanel pnlForFiles = new JPanel(new BorderLayout());
		JScrollPane scroll = new JScrollPane(listPictures);
		pnlForFiles.add(scroll);
		pnlForFiles.setBorder(GUIUtils.createEtchedTitledBorder("Fichiers trouves"));
		pnlLeft.add(pnlForFiles);
		pnlLeft.add(pnlForDuplicatedLocations);

		JPanel pnlDuplicatedFiles = new JPanel(new GridLayout());
		pnlDuplicatedFiles.add(pnlLeft);
		pnlDuplicatedFiles.add(pnlPicture);

		add(pnlDirToScan, BorderLayout.NORTH);
		add(pnlDuplicatedFiles, BorderLayout.CENTER);
	}

	private long deleteDuplicatesForPicture(Picture p) {

		return p.deleteDuplicates();
	}

	private long deleteAllDuplicates() {

		long deletedSpace = 0;

		for (int i = listModelPictures.getSize() - 1; i >= 0; i--) {

			Picture p = listModelPictures.get(i);
			deletedSpace += deleteDuplicatesForPicture(p);
		}

		return deletedSpace;
	}

	private void updateDuplicatesList(Picture selectedPic) {

		if (!listModelLocations.isEmpty()) {

			listModelLocations.clear();
		}
		if (selectedPic != null) {

			selectedPic.getDuplicates().forEach(location -> listModelLocations.addElement(location));
		}

		String lostSize = FileUtils.getReadableFileSize(picManager.getTotalWastedSpace());
		pnlForDuplicatedLocations.setBorder(GUIUtils.createEtchedTitledBorder("Doublons (" + lostSize + ")"));
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == btnChooseDir) {

			JFileChooser fileChooser = new JFileChooser(new File(FileUtils.USER_HOME));
			fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			fileChooser.setMultiSelectionEnabled(false);
			fileChooser.setAcceptAllFileFilterUsed(true);
			fileChooser.addChoosableFileFilter(new MoviesFileFilter());
			fileChooser.setFileFilter(new PicturesFileFilter());
			int returnedValue = fileChooser.showOpenDialog(this);

			if (returnedValue == JFileChooser.APPROVE_OPTION) {

				File f = fileChooser.getSelectedFile();
				lblDirToScan.setText(f.getPath());
				PicturesLister picturesLister = new PicturesLister(f);
				FileFilter fileFilter = null;
				try {
					fileFilter = (FileFilter) fileChooser.getFileFilter();
					picturesLister.setFileFilter(fileFilter);
				} catch (ClassCastException ex) {
					logger.debug("AllFilesFilter has been chosen");
					// doNothing();
				}
				picturesLister.execute();
			}
		} else if (e.getSource().equals(btnDeleteDuplicates)) {

			String message = null;
			boolean somethingToDelete = true;
			if (listModelLocations.size() > 0) {

				// Ici, on supprime les doublons mais on garde le fichier original.
				message = "Confirmer la suppression des doublons pour ce fichier ?";
			} else {

				message = "Aucun element a supprimer";
				somethingToDelete = false;
			}

			int reply = JOptionPane.showConfirmDialog(null, message, "Confirmation de suppression", JOptionPane.YES_NO_OPTION);

			if (reply == JOptionPane.YES_OPTION) {

				if (somethingToDelete) {
					Picture p = listPictures.getSelectedValue();
					logger.info("Deleting duplicates for " + p.getFilePath());
					logger.info("It will free " + p.getWastedSpace());
					if (p != null) {
						long deletedSpace = deleteDuplicatesForPicture(p);

						JOptionPane.showMessageDialog(null, "Vous avez gagne " + FileUtils.getReadableFileSize(deletedSpace), "Espace gagne", JOptionPane.PLAIN_MESSAGE);
					}
					updateDuplicatesList(p);
				}
			}
		} else if (e.getSource().equals(btnDeleteAllDuplicates)) {

			if (picManager.countDuplicates() > 0) {

				String message = "Confirmer la suppression de tous les doublons ?";
				int reply = JOptionPane.showConfirmDialog(null, message, "Confirmation de suppression", JOptionPane.YES_NO_OPTION);

				if (reply == JOptionPane.YES_OPTION) {

					long deletedSpace = deleteAllDuplicates();
					logger.info("Deleting all duplicates");
					logger.info("It has freed " + FileUtils.getReadableFileSize(deletedSpace));

					JOptionPane.showMessageDialog(null, "Vous avez gagne " + FileUtils.getReadableFileSize(deletedSpace), "Espace gagne", JOptionPane.PLAIN_MESSAGE);
					updateDuplicatesList(listPictures.getSelectedValue());
				}
			} else {

				JOptionPane.showMessageDialog(null, "Aucun element a supprimer", "Info", JOptionPane.PLAIN_MESSAGE);
			}
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent evt) {

		if (evt.getSource() == listPictures && !evt.getValueIsAdjusting()) {

			Picture tmpPic = listPictures.getSelectedValue();
			if (tmpPic != null) {

				updateDuplicatesList(tmpPic);
				pnlPicture.updatePicture(tmpPic);
				btnDeleteDuplicates.setEnabled(tmpPic.hasDuplicates()); // Enable the button only if needed ...
			} else {

				btnDeleteDuplicates.setEnabled(false);
			}
		}
	}

	// Class to list pictures and display their names smoothly in the GUI
	private class PicturesLister extends SwingWorker<List<Picture>, Picture> {

		private File dirToScan = null;
		private FileFilter fileFilter = null;

		public PicturesLister(File dirToScan) {

			this.dirToScan = dirToScan;
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

			String lostSize = FileUtils.getReadableFileSize(picManager.getTotalWastedSpace());
			pnlForDuplicatedLocations.setBorder(GUIUtils.createEtchedTitledBorder("Doublons (" + lostSize + ")"));
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
}
