package com.hr.library_1_0;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

public class MainActivity extends Activity {
	private LinearLayout mSearch ;
	private LinearLayout mAbout;
	private LinearLayout mGuide;
	private LinearLayout mPerson;
	private LinearLayout mMail;
	private LinearLayout mNews;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mSearch = (LinearLayout) findViewById(R.id.category_search);
		mPerson = (LinearLayout) findViewById(R.id.category_person);
		
		mMail = (LinearLayout) findViewById(R.id.category_mails);
		mNews = (LinearLayout) findViewById(R.id.category_newss);
		
		mGuide = (LinearLayout) findViewById(R.id.category_guide);
		mAbout = (LinearLayout) findViewById(R.id.category_about);
		
		//mNews.setVisibility(mNews.INVISIBLE);
		
		mSearch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, search_start.class);
				startActivity(intent);
			}
		});
		
		mAbout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, about.class);
				startActivity(intent);
			}
		});
		
		mGuide.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, guide.class);
				startActivity(intent);
			}
		});
		
		mPerson.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, login.class);
				startActivity(intent);
			}
		});
		
		mMail.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, chatting.class);
				startActivity(intent);
			}
		});
		
		mNews.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, news.class);
				startActivity(intent);
			}
		});
		
	}

}
