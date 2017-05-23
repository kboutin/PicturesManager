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

	// private PanelSearchPictures pnlSearchPictures = new PanelSearchPictures();

	// private PanelScanDir pnlScanDir = null;

	public GenFrame() {

		JTabbedPane tabPane = new JTabbedPane();
		setContentPane(tabPane);

		//PanelMetadataExtractor pnlMetadataExtractor = new PanelMetadataExtractor();
		//PanelDupFinder pnlDuplicateFinder = new PanelDupFinder();

		tabPane.addTab("MetadataExtractor", new PanelMetadataExtractor());
		tabPane.addTab("DuplicateFinder", new PanelDupFinder());

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
