package com.kboutin.utils;

import com.kboutin.gui.filefilters.JSONFileFilter;
import com.kboutin.gui.filefilters.PicturesFileFilter;

import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.Component;
import java.io.File;

import static com.kboutin.utils.FileUtils.USER_HOME;

public class GUIUtils {

	public static TitledBorder createEtchedTitledBorder(String title) {

		return BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED), title);
	}

	public static File showFileChooser(Component parent) {

		JFileChooser fileChooser = new JFileChooser(USER_HOME);
		fileChooser.addChoosableFileFilter(new JSONFileFilter());
		fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		fileChooser.setMultiSelectionEnabled(false);
		fileChooser.setAcceptAllFileFilterUsed(true);
		fileChooser.setFileFilter(new PicturesFileFilter());

		return JFileChooser.APPROVE_OPTION == fileChooser.showOpenDialog(parent)
				? fileChooser.getSelectedFile()
				: null;
	}

	public static File showFileSaver(Component parent) {

		JFileChooser fileChooser = new JFileChooser(USER_HOME);
		fileChooser.addChoosableFileFilter(new JSONFileFilter());
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileChooser.setMultiSelectionEnabled(false);
		fileChooser.setAcceptAllFileFilterUsed(true);

		return JFileChooser.APPROVE_OPTION == fileChooser.showSaveDialog(parent)
				? fileChooser.getSelectedFile()
				: null;
	}

	public static File showFileLoader(Component parent) {

		JSONFileFilter jsonFileFilter = new JSONFileFilter();
		JFileChooser fileChooser = new JFileChooser(USER_HOME);
		fileChooser.addChoosableFileFilter(jsonFileFilter);
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.setFileFilter(jsonFileFilter);

		return JFileChooser.APPROVE_OPTION == fileChooser.showOpenDialog(parent)
				? fileChooser.getSelectedFile()
				: null;
	}
}
