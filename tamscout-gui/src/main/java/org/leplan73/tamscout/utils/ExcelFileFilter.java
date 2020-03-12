package org.leplan73.tamscout.utils;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class ExcelFileFilter extends FileFilter {

	@Override
	public boolean accept(File f) {
		if (f.isDirectory()) {  
	        return true;  
	    }  
	    return (f.getName().toLowerCase().endsWith(".xlsx") || f.getName().toLowerCase().endsWith(".xls"));
	}

	@Override
	public String getDescription() {
		return "Excel file(*.xls *.xlsx)";  
	}
}
