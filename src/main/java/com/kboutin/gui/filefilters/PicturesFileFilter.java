package com.kboutin.gui.filefilters;

import java.io.File;

import javax.swing.filechooser.FileFilter;

import com.kboutin.utils.FileUtils;

/**
 * 
 * @author Kouikoui
 */
public class PicturesFileFilter extends FileFilter implements java.io.FileFilter {

	@Override
	public boolean accept(File f) {

		return f.isDirectory() || FileUtils.isPicture(f);
	}

	@Override
	public String getDescription() {

		return "Pictures Filter (*.jpg, *.png)";
	}
}
