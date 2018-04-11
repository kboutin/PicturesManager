package com.kboutin.gui;

import com.kboutin.core.PicturesManager;
import com.kboutin.utils.JSONUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Collections;

public class GenFrame extends JFrame implements ActionListener {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private final static Logger logger = LogManager.getLogger(GenFrame.class);

	//private PanelMetadataExtractor panelMetadataExtractor = new PanelMetadataExtractor();

	//private ComboBoxModel cboModel = new DefaultComboBoxModel();
	//private JComboBox cboPictures = new JComboBox(cboModel);
	//private JList listOfPictures = new JList(listModel);

	// private PanelScanDir pnlScanDir = null;
	private PicturesManager picManager;
	private MenuItem menuFileReset = new MenuItem("Reset List");
	private MenuItem menuFileSave = new MenuItem("Save");

	public GenFrame() {

		picManager = PicturesManager.getInstance();

		PanelMetadataExtractor panelMetadataExtractor = new PanelMetadataExtractor();

		MenuBar menuBar = new MenuBar();
		Menu menuFile = new Menu("File");

		menuFileReset.addActionListener(this);
		menuFileReset.addActionListener(panelMetadataExtractor);
		menuFileReset.setActionCommand("Reset");
		menuFileSave.addActionListener(this);
		menuFileSave.addActionListener(panelMetadataExtractor);
		menuFileSave.setActionCommand("Save");
		menuFile.add(menuFileReset);
		menuFile.add(menuFileSave);
		menuBar.add(menuFile);
		setMenuBar(menuBar);

		JTabbedPane tabPane = new JTabbedPane();
		setContentPane(tabPane);

		tabPane.addTab("Extract Metadata", panelMetadataExtractor);
		tabPane.addTab("Find duplicated files", new PanelDupFinder());
		tabPane.addTab("Advanced pictures search", new PanelSearchPictures());

		// Default Properties
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setTitle("PicturesManager");
		setSize(getToolkit().getScreenSize());
		setLocationRelativeTo(null);
		setVisible(true);

		setExtendedState(JFrame.MAXIMIZED_BOTH);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		//if (e.getSource().equals(menuFileReset)) {
		String actionCommand = e.getActionCommand();
		if (actionCommand.equals(menuFileReset.getActionCommand())) {
			logger.debug("Resetting list of pictures");
			picManager.setPictures(Collections.emptyList());
		} else if (actionCommand.equals(menuFileSave.getActionCommand())) {
			logger.debug("Saving file");
			File file = new File("/Users/kouikoui/Desktop/Pictures.json");
			JSONUtils.savePictures(picManager.getPictures(), file.getAbsolutePath());
		}
		/*int selectedTab = tabPane.getSelectedIndex();
		if (selectedTab == 0) {
			pnlMetadataExtractor.chooseDir();
		} else if (selectedTab == 1) {
			pnlDuplicateFinder.chooseDir();
		}*/
	}
}
