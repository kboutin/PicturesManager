package com.kboutin.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileFilter;
import java.util.List;

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

import com.kboutin.core.Picture;
import com.kboutin.core.PicturesManager;
import com.kboutin.gui.filefilters.MoviesFileFilter;
import com.kboutin.gui.filefilters.PicturesFileFilter;
import com.kboutin.utils.FileUtils;
import com.kboutin.utils.GUIUtils;

public class PanelDupFinder extends JPanel implements ActionListener, ListSelectionListener {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private JPanel pnlDirToScan = new JPanel(new BorderLayout());
	private JLabel lblDirToScan = new JLabel();
	private JButton btnChooseDir = new JButton("...");
	//private PanelScanDir pnlScanDir = null;

	private JPanel pnlDuplicatedFiles = new JPanel(new BorderLayout());
	private DefaultListModel<Picture> listModelPictures = new DefaultListModel<Picture>();
	private JList<Picture> listPictures = new JList<Picture>(listModelPictures);
	private JScrollPane scroll = new JScrollPane(listPictures);

	private DefaultListModel<String> listModelLocations = new DefaultListModel<String>();
	private JList<String> listDupLocations = new JList<String>(listModelLocations);
	private JScrollPane scrollDupLocations = new JScrollPane(listDupLocations);

	private JLabel lblLostSize = new JLabel();

	private JPanel pnlForDeleteButtons = new JPanel();
	private JButton btnDeleteDuplicates = new JButton("Supprimer les doublons de ce fichier");
	private JButton btnDeleteAllDuplicates = new JButton("Supprimer tous les doublons");

	private JPanel pnlLeft = new JPanel(new GridLayout(0, 1));
	private JPanel pnlForFiles = new JPanel(new BorderLayout());
	private JPanel pnlForDuplicatedLocations = new JPanel(new BorderLayout());

	private PanelPicture pnlPicture = new PanelPicture();

	private PicturesManager picManager = PicturesManager.getInstance();
	private PicturesLister picturesLister = null;

	private FileFilter selectedFilter = null;

	public PanelDupFinder() {

		super(new BorderLayout());

		btnChooseDir.addActionListener(this);

		listPictures.addListSelectionListener(this);
		listPictures.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listDupLocations.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		listDupLocations.addListSelectionListener(this);

		pnlDirToScan.add(lblDirToScan, BorderLayout.CENTER);
		pnlDirToScan.add(btnChooseDir, BorderLayout.EAST);
		pnlDirToScan.setBorder(GUIUtils.createEtchedTitledBorder("Repertoire a analyser"));

		scrollDupLocations.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollDupLocations.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

		btnDeleteDuplicates.setEnabled(false);
		btnDeleteDuplicates.addActionListener(this);
		btnDeleteAllDuplicates.addActionListener(this);
		pnlForDeleteButtons.add(btnDeleteDuplicates);
		pnlForDeleteButtons.add(btnDeleteAllDuplicates);

		pnlForDuplicatedLocations.add(lblLostSize, BorderLayout.NORTH);
		pnlForDuplicatedLocations.add(scrollDupLocations, BorderLayout.CENTER);
		pnlForDuplicatedLocations.add(pnlForDeleteButtons, BorderLayout.SOUTH);
		pnlForDuplicatedLocations.setBorder(GUIUtils.createEtchedTitledBorder("Doublons"));

		pnlForFiles.add(scroll);
		pnlForFiles.setBorder(GUIUtils.createEtchedTitledBorder("Fichiers trouves"));
		pnlLeft.add(pnlForFiles);
		pnlLeft.add(pnlForDuplicatedLocations);

		pnlDuplicatedFiles.add(pnlLeft, BorderLayout.WEST);
		pnlDuplicatedFiles.add(pnlPicture, BorderLayout.CENTER);

		add(pnlDirToScan, BorderLayout.NORTH);
		add(pnlDuplicatedFiles, BorderLayout.CENTER);
	}

	private final long deleteDuplicatesForPicture(Picture p) {

		long deletedSpace = p.removeAllDuplicates();

		return deletedSpace;
	}

	private final long deleteAllDuplicates() {

		long deletedSpace = 0;

		for (int i = listModelPictures.getSize() - 1; i >= 0; i--) {

			Picture p = listModelPictures.get(i);
			deletedSpace += deleteDuplicatesForPicture(p);
		}

		return deletedSpace;
	}

