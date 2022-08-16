package com.kboutin.gui;

import com.kboutin.core.Picture;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.BorderLayout;
import java.io.Serial;
import java.util.List;

import static com.kboutin.utils.GUIUtils.createEtchedTitledBorder;
import static java.awt.BorderLayout.CENTER;

public class PanelListPicturesNames extends JPanel {

	@Serial
	private static final long serialVersionUID = 1L;

	private final DefaultListModel<Picture> listModelPicture = new DefaultListModel<>();
	private final JList<Picture> lstPictures = new JList<>(listModelPicture);

	public PanelListPicturesNames() {

		setLayout(new BorderLayout());
		JScrollPane scrollLstPictures = new JScrollPane(lstPictures);
		scrollLstPictures.setBorder(createEtchedTitledBorder("Fichier(s) trouve(s)"));
		add(scrollLstPictures, CENTER);
	}

	public final void clearList() {
		if (!listModelPicture.isEmpty()) {
			listModelPicture.clear();
		}
	}

	public final void updateList(List<Picture> lstPictures) {
		lstPictures.forEach(listModelPicture::addElement);
	}

	/*
	 * Graphical methods section
	 */

	/**
	 * Method to add the given listSelectionListener to the lstPictures object.
	 */
	public final void addListSelectionListener(ListSelectionListener l) {
		lstPictures.addListSelectionListener(l);
	}

	/**
	 * Method to return the selected value in the lstPictures object.
	 * @return the selected picture if it exists, null otherwise.
	 */
	public final Picture getSelectedValue() {
		return lstPictures.getSelectedValue();
	}

	public final void selectFirstValue() {
		if (!listModelPicture.isEmpty() && lstPictures.getSelectedIndex() == -1) {
			lstPictures.setSelectedIndex(0);
		}
	}

	public final boolean triggeredListSelectionEvent(ListSelectionEvent e) {
		return !e.getValueIsAdjusting() && e.getSource().equals(lstPictures);
	}
}
