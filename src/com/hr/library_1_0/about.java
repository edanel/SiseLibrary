package com.hr.library_1_0;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class about extends Activity {
	private RadioGroup mRadioGroup;
	private RadioButton mUseButton;
	private RadioButton mUsButton;

	private LinearLayout mLayout_1;
	private LinearLayout mLayout_2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		initView();
	}
	private void initView() {
		mRadioGroup = (RadioGroup) findViewById(R.id.about_rgroup);
		mUseButton = (RadioButton) findViewById(R.id.about_rbutton_1);
		mUsButton = (RadioButton) findViewById(R.id.about_rbutton_2);

		
		mLayout_1 = (LinearLayout) findViewById(R.id.about_use_layout);
		mLayout_2 = (LinearLayout) findViewById(R.id.about_us_layout);

		
		
		mRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == R.id.about_rbutton_1) {
					mLayout_1.setVisibility(mLayout_1.VISIBLE);
					mLayout_2.setVisibility(mLayout_2.GONE);

				}
				else if (checkedId == R.id.about_rbutton_2) {
					mLayout_1.setVisibility(mLayout_1.GONE);
					mLayout_2.setVisibility(mLayout_2.VISIBLE);

				}
			}
		});

	}

	public void back(View view) {
		finish();
	}
}
