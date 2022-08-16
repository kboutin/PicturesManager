package com.kboutin.gui;

import com.kboutin.core.Picture;
import com.kboutin.core.PicturesManager;
import com.kboutin.gui.filefilters.PicturesFileFilter;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
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
import java.io.Serial;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static com.kboutin.utils.FileUtils.getReadableFileSize;
import static com.kboutin.utils.GUIUtils.createEtchedTitledBorder;
import static com.kboutin.utils.StringConstants.USER_HOME;
import static javax.swing.JFileChooser.APPROVE_OPTION;
import static javax.swing.JFileChooser.DIRECTORIES_ONLY;
import static javax.swing.JOptionPane.DEFAULT_OPTION;
import static javax.swing.JOptionPane.PLAIN_MESSAGE;
import static javax.swing.JOptionPane.YES_NO_OPTION;
import static javax.swing.JOptionPane.YES_OPTION;
import static javax.swing.JOptionPane.showConfirmDialog;
import static javax.swing.JOptionPane.showMessageDialog;

public class PanelDupFinder extends JPanel implements ActionListener, ListSelectionListener {

	@Serial
	private static final long serialVersionUID = 1L;

	//private final static Logger logger = LogManager.getLogger(PanelDupFinder.class);

	private final JLabel lblDirToScan = new JLabel();
	private final JButton btnChooseDir = new JButton("...");
	//private PanelScanDir pnlScanDir = null;

	private final DefaultListModel<Picture> listModelPictures = new DefaultListModel<>();
	private final JList<Picture> listPictures = new JList<>(listModelPictures);

	private final DefaultListModel<String> listModelLocations = new DefaultListModel<>();

	private final JButton btnDeleteDuplicates = new JButton("Supprimer les doublons de ce fichier");
	private final JButton btnDeleteAllDuplicates = new JButton("Supprimer tous les doublons");

	private final JPanel pnlForDuplicatedLocations = new JPanel(new BorderLayout());

	private final PanelPicture pnlPicture = new PanelPicture();

	private final PicturesManager picManager = PicturesManager.getInstance();

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
		pnlDirToScan.setBorder(createEtchedTitledBorder("Repertoire a analyser"));

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
		pnlForDuplicatedLocations.setBorder(createEtchedTitledBorder("Doublons"));

		JPanel pnlForFiles = new JPanel(new BorderLayout());
		JScrollPane scroll = new JScrollPane(listPictures);
		pnlForFiles.add(scroll);
		pnlForFiles.setBorder(createEtchedTitledBorder("Fichiers trouves"));
		JPanel pnlLeft = new JPanel(new GridLayout(0, 1));
		pnlLeft.add(pnlForFiles);
		pnlLeft.add(pnlForDuplicatedLocations);

		//private JPanel pnlDuplicatedFiles = new JPanel(new BorderLayout());
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

