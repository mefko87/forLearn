package com.example.alex.filemanager_git;


import java.util.Date;

public class FileListModel {
	private String mName;
	private String mExtension;
	private int mSize;
	private Date mCreateDate;

	public FileListModel(String mName, String mExtension, int mSize, Date mCreateDate) {
		this.mName = mName;
		this.mExtension = mExtension;
		this.mSize = mSize;
		this.mCreateDate = mCreateDate;

	}

	public String getmName() {
		return mName;
	}

	public String getmExtension() {
		return mExtension;
	}

	public int getmSize() {
		return mSize;
	}

	public Date getmCreateDate() {
		return mCreateDate;
	}

	public void setmName(String mName) {
		this.mName = mName;
	}

	public void setmExtension(String mExtension) {
		this.mExtension = mExtension;
	}

	public void setmSize(int mSize) {
		this.mSize = mSize;
	}

	public void setmCreateDate(Date mCreateDate) {
		this.mCreateDate = mCreateDate;
	}
}
