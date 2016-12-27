package com.example.alex.filemanager_git;

import android.content.res.Configuration;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
	private FileViewFragment leftFragment;
	private FileViewFragment rightFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		PagerAdapter pagerAdapter;


		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

			ViewPager vpPager = (ViewPager) findViewById(R.id.vpPager);
			pagerAdapter = new PagerAdapter(getSupportFragmentManager());
			vpPager.setAdapter(pagerAdapter);
		} else {
			leftFragment = new FileViewFragment();
			rightFragment = new FileViewFragment();
			getSupportFragmentManager().beginTransaction().replace(R.id.leftFragment, leftFragment).commit();
			getSupportFragmentManager().beginTransaction().replace(R.id.rightFragment, rightFragment).commit();
		}
	}
}
