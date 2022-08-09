package com.kboutin.gui.filefilters;

import javax.swing.filechooser.FileFilter;
import java.io.File;

import static com.kboutin.utils.FileUtils.isPicture;

/**
 *
 * @author Kouikoui
 */
public class PicturesFileFilter extends FileFilter implements java.io.FileFilter {

	@Override
	public boolean accept(File f) {

		return f.isDirectory() || isPicture(f);
	}

	@Override
	public String getDescription() {

		return "Pictures Filter (*.jpg, *.png)";
	}
}
