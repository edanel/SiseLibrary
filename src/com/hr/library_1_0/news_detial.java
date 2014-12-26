package com.hr.library_1_0;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;

public class news_detial extends Activity {

	private String myTitle;
	private String myLinks;
	private WebView mWebView;
	private Document mDocument;

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				//mWebView.loadUrl(mDocument.select("div#BContent2").text());
				mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
				mWebView.loadDataWithBaseURL(null, mDocument.select("div#BContent2").toString(), "text/html", "utf-8", null);
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news_more);
		init();
	}

	private void init() {

		mWebView = (WebView) findViewById(R.id.webview);

		WebSettings settings = mWebView.getSettings(); 
		settings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN); 
		
		myTitle = getIntent().getStringExtra("title");
		myLinks = getIntent().getStringExtra("links");
		myLinks = "http://news.sise.com.cn/" + myLinks;
		getInfo();
	}

	private void getInfo() {
		new Thread() {
			public void run() {
				try {
					mDocument = Jsoup.connect(myLinks).timeout(50000).get();
					//System.out.println(mDocument.select("div#BContent2"));					
					mHandler.sendEmptyMessage(1);

				} catch (IOException e) {
					e.printStackTrace();
				}

			};// run结束
		}.start();// 结束线程
	}

	public void back(View view) {
		finish();
	}
}
