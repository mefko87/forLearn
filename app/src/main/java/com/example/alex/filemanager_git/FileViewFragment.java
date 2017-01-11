package com.example.alex.filemanager_git;

import android.app.ProgressDialog;
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
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;


public class FileViewFragment extends Fragment implements BecameVisibleFragmentInterface {


	private static ArrayList<File> mActionFileList = new ArrayList<File>();
	private FileAdapter fileAdapter;
	private FileViewer fileViewer;
	private File currentDirNew;
	private String newFolderName;
	private File newFile;
	private AsyncTaskCopy asyncTaskCopy;
	private AsyncTaskMove asyncTaskMove;
	private AsyncTaskDelete asyncTaskDelete;
	private ProgressDialog mProgressDialog;
	private Button copyBtn;
	private Button moveBtn;
	private Button deleteBtn;



	public static FileViewFragment newInstance(int page, String title) {
		FileViewFragment fragmentFirst = new FileViewFragment();

		return fragmentFirst;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public void onResume() {

		super.onResume();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_file, container, false);


		final String rootPath = "/storage";
		final File startDir = new File(String.valueOf(Environment.getExternalStorageDirectory()));

		asyncTaskCopy = new AsyncTaskCopy(this);
		asyncTaskMove = new AsyncTaskMove(this);
		asyncTaskDelete = new AsyncTaskDelete(this);

		final ListView lview = (ListView) view.findViewById(R.id.lview);
		final Button curentDirectory = (Button) view.findViewById(R.id.currentPath);
		final Button parentDirectory = (Button) view.findViewById(R.id.parentPath);
		final Button addDirectory = (Button) view.findViewById(R.id.addFolder);
		copyBtn = (Button) view.findViewById(R.id.copyBtn);
		moveBtn = (Button) view.findViewById(R.id.moveBtn);
		deleteBtn = (Button) view.findViewById(R.id.dltBtn);


		getmActionFileList().clear();


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


		//Реалізація копіювання
		//
		copyBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {


				if (getmActionFileList().isEmpty()) {

					Toast.makeText(getActivity().getApplicationContext(),
							R.string.toast_check_if_list_is_empty_copy,
							Toast.LENGTH_LONG).show();

				} else {
					AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
					builder.setTitle(R.string.copy_to_alert);

					builder.setMessage(currentDirNew.getName() + "  ?");

					builder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							getmActionFileList().add(currentDirNew);
							asyncTaskCopy.execute(getmActionFileList());

						}
					});
					builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					});
					builder.show();
				}
			}
		});
		// END // pеалізація копіювання

		//Реалізація переміщення
		//

		moveBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (getmActionFileList().isEmpty()) {

					Toast.makeText(getActivity().getApplicationContext(),
							R.string.toast_check_if_list_is_empty_move,
							Toast.LENGTH_LONG).show();

				} else {
					AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
					builder.setTitle(R.string.move_to_alert + currentDirNew.getName() + "?");
					builder.setMessage(currentDirNew.getName() + "  ?");

					builder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							getmActionFileList().add(currentDirNew);
							asyncTaskMove.execute(getmActionFileList());

						}
					});
					builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					});
					builder.show();
				}

			}
		});
		// END //Реалізація переміщення


		//Реалізація видалення
		//

		deleteBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				if (getmActionFileList().isEmpty()) {

					Toast.makeText(getActivity().getApplicationContext(),
							R.string.toast_check_if_list_is_empty_delete,
							Toast.LENGTH_LONG).show();

				} else {
					AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
					builder.setTitle(R.string.delete_alert);
					builder.setMessage(currentDirNew.toString() + "  ?");

					builder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							getmActionFileList().add(currentDirNew);
							asyncTaskDelete.execute(getmActionFileList());

						}
					});
					builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					});
					builder.show();
				}


			}
		});

		// END //Реалізація видалення

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


				builder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						newFolderName = input.getText().toString();
						newFile = new File(currentDirNew + "/" + newFolderName);
						newFile.mkdirs();
						fileAdapter.notifyDataSetChanged();
						fileViewer.fileItemGet(currentDirNew);
					}
				});
				builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
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

		//Реалізація вибору файлів для наступних дій з ними
		//
		lview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {

				File item = (File) fileAdapter.getItem(position);
				if (getmActionFileList().contains(item)) {
					getmActionFileList().remove(item);
					fileAdapter.notifyDataSetChanged();
				} else {
					getmActionFileList().add(item);
					fileAdapter.notifyDataSetChanged();
				}


				return true;
			}
		});
		//END // Реалізація вибору файлів для наступних дій з ними


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

	public void showProgress() {
		mProgressDialog = new ProgressDialog(getActivity(), ProgressDialog.STYLE_SPINNER);

		mProgressDialog.setIndeterminate(false);
		mProgressDialog.show();
	}

	public void dissmisProgress() {

		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
		} else {
			mProgressDialog = new ProgressDialog(getActivity(), ProgressDialog.STYLE_SPINNER);
			mProgressDialog.dismiss();
		}

	}

	public void refreshAdapter() {
		fileViewer.fileItemGet(currentDirNew);
		fileAdapter.notifyDataSetChanged();
	}

	@Override
	public void fragmentBecameVisible() {

		copyBtn.setVisibility(View.VISIBLE);
		moveBtn.setVisibility(View.VISIBLE);
		deleteBtn.setVisibility(View.VISIBLE);


		fileAdapter.notifyDataSetChanged();
		fileViewer.fileItemGet(currentDirNew);

	}
}