package com.example.alex.filemanager_git;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;


public class FileViewFragment extends Fragment {


	private static ArrayList<File> mActionFileList = new ArrayList<File>();
	private FileAdapter fileAdapter;
	private FileViewer fileViewer;
	private File currentDirNew;
	private String newFolderName;
	private File newFile;
	AsyncTaskCopy asyncTaskCopy;


	public static FileViewFragment newInstance(int page, String title) {
		FileViewFragment fragmentFirst = new FileViewFragment();

		return fragmentFirst;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_file, container, false);


		final String rootPath = "/storage";
		final File startDir = new File(String.valueOf(Environment.getExternalStorageDirectory()));

		asyncTaskCopy = new AsyncTaskCopy(this.getContext());

		final ListView lview = (ListView) view.findViewById(R.id.lview);
		final Button curentDirectory = (Button) view.findViewById(R.id.currentPath);
		final Button parentDirectory = (Button) view.findViewById(R.id.parentPath);
		final Button addDirectory = (Button) view.findViewById(R.id.addFolder);
		final Button copyBtn = (Button) view.findViewById(R.id.copyBtn);
		final Button moveBtn = (Button) view.findViewById(R.id.moveBtn);
		final TextView textAdvise = (TextView) view.findViewById(R.id.textAdvise);

		getmActionFileList().clear();


		//Реалізація копіювання і виділення елементів для копіювання
		//
		copyBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (getmActionFileList().isEmpty()) {
					copyBtn.setVisibility(View.GONE);

					textAdvise.setVisibility(View.VISIBLE);
					textAdvise.setText(R.string.text_advise);


					lview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
							File item = (File) fileAdapter.getItem(position);
							if (getmActionFileList().contains(item)) {
								getmActionFileList().remove(item);
								fileAdapter.notifyDataSetChanged();
							} else {
								getmActionFileList().add(item);
								fileAdapter.notifyDataSetChanged();
							}

						}
					});
				} else {
					getmActionFileList().add(currentDirNew);

					asyncTaskCopy.execute(getmActionFileList());
					copyBtn.setVisibility(View.VISIBLE);

					textAdvise.setVisibility(View.GONE);


				}
			}
		});
		// END // pеалізація копіювання і виділення елементів для копіювання


		//Баттон створення директорії
		//
		addDirectory.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setTitle(R.string.add_folder__dialog_title);


				final EditText input = new EditText(getActivity());

				input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
				builder.setView(input);


				builder.setPositiveButton(R.string.add_folder__dialog_ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						newFolderName = input.getText().toString();
						newFile = new File(currentDirNew + "/" + newFolderName);
						newFile.mkdirs();
						fileAdapter.notifyDataSetChanged();
						fileViewer.fileItemGet(currentDirNew);
					}
				});
				builder.setNegativeButton(R.string.add_folder__dialog_cancel, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});

				builder.show();
				fileAdapter.notifyDataSetChanged();
			}
		});
		// END // Баттон створення директорії

		fileViewer = new FileViewer();
		fileAdapter = new FileAdapter(fileViewer.getmFList(), getActivity());
		fileViewer.fileItemGet(startDir);
		if (currentDirNew == null) {
			currentDirNew = startDir;
		}
		lview.setAdapter(fileAdapter);
		fileAdapter.notifyDataSetChanged();
		parentDirectory.setText(startDir.getParentFile().getName());
		curentDirectory.setText(startDir.getName());

		// 	Кнопка навігації назад
		//
		parentDirectory.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				File parentDir = new File(currentDirNew.getParent());
				fileViewer.fileItemGet(parentDir);
				fileAdapter.notifyDataSetChanged();
				currentDirNew = parentDir;
				if (parentDir.getPath().equals(rootPath)) {
					curentDirectory.setText(currentDirNew.getName());
					parentDirectory.setVisibility(View.GONE);
				} else {
					curentDirectory.setText(parentDir.getName());
					parentDirectory.setText(parentDir.getParentFile().getName());
				}

			}
		});
		//END // 	Кнопка навігації назад

		//Реалізація перегляду файлової системи
		//
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


					}
				} else {
					Toast.makeText(getActivity().getApplicationContext(),
							R.string.toast_on_file,
							Toast.LENGTH_LONG).show();
				}


			}
		});
		//END // Реалізація перегляду файлової системи

		return view;
	}


	public static ArrayList<File> getmActionFileList() {
		return mActionFileList;
	}


}