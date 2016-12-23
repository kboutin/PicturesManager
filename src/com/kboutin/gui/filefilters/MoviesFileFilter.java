package com.kboutin.gui.filefilters;

import java.io.File;

import javax.swing.filechooser.FileFilter;

import com.kboutin.utils.FileUtils;

public class MoviesFileFilter extends FileFilter implements java.io.FileFilter {

	@Override
	public boolean accept(File f) {

		return f.isDirectory() || FileUtils.isMovie(f);
	}

	@Override
	public String getDescription() {

		return "Movies Filter (*.avi, *.mp4, *.mpg, *.mov)";
	}
}