	private final void updateDuplicatesList(Picture selectedPic) {

		if (!listModelLocations.isEmpty()) {

			listModelLocations.clear();
		}
		if (selectedPic != null) {

			for (String location : selectedPic.getDuplicates()) {
				listModelLocations.addElement(location);
			}
		}

		String lostSize = FileUtils.getReadableFileSize(picManager.getTotalWastedSpace());
		pnlForDuplicatedLocations.setBorder(GUIUtils.createEtchedTitledBorder("Doublons (" + lostSize + ")"));
	}

	/*public final void chooseDir() {

		JFileChooser fileChooser = new JFileChooser(new File(System.getProperty("user.home")));
		fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		fileChooser.setMultiSelectionEnabled(false);
		fileChooser.setAcceptAllFileFilterUsed(true);
		fileChooser.addChoosableFileFilter(new MoviesFileFilter());
		fileChooser.setFileFilter(new PicturesFileFilter());
		int returnedValue = fileChooser.showOpenDialog(this);

		if (returnedValue == JFileChooser.APPROVE_OPTION) {

			File f = fileChooser.getSelectedFile();
			selectedFilter = (FileFilter) fileChooser.getFileFilter();
			//pnlScanDir.updateDirToScan(f.getPath());
			picturesLister = new PicturesLister(f);
			picturesLister.execute();
		}
	}*/

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == btnChooseDir) {

			JFileChooser fileChooser = new JFileChooser(new File(System.getProperty("user.home")));
			fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			fileChooser.setMultiSelectionEnabled(false);
			fileChooser.setAcceptAllFileFilterUsed(true);
			fileChooser.addChoosableFileFilter(new MoviesFileFilter());
			fileChooser.setFileFilter(new PicturesFileFilter());
			int returnedValue = fileChooser.showOpenDialog(this);

			if (returnedValue == JFileChooser.APPROVE_OPTION) {

				File f = fileChooser.getSelectedFile();
				selectedFilter = (FileFilter) fileChooser.getFileFilter();
				//pnlScanDir.updateDirToScan(f.getPath());
				lblDirToScan.setText(f.getPath());
				picturesLister = new PicturesLister(f);
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

					JOptionPane.showMessageDialog(null, "Vous avez gagne " + FileUtils.getReadableFileSize(deletedSpace), "Espace gagne", JOptionPane.PLAIN_MESSAGE);
					updateDuplicatesList(listPictures.getSelectedValue());
				}
			} else {

				JOptionPane.showMessageDialog(null, "Aucun element a supprimer", "Info", JOptionPane.PLAIN_MESSAGE);
			}
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public void valueChanged(ListSelectionEvent evt) {

		if (evt.getSource() == listPictures && !evt.getValueIsAdjusting()) {

			JList<Picture> list = (JList<Picture>) evt.getSource();
			Picture tmpPic = list.getSelectedValue();
			updateDuplicatesList(tmpPic);
			if (tmpPic != null) {

				pnlPicture.updatePicture(tmpPic);
				btnDeleteDuplicates.setEnabled(tmpPic.hasDuplicates()); // Enable the button only if needed ...

				tmpPic = null;
			} else {

				btnDeleteDuplicates.setEnabled(false);
			}
		}
	}

	// Class to list pictures and display their names smoothly in the GUI
	private class PicturesLister extends SwingWorker<List<Picture>, Picture> {

		private File dirToScan = null;

		public PicturesLister(File dirToScan) {

			this.dirToScan = dirToScan;
		}

		@Override
		protected List<Picture> doInBackground() throws Exception {

			scanDir(dirToScan, selectedFilter);
			//scanDir(PictureScanner)

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

		private final List<Picture> scanDir(File f, FileFilter fileFilter) {

			if (f.isDirectory()) {
				for (File subFile : f.listFiles(fileFilter)) {
					scanDir(subFile, fileFilter);
				}
			} else if (f.isFile()) {
				if (fileFilter.accept(f)) {
					int prevSize = picManager.countPictures();
					Picture p = new Picture(f);
					picManager.addPicture(p);
					if (picManager.countPictures() > prevSize) {
						// Publish name only if picture has been added to the list ...
						publish(p);
					}
				}
			}

			return picManager.getPictures();
		}
	}
}
