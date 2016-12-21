package com.example.alex.filemanager_git;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class FileAdapter extends BaseAdapter {
	private ArrayList<FileList> fileList;
	private Activity activity;
	final String LOG_TAG = "myLogs";
	ViewHolder holder;

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
			convertView = inflater.inflate(R.layout.file_folder, null);
			holder = new ViewHolder();
			holder.mName = (TextView) convertView.findViewById(R.id.name);
			holder.mSize = (TextView) convertView.findViewById(R.id.size);
//			holder.mExtension =
//			holder.mCreateDate =
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		return convertView;
	}


}
