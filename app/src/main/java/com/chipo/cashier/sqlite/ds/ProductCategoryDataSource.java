package com.chipo.cashier.sqlite.ds;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.chipo.cashier.entity.ProductCategory;
import com.chipo.cashier.sqlite.DbSchema;

public class ProductCategoryDataSource {
	
	private SQLiteDatabase db;
	public ProductCategoryDataSource(SQLiteDatabase db)
	{
		this.db = db;
	}
	
	public long truncate()
	{
		return db.delete(DbSchema.TBL_PRODUCT_CATEGORY,null,null);
	}
	
	public ProductCategory get(String code) {
		 
		ProductCategory item = new ProductCategory();
		 
		String selectQuery = " SELECT  *  FROM " + DbSchema.TBL_PRODUCT_CATEGORY  + 
						       " Where " +DbSchema.COL_PRODUCT_CATEGORY_CODE + " = '"+code+"'";
		
		Cursor c = db.rawQuery(selectQuery, null);
	
		if (c.moveToFirst()) {
			do {
			
				item.setCategoryID(c.getString(c.getColumnIndex(DbSchema.COL_PRODUCT_CATEGORY_CODE)));
				item.setCategoryName(c.getString(c.getColumnIndex(DbSchema.COL_PRODUCT_CATEGORY_NAME)));
				
			} while (c.moveToNext());
		}
		return item;
	}
	

	public ArrayList<ProductCategory> getAll() {
		 
		ArrayList<ProductCategory> items = new ArrayList<ProductCategory>();
		String selectQuery = " SELECT  *  FROM " + DbSchema.TBL_PRODUCT_CATEGORY ;
		Cursor c = db.rawQuery(selectQuery, null);
	
		if (c.moveToFirst()) {
			do {
				ProductCategory item = new ProductCategory();
				item.setCategoryID(c.getString(c.getColumnIndex(DbSchema.COL_PRODUCT_CATEGORY_CODE)));
				item.setCategoryName(c.getString(c.getColumnIndex(DbSchema.COL_PRODUCT_CATEGORY_NAME)));
				items.add(item);
			} while (c.moveToNext());
		}
	
		return items;
	}
	
	public long insert(ProductCategory item)
	{
		ContentValues values = new ContentValues();
		values.put(DbSchema.COL_PRODUCT_CATEGORY_CODE, item.getCategoryID());
		values.put(DbSchema.COL_PRODUCT_CATEGORY_NAME, item.getCategoryName());
		
		return db.insert(DbSchema.TBL_PRODUCT_CATEGORY, null, values);
	}
	
	public long update(ProductCategory item,String lastCode)
	{
		ContentValues values = new ContentValues();
		values.put(DbSchema.COL_PRODUCT_CATEGORY_CODE, item.getCategoryID());
		values.put(DbSchema.COL_PRODUCT_CATEGORY_NAME, item.getCategoryName());
		
		return db.update(DbSchema.TBL_PRODUCT_CATEGORY, values, DbSchema.COL_PRODUCT_CATEGORY_CODE+"= '"+lastCode+"' ", null);
	}
	
	public int delete(String code)
	{
		return db.delete(DbSchema.TBL_PRODUCT_CATEGORY, DbSchema.COL_PRODUCT_CATEGORY_CODE + "= '" + code + "'", null);
	}
	
	public boolean cekCode(String code) {
		 
		boolean has = false;
		String selectQuery = " SELECT  * FROM " + DbSchema.TBL_PRODUCT_CATEGORY  + 
						      " Where lower(" +DbSchema.COL_PRODUCT_CATEGORY_CODE + ") = '"+code.toLowerCase()+"'";
		 
		Cursor c = db.rawQuery(selectQuery, null);
		if(c.getCount() > 0)
			has = true;
			
		return has;
	}
	
	public boolean cekName(String name) {
		 
		boolean has = false;
		String selectQuery = " SELECT  * FROM " + DbSchema.TBL_PRODUCT_CATEGORY  + 
						      " Where lower(" +DbSchema.COL_PRODUCT_CATEGORY_NAME + ") = '"+name.toLowerCase()+"'";
		 
		Cursor c = db.rawQuery(selectQuery, null);
		if(c.getCount() > 0)
			has = true;
			
		return has;
	}
	
	public boolean cekAvailable(String code) {
		 
		boolean has = false;
		String selectQuery = " SELECT  * FROM " + DbSchema.TBL_PRODUCT  + 
						      " Where lower(" +DbSchema.COL_PRODUCT_PRODUCT_CATEGORY_CODE + ") = '"+code.toLowerCase()+"'";
		 
		Cursor c = db.rawQuery(selectQuery, null);
		if(c.getCount() > 0)
			has = true;
			
		return has;
	}

}
