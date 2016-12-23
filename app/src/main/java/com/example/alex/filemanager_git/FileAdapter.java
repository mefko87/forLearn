package com.example.alex.filemanager_git;


import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class FileAdapter extends BaseAdapter {
	private ArrayList<File> fileList;
	private Activity activity;
	final String LOG_TAG = "myLogs";
	ViewHolder holder;

	public FileAdapter(ArrayList<File> fileList, Activity activity) {
		this.fileList = fileList;
		this.activity = activity;
	}

	private class ViewHolder {
		TextView mName;
		TextView mSize;
		TextView mExtension;
		TextView mCreateDate;

	}

	@Override
	public int getCount() {
		return fileList.size();
	}

	@Override
	public Object getItem(int position) {
		return fileList.get(position);
	}

	@Override
	public long getItemId(int i) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup viewGroup) {


		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(activity.getApplicationContext());
			convertView = inflater.inflate(R.layout.adapter_view, null);
			holder = new ViewHolder();
			holder.mName = (TextView) convertView.findViewById(R.id.name);
			holder.mSize = (TextView) convertView.findViewById(R.id.size);
			holder.mExtension = (TextView) convertView.findViewById(R.id.ext);
			holder.mCreateDate = (TextView) convertView.findViewById(R.id.date);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		File item = fileList.get(position);
		convertView.setBackgroundColor(Color.WHITE);
		if (FileViewFragment.getmActionFileList() != null) {
			if (FileViewFragment.getmActionFileList().contains(item)) {
				convertView.setBackgroundColor(Color.LTGRAY);
			}
		}
		if (item.isDirectory()) {
			holder.mName.setText(item.getName());
			holder.mSize.setText("DIR");
			holder.mExtension.setText(" ");
			holder.mCreateDate.setText(setLastModifiedDate(item));

		} else if (item.getName().equals("empty")) {
			holder.mName.setText("Folder is emty");
			holder.mSize.setText(" ");
			holder.mExtension.setText(" ");
			holder.mCreateDate.setText(" ");
		} else {

			holder.mName.setText(setFileName(item));
			holder.mSize.setText(Integer.toString((int) item.length()));
			holder.mExtension.setText(getFileExt(item));
			holder.mCreateDate.setText(setLastModifiedDate(item));
		}
		return convertView;
	}

	public String getFileExt(File file) {
		String fileName = file.getName();
		return fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
	}


	public String setFileName(File file) {
		String name = file.getName();
		return name.split("\\.", 5)[0];

	}

	public String setLastModifiedDate(File file) {

		return new SimpleDateFormat("yyyy.MM.dd  HH:mm:ss ").format(new Date(file.lastModified())).toString();
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
		Collections.sort(fileList, new Comparator<File>() {
			@Override
			public int compare(File file, File file1) {
				return file.getName().compareTo(file1.getName());
			}

		});
		Collections.sort(fileList, new Comparator<File>() {
			@Override
			public int compare(File file, File file1) {
				if (file.isDirectory() == file1.isDirectory())
					return 0;
				else if (file.isDirectory() && !file1.isDirectory())
					return -1;
				else
					return 1;
			}
		});
	}
}
