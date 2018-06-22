package com.kboutin.gui;

import com.kboutin.core.MetaDataHandler;
import com.kboutin.core.Picture;
import com.kboutin.core.PicturesFinder;
import com.kboutin.core.PicturesManager;
import com.kboutin.gui.filefilters.PicturesFileFilter;
import com.kboutin.utils.FileUtils;
import com.kboutin.utils.GUIUtils;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
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
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Stream;

import static com.kboutin.utils.StringConstants.CHOOSE_DIR;

public class PanelSearchPictures extends JPanel implements ActionListener, ItemListener, ListSelectionListener {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private PanelScanDir pnlDirToScan = new PanelScanDir();

	private ComboBoxModel<String> cboBoxModelSearchCriteria = new DefaultComboBoxModel<>();
	private JComboBox<String> cboSearchCriteria = new JComboBox<>(cboBoxModelSearchCriteria);
	private DefaultListModel<String> listModelValues = new DefaultListModel<>();
	private JList<String> lstValues = new JList<>(listModelValues);

	private PanelListPicturesNames pnlListPictures = new PanelListPicturesNames();
	private PanelPicture pnlPicture = new PanelPicture();

	private PicturesManager picManager = PicturesManager.getInstance();

	private MetaDataHandler metaDataHandler = new MetaDataHandler();

	public PanelSearchPictures() {

		super(new BorderLayout());

		pnlDirToScan.addActionListener(this);
		pnlDirToScan.setActionCommand(CHOOSE_DIR);

		pnlListPictures.addListSelectionListener(this);
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

		if (CHOOSE_DIR.equals(e.getActionCommand())) {

			JFileChooser fileChooser = new JFileChooser(new File(System.getProperty("user.home")));
			fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			fileChooser.setMultiSelectionEnabled(false);
			fileChooser.setAcceptAllFileFilterUsed(false);
			fileChooser.setFileFilter(new PicturesFileFilter());

			if (JFileChooser.APPROVE_OPTION == fileChooser.showOpenDialog(this)) {

				File f = fileChooser.getSelectedFile();
				pnlDirToScan.setDirToScan(f.getPath());
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
			metaDataHandler.getValuesForKey(selectedCriteria).stream().forEach(foundValue -> listModelValues.addElement(foundValue));
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {

		if (e.getSource().equals(lstValues)) {

			if (!e.getValueIsAdjusting()) {

				pnlListPictures.clearList();
				if (cboBoxModelSearchCriteria.getSelectedItem() != null && lstValues.getSelectedIndex() >= 0) {
					List<Picture> filteredList = PicturesFinder.findPicturesWithCondition(picManager.getPictures(), cboBoxModelSearchCriteria.getSelectedItem().toString(), lstValues.getSelectedValue());
					pnlListPictures.updateList(filteredList);
				}
			}
		} else if (pnlListPictures.triggeredListSelectionEvent(e)) {

			Picture selectedPic = pnlListPictures.getSelectedPicture();
			pnlPicture.updatePicture(selectedPic);
		}
	}

	// Class to list pictures and display their names smoothly in the GUI
	private class PicturesLister extends SwingWorker<Set<String>, String> {

		private File dirToScan = null;
		private Set<String> keySet = null;

		public PicturesLister(File dirToScan) {

			this.dirToScan = dirToScan;
			this.keySet = new TreeSet<>();
		}

		@Override
		protected Set<String> doInBackground() {

			scanDir(dirToScan);

			picManager.getPictures().forEach(picture -> this.keySet.addAll(picture.getMetadata().keySet()));

			return this.keySet;
		}

		@Override
		protected void done() {

			if (!listModelValues.isEmpty()) {
				listModelValues.clear();
			}
			String selectedCriteria = cboBoxModelSearchCriteria.getSelectedItem().toString();
			metaDataHandler.getValuesForKey(selectedCriteria).forEach(foundValue -> listModelValues.addElement(foundValue));
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
					int oldKeysSize = metaDataHandler.getMetadataKeySet().size();
					Picture p = new Picture(f);
					picManager.addPicture(p);
					metaDataHandler.addMetadataForPicture(p);
					Set<String> newKeys = metaDataHandler.getMetadataKeySet();
					if (newKeys.size() > oldKeysSize) {
						// Publish name only if a new metaData has been added to the list ...
						newKeys.forEach(newKey -> {
							if (metaDataHandler.isMetaDataAccepted(newKey)) {
								publish(newKey);
							}
						});
					}
				}
			}
		}
	}
}