		return Collections.list(listModelPictures.elements())
				.stream()
				.mapToLong(this::deleteDuplicatesForPicture)
				.sum();
	}

	private void updateDuplicatesList(Picture selectedPic) {

		if (!listModelLocations.isEmpty()) {
			listModelLocations.clear();
		}
		if (selectedPic != null) {
			selectedPic.getDuplicates().forEach(listModelLocations::addElement);
		}

		String lostSize = getReadableFileSize(picManager.getTotalWastedSpace());
		pnlForDuplicatedLocations.setBorder(createEtchedTitledBorder("Doublons (" + lostSize + ")"));
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource().equals(btnChooseDir)) {
			JFileChooser fileChooser = new JFileChooser(new File(USER_HOME));
			fileChooser.setFileSelectionMode(DIRECTORIES_ONLY);
			fileChooser.setMultiSelectionEnabled(false);
			fileChooser.setAcceptAllFileFilterUsed(true);
			int returnedValue = fileChooser.showOpenDialog(this);

			if (returnedValue == APPROVE_OPTION) {
				if (!picManager.getPictures().isEmpty()) {
					picManager.clear();
				}
				File f = fileChooser.getSelectedFile();
				lblDirToScan.setText(f.getPath());
				PicturesLister picturesLister = new PicturesLister(f, new PicturesFileFilter());
				picturesLister.execute();
			}
		} else if (e.getSource().equals(btnDeleteDuplicates)) {
			String message;
			if (listModelLocations.size() > 0) {
				// Ici, on supprime les doublons mais on garde le fichier original.
				message = "Confirmer la suppression des doublons pour ce fichier ?";
				int reply = showConfirmDialog(null, message, "Confirmation de suppression", YES_NO_OPTION);
				if (reply == YES_OPTION) {
					Picture p = listPictures.getSelectedValue();
					//logger.info("Deleting duplicates for " + p.getFilePath());
					//logger.info("It will free " + p.getWastedSpace());
					if (p != null) {
						long deletedSpace = deleteDuplicatesForPicture(p);
						showMessageDialog(null, "Vous avez gagne " + getReadableFileSize(deletedSpace), "Espace gagne", PLAIN_MESSAGE);
					}
					updateDuplicatesList(p);
				}
			} else {
				message = "Aucun element a supprimer";
				showConfirmDialog(null, message, "Confirmation de suppression", DEFAULT_OPTION);
			}
		} else if (e.getSource().equals(btnDeleteAllDuplicates)) {

			if (picManager.countDuplicates() > 0) {
				String message = "Confirmer la suppression de tous les doublons ?";
				int reply = showConfirmDialog(null, message, "Confirmation de suppression", YES_NO_OPTION);

				if (reply == YES_OPTION) {

					long deletedSpace = deleteAllDuplicates();
					//logger.info("Deleting all duplicates");
					//logger.info("It has freed " + FileUtils.getReadableFileSize(deletedSpace));
					showMessageDialog(null, "Vous avez gagne " + getReadableFileSize(deletedSpace), "Espace gagne", PLAIN_MESSAGE);
					updateDuplicatesList(listPictures.getSelectedValue());
				}
			} else {
				//showMessageDialog(null, "Aucun element a supprimer", "Info", PLAIN_MESSAGE);
				showConfirmDialog(null, "Aucun element a supprimer", "Info", DEFAULT_OPTION);
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

		private final File dirToScan;
		private final FileFilter fileFilter;

		public PicturesLister(File dirToScan, FileFilter fileFilter) {

			this.dirToScan = dirToScan;
			this.fileFilter = fileFilter;
			if (!listModelPictures.isEmpty()) {
				listModelPictures.clear();
			}
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

			String lostSize = getReadableFileSize(picManager.getTotalWastedSpace());
			pnlForDuplicatedLocations.setBorder(createEtchedTitledBorder("Doublons (" + lostSize + ")"));
		}

		@Override
		protected void process(List<Picture> chunks) {
			chunks.forEach(listModelPictures::addElement);
		}

		private List<Picture> scanDir(File f) {
			return scanDir(f, null);
		}

		private List<Picture> scanDir(File f, FileFilter fileFilter) {

			if (f.exists() && f.isDirectory()) {
				List<File> filteredFiles = Stream.of(f.listFiles())
						.filter(file -> isFileAcceptedByFileFilter(file, fileFilter))
						.toList();
				for (File subFile : filteredFiles) {
					scanDir(subFile, fileFilter);
				}
			} else if (f.isFile()) {
				if (isFileAcceptedByFileFilter(f, fileFilter)) {
					addAndPublishPicture(f);
				}
			}

			return picManager.getPictures();
		}

		private boolean isFileAcceptedByFileFilter(File f, FileFilter fileFilter) {
			return fileFilter == null || fileFilter.accept(f);
		}

		private void addAndPublishPicture(File f) {

			int prevSize = picManager.countPictures();
			Picture p = new Picture(f);
			picManager.addPicture(p);
			if (picManager.countPictures() > prevSize) {
				// Publish name only if picture has been added to the list ...
				publish(p);
			}
		}
	}
}
