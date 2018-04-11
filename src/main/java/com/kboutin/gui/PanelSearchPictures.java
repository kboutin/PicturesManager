package com.kboutin.gui;

import com.kboutin.core.Picture;
import com.kboutin.core.PicturesFinder;
import com.kboutin.core.PicturesManager;
import com.kboutin.gui.filefilters.PicturesFileFilter;
import com.kboutin.utils.GUIUtils;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.List;

public class PanelSearchPictures extends JPanel implements ActionListener, ItemListener, ListSelectionListener {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private JLabel lblDirToScan = new JLabel();
	private JButton btnChooseDir = new JButton("...");

	private ComboBoxModel<String> cboBoxModelSearchCriteria = new DefaultComboBoxModel<>();
	private JComboBox<String> cboSearchCriteria = new JComboBox<>(cboBoxModelSearchCriteria);
	private DefaultListModel<String> listModelValues = new DefaultListModel<>();
	private JList<String> lstValues = new JList<>(listModelValues);

	private PanelListPicturesNames pnlListPictures = new PanelListPicturesNames();
	private PanelPicture pnlPicture = new PanelPicture();

	private PicturesManager picManager = PicturesManager.getInstance();

	public PanelSearchPictures() {

		super(new BorderLayout());

		btnChooseDir.addActionListener(this);
		pnlListPictures.addListSelectionListener(this);

		JPanel pnlDirToScan = new JPanel(new BorderLayout());
		pnlDirToScan.add(lblDirToScan, BorderLayout.CENTER);
		pnlDirToScan.add(btnChooseDir, BorderLayout.EAST);
		pnlDirToScan.setBorder(GUIUtils.createEtchedTitledBorder("Repertoire a analyser"));

		lstValues.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		lstValues.setVisibleRowCount(3);
		cboSearchCriteria.addItemListener(this);
		lstValues.addListSelectionListener(this);

		JScrollPane scrollLstValues = new JScrollPane(lstValues);
		JPanel pnlSearch = new JPanel(new GridLayout(0, 2));
		pnlSearch.add(cboSearchCriteria);
		pnlSearch.add(scrollLstValues);
		pnlSearch.setBorder(GUIUtils.createEtchedTitledBorder("Criteres de recherche"));

		JPanel pnlVisu = new JPanel(new BorderLayout());
		pnlVisu.add(pnlListPictures, BorderLayout.WEST);
		pnlVisu.add(pnlPicture, BorderLayout.CENTER);

		JPanel pnlContent = new JPanel(new BorderLayout());
		pnlContent.add(pnlSearch, BorderLayout.NORTH);
		pnlContent.add(pnlVisu, BorderLayout.CENTER);

		add(pnlDirToScan, BorderLayout.NORTH);
		add(pnlContent, BorderLayout.CENTER);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == btnChooseDir) {

			JFileChooser fileChooser = new JFileChooser(new File(System.getProperty("user.home")));
			fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			fileChooser.setMultiSelectionEnabled(false);
			fileChooser.setAcceptAllFileFilterUsed(false);
			fileChooser.setFileFilter(new PicturesFileFilter());
			int returnedValue = fileChooser.showOpenDialog(this);

			if (returnedValue == JFileChooser.APPROVE_OPTION) {

				File f = fileChooser.getSelectedFile();
				lblDirToScan.setText(f.getPath());
				//PicturesLister picturesLister = new PicturesLister(f);
				//picturesLister.execute();
			}
		}
	}

	@Override
	public void itemStateChanged(ItemEvent e) {

		if (e.getSource().equals(cboSearchCriteria)) {

			if (!listModelValues.isEmpty()) {
				listModelValues.clear();
			}
			String selectedCriteria = cboBoxModelSearchCriteria.getSelectedItem().toString();
			//picManager.getValuesForKey(selectedCriteria).stream().forEach(foundValue -> listModelValues.addElement(foundValue));
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {

		if (e.getSource().equals(lstValues)) {

			if (!e.getValueIsAdjusting()) {

				PicturesFinder picsFinder = new PicturesFinder(picManager.getPictures());
				pnlListPictures.clearList();
				if (cboBoxModelSearchCriteria.getSelectedItem() != null && lstValues.getSelectedIndex() >= 0) {
					List<Picture> filteredList = picsFinder.findPicturesWithCondition(cboBoxModelSearchCriteria.getSelectedItem().toString(), lstValues.getSelectedValue());
					pnlListPictures.updateList(filteredList);
				}
			}
		} else if (pnlListPictures.triggeredListSelectionEvent(e)) {

			Picture selectedPic = pnlListPictures.getSelectedValue();
			pnlPicture.updatePicture(selectedPic);
		}
	}

	// Class to list pictures and display their names smoothly in the GUI
	/*private class PicturesLister extends SwingWorker<Set<String>, String> {

		private File dirToScan = null;

		public PicturesLister(File dirToScan) {

			this.dirToScan = dirToScan;
		}

		@Override
		protected Set<String> doInBackground() throws Exception {

			scanDir(dirToScan);

			return picManager.getMetadataKeySet();
		}

		@Override
		protected void done() {

			if (!listModelValues.isEmpty()) {
				listModelValues.clear();
			}
			String selectedCriteria = cboBoxModelSearchCriteria.getSelectedItem().toString();
			picManager.getValuesForKey(selectedCriteria).forEach(foundValue -> listModelValues.addElement(foundValue));
		}

		@Override
		protected void process(List<String> chunks) {

			for (String s : chunks) {
				cboSearchCriteria.addItem(s);
			}
		}

		private void scanDir(File f) {

			if (f.isDirectory()) {
				Stream.of(f.listFiles()).forEach(subFile -> scanDir(subFile));
			} else if (f.isFile()) {
				if (FileUtils.isPicture(f)) {
					int oldKeysSize = picManager.getMetadataKeySet().size();
					Picture p = new Picture(f);
					picManager.addPicture(p);
					picManager.addMetadataForPicture(p);
					Set<String> newKeys = picManager.getMetadataKeySet();
					if (newKeys.size() > oldKeysSize) {
						// Publish name only if a new metaData has been added to the list ...
						newKeys.forEach(newKey -> publish(newKey));
					}
				}
			}
		}
	}*/
}
