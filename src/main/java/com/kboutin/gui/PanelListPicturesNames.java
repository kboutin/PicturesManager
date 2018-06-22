package com.kboutin.gui;

import com.kboutin.core.Picture;
import com.kboutin.utils.GUIUtils;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.BorderLayout;
import java.util.List;

public class PanelListPicturesNames extends JPanel {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private DefaultListModel<Picture> listModelPicture = new DefaultListModel<>();
	private JList<Picture> lstPictures = new JList<>(listModelPicture);

	public PanelListPicturesNames() {

		setLayout(new BorderLayout());
		JScrollPane scrollLstPictures = new JScrollPane(lstPictures);
		scrollLstPictures.setBorder(GUIUtils.createEtchedTitledBorder("Fichier(s) trouve(s)"));
		add(scrollLstPictures, BorderLayout.CENTER);
	}

	public final void clearList() {

		if (!listModelPicture.isEmpty()) {
			listModelPicture.clear();
		}
	}

	public final void updateList(List<Picture> lstPictures) {

		lstPictures.forEach(picture -> listModelPicture.addElement(picture));
	}

	/*
	 * Graphical methods section
	 */

	/**
	 * Method to add the given listSelectionListener to the lstPictures object.
	 * @param l
	 */
	public final void addListSelectionListener(ListSelectionListener l) {

		lstPictures.addListSelectionListener(l);
	}

	/**
	 * Method to return the selected value in the lstPictures object.
	 * @return the selected picture if it exists, null otherwise.
	 */
	public final Picture getSelectedPicture() {

		return lstPictures.getSelectedValue();
	}

	public final boolean triggeredListSelectionEvent(ListSelectionEvent e) {

		return e.getSource().equals(lstPictures);
	}
}
