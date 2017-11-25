package com.kboutin.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.kboutin.core.Picture;
import com.kboutin.core.PicturesManager;
import com.kboutin.utils.GUIUtils;

public class PanelMetadataExtractor extends JPanel implements ActionListener {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private JPanel pnlForPicture = new JPanel(new BorderLayout());

	private JPanel pnlForButtons = new JPanel();
	private JButton btnPrevious = new JButton(" < ");
	private JButton btnNext = new JButton(" > ");

	private PanelPicture pnlPicture = new PanelPicture();

	private JScrollPane pnlPictureMetadata = null;
	private JTextArea txtPictureMetadata = null;

	private PicturesManager picManager = PicturesManager.getInstance();

	public PanelMetadataExtractor() {

		super(new BorderLayout());

		btnPrevious.addActionListener(this);
		btnNext.addActionListener(this);
		pnlForButtons.add(btnPrevious);
		pnlForButtons.add(btnNext);

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
		pnlPictureMetadata = new JScrollPane(txtPictureMetadata);
		pnlPictureMetadata.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		pnlPictureMetadata.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		pnlPictureMetadata.setViewportView(txtPictureMetadata);
		pnlPictureMetadata.setBorder(GUIUtils.createEtchedTitledBorder("Metadonnees"));

		JPanel pnlCenter = new JPanel(new GridLayout(1, 0));
		pnlCenter.add(pnlForPicture);
		pnlCenter.add(pnlPictureMetadata);

		add(pnlCenter, BorderLayout.CENTER);
	}

	@Override
	public void actionPerformed(ActionEvent ae) {

		if (ae.getSource().equals(btnPrevious)) {

			updatePicture(picManager.previousPicture());
		} else if (ae.getSource().equals(btnNext)) {

			updatePicture(picManager.nextPicture());
			/*PictureFinder finder = new PictureFinder(lstPictures);
			List<Picture> filteredPictures = finder.findPicturesWithCondition("F-Number", "f/2,8");
			if (filteredPictures != null && !filteredPictures.isEmpty())
				updatePicture(new File(filteredPictures.get(0).getFilePath()));*/
		}
	}

	private void displayPictureInfo(Picture p) {

		txtPictureMetadata.setText(p.getMetadataAsString());
		txtPictureMetadata.setCaretPosition(0);
	}

	public void updatePicture(Picture picture) {

		if (picture != null) {
			pnlPicture.updatePicture(picture);
			displayPictureInfo(picture);
		}
	}
}

/*private final void chooseDir() {

JFileChooser fileChooser = new JFileChooser(new File(StringUtils.USER_HOME));
fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
fileChooser.setMultiSelectionEnabled(false);
fileChooser.setAcceptAllFileFilterUsed(true);
fileChooser.setFileFilter(new PicturesFileFilter());
int returnedValue = fileChooser.showOpenDialog(this);

if (returnedValue == JFileChooser.APPROVE_OPTION) {

	File f = fileChooser.getSelectedFile();
	//pnlScanDir.updateDirToScan(f.getPath());
	//lblDirToScan.setText(f.getPath());
	//metadataExtractor.scanDir(f);
	picManager.scanDir(f);
	//lstPictures = picManager.getPictures();
	updatePicture(picManager.getCurrentPicture());
}
}*/

/*else if (ae.getSource().equals(btnChooseDir)) {

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
}
}*/
