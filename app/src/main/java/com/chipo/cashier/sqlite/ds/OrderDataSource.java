package com.chipo.cashier.sqlite.ds;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.chipo.cashier.entity.Order;
import com.chipo.cashier.entity.OrderDetails;
import com.chipo.cashier.sqlite.DbSchema;
import com.chipo.cashier.utils.Shared;

public class OrderDataSource {
	private SQLiteDatabase db;
	public OrderDataSource(SQLiteDatabase db)
	{
		this.db = db;
	}
	
	public long truncate()
	{
		return db.delete(DbSchema.TBL_ORDER,null,null);
	}
	
	public Order get(String code) {
		 
		Order item = new Order();
		 
		String selectQuery = 	" SELECT  o.*,u."+DbSchema.COL_USER_NAME+"  FROM " + DbSchema.TBL_ORDER   + " o " +
								" LEFT JOIN " +  DbSchema.TBL_USER +  " u ON u." +  DbSchema.COL_USER_CODE + " = o." + DbSchema.COL_ORDER_USER_ID +
								" Where " +DbSchema.COL_ORDER_CODE + " = '"+code+"'";
		
		Cursor c = db.rawQuery(selectQuery, null);
	
		if (c.moveToFirst()) {
			do {
			
				item.setOrderID(c.getString(c.getColumnIndex(DbSchema.COL_ORDER_CODE)));
				item.setDescription(c.getString(c.getColumnIndex(DbSchema.COL_ORDER_DESCRIPTION)));
				item.setAmount(c.getDouble(c.getColumnIndex(DbSchema.COL_ORDER_AMOUNT)));
				item.setDiscount(c.getDouble(c.getColumnIndex(DbSchema.COL_ORDER_DESCRIPTION)));
				item.setBranchID(c.getString(c.getColumnIndex(DbSchema.COL_ORDER_BRANCH_ID)));
				item.setUserID(c.getString(c.getColumnIndex(DbSchema.COL_ORDER_USER_ID)));
				item.setUserName(c.getString(c.getColumnIndex(DbSchema.COL_USER_NAME)));
				
				try {  
				    item.setCreatedOn( Shared.dateformat.parse(c.getString(c.getColumnIndex(DbSchema.COL_ORDER_ORDERED_ON))));
				    item.setUpdatedOn( Shared.dateformat.parse(c.getString(c.getColumnIndex(DbSchema.COL_ORDER_UPDATED_ON))));
				    item.setSycnOn( Shared.dateformat.parse(c.getString(c.getColumnIndex(DbSchema.COL_ORDER_SYCN_ON))));
				} catch (Exception e) {  
				}
				
				
				String selectQueryDetail =  " SELECT  o.*,p."+DbSchema.COL_PRODUCT_NAME+",c."+DbSchema.COL_PRODUCT_PRODUCT_CATEGORY_NAME+"  FROM " + DbSchema.TBL_PRODUCT_ORDER_DETAIL  + " o " +
											" LEFT JOIN " +  DbSchema.TBL_PRODUCT +  " p ON p." +  DbSchema.COL_PRODUCT_CODE + " = o." + DbSchema.COL_PRODUCT_ORDER_DETAIL_PRODUCT_CODE +
											" LEFT JOIN " +  DbSchema.TBL_PRODUCT_CATEGORY +  " c ON c." +  DbSchema.COL_PRODUCT_CATEGORY_CODE + " = p." + DbSchema.COL_PRODUCT_CATEGORY_CODE +
											" WHERE " +DbSchema.COL_PRODUCT_ORDER_DETAIL_ORDER_CODE + " = '"+code+"'";
				Cursor cDetail = db.rawQuery(selectQueryDetail, null);
				
				ArrayList<OrderDetails> details = new ArrayList<OrderDetails>();
				if (cDetail.moveToFirst()) {
					do {
						
						OrderDetails  order = new OrderDetails();
						order.setDetailID(c.getString(c.getColumnIndex(DbSchema.COL_PRODUCT_ORDER_DETAIL_CODE)));
						order.setName(c.getString(c.getColumnIndex(DbSchema.COL_PRODUCT_NAME)));
						order.setOrderID(c.getString(c.getColumnIndex(DbSchema.COL_PRODUCT_ORDER_DETAIL_ORDER_CODE)));
						order.setProductID(c.getString(c.getColumnIndex(DbSchema.COL_PRODUCT_ORDER_DETAIL_PRODUCT_CODE)));
						order.setCategoryName(c.getString(c.getColumnIndex(DbSchema.COL_PRODUCT_PRODUCT_CATEGORY_NAME)));
						order.setQty(c.getInt(c.getColumnIndex(DbSchema.COL_PRODUCT_ORDER_DETAIL_QTY)));
						order.setDiscount(c.getDouble(c.getColumnIndex(DbSchema.COL_PRODUCT_ORDER_DETAIL_DISCOUNT)));
						order.setPrice(c.getDouble(c.getColumnIndex(DbSchema.COL_PRODUCT_ORDER_DETAIL_PRICE)));
						
						details.add(order);
					} while (cDetail.moveToNext());
				}
				
				 item.setOrderDetails(details);
			
			} while (c.moveToNext());
		}
		return item;
	}
	
	
	public ArrayList<Order> getAll() {
		return getAll(null,null,null,null);
	}
	
