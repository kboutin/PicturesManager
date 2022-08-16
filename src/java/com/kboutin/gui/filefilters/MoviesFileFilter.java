package com.kboutin.gui.filefilters;

import javax.swing.filechooser.FileFilter;
import java.io.File;

import static com.kboutin.utils.FileUtils.isMovie;

public class MoviesFileFilter extends FileFilter implements java.io.FileFilter {

	@Override
	public boolean accept(File f) {

		return f.isDirectory() || isMovie(f);
	}

	@Override
	public String getDescription() {

		return "Movies Filter (*.avi, *.mp4, *.mpg, *.mov)";
	}
}
