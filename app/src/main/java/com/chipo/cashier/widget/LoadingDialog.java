package com.chipo.cashier.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;

import com.chipo.cashier.R;
import com.nineoldandroids.animation.ObjectAnimator;

public class LoadingDialog extends Dialog{

	public LoadingDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		setContentView(R.layout.loading_dialog);
		
		setCancelable(false);
		
		 ObjectAnimator fadeOut = ObjectAnimator.ofFloat(findViewById(R.id.imageView1), "alpha", 0.5f);
		 fadeOut.setDuration(1000);
		 fadeOut.setRepeatCount(ObjectAnimator.INFINITE);
		 fadeOut.setRepeatMode(ObjectAnimator.REVERSE);
		 fadeOut.start();;
		
	}
	
}
