package com.example.alex.filemanager_git;


import android.content.Context;
import android.os.AsyncTask;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class AsyncTaskMove extends AsyncTask<ArrayList<File>, Integer, Integer> {
	int progress = 0;

	private static final String LOG_TAG = "myLog";
	private volatile boolean running = true;
	FileViewFragment container;

	private Context context;

	public AsyncTaskMove(FileViewFragment f) {
		super();
		this.container = f;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		container.showProgress();


	}

	@Override
	protected Integer doInBackground(ArrayList<File>... file) {
		int i = file[0].size() - 1;
		File dst = file[0].get(i);


		for (int n = 0; n < i; n++) {

			File src = file[0].get(n);
			try {
				if (src.isFile()) {
					FileUtils.moveFileToDirectory(src, dst, true);
				} else {
					FileUtils.moveDirectoryToDirectory(src, dst, true);
				}

			} catch (IOException e) {
				e.printStackTrace();
			}


			if (!running) {
				return 1;
			}
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
		if (container != null) {
			container.dissmisProgress();
			FileViewFragment.getmActionFileList().clear();
			container.refreshAdapter();
			this.container = null;
		}

	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
		running = false;
	}


}
