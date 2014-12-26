package com.hr.library_1_0;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class guide extends Activity {
	private RadioGroup mRadioGroup;
	private RadioButton mOpenButton;
	private RadioButton mBorrowButton;
	private RadioButton mPeiButton;
	private RadioButton mCallButton;

	private LinearLayout mLayout_1;
	private LinearLayout mLayout_2;
	private LinearLayout mLayout_3;
	private LinearLayout mLayout_4;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.guide);
		initView();
	}

	private void initView() {
		mRadioGroup = (RadioGroup) findViewById(R.id.guide_rgroup);
		mOpenButton = (RadioButton) findViewById(R.id.guide_rbutton_1);
		mBorrowButton = (RadioButton) findViewById(R.id.guide_rbutton_2);
		mPeiButton = (RadioButton) findViewById(R.id.guide_rbutton_3);
		mCallButton = (RadioButton) findViewById(R.id.guide_rbutton_4);
		
		mLayout_1 = (LinearLayout) findViewById(R.id.guide_open_layout);
		mLayout_2 = (LinearLayout) findViewById(R.id.guide_borrow_layout);
		mLayout_3 = (LinearLayout) findViewById(R.id.guide_pei_layout);
		mLayout_4 = (LinearLayout) findViewById(R.id.guide_call_layout);
		
		
		mRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == R.id.guide_rbutton_1) {
					mLayout_1.setVisibility(mLayout_1.VISIBLE);
					mLayout_2.setVisibility(mLayout_2.GONE);
					mLayout_3.setVisibility(mLayout_3.GONE);
					mLayout_4.setVisibility(mLayout_4.GONE);
				}
				else if (checkedId == R.id.guide_rbutton_2) {
					mLayout_1.setVisibility(mLayout_1.GONE);
					mLayout_2.setVisibility(mLayout_2.VISIBLE);
					mLayout_3.setVisibility(mLayout_3.GONE);
					mLayout_4.setVisibility(mLayout_4.GONE);
				}
				else if (checkedId == R.id.guide_rbutton_3) {
					mLayout_1.setVisibility(mLayout_1.GONE);
					mLayout_2.setVisibility(mLayout_2.GONE);
					mLayout_3.setVisibility(mLayout_3.VISIBLE);
					mLayout_4.setVisibility(mLayout_4.GONE);
				}
				else if (checkedId == R.id.guide_rbutton_4) {
					mLayout_1.setVisibility(mLayout_1.GONE);
					mLayout_2.setVisibility(mLayout_2.GONE);
					mLayout_3.setVisibility(mLayout_3.GONE);
					mLayout_4.setVisibility(mLayout_4.VISIBLE);
				}
			}
		});

	}

	public void back(View view) {
		guide.this.finish();
	}
}
