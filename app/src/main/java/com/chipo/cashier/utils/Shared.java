package com.chipo.cashier.utils;

import java.io.File;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Locale;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Environment;
import android.view.Display;
import android.view.WindowManager;

import com.chipo.cashier.R;

public final class Shared
{
	private static ContextWrapper instance;
	private static SharedPreferences pref;
	public static Typeface OpenSansSemibold;
	public static Typeface OpenSansBold;
	public static Typeface OpenSansRegular;
	public static Typeface openSansRegularItalic;
	public static Typeface openSansLightItalic;
	public static Typeface openSansLight;
	public static String SERVER_URL = "http://trionoputra.com/kampus/lockedu/index.php/api/mobile/";
	
	public static SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static SimpleDateFormat dateformatID = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	public static SimpleDateFormat dateformatStruk = new SimpleDateFormat("dd.MM.yyyy HH:mm");
	public static DecimalFormat decimalformat = new DecimalFormat("#,###.#");
	public static DecimalFormat decimalformat2 = new DecimalFormat("###.#");
	public static void initialize(Context base)
	{
		DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.GERMAN);
		decimalformat.setDecimalFormatSymbols(otherSymbols);
		
		instance = new ContextWrapper(base);
		pref = instance.getSharedPreferences("com.chipo.cashier", Context.MODE_PRIVATE);
		OpenSansSemibold = Typeface.createFromAsset(instance.getAssets(),"fonts/OpenSans-Semibold.ttf");
		OpenSansBold = Typeface.createFromAsset(instance.getAssets(),"fonts/OpenSans-Bold.ttf");
		OpenSansRegular = Typeface.createFromAsset(instance.getAssets(),"fonts/OpenSans-Regular.ttf");
		openSansRegularItalic = Typeface.createFromAsset(instance.getAssets(),"fonts/OpenSans-Italic.ttf");
		openSansLightItalic = Typeface.createFromAsset(instance.getAssets(),"fonts/OpenSans-LightItalic.ttf");
		openSansLight = Typeface.createFromAsset(instance.getAssets(),"fonts/OpenSans-Light.ttf");
	}
	public static void write(String key, String value)
	{
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(key, value);
		editor.commit();
	}
	
	public static String read(String key)
	{
		return Shared.read(key, null);
	}
	
	public static String read(String key, String defValue)
	{
		return pref.getString(key, defValue);
	}
	
	public static void clear()
	{
		SharedPreferences.Editor editor = pref.edit();
		editor.clear();
		editor.commit();
	}
	
	public static void clear(String key)
	{
		SharedPreferences.Editor editor = pref.edit();
		editor.remove(key);
		editor.commit();
	}
	
	public static Context getContext()
	{
		return instance.getBaseContext();
	}
	
	public static String getUserID()
	{
		return getMD5("U" + System.currentTimeMillis());
	}
	
	public static String getOrderID()
	{
		return getMD5("O" + System.currentTimeMillis());
	}
	
	public static String getOrderDetailID(int i)
	{
		if(i != 0)
			return getMD5("DO" + System.currentTimeMillis());
		else
			return getMD5("DO" + System.currentTimeMillis()+i);
	}
	public static String getCashierID()
	{
		return getMD5("K" + System.currentTimeMillis());
	}
	
	public static String getCategoryID()
	{
		return getMD5("C" + System.currentTimeMillis());
	}
	
	public static String getProductID()
	{
		return getMD5("P" + System.currentTimeMillis());
	}
	
	public static String getMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            String hashtext = number.toString(16);
            // Now we need to zero pad it if you actually want the full 32 chars.
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
	
	public static boolean isMediaMounted()
    {
        String storageState = Environment.getExternalStorageState();
        return storageState.equals(Environment.MEDIA_MOUNTED);
    }
	 
	
	 
	public static String getAppDir()
	{
		File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator  + instance.getResources().getString(R.string.app_name));
		if(!f.exists())
			f.mkdir();
		
		return f.getAbsolutePath();
	}
	
	public static float scaleFactor(int w)
    {
       return 512f / w;
    }
	
	public static int getDisplayHeight()
	{
		
		 WindowManager wm = (WindowManager) instance.getSystemService(Context.WINDOW_SERVICE);
		 Display display = wm.getDefaultDisplay();
		 final int version = android.os.Build.VERSION.SDK_INT;
		 
		 if (version >= 13)
		 {
		     Point size = new Point();
		     display.getSize(size);
		    return size.y;
		 }
		 else
		 {
		     
		     return  display.getHeight();
		 }
	}
	
	public static int getDisplayWidth()
	{
		
		 WindowManager wm = (WindowManager) instance.getSystemService(Context.WINDOW_SERVICE);
		 Display display = wm.getDefaultDisplay();
		 final int version = android.os.Build.VERSION.SDK_INT;
		 
		 if (version >= 13)
		 {
		     Point size = new Point();
		     display.getSize(size);
		     return size.x;
		 }
		 else
		 {
		     
		     return  display.getWidth();
		 }
	}
	
	public static String padRight(String s, int n) {
	     return String.format("%1$-" + n + "s", s);  
	}

	public static String padLeft(String s, int n) {
	    return String.format("%1$" + n + "s", s);  
	}
	
	public static String printerLeftRight(String left,String right)
	{
		int paddLength = Constants.MAX_PRINTER_CHAR - (left.length());
		return left + padLeft(right, paddLength);
	}
	
	
	
}

