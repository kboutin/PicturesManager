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

import static com.kboutin.utils.GUIUtils.showFileLoader;
import static com.kboutin.utils.GUIUtils.showFileSaver;
import static com.kboutin.utils.StringConstants.LABEL_MENU_FILE;
import static com.kboutin.utils.StringConstants.LABEL_MENU_FILE_LOAD;
import static com.kboutin.utils.StringConstants.LABEL_MENU_FILE_RESET;
import static com.kboutin.utils.StringConstants.LABEL_MENU_FILE_SAVE;

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

	public GenFrame() {

		picManager = PicturesManager.getInstance();

		PanelMetadataExtractor panelMetadataExtractor = new PanelMetadataExtractor();

		MenuBar menuBar = new MenuBar();
		Menu menuFile = new Menu(LABEL_MENU_FILE);
		MenuItem menuFileReset = new MenuItem(LABEL_MENU_FILE_RESET);
		MenuItem menuFileSave = new MenuItem(LABEL_MENU_FILE_SAVE);
		MenuItem menuFileLoad = new MenuItem(LABEL_MENU_FILE_LOAD);

		menuFileReset.addActionListener(this);
		menuFileReset.addActionListener(panelMetadataExtractor);
		menuFileReset.setActionCommand(LABEL_MENU_FILE_RESET);

		menuFileSave.addActionListener(this);
		menuFileSave.addActionListener(panelMetadataExtractor);
		menuFileSave.setActionCommand(LABEL_MENU_FILE_SAVE);

		menuFileLoad.addActionListener(this);
		menuFileLoad.addActionListener(panelMetadataExtractor);
		menuFileLoad.setActionCommand(LABEL_MENU_FILE_LOAD);

		menuFile.add(menuFileReset);
		menuFile.add(menuFileSave);
		menuFile.add(menuFileLoad);

		menuBar.add(menuFile);
		setMenuBar(menuBar);

		JTabbedPane tabPane = new JTabbedPane();
		setContentPane(tabPane);

		tabPane.addTab("Extract Metadata", panelMetadataExtractor);
		tabPane.addTab("Find duplicated files", new PanelDupFinder());
		tabPane.addTab("Advanced pictures search", new PanelSearchPictures());

		// Default Properties
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setTitle("Pictures Manager");
		setSize(getToolkit().getScreenSize());
		setLocationRelativeTo(null);
		setVisible(true);

		setExtendedState(JFrame.MAXIMIZED_BOTH);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		String actionCommand = e.getActionCommand();
		if (LABEL_MENU_FILE_RESET.equals(actionCommand)) {
			logger.debug("Resetting list of pictures");
			picManager.clearListPictures();
		} else if (LABEL_MENU_FILE_SAVE.equals(actionCommand)) {
			File fileToSave = showFileSaver(this);
			if (fileToSave != null) {
				logger.debug("Saving file " + fileToSave.getAbsolutePath());
				JSONUtils.savePictures(picManager.getPictures(), fileToSave.getAbsolutePath());
			}
		} else if (LABEL_MENU_FILE_LOAD.equals(actionCommand)) {
			File fileToLoad = showFileLoader(this);
			if (fileToLoad != null) {
				picManager.loadPicturesFromFile(fileToLoad.getAbsolutePath());
			}
		}
		/*int selectedTab = tabPane.getSelectedIndex();
		if (selectedTab == 0) {
			pnlMetadataExtractor.chooseDir();
		} else if (selectedTab == 1) {
			pnlDuplicateFinder.chooseDir();
		}*/
	}
}
