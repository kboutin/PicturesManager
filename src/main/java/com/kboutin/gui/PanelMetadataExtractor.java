package com.kboutin.gui;

import com.kboutin.core.Picture;
import com.kboutin.core.PicturesManager;
import com.kboutin.gui.filefilters.JSONFileFilter;
import com.kboutin.gui.filefilters.MoviesFileFilter;
import com.kboutin.gui.filefilters.PicturesFileFilter;
import com.kboutin.utils.FileUtils;
import com.kboutin.utils.GUIUtils;
import com.kboutin.utils.JSONUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class PanelMetadataExtractor extends JPanel implements ActionListener {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private final static Logger logger = LogManager.getLogger(GenFrame.class);

	private JButton btnFirst = new JButton(" << ");
	private JButton btnPrevious = new JButton(" < ");
	private JButton btnNext = new JButton(" > ");
	private JButton btnLast = new JButton(" >> ");

	private JLabel lblDirToScan = new JLabel();
	private JButton btnChooseDir = new JButton("...");

	private PanelPicture pnlPicture = new PanelPicture();

	private JTextArea txtPictureMetadata = null;

	private PicturesManager picManager = PicturesManager.getInstance();

	public PanelMetadataExtractor() {

		super(new BorderLayout());

		btnFirst.addActionListener(this);
		btnPrevious.addActionListener(this);
		btnNext.addActionListener(this);
		btnLast.addActionListener(this);
		JPanel pnlForButtons = new JPanel(new GridLayout(1, 4));
		pnlForButtons.add(btnFirst);
		pnlForButtons.add(btnPrevious);
		pnlForButtons.add(btnNext);
		pnlForButtons.add(btnLast);

		btnChooseDir.addActionListener(this);
		JPanel pnlDirToScan = new JPanel(new BorderLayout());
		pnlDirToScan.add(lblDirToScan, BorderLayout.CENTER);
		pnlDirToScan.add(btnChooseDir, BorderLayout.EAST);
		pnlDirToScan.setBorder(GUIUtils.createEtchedTitledBorder("Repertoire a analyser"));

		JPanel pnlForPicture = new JPanel(new BorderLayout());
		pnlForPicture.add(pnlPicture, BorderLayout.CENTER);
		pnlForPicture.add(pnlForButtons, BorderLayout.SOUTH);
		pnlForPicture.setFocusable(true);
		// TODO KBO Find a way to add a keyListener
		// https://stackoverflow.com/questions/3728035/java-tracking-keystrokes-with-inputmap
		/*pnlForPicture.addKeyListener(new KeyAdapter() {
			@Override
			public final void keyPressed(KeyEvent ke) {
				logger.debug(ke.getKeyCode());
			}
		});*/

		txtPictureMetadata = new JTextArea(10, 30);
		txtPictureMetadata.setEditable(false);
		JScrollPane pnlPictureMetadata = new JScrollPane(txtPictureMetadata);
		pnlPictureMetadata.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		pnlPictureMetadata.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		pnlPictureMetadata.setViewportView(txtPictureMetadata);
		pnlPictureMetadata.setBorder(GUIUtils.createEtchedTitledBorder("Metadonnees"));

		JPanel pnlCenter = new JPanel(new GridLayout(1, 0));
		pnlCenter.add(pnlForPicture);
		pnlCenter.add(pnlPictureMetadata);

		add(pnlDirToScan, BorderLayout.NORTH);
		add(pnlCenter, BorderLayout.CENTER);
		//add(this.pnlScanDir, BorderLayout.NORTH);
		//add(pnlForPicture, BorderLayout.WEST);
		//add(pnlPictureMetadata, BorderLayout.CENTER);
	}

	private void chooseDir() {

		JFileChooser fileChooser = new JFileChooser(new File(FileUtils.USER_HOME));
		fileChooser.addChoosableFileFilter(new MoviesFileFilter());
		fileChooser.addChoosableFileFilter(new JSONFileFilter());
		fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		fileChooser.setMultiSelectionEnabled(false);
		fileChooser.setAcceptAllFileFilterUsed(true);
		fileChooser.setFileFilter(new PicturesFileFilter());
		int returnedValue = fileChooser.showOpenDialog(this);

		if (returnedValue == JFileChooser.APPROVE_OPTION) {

			File f = fileChooser.getSelectedFile();
			//pnlScanDir.updateDirToScan(f.getPath());
			if (f.isDirectory()) {
				lblDirToScan.setText(f.getPath());
				//metadataExtractor.scanDir(f);
				picManager.scanDir(f);
				//lstPictures = picManager.getPictures();
				updatePicture(picManager.getCurrentPicture());

				File file = new File("/Users/kouikoui/Desktop/Pictures.json");
				JSONUtils.savePictures(picManager.getPictures(), file.getAbsolutePath());
			} else if(FileUtils.isJSONFile(fileChooser.getSelectedFile())) {
				picManager.setPictures(JSONUtils.readFile(f.getAbsolutePath()));
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent ae) {

		if (ae.getSource().equals(btnFirst)) {
			updatePicture(picManager.firstPicture());
		} else if (ae.getSource().equals(btnPrevious)) {
			updatePicture(picManager.previousPicture());
		} else if (ae.getSource().equals(btnNext)) {
			updatePicture(picManager.nextPicture());
			/*PictureFinder finder = new PictureFinder(lstPictures);
			List<Picture> filteredPictures = finder.findPicturesWithCondition("F-Number", "f/2,8");
			if (filteredPictures != null && !filteredPictures.isEmpty())
				updatePicture(new File(filteredPictures.get(0).getFilePath()));*/
		} else if (ae.getSource().equals(btnLast)) {
			updatePicture(picManager.lastPicture());
		} else if (ae.getSource().equals(btnChooseDir)) {

			chooseDir();

			/*JFileChooser fileChooser = new JFileChooser(new File(StringUtils.USER_HOME));
			fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			fileChooser.setMultiSelectionEnabled(false);
			fileChooser.setAcceptAllFileFilterUsed(true);
			fileChooser.setFileFilter(new PicturesFileFilter());
			int returnedValue = fileChooser.showOpenDialog(this);

			if (returnedValue == JFileChooser.APPROVE_OPTION) {

				File f = fileChooser.getSelectedFile();
				//pnlScanDir.updateDirToScan(f.getPath());
				lblDirToScan.setText(f.getPath());
				//metadataExtractor.scanDir(f);
				picManager.setDirToScan(f);
				picManager.scanDir(f);
				//metadataExtractor.printMetas(); // Test
				//lstPictures = metadataExtractor.getLstPictures();
				lstPictures = picManager.getPictures();
				updatePicture(picManager.getCurrentPicture());
				//updatePicture(getCurrentPicture());
			}*/
		} else if (ae.getActionCommand().equals("Reset")) {
			updatePicture(null);
		} else if (ae.getActionCommand().equals("Save")) {
			JFileChooser fileChooser = new JFileChooser(new File(FileUtils.USER_HOME));
			fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			fileChooser.setMultiSelectionEnabled(false);
			fileChooser.setAcceptAllFileFilterUsed(true);
			int returnedValue = fileChooser.showSaveDialog(this);
			logger.debug("returnedValue = " + returnedValue);
			//updatePicture(null);
			logger.debug("Saving file");
			JSONUtils.savePictures(picManager.getPictures(), fileChooser.getSelectedFile().getAbsolutePath());
		}
	}

	private void displayPictureInfo(Picture p) {

		// First, empty the screen...
		txtPictureMetadata.setText("");
		if (p != null) {
			txtPictureMetadata.setText(p.getMetadataAsString());
			txtPictureMetadata.setCaretPosition(0);
		}
	}

	private void updatePicture(Picture picture) {

		pnlPicture.updatePicture(picture);
		displayPictureInfo(picture);
	}
}
