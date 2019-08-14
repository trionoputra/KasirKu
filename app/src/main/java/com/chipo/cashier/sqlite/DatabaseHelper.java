package com.chipo.cashier.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper{
	public DatabaseHelper(Context context) {
		super(context, DbSchema.DB_NAME, null, DbSchema.DB_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(DbSchema.CREATE_TBL_PRODUCT_CATEGORY);
		db.execSQL(DbSchema.CREATE_TBL_PRODUCT);
		db.execSQL(DbSchema.CREATE_TBL_SETTING);
		db.execSQL(DbSchema.CREATE_TBL_USER);
		db.execSQL(DbSchema.CREATE_TBL_ORDER);
		db.execSQL(DbSchema.CREATE_TBL_PRODUCT_ORDER_DETAIL);
		db.execSQL(DbSchema.INSERT_TBL_USER);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL(DbSchema.DROP_TBL_PRODUCT_CATEGORY);
		db.execSQL(DbSchema.DROP_TBL_PRODUCT);
		db.execSQL(DbSchema.DROP_TBL_SETTING);
		db.execSQL(DbSchema.DROP_TBL_USER);
		db.execSQL(DbSchema.DROP_TBL_ORDER);
		db.execSQL(DbSchema.DROP_TBL_PRODUCT_ORDER_DETAIL);
	}
}
