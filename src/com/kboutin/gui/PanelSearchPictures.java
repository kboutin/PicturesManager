package com.kboutin.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.kboutin.core.Picture;
import com.kboutin.core.PicturesFinder;
import com.kboutin.core.PicturesManager;
import com.kboutin.utils.GUIUtils;

public class PanelSearchPictures extends JPanel implements ActionListener, ItemListener, ListSelectionListener {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private JPanel pnlContent = new JPanel(new BorderLayout());
	private JPanel pnlSearch = new JPanel(new GridLayout(0, 2));
	private JPanel pnlVisu = new JPanel(new BorderLayout());

	private ComboBoxModel<String> cboBoxModelSearchCriteria = new DefaultComboBoxModel<String>();
	private JComboBox<String> cboSearchCriteria = new JComboBox<String>(cboBoxModelSearchCriteria);
	private DefaultListModel<String> listModelValues = new DefaultListModel<String>();
	private JList<String> lstValues = new JList<String>(listModelValues);
	private JScrollPane scrollLstValues = new JScrollPane(lstValues);

	private PanelListPicturesNames pnlListPictures = new PanelListPicturesNames();
	private PanelPicture pnlPicture = new PanelPicture();

	private PicturesManager picManager = PicturesManager.getInstance();
	private Map<String, Set<String>> mapValuesForMetadata = new TreeMap<>();

	public PanelSearchPictures() {

		super(new BorderLayout());

		lstValues.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		lstValues.setVisibleRowCount(3);
		cboSearchCriteria.addItemListener(this);
		lstValues.addListSelectionListener(this);
		pnlSearch.add(cboSearchCriteria);
		pnlSearch.add(scrollLstValues);
		pnlSearch.setBorder(GUIUtils.createEtchedTitledBorder("Criteres de recherche"));

		pnlListPictures.addListSelectionListener(this);
		pnlVisu.add(pnlListPictures, BorderLayout.WEST);
		pnlVisu.add(pnlPicture, BorderLayout.CENTER);

		pnlContent.add(pnlSearch, BorderLayout.NORTH);
		pnlContent.add(pnlVisu, BorderLayout.CENTER);

		add(pnlContent, BorderLayout.CENTER);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		/*if (e.getSource() == btnChooseDir) {

			JFileChooser fileChooser = new JFileChooser(new File(System.getProperty("user.home")));
			fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			fileChooser.setMultiSelectionEnabled(false);
			fileChooser.setAcceptAllFileFilterUsed(false);
			fileChooser.setFileFilter(new PicturesFileFilter());
			int returnedValue = fileChooser.showOpenDialog(this);

			if (returnedValue == JFileChooser.APPROVE_OPTION) {

				File f = fileChooser.getSelectedFile();
				lblDirToScan.setText(f.getPath());
				PicturesLister picturesLister = new PicturesLister(f);
				picturesLister.execute();
			}
		}*/
	}

	@Override
	public void itemStateChanged(ItemEvent e) {

		if (e.getSource().equals(cboSearchCriteria)) {

			if (!listModelValues.isEmpty()) {
				listModelValues.clear();
			}
			String selectedCriteria = cboBoxModelSearchCriteria.getSelectedItem().toString();
			// Récupérer les valeurs du map pour la clé du selectedCriteria.
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
	public class PicturesLister extends SwingWorker<Set<String>, String> {

		//private File dirToScan = null;

		/*public PicturesLister(File dirToScan) {

			this.dirToScan = dirToScan;
		}*/

		@Override
		protected Set<String> doInBackground() throws Exception {

			//scanDir(dirToScan);
			scanPictures(picManager.getPictures());

			//return picManager.getMetadataKeySet();
			return mapValuesForMetadata.keySet();
		}

		@Override
		protected void done() {

			if (!listModelValues.isEmpty()) {
				listModelValues.clear();
			}
			/*String selectedCriteria = String.valueOf(cboBoxModelSearchCriteria.getSelectedItem());
			// Récupérer le Set de valeurs de selectedCriteria, et le retourner ...
			List<Entry<String, Set<String>>> entries = mapValuesForMetadata.entrySet().stream()
					.filter(entry -> entry.getKey().equals(selectedCriteria))
					.collect(Collectors.toList());
			for (Entry<String, Set<String>> entry : entries) {

			}*/
			//.map(entry -> listModelValues.addElement(entry.getValue()));
			//picManager.getValuesForKey(selectedCriteria).forEach(foundValue -> listModelValues.addElement(foundValue));

		}

		@Override
		protected void process(List<String> chunks) {

			//chunks.stream().forEach(cboSearchCriteria::addItem);
			chunks.stream().forEach(s -> cboSearchCriteria.addItem(s));
		}

		/*private void scanDir(File f) {

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
		}*/

		private void scanPictures(List<Picture> lstPictures) {

			lstPictures.forEach(pic -> extractMetadataFromPicture(pic));
		}

		private void extractMetadataFromPicture(Picture pic) {

			for (Entry<String, String> entry : pic.getMetadata().entrySet()) {
				Set<String> keySet = mapValuesForMetadata.get(entry.getKey());
				if (keySet == null) {
					keySet = new TreeSet<>();
				}
				if (keySet.add(entry.getValue())) {
					publish(entry.getValue());
				}
				mapValuesForMetadata.put(entry.getKey(), keySet);
			}
		}
	}
}
