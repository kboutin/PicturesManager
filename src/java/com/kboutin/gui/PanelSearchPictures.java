package com.kboutin.gui;

import com.kboutin.core.Picture;
import com.kboutin.core.PicturesFinder;
import com.kboutin.core.PicturesManager;
import com.kboutin.gui.filefilters.PicturesFileFilter;
import com.kboutin.gui.filefilters.RAWPicturesFileFilter;

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
import javax.swing.SwingWorker;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.Serial;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static com.kboutin.utils.FileUtils.isPicture;
import static com.kboutin.utils.GUIUtils.createEtchedTitledBorder;
import static com.kboutin.utils.StringConstants.USER_HOME;
import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.EAST;
import static java.awt.BorderLayout.NORTH;
import static java.awt.BorderLayout.WEST;
import static javax.swing.JFileChooser.APPROVE_OPTION;
import static javax.swing.JFileChooser.FILES_AND_DIRECTORIES;
import static javax.swing.ListSelectionModel.SINGLE_SELECTION;

public class PanelSearchPictures extends JPanel implements ActionListener, ItemListener, ListSelectionListener {

	@Serial
	private static final long serialVersionUID = 1L;

	private final JLabel lblDirToScan = new JLabel();
	private final JButton btnChooseDir = new JButton("...");

	private final ComboBoxModel<String> cboBoxModelSearchCriteria = new DefaultComboBoxModel<>();
	private final JComboBox<String> cboSearchCriteria = new JComboBox<>(cboBoxModelSearchCriteria);
	private final DefaultListModel<String> listModelValues = new DefaultListModel<>();
	private final JList<String> lstValues = new JList<>(listModelValues);

	private final PanelListPicturesNames pnlListPictures = new PanelListPicturesNames();
	private final PanelPicture pnlPicture = new PanelPicture();

	private final PicturesManager picManager = PicturesManager.getInstance();

	public PanelSearchPictures() {

		super(new BorderLayout());

		btnChooseDir.addActionListener(this);

		JPanel pnlDirToScan = new JPanel(new BorderLayout());
		pnlDirToScan.add(lblDirToScan, CENTER);
		pnlDirToScan.add(btnChooseDir, EAST);
		pnlDirToScan.setBorder(createEtchedTitledBorder("Repertoire a analyser"));

		lstValues.setSelectionMode(SINGLE_SELECTION);
		lstValues.setVisibleRowCount(3);
		cboSearchCriteria.addItemListener(this);
		lstValues.addListSelectionListener(this);
		JPanel pnlSearch = new JPanel(new GridLayout(0, 2));
		pnlSearch.add(cboSearchCriteria);
		JScrollPane scrollLstValues = new JScrollPane(lstValues);
		pnlSearch.add(scrollLstValues);
		pnlSearch.setBorder(createEtchedTitledBorder("Criteres de recherche"));

		pnlListPictures.addListSelectionListener(this);
		JPanel pnlVisu = new JPanel(new BorderLayout());
		pnlVisu.add(pnlListPictures, WEST);
		pnlVisu.add(pnlPicture, CENTER);

		JPanel pnlContent = new JPanel(new BorderLayout());
		pnlContent.add(pnlSearch, NORTH);
		pnlContent.add(pnlVisu, CENTER);

		add(pnlDirToScan, NORTH);
		add(pnlContent, CENTER);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == btnChooseDir) {

			JFileChooser fileChooser = new JFileChooser(new File(USER_HOME));
			fileChooser.setFileSelectionMode(FILES_AND_DIRECTORIES);
			fileChooser.setMultiSelectionEnabled(false);
			fileChooser.setAcceptAllFileFilterUsed(false);
			fileChooser.addChoosableFileFilter(new RAWPicturesFileFilter());
			fileChooser.setFileFilter(new PicturesFileFilter());
			int returnedValue = fileChooser.showOpenDialog(this);

			if (returnedValue == APPROVE_OPTION) {

				File f = fileChooser.getSelectedFile();
				lblDirToScan.setText(f.getPath());
				PicturesLister picturesLister = new PicturesLister(f);
				picturesLister.execute();
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
			picManager.getValuesForKey(selectedCriteria).forEach(listModelValues::addElement);
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
					pnlListPictures.selectFirstValue();
				}
			}
		} else if (pnlListPictures.triggeredListSelectionEvent(e)) {
			pnlPicture.updatePicture(pnlListPictures.getSelectedValue());
		}
	}

	// Class to list pictures and display their names smoothly in the GUI
	private class PicturesLister extends SwingWorker<Set<String>, String> {

		private final File dirToScan;

		public PicturesLister(File dirToScan) {
			this.dirToScan = dirToScan;
		}

		@Override
		protected Set<String> doInBackground() {
			scanDir(dirToScan);
			return picManager.getMetaDataKeySet();
		}

		@Override
		protected void done() {
			if (!listModelValues.isEmpty()) {
				listModelValues.clear();
			}
			String selectedCriteria = cboBoxModelSearchCriteria.getSelectedItem().toString();
			picManager.getValuesForKey(selectedCriteria).forEach(listModelValues::addElement);
		}

		@Override
		protected void process(List<String> chunks) {
			chunks.forEach(cboSearchCriteria::addItem);
		}

		private void scanDir(File f) {

			if (f.isDirectory()) {
				Stream.of(f.listFiles()).forEach(this::scanDir);
			} else if (f.isFile()) {
				if (isPicture(f)) {
					int oldKeysSize = picManager.getMetaDataKeySet().size();
					Picture p = new Picture(f);
					picManager.addPicture(p);
					picManager.addMetadataForPicture(p);
					Set<String> newKeys = picManager.getMetaDataKeySet();
					if (newKeys.size() > oldKeysSize) {
						// Publish name only if a new metaData has been added to the list ...
						newKeys.forEach(this::publish);
					}
				}
			}
		}
	}
}
