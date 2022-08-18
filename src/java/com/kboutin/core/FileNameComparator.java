package com.kboutin.core;

import java.io.File;
import java.util.Comparator;

public class FileNameComparator implements Comparator<File> {

	@Override
	public int compare(File f1, File f2) {
		return f2.getName().compareTo(f1.getName());
	}
}
