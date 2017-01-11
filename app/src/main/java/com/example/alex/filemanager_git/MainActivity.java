package com.example.alex.filemanager_git;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
	private FileViewFragment leftFragment;
	private FileViewFragment rightFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		final PagerAdapter pagerAdapter;


		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

			final ViewPager vpPager = (ViewPager) findViewById(R.id.vpPager);
			pagerAdapter = new PagerAdapter(getSupportFragmentManager());
			vpPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
				@Override
				public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

				}

				@Override
				public void onPageSelected(int position) {
					BecameVisibleFragmentInterface fragment = (BecameVisibleFragmentInterface) pagerAdapter.instantiateItem(vpPager, position);
					if (fragment != null && FileViewFragment.getmActionFileList().isEmpty()) {

						fragment.fragmentBecameVisible();
					}
				}

				@Override
				public void onPageScrollStateChanged(int state) {

				}
			});

			vpPager.setAdapter(pagerAdapter);

		} else {
			leftFragment = new FileViewFragment();
			rightFragment = new FileViewFragment();
			getSupportFragmentManager().beginTransaction().replace(R.id.leftFragment, leftFragment).commit();
			getSupportFragmentManager().beginTransaction().replace(R.id.rightFragment, rightFragment).commit();

		}
	}


}