	public ArrayList<Order> getAll(ArrayList<HashMap<String, String>> filter,String orderby,String limit,String offset) {
		 
		ArrayList<Order> items = new ArrayList<Order>();
		
		String selectQuery = " SELECT  *  FROM " + DbSchema.TBL_ORDER;
	
		Cursor c = db.rawQuery(selectQuery, null);
		if (c.moveToFirst()) {
			do {
				Order item = new Order();
				item.setOrderID(c.getString(c.getColumnIndex(DbSchema.COL_ORDER_CODE)));
				item.setDescription(c.getString(c.getColumnIndex(DbSchema.COL_ORDER_DESCRIPTION)));
				item.setAmount(c.getDouble(c.getColumnIndex(DbSchema.COL_ORDER_AMOUNT)));
				item.setDiscount(c.getDouble(c.getColumnIndex(DbSchema.COL_ORDER_DESCRIPTION)));
				item.setBranchID(c.getString(c.getColumnIndex(DbSchema.COL_ORDER_BRANCH_ID)));
				item.setUserID(c.getString(c.getColumnIndex(DbSchema.COL_ORDER_USER_ID)));
				item.setUserName(c.getString(c.getColumnIndex(DbSchema.COL_USER_NAME)));
				
				try {  
				    item.setCreatedOn( Shared.dateformat.parse(c.getString(c.getColumnIndex(DbSchema.COL_ORDER_ORDERED_ON))));
				    item.setUpdatedOn( Shared.dateformat.parse(c.getString(c.getColumnIndex(DbSchema.COL_ORDER_UPDATED_ON))));
				    item.setSycnOn( Shared.dateformat.parse(c.getString(c.getColumnIndex(DbSchema.COL_ORDER_SYCN_ON))));
				} catch (Exception e) {  
				}
				
				
				String selectQueryDetail =  " SELECT  o.*,p."+DbSchema.COL_PRODUCT_NAME+",c."+DbSchema.COL_PRODUCT_PRODUCT_CATEGORY_NAME+"  FROM " + DbSchema.TBL_PRODUCT_ORDER_DETAIL  + " o " +
											" LEFT JOIN " +  DbSchema.TBL_PRODUCT +  " p ON p." +  DbSchema.COL_PRODUCT_CODE + " = o." + DbSchema.COL_PRODUCT_ORDER_DETAIL_PRODUCT_CODE +
											" LEFT JOIN " +  DbSchema.TBL_PRODUCT_CATEGORY +  " c ON c." +  DbSchema.COL_PRODUCT_CATEGORY_CODE + " = p." + DbSchema.COL_PRODUCT_CATEGORY_CODE +
											" WHERE " +DbSchema.COL_PRODUCT_ORDER_DETAIL_ORDER_CODE + " = '"+item.getOrderID()+"'";
				Cursor cDetail = db.rawQuery(selectQueryDetail, null);
				
				ArrayList<OrderDetails> details = new ArrayList<OrderDetails>();
				if (cDetail.moveToFirst()) {
					do {
						
						OrderDetails  order = new OrderDetails();
						order.setDetailID(c.getString(c.getColumnIndex(DbSchema.COL_PRODUCT_ORDER_DETAIL_CODE)));
						order.setName(c.getString(c.getColumnIndex(DbSchema.COL_PRODUCT_NAME)));
						order.setOrderID(c.getString(c.getColumnIndex(DbSchema.COL_PRODUCT_ORDER_DETAIL_ORDER_CODE)));
						order.setProductID(c.getString(c.getColumnIndex(DbSchema.COL_PRODUCT_ORDER_DETAIL_PRODUCT_CODE)));
						order.setCategoryName(c.getString(c.getColumnIndex(DbSchema.COL_PRODUCT_PRODUCT_CATEGORY_NAME)));
						order.setQty(c.getInt(c.getColumnIndex(DbSchema.COL_PRODUCT_ORDER_DETAIL_QTY)));
						order.setDiscount(c.getDouble(c.getColumnIndex(DbSchema.COL_PRODUCT_ORDER_DETAIL_DISCOUNT)));
						order.setPrice(c.getDouble(c.getColumnIndex(DbSchema.COL_PRODUCT_ORDER_DETAIL_PRICE)));
						
						details.add(order);
					} while (cDetail.moveToNext());
				}
				
				 item.setOrderDetails(details);
				
				items.add(item);
			} while (c.moveToNext());
		}
	
		return items;
	}
	
