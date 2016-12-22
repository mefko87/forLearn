package com.example.alex.filemanager_git;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class PagerAdapter extends FragmentPagerAdapter {

	private static int NUM_PAGES = 2;

	public PagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		switch (position) {
			case 0:
				return FileViewFragment.newInstance(0, "Page 1");
			case 1:
				return FileViewFragment.newInstance(1, "Page 2");


		}
		return null;
	}

	@Override
	public int getCount() {
		return NUM_PAGES;
	}
}
