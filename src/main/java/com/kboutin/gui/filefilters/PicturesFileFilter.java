package com.kboutin.gui.filefilters;

import com.kboutin.utils.FileUtils;

import javax.swing.filechooser.FileFilter;
import java.io.File;

/**
 * 
 * @author kboutin
 */
public class PicturesFileFilter extends FileFilter implements java.io.FileFilter {

	@Override
	public boolean accept(File f) {

		return f.isDirectory() || FileUtils.isPicture(f);
	}

	@Override
	public String getDescription() {

		return "Pictures Files (*.jpg, *.png)";
	}
}