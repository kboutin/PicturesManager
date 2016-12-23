package com.kboutin.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.kboutin.core.Picture;
import com.kboutin.core.PicturesManager;
import com.kboutin.gui.filefilters.PicturesFileFilter;
import com.kboutin.utils.GUIUtils;
import com.kboutin.utils.StringUtils;

public class PanelMetadataExtractor extends JPanel implements ActionListener {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private JPanel pnlForPicture = new JPanel(new BorderLayout());

	private JPanel pnlForButtons = new JPanel();
	private JButton btnPrevious = new JButton(" < ");
	private JButton btnNext = new JButton(" > ");

	//private PanelScanDir pnlScanDir = null;
	private JPanel pnlDirToScan = new JPanel(new BorderLayout());
	private JLabel lblDirToScan = new JLabel();
	private JButton btnChooseDir = new JButton("...");

	private PanelPicture pnlPicture = new PanelPicture();

	private JScrollPane pnlPictureMetadata = null;
	private JTextArea txtPictureMetadata = null;

	private PicturesManager picManager = PicturesManager.getInstance();
	private List<Picture> lstPictures = new ArrayList<Picture>();
	private int selectedIndex = 0;

	public PanelMetadataExtractor() {

		super(new BorderLayout());

		btnPrevious.addActionListener(this);
		btnNext.addActionListener(this);
		pnlForButtons.add(btnPrevious);
		pnlForButtons.add(btnNext);

		//this.pnlScanDir = pnlScanDir;
		//this.pnlScanDir.getButtonChooseDir().addActionListener(this);
		btnChooseDir.addActionListener(this);
		pnlDirToScan.add(lblDirToScan, BorderLayout.CENTER);
		pnlDirToScan.add(btnChooseDir, BorderLayout.EAST);
		pnlDirToScan.setBorder(GUIUtils.createEtchedTitledBorder("Repertoire a analyser"));

		//pnlLeft.add(pnlDirToScan, BorderLayout.NORTH);
		pnlForPicture.add(pnlPicture, BorderLayout.CENTER);
		pnlForPicture.add(pnlForButtons, BorderLayout.SOUTH);

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

		add(pnlDirToScan, BorderLayout.NORTH);
		add(pnlCenter, BorderLayout.CENTER);
		//add(this.pnlScanDir, BorderLayout.NORTH);
		//add(pnlForPicture, BorderLayout.WEST);
		//add(pnlPictureMetadata, BorderLayout.CENTER);
	}

	public final void chooseDir() {

		JFileChooser fileChooser = new JFileChooser(new File(StringUtils.USER_HOME));
		fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		fileChooser.setMultiSelectionEnabled(false);
		fileChooser.setAcceptAllFileFilterUsed(true);
		fileChooser.setFileFilter(new PicturesFileFilter());
		int returnedValue = fileChooser.showOpenDialog(this);

		if (returnedValue == JFileChooser.APPROVE_OPTION) {

			File f = fileChooser.getSelectedFile();
			//pnlScanDir.updateDirToScan(f.getPath());
			lblDirToScan.setText("Repertoire a analyser : " + f.getPath());
			//metadataExtractor.scanDir(f);
			picManager.setDirToScan(f);
			picManager.scanDir(f);
			//metadataExtractor.printMetas(); // Test
			//lstPictures = metadataExtractor.getLstPictures();
			lstPictures = picManager.getPictures();
			updatePicture(getCurrentPicture());
		}
	}

	@Override
	public void actionPerformed(ActionEvent ae) {

		if (ae.getSource().equals(btnPrevious)) {

			selectedIndex--;
			if (selectedIndex < 0) {
				selectedIndex = lstPictures.size() - 1;
			}
			updatePicture(getCurrentPicture());
		} else if (ae.getSource().equals(btnNext)) {

			selectedIndex++;
			if (selectedIndex >= lstPictures.size()) {
				selectedIndex = 0;
			}
			updatePicture(getCurrentPicture());
			/*PictureFinder finder = new PictureFinder(lstPictures);
			List<Picture> filteredPictures = finder.findPicturesWithCondition("F-Number", "f/2,8");
			if (filteredPictures != null && !filteredPictures.isEmpty())
				updatePicture(new File(filteredPictures.get(0).getFilePath()));*/
		} else if (ae.getSource().equals(btnChooseDir)) {

			JFileChooser fileChooser = new JFileChooser(new File(StringUtils.USER_HOME));
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
				updatePicture(getCurrentPicture());
			}
		}
	}

	private final void displayPictureInfo(Picture p) {

		if (!txtPictureMetadata.getText().isEmpty()) {

			txtPictureMetadata.setText("");
		}

		txtPictureMetadata.setText(p.getMetadataAsString());
		txtPictureMetadata.setCaretPosition(0);
	}

	private final Picture getCurrentPicture() {

		return lstPictures.isEmpty() ? null : lstPictures.get(selectedIndex);
	}

	private final void updatePicture(Picture picture) {

		if (picture != null) {
			pnlPicture.updatePicture(picture);
			displayPictureInfo(picture);
		}
	}
}
