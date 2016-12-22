package com.example.alex.filemanager_git;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


import java.io.File;
import java.util.List;


public class FileViewFragment extends Fragment {


	private FileAdapter fileAdapter;
	private FileViewer fileViewer;
	private File currentDirNew;
	private String newFolderName;
	private File newFile;


	public static FileViewFragment newInstance(int page, String title) {
		FileViewFragment fragmentFirst = new FileViewFragment();
		Bundle args = new Bundle();
		args.putInt("someInt", page);
		args.putString("someTitle", title);
		fragmentFirst.setArguments(args);
		return fragmentFirst;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		final File startDir = new File(String.valueOf(Environment.getRootDirectory()));

		View view = inflater.inflate(R.layout.fragment_file, container, false);

		ListView lview = (ListView) view.findViewById(R.id.lview);
		final Button curentDirectory = (Button) view.findViewById(R.id.currentPath);
		final Button parentDirectory = (Button) view.findViewById(R.id.parentPath);
		Button addDirectory = (Button) view.findViewById(R.id.addFolder);

		addDirectory.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setTitle("Title");


				final EditText input = new EditText(getActivity());

				input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
				builder.setView(input);


				builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						newFolderName = input.getText().toString();
						newFile = new File(currentDirNew + "/" + newFolderName);
						newFile.mkdirs();
						fileAdapter.notifyDataSetChanged();
					}
				});
				builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});

				builder.show();
				currentDirNew.mkdir();
			}
		});


		fileViewer = new FileViewer();
		fileAdapter = new FileAdapter(fileViewer.getmFList(), getActivity());
		fileViewer.fileItemGet(startDir);
		if (currentDirNew == null) {
			currentDirNew = startDir;
		}
		lview.setAdapter(fileAdapter);
		fileAdapter.notifyDataSetChanged();
		parentDirectory.setVisibility(View.GONE);
		curentDirectory.setText(startDir.getName());

		lview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long l) {


				File item = (File) fileAdapter.getItem(position);


				if (item.isDirectory()) {
					currentDirNew = new File(currentDirNew + "/" + item.getName());

					fileViewer.fileItemGet(currentDirNew);

					fileAdapter.notifyDataSetChanged();
					if (currentDirNew.equals(startDir)) {
						curentDirectory.setText(startDir.getName());
						parentDirectory.setVisibility(View.GONE);
					} else {
						parentDirectory.setVisibility(View.VISIBLE);
						parentDirectory.setText(currentDirNew.getParentFile().getName());
						curentDirectory.setText(currentDirNew.getName());

						// Кнопка навігації

						parentDirectory.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View view) {
								File parentDir = new File(currentDirNew.getParent());
								fileViewer.fileItemGet(parentDir);
								fileAdapter.notifyDataSetChanged();
								currentDirNew = parentDir;
								if (parentDir.equals(startDir)) {
									parentDirectory.setText(startDir.getName());
									curentDirectory.setVisibility(View.GONE);
								} else {
									curentDirectory.setText(parentDir.getName());
									parentDirectory.setText(parentDir.getParentFile().getName());
								}

							}
						});

						//


					}
				} else {
					Toast.makeText(getActivity().getApplicationContext(),
							"It`s a file",
							Toast.LENGTH_LONG).show();
				}


			}
		});
		return view;
	}

}