	public long insert(Order item)
	{
		ContentValues values = new ContentValues();
		values.put(DbSchema.COL_ORDER_CODE, item.getOrderID());
		values.put(DbSchema.COL_ORDER_DESCRIPTION, item.getDescription());
		values.put(DbSchema.COL_ORDER_TAX, item.getTax());
		values.put(DbSchema.COL_ORDER_AMOUNT, item.getAmount());
		values.put(DbSchema.COL_ORDER_DISCOUNT,item.getDiscount());
		values.put(DbSchema.COL_ORDER_BRANCH_ID, item.getBranchID());
		values.put(DbSchema.COL_ORDER_USER_ID, item.getUserID());
		values.put(DbSchema.COL_ORDER_ORDERED_ON,  Shared.dateformat.format(item.getCreatedOn()));
		values.put(DbSchema.COL_ORDER_UPDATED_ON,  Shared.dateformat.format(item.getUpdatedOn()));
	//	values.put(DbSchema.COL_ORDER_SYCN_ON, Shared.dateformat.format(item.getSycnOn()));
		db.insert(DbSchema.TBL_ORDER, null, values);
		
		for (OrderDetails detail : item.getOrderDetails()) {
			ContentValues valuesDetails = new ContentValues();
			valuesDetails.put(DbSchema.COL_PRODUCT_ORDER_DETAIL_CODE, detail.getDetailID());
			valuesDetails.put(DbSchema.COL_PRODUCT_ORDER_DETAIL_ORDER_CODE, detail.getOrderID());
			valuesDetails.put(DbSchema.COL_PRODUCT_ORDER_DETAIL_PRODUCT_CODE, detail.getProductID());
			valuesDetails.put(DbSchema.COL_PRODUCT_ORDER_DETAIL_PRICE, detail.getPrice());
			valuesDetails.put(DbSchema.COL_PRODUCT_ORDER_DETAIL_QTY,detail.getQty());
			valuesDetails.put(DbSchema.COL_PRODUCT_ORDER_DETAIL_DISCOUNT, detail.getDiscount());
			db.insert(DbSchema.TBL_PRODUCT_ORDER_DETAIL, null, valuesDetails);
		}
		
		return 1;
	}
	
	
	public int delete(String code)
	{
		db.delete(DbSchema.TBL_PRODUCT_ORDER_DETAIL, DbSchema.COL_PRODUCT_ORDER_DETAIL_ORDER_CODE + "= '" + code + "'", null);
		db.delete(DbSchema.TBL_ORDER, DbSchema.COL_ORDER_CODE + "= '" + code + "'", null);
		return 1;
	}
	
	public boolean cekCode(String code) {
		 
		boolean has = false;
		String selectQuery = " SELECT  * FROM " + DbSchema.TBL_ORDER  + 
						      " Where lower(" +DbSchema.COL_ORDER_CODE + ") = '"+code.toLowerCase()+"'";
		 
		Cursor c = db.rawQuery(selectQuery, null);
		if(c.getCount() > 0)
			has = true;
			
		return has;
	}

}
