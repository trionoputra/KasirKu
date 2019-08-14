package com.chipo.cashier;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.chipo.cashier.utils.Checker;
import com.chipo.cashier.utils.Constants;
import com.chipo.cashier.utils.Shared;
import com.chipo.cashier.widget.LoadingDialog;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;

public class ActivationActivity extends Activity implements OnClickListener {
	private EditText txtSN;
	private LoadingDialog loading;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Shared.initialize(getBaseContext());

		Checker checker = new Checker(this);
		if(checker.cek())
		{
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
			overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
			finish();
		}
		
		setContentView(R.layout.activity_activation);

		Button btnSubmit = (Button)findViewById(R.id.btnSubmit);
		btnSubmit.setOnClickListener(this);
		txtSN = (EditText)findViewById(R.id.txtSN);
		loading = new LoadingDialog(this);
		
		TextView t1 = (TextView)findViewById(R.id.textView1);
		t1.setTypeface(Shared.openSansLight);
		txtSN.setTypeface(Shared.OpenSansRegular);
		btnSubmit.setTypeface(Shared.OpenSansSemibold);
	}



	@Override
	public void onClick(View v) {

		// TODO Auto-generated method stub
		String sn = txtSN.getText().toString();
		if(sn.equals(""))
		{
			YoYo.with(Techniques.Shake).duration(700).playOn(findViewById(R.id.activationWrapper));
			Toast.makeText(ActivationActivity.this, getString(R.string.activation_enter_sn), Toast.LENGTH_SHORT).show();
			return;
		}
		
		activation(sn);
	}
	
	private void activation(String sn)
	{
		 this.dummyactivation(sn);
	}
	
	private void dummyactivation(String sn)
	{
		loading.show();
		GrabURL grab = new GrabURL(this);
		grab.execute();
	}
	
	public class GrabURL extends AsyncTask<String, String, String>
	{
		private Activity context;
		public GrabURL(Activity context)
		{
			this.context = context;
		}
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(context.TELEPHONY_SERVICE);
            String imei = "";
            String device_id = "";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                if (this.context.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    imei = telephonyManager.getDeviceId();
                    device_id = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
                }
            }
            else
            {
                imei = telephonyManager.getDeviceId();
                device_id = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
            }
			
			String result = "";
			String SN = "3333";
			if(SN.equals(txtSN.getText().toString()))
			{
				result = "ok";
				Shared.write(Constants.KEY_SETTING_MERCHANT_ID, "MCT001");
				Shared.write(Constants.KEY_SETTING_BRANCH_ID, "BR001");
				Shared.write(Constants.KEY_SETTING_CASHIER_ID, Shared.getCashierID());
				Shared.write(Constants.KEY_SETTING_CASHIER_SN, txtSN.getText().toString());
				Shared.write(Constants.KEY_SETTING_IME, imei);
				Shared.write(Constants.KEY_SETTING_DEVICE_ID, device_id);
			}
			
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return result;
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			if(result.equals("ok"))
			{
				YoYo.with(Techniques.FadeOutDown).duration(700).withListener(new AnimatorListener() {
					@Override
					public void onAnimationStart(Animator arg0) {
						// TODO Auto-generated method stub
					}
					@Override
					public void onAnimationRepeat(Animator arg0) {
						// TODO Auto-generated method stub
					}
					@Override
					public void onAnimationEnd(Animator arg0) {
						// TODO Auto-generated method stub
						Toast.makeText(ActivationActivity.this, getString(R.string.activation_succeed), Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(context, LoginActivity.class);
						startActivity(intent);
						overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
						finish();
					}
					
					@Override
					public void onAnimationCancel(Animator arg0) {
						// TODO Auto-generated method stub
					}
				}).playOn(findViewById(R.id.activationWrapper));
				
			}
			else
			{
				YoYo.with(Techniques.Shake).duration(700).playOn(findViewById(R.id.activationWrapper));
				Toast.makeText(ActivationActivity.this, getString(R.string.activation_failed), Toast.LENGTH_SHORT).show();
			}
			
			
			loading.dismiss();
		}
		
	}
}
