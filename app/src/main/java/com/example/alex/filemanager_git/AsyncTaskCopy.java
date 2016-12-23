package com.example.alex.filemanager_git;


import android.content.Context;
import android.os.AsyncTask;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class AsyncTaskCopy extends AsyncTask<ArrayList<File>, Integer, Integer> {
	int progress = 0;

	private static final String LOG_TAG = "myLog";
	private volatile boolean running = true;

	private Context context;

	public AsyncTaskCopy(Context context) {
		super();
		this.context = context;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();

	}

	@Override
	protected Integer doInBackground(ArrayList<File>... file) {
		int i = file[0].size() - 1;
		File dst = file[0].get(i);


		for (int n = 0; n < i; n++) {
			while (running) {
				File src = file[0].get(n);
				try {
					if (src.isFile()) {
						FileUtils.copyFileToDirectory(src, dst);
					} else {
						FileUtils.copyDirectoryToDirectory(src, dst);
					}

				} catch (IOException e) {
					e.printStackTrace();
				}

			}
			return 1;
		}

		return 1;
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);


	}


	@Override
	protected void onPostExecute(Integer result) {
		super.onPostExecute(result);
		FileViewFragment.getmActionFileList().clear();
	}


	@Override
	protected void onCancelled() {
		super.onCancelled();
		running = false;
	}


	public File checkDestDirToSrc(File sourceLocation, File targetLocation) {

		File mDestNew = new File(targetLocation + "/" + sourceLocation.getName());
		if (!mDestNew.exists()) {
			mDestNew.mkdirs();
		}
		return mDestNew;
	}


}
