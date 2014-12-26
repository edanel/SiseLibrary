package com.hr.library_1_0;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class person extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_show);
	}
	
	
	
	
	
	public void back(View view){
		person.this.finish();
	}

}
