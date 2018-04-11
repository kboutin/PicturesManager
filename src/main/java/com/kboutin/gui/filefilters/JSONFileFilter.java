package com.kboutin.gui.filefilters;

import com.kboutin.utils.FileUtils;

import javax.swing.filechooser.FileFilter;
import java.io.File;

/**
 * 
 * @author kboutin
 */
public class JSONFileFilter extends FileFilter implements java.io.FileFilter {

	@Override
	public boolean accept(File f) {

		return FileUtils.isJSONFile(f);
	}

	@Override
	public String getDescription() {

		return "JSON Files (*.json)";
	}
}
