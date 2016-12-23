package com.example.alex.filemanager_git;

import java.io.File;
import java.util.ArrayList;


public class FileViewer {


	private File mDirToView;
	private ArrayList<File> mFList = new ArrayList<File>();


	public void fileItemGet(File mDirToView) {

		mFList.clear();

		File emptyFile = new File("empty");

		long size;
		File[] listoffiles = mDirToView.listFiles();
		if (listoffiles == null) {
			mFList.add(emptyFile);
		} else {
			for (int i = 0; i < listoffiles.length; i++) {
				mFList.add(listoffiles[i]);
			}
		}
	}


	public ArrayList<File> getmFList() {
		return mFList;
	}
}


