package com.yc.cepelin;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;

public class SplashScreenActivity extends Activity {
	private static final int COMING_IN_DURATION = 1200;
	private static final int DROP_ANIMATION_DURATION = 500;
	private static final int GETTING_OUT_DURATION = 400;
	TranslateAnimation animation1;
	TranslateAnimation animation2;
	TranslateAnimation animation3;
	TranslateAnimation animation4;
	AnimationSet set;
	
	private List<Animation> anims = new ArrayList<Animation>();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		ImageView cepelin = (ImageView) findViewById(R.id.cepelin);
		cepelin.setVisibility(View.INVISIBLE);
		this.setupAnimation();

		findViewById(R.id.splashScreen).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				clearAnimation(animation1);
				SplashScreenActivity.this.finish();
				startApp();
			}
		});
//		this.setupButton();
	}

	private void setupAnimation() {
		CepelinAnimationManager listener = new CepelinAnimationManager();
		
		animation1 = new TranslateAnimation(TranslateAnimation.RELATIVE_TO_PARENT, -0.2f,
	               TranslateAnimation.RELATIVE_TO_PARENT, 0.4f, TranslateAnimation.RELATIVE_TO_PARENT, 0.5f,
	               TranslateAnimation.RELATIVE_TO_PARENT, 0.5f);
		animation1.setDuration(COMING_IN_DURATION); 
		animation1.setAnimationListener(listener);
//		animation1.setInterpolator(new DecelerateInterpolator(1f));
		
		animation2 = new TranslateAnimation(TranslateAnimation.RELATIVE_TO_PARENT, 0.4f,
	               TranslateAnimation.RELATIVE_TO_PARENT, 0.5f, TranslateAnimation.RELATIVE_TO_PARENT, 0.5f,
	               TranslateAnimation.RELATIVE_TO_PARENT, 0.6f);
		animation2.setDuration(DROP_ANIMATION_DURATION); 
		animation2.setAnimationListener(listener);
		
		animation3 = new TranslateAnimation(TranslateAnimation.RELATIVE_TO_PARENT, 0.5f,
	               TranslateAnimation.RELATIVE_TO_PARENT, 0.6f, TranslateAnimation.RELATIVE_TO_PARENT, 0.6f,
	               TranslateAnimation.RELATIVE_TO_PARENT, 0.5f);
		animation3.setDuration(DROP_ANIMATION_DURATION); 
		animation3.setAnimationListener(listener);
		animation3.setInterpolator(new AccelerateInterpolator(1f));
		
		animation4 = new TranslateAnimation(TranslateAnimation.RELATIVE_TO_PARENT, 0.6f,
	               TranslateAnimation.RELATIVE_TO_PARENT, 1.2f, TranslateAnimation.RELATIVE_TO_PARENT, 0.5f,
	               TranslateAnimation.RELATIVE_TO_PARENT, 0.5f);
		animation4.setDuration(GETTING_OUT_DURATION); 
		animation4.setAnimationListener(listener);
//		animation4.setInterpolator(new AccelerateInterpolator(1f));
		
		anims.add(animation1);
		anims.add(animation2);
		anims.add(animation3);
		anims.add(animation4);
		ImageView cepelin = (ImageView) findViewById(R.id.cepelin);
		cepelin.setVisibility(View.VISIBLE);
		animateView(animation1);
		
	}
//
//	private void setupButton() {
//		Button b = (Button) this.findViewById(R.id.btn_animate);
//		b.setOnClickListener(new Button.OnClickListener() {
//			public void onClick(View v) {
//				animateView(animation1);
//			}
//		});
//	}

	private void startApp() {
		Intent intent = new Intent(this, MainTabActivity.class);
		this.startActivity(intent);
	}

	private void animateView(Animation animation) {
		ImageView cepelin = (ImageView) findViewById(R.id.cepelin);
		cepelin.startAnimation(animation);
	}

	private void clearAnimation(Animation animation) {
		ImageView cepelin = (ImageView) findViewById(R.id.cepelin);
		cepelin.clearAnimation();
	}	
	private class CepelinAnimationManager implements AnimationListener {
		private int runningAnim = 0;

		
		@Override
		public void onAnimationEnd(Animation animation) {
			if (runningAnim < anims.size()) {
				animateView(anims.get(runningAnim));
			} else {
				ImageView cepelin = (ImageView) findViewById(R.id.cepelin);
				cepelin.setVisibility(View.INVISIBLE);
				runningAnim = 0;
				startApp();
				SplashScreenActivity.this.finish();
			}
		}

		@Override
		public void onAnimationRepeat(Animation animation) {

		}

		@Override
		public void onAnimationStart(Animation animation) {
			runningAnim++;
		}
		
	}

}
