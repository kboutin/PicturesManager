package com.kboutin.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.drew.imaging.ImageProcessingException;
import com.kboutin.core.Picture;
import com.kboutin.core.PicturesManager;
import com.kboutin.gui.filefilters.PicturesFileFilter;
import com.kboutin.utils.FileUtils;
import com.kboutin.utils.StringUtils;

public class GenFrame extends JFrame implements ActionListener {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private final static Logger logger = LogManager.getLogger(PicturesManager.class);

	private PicturesManager picManager = PicturesManager.getInstance();

	private PanelScanDir pnlScanDir = new PanelScanDir();
	private PanelMetadataExtractor pnlMetadataExtractor = new PanelMetadataExtractor();
	private PanelDupFinder pnlDupFinder = new PanelDupFinder();
	private PanelSearchPictures pnlSearchPictures = new PanelSearchPictures();

	//private ComboBoxModel cboModel = new DefaultComboBoxModel();
	//private JComboBox cboPictures = new JComboBox(cboModel);
	//private JList listOfPictures = new JList(listModel);

	// private PanelScanDir pnlScanDir = null;

	public GenFrame() {

		getContentPane().setLayout(new BorderLayout());
		pnlScanDir.addActionListener(this);
		getContentPane().add(pnlScanDir, BorderLayout.NORTH);

		JTabbedPane tabPane = new JTabbedPane();
		tabPane.addTab("DuplicateFinder", pnlDupFinder);
		tabPane.addTab("MetadataExtractor", pnlMetadataExtractor);
		tabPane.addTab("PicturesFinder", pnlSearchPictures);

		getContentPane().add(tabPane, BorderLayout.CENTER);

		// Default Properties
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("PicturesManager");
		setSize(getToolkit().getScreenSize());
		setLocationRelativeTo(null);
		setVisible(true);

		setExtendedState(JFrame.MAXIMIZED_BOTH);
	}

	private final void scanDir(File f) {

		if (f.isDirectory()) {
			Stream.of(f.listFiles()).forEach(subFile -> scanDir(subFile));
		} else if (f.isFile()) {
			if(FileUtils.isPicture(f)) {
				//Picture p = new Picture(f);
				picManager.addPicture(new Picture(f));
				//addMetadataForPicture(p);
			}
		}
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

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getActionCommand().equals(StringUtils.SCAN_DIR_COMMAND)) {

			JFileChooser fileChooser = new JFileChooser(new File(StringUtils.USER_HOME));
			fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			fileChooser.setMultiSelectionEnabled(false);
			fileChooser.setAcceptAllFileFilterUsed(true);
			fileChooser.setFileFilter(new PicturesFileFilter());
			int returnedValue = fileChooser.showOpenDialog(this);

			if (returnedValue == JFileChooser.APPROVE_OPTION) {

				File f = fileChooser.getSelectedFile();
				pnlScanDir.updateDirToScan(f.getPath());
				//lblDirToScan.setText(f.getPath());
				//metadataExtractor.scanDir(f);
				scanDir(f);
				//lstPictures = picManager.getPictures();
				//updatePicture(picManager.getCurrentPicture());
				pnlMetadataExtractor.updatePicture(picManager.getCurrentPicture());

				// TODO KBO : Use PicturesLister to display fileNames smoothly in the UI.
				//PicturesLister picLister = pnlSearchPictures.new PicturesLister();
				//picLister.execute();
				// FIXME KBO : Update the list of found files and update the lstPictures of the pnlDupFinder object accordingly.
				//pnlDupFinder
			}
		}
	}
}
