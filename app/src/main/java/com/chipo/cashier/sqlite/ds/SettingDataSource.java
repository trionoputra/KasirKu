package com.chipo.cashier.sqlite.ds;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.chipo.cashier.entity.Setting;
import com.chipo.cashier.sqlite.DbSchema;

public class SettingDataSource {
	
	private SQLiteDatabase db;
	public SettingDataSource(SQLiteDatabase db)
	{
		this.db = db;
	}
	
	public long truncate()
	{
		return db.delete(DbSchema.TBL_SETTING,null,null);
	}
	
	public Setting get(String code) {
		 
		Setting item = new Setting();
		 
		String selectQuery = " SELECT  *  FROM " + DbSchema.TBL_SETTING  + 
						       " Where " +DbSchema.COL_SETTING_CODE + " = '"+code+"'";
		
		Cursor c = db.rawQuery(selectQuery, null);
	
		if (c.moveToFirst()) {
			do {
			
				item.setCode(c.getString(c.getColumnIndex(DbSchema.COL_SETTING_CODE)));
				item.setValue(c.getString(c.getColumnIndex(DbSchema.COL_SETTING_VALUE)));
				
			} while (c.moveToNext());
		}
		return item;
	}
	

	public ArrayList<Setting> getAll() {
		 
		ArrayList<Setting> items = new ArrayList<Setting>();
		String selectQuery = " SELECT  *  FROM " + DbSchema.TBL_SETTING ;
		Cursor c = db.rawQuery(selectQuery, null);
	
		if (c.moveToFirst()) {
			do {
				Setting item = new Setting();
				item.setCode(c.getString(c.getColumnIndex(DbSchema.COL_SETTING_CODE)));
				item.setValue(c.getString(c.getColumnIndex(DbSchema.COL_SETTING_VALUE)));
				items.add(item);
			} while (c.moveToNext());
		}
	
		return items;
	}
	
	public long insert(Setting item)
	{
		ContentValues values = new ContentValues();
		values.put(DbSchema.COL_SETTING_CODE, item.getCode());
		values.put(DbSchema.COL_SETTING_VALUE, item.getValue());
		
		return db.insert(DbSchema.TBL_SETTING, null, values);
	}
	
	public long update(Setting item,String lastCode)
	{
		ContentValues values = new ContentValues();
		values.put(DbSchema.COL_SETTING_CODE, item.getCode());
		values.put(DbSchema.COL_SETTING_VALUE, item.getValue());
		
		return db.update(DbSchema.TBL_SETTING, values, DbSchema.COL_SETTING_CODE+"= '"+lastCode+"' ", null);
	}
	
	public int delete(String code)
	{
		return db.delete(DbSchema.TBL_SETTING, DbSchema.COL_SETTING_CODE + "= '" + code + "'", null);
	}
	
	public boolean cekCode(String code) {
		 
		boolean has = false;
		String selectQuery = " SELECT  * FROM " + DbSchema.TBL_SETTING  + 
						      " Where lower(" +DbSchema.COL_SETTING_CODE + ") = '"+code.toLowerCase()+"'";
		 
		Cursor c = db.rawQuery(selectQuery, null);
		if(c.getCount() > 0)
			has = true;
			
		return has;
	}

}
