package com.kboutin.gui;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

public class GenFrame extends JFrame {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	//private ComboBoxModel cboModel = new DefaultComboBoxModel();
	//private JComboBox cboPictures = new JComboBox(cboModel);
	//private JList listOfPictures = new JList(listModel);

	private JTabbedPane tabPane = new JTabbedPane();

	// private PanelSearchPictures pnlSearchPictures = new
	// PanelSearchPictures();

	// private PanelScanDir pnlScanDir = null;

	private PanelMetadataExtractor pnlMetadataExtractor = null;
	private PanelDupFinder pnlDuplicateFinder = null;

	public GenFrame() {

		setContentPane(tabPane);

		pnlMetadataExtractor = new PanelMetadataExtractor();
		pnlDuplicateFinder = new PanelDupFinder();

		tabPane.addTab("MetadataExtractor", pnlMetadataExtractor);
		tabPane.addTab("DuplicateFinder", pnlDuplicateFinder);
		// tabPane.addTab("PicturesFinder", pnlSearchPictures);

		// Default Properties
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("PicturesManager");
		setSize(getToolkit().getScreenSize());
		setLocationRelativeTo(null);
		setVisible(true);

		setExtendedState(JFrame.MAXIMIZED_BOTH);
	}

	/*public int getSelectedTab() {

		return tabPane.getSelectedIndex();
	}*/

	/*@Override
	public void actionPerformed(ActionEvent e) {

		int selectedTab = getSelectedTab();
		if (selectedTab == 0) {
			pnlMetadataExtractor.chooseDir();
		} else if (selectedTab == 1) {
			pnlDuplicateFinder.chooseDir();
		}
	}*/
}
