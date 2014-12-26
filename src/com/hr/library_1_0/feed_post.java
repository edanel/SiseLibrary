package com.hr.library_1_0;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.Connection.Method;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class feed_post extends Activity {
	
	private RadioGroup mRadioGroup;
	private RadioButton mFindButton;
	private RadioButton mSwapButton;
	private RadioButton mHelpButton;
	private RadioButton mShitButton;
	
	private EditText mdEditText;
	
	private TextView mPosTextView;
	
	private String mSearchType = "寻物";
	private String mPostData;
	
	private Dialog mDialog;
	
	
	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				mDialog.showDialog("正在发布……");
				break;
			case 2:
				mDialog.cancelDialog();
				break;
			}
		}
	};
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feed_post);
		init();
	}
	private void init(){
		
		mDialog = new Dialog(feed_post.this);
		
			mdEditText = (EditText) findViewById(R.id.feed_edt);
		// 初始化筛选组件
				mRadioGroup = (RadioGroup) findViewById(R.id.feed_rgroup);
				mFindButton = (RadioButton) findViewById(R.id.feed_rgroup_1);
				mSwapButton = (RadioButton) findViewById(R.id.feed_rgroup_2);
				mHelpButton = (RadioButton) findViewById(R.id.feed_rgroup_3);
				mShitButton = (RadioButton) findViewById(R.id.feed_rgroup_4);
				
				mRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						if (checkedId == R.id.feed_rgroup_1) {
							mSearchType = "【寻物】";
						} else if (checkedId == R.id.feed_rgroup_2) {
							mSearchType = "【交易】";
						} else if (checkedId == R.id.feed_rgroup_3) {
							mSearchType = "【求助】";
						} else if (checkedId == R.id.feed_rgroup_4) {
							mSearchType = "【爆料】";
						}
					}
				});
				mPosTextView =(TextView) findViewById(R.id.feed_postdata);
				mPosTextView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						try {
							mHandler.sendEmptyMessage(1);
							postData();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
				
	}
	
	private void postData() throws Exception{
		new Thread() {
			public void run() {
				try {
					mPostData = mdEditText.getText().toString();
					 Jsoup
							.connect("http://172.16.111.87/xiabancao/api/board/Create")
							.data("Board[uid]",mSearchType, "Board[content]", mPostData).method(Method.POST)
							.execute();
					 mHandler.sendEmptyMessage(2);
					 finish();
					//Document dcm = res.parse();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}
	
	
	
	
	
	public void back(View view) {
		
		finish();
	}
}
