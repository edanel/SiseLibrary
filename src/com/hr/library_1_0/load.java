package com.hr.library_1_0;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;

public class load extends Activity {
	private ImageView mImageView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.load);
		
		mImageView = (ImageView) findViewById(R.id.welcome);
		
		AlphaAnimation anima = new AlphaAnimation(1.0f, 1.0f); 
		anima.setDuration(3000);// 设置动画显示时间  
		mImageView.startAnimation(anima);  
		anima.setAnimationListener(new AnimationImpl()); 
	}
	private class AnimationImpl implements AnimationListener {
		@Override
		public void onAnimationStart(Animation animation) {
			mImageView.setBackgroundResource(R.drawable.library_start);  
		}
		@Override
		public void onAnimationEnd(Animation animation) {
			skip();
		}
		@Override
		public void onAnimationRepeat(Animation animation) {
		}  
    }  
	
	   private void skip() {  
	        startActivity(new Intent(this, MainActivity.class));  
	        finish();  
	    }  
}
