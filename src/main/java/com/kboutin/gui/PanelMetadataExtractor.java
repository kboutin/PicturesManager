package com.kboutin.gui;

import com.kboutin.core.Picture;
import com.kboutin.core.PicturesManager;
import com.kboutin.utils.FileUtils;
import com.kboutin.utils.GUIUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import static com.kboutin.utils.GUIUtils.showFileChooser;
import static com.kboutin.utils.StringConstants.CHOOSE_DIR;
import static com.kboutin.utils.StringConstants.FIRST;
import static com.kboutin.utils.StringConstants.LABEL_MENU_FILE_RESET;
import static com.kboutin.utils.StringConstants.LAST;
import static com.kboutin.utils.StringConstants.NEXT;
import static com.kboutin.utils.StringConstants.PREVIOUS;

public class PanelMetadataExtractor extends JPanel implements ActionListener {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private final static Logger logger = LogManager.getLogger(GenFrame.class);

	private PanelScanDir pnlDirToScan = new PanelScanDir();

	private PanelPicture pnlPicture = new PanelPicture();

	private JTextArea txtPictureMetadata = null;

	private PicturesManager picManager = PicturesManager.getInstance();

	public PanelMetadataExtractor() {

		super(new BorderLayout());

		JButton btnFirst = new JButton(" << ");
		btnFirst.addActionListener(this);
		btnFirst.setActionCommand(FIRST);

		JButton btnPrevious = new JButton(" < ");
		btnPrevious.addActionListener(this);
		btnPrevious.setActionCommand(PREVIOUS);

		JButton btnNext = new JButton(" > ");
		btnNext.addActionListener(this);
		btnNext.setActionCommand(NEXT);

		JButton btnLast = new JButton(" >> ");
		btnLast.addActionListener(this);
		btnLast.setActionCommand(LAST);

		JPanel pnlForButtons = new JPanel(new GridLayout(1, 4));
		pnlForButtons.add(btnFirst);
		pnlForButtons.add(btnPrevious);
		pnlForButtons.add(btnNext);
		pnlForButtons.add(btnLast);

		pnlDirToScan.addActionListener(this);
		pnlDirToScan.setActionCommand(CHOOSE_DIR);

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
	}

	@Override
	public void actionPerformed(ActionEvent ae) {

		String actionCommand = ae.getActionCommand();
		if (FIRST.equals(actionCommand)) {
			updatePicture(picManager.firstPicture());
		} else if (PREVIOUS.equals(actionCommand)) {
			updatePicture(picManager.previousPicture());
		} else if (NEXT.equals(actionCommand)) {
			updatePicture(picManager.nextPicture());
		} else if (LAST.equals(actionCommand)) {
			updatePicture(picManager.lastPicture());
		} else if (CHOOSE_DIR.equals(actionCommand)) {
			File selectedFile = showFileChooser(this);
			if (selectedFile != null) {
				pnlDirToScan.setDirToScan(selectedFile.getPath());
				if (selectedFile.isDirectory()) {
					//metadataExtractor.scanDir(f);
					//picManager.scanDir(f);
					new PicturesLister(selectedFile, this).execute();
					//lstPictures = picManager.getPictures();
					//updatePicture(picManager.currentPicture());
				} else if (FileUtils.isJSONFile(selectedFile)) {
					picManager.loadPicturesFromFile(selectedFile.getAbsolutePath());
				}
			}
		} else if (LABEL_MENU_FILE_RESET.equals(actionCommand)) {
			updatePicture(null);
		} /*else if (LABEL_MENU_FILE_SAVE.equals(actionCommand)) {
			File fileToSave = showFileSaver();
			if (fileToSave != null) {
				logger.debug("Saving file");
				JSONUtils.savePictures(picManager.getPictures(), fileToSave.getAbsolutePath());
			}
		} else if (LABEL_MENU_FILE_LOAD.equals(actionCommand)) {
			updatePicture(picManager.currentPicture());
		}*/
	}

	private void displayPictureInfo(Picture p) {

		// First, empty the screen...
		txtPictureMetadata.setText("");
		if (p != null) {
			txtPictureMetadata.setText(p.getMetadataAsString());
			txtPictureMetadata.setCaretPosition(0);
		}
	}

	public void updatePicture(Picture picture) {

		pnlPicture.updatePicture(picture);
		displayPictureInfo(picture);
	}

	/*PictureFinder finder = new PictureFinder(lstPictures);
			List<Picture> filteredPictures = finder.findPicturesWithCondition("F-Number", "f/2,8");
			if (filteredPictures != null && !filteredPictures.isEmpty())
				updatePicture(new File(filteredPictures.get(0).getFilePath()));*/
}
