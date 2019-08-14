package com.chipo.cashier.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.chipo.cashier.ActivationActivity;
import com.chipo.cashier.R;

public class Checker {
	private Activity context;

	public Checker(Activity context) {
		this.context = context;
	}

	public boolean cek() {
		return this.cek(false);
	}

	public boolean cek(boolean isRedirect) {
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);

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

		boolean hasActive = false;
		if(!Shared.read(Constants.KEY_SETTING_CASHIER_SN, "").equals(""))
		{
			if(Shared.read(Constants.KEY_SETTING_DEVICE_ID, "").equals(device_id) && Shared.read(Constants.KEY_SETTING_IME, "").equals(imei))
			{
				hasActive = true;
			}
		}

		if(isRedirect)
		{
			Intent intent = new Intent(context, ActivationActivity.class);
			context.startActivity(intent);
			context.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
			context.finish();
			Toast.makeText(context.getApplicationContext(), context.getString(R.string.activation_message), Toast.LENGTH_LONG).show();
		}
		
		return hasActive;
		
	}
}
