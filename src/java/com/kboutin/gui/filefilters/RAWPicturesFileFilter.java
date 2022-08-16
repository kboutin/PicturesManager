package com.kboutin.gui.filefilters;

import javax.swing.filechooser.FileFilter;
import java.io.File;

import static com.kboutin.utils.FileUtils.isRAWPicture;

/**
 *
 * @author kboutin
 */
public class RAWPicturesFileFilter extends FileFilter implements java.io.FileFilter {

	@Override
	public boolean accept(File f) {

		return f.isDirectory() || isRAWPicture(f);
	}

	@Override
	public String getDescription() {

		return "RAW Pictures (*.ARW (Sony), *.CR2 (Canon))";
	}
}
