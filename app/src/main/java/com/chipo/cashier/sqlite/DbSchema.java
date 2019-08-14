package com.chipo.cashier.sqlite;

import com.chipo.cashier.utils.Constants;
import com.chipo.cashier.utils.Shared;

public interface DbSchema {
	
	String DB_NAME = "com_chipo_cashier.db";
	int DB_VERSION = 1;
	
	String TBL_SETTING = "setting";	
	String COL_SETTING_CODE = "code";
	String COL_SETTING_VALUE = "value";
	
	String CREATE_TBL_SETTING = "CREATE TABLE "
								+ TBL_SETTING
								+ "(" 
									+ COL_SETTING_CODE  + " TEXT PRIMARY KEY," 
									+ COL_SETTING_VALUE + " TEXT" 
								+ ");";

	
	String TBL_PRODUCT_CATEGORY = "product_category";
	String COL_PRODUCT_CATEGORY_CODE = "category_id";
	String COL_PRODUCT_CATEGORY_NAME = "name";
	
	String CREATE_TBL_PRODUCT_CATEGORY = "CREATE TABLE "
			+ TBL_PRODUCT_CATEGORY
			+ "(" 
				+ COL_PRODUCT_CATEGORY_CODE  + " TEXT PRIMARY KEY," 
				+ COL_PRODUCT_CATEGORY_NAME + " TEXT" 
			+ ");";
	
	
	String TBL_PRODUCT = "product";
	String COL_PRODUCT_CODE = "product_id";
	String COL_PRODUCT_PRODUCT_CATEGORY_CODE = "category_id";
	String COL_PRODUCT_PRODUCT_CATEGORY_NAME = "category";
	String COL_PRODUCT_NAME = "name";
	String COL_PRODUCT_DESCRIPTION = "description";
	String COL_PRODUCT_PRICE = "price";
	String COL_PRODUCT_DISCOUNT = "discount";
	String COL_PRODUCT_CREATED_ON = "created_on";
	String COL_PRODUCT_UPDATED_ON = "updated_on";
	String COL_PRODUCT_SYCN_ON = "sycn_on";
	String COL_PRODUCT_CREATED_BY = "created_by";
	String COL_PRODUCT_UPDATED_BY = "updated_by";
	String COL_PRODUCT_MERCHANT_ID = "merchant_id";
	String COL_PRODUCT_STATUS = "status";
	String COL_PRODUCT_REF_ID = "ref_id";
	String COL_PRODUCT_IMAGE = "image";
	
	String CREATE_TBL_PRODUCT = "CREATE TABLE "
			+ TBL_PRODUCT
			+ "(" 
				+ COL_PRODUCT_CODE  + " TEXT PRIMARY KEY,"
				+ COL_PRODUCT_PRODUCT_CATEGORY_CODE + " TEXT," 
				+ COL_PRODUCT_NAME + " TEXT," 
				+ COL_PRODUCT_DESCRIPTION + " TEXT," 
				+ COL_PRODUCT_PRICE + " DOUBLE," 
				+ COL_PRODUCT_DISCOUNT + " DOUBLE," 
				+ COL_PRODUCT_CREATED_ON + " DATETIME," 
				+ COL_PRODUCT_UPDATED_ON + " DATETIME," 
				+ COL_PRODUCT_SYCN_ON + " DATETIME," 
				+ COL_PRODUCT_CREATED_BY + " TEXT," 
				+ COL_PRODUCT_UPDATED_BY + " TEXT," 
				+ COL_PRODUCT_MERCHANT_ID + " TEXT," 
				+ COL_PRODUCT_STATUS + " TEXT," 
				+ COL_PRODUCT_REF_ID + " TEXT," 
				+ COL_PRODUCT_IMAGE + " TEXT" 
			+ ");";
	
	
	String TBL_USER = "user";
	String COL_USER_CODE = "user_id";
	String COL_USER_NAME = "username";
	String COL_USER_PASSWORD = "password";
	String COL_USER_LEVEL = "level";
	String COL_USER_LAST_LOGIN = "last_login";
	String COL_USER_CASHIER_ID = "cashier_id";
	
	String CREATE_TBL_USER = "CREATE TABLE "
			+ TBL_USER
			+ "(" 
				+ COL_USER_CODE  + " TEXT PRIMARY KEY," 
				+ COL_USER_NAME + " TEXT,"
				+ COL_USER_PASSWORD + " TEXT,"
				+ COL_USER_LEVEL + " TEXT,"
				+ COL_USER_LAST_LOGIN + " TEXT,"
				+ COL_USER_CASHIER_ID + " TEXT"
			+ ");";
	
	
	String INSERT_TBL_USER = " INSERT INTO " + TBL_USER + " VALUES('"+ Shared.getUserID() +"','demo','"+ Shared.getMD5("demo") +"',1,'2016-09-16 01:01:01','"+ Shared.read(Constants.KEY_SETTING_CASHIER_ID,"") +"');" ;
	
	String TBL_ORDER = "product_order";
	String COL_ORDER_CODE = " order_id";
	String COL_ORDER_ORDERED_ON = "ordered_on";
	String COL_ORDER_UPDATED_ON = "updated_on";
	String COL_ORDER_SYCN_ON = "sycn_on";
	String COL_ORDER_DESCRIPTION = "description";
	String COL_ORDER_TAX = "tax";
	String COL_ORDER_DISCOUNT = "discount";
	String COL_ORDER_AMOUNT = "amount";
	String COL_ORDER_USER_ID = "user_id";
	String COL_ORDER_BRANCH_ID = "branch_id";
	
	String CREATE_TBL_ORDER = "CREATE TABLE "
			+ TBL_ORDER
			+ "(" 
				+ COL_ORDER_CODE  + " TEXT PRIMARY KEY," 
				+ COL_ORDER_ORDERED_ON + " DATETIME,"
				+ COL_ORDER_UPDATED_ON + " DATETIME,"
				+ COL_ORDER_SYCN_ON + " DATETIME,"
				+ COL_ORDER_DESCRIPTION + " TEXT,"
				+ COL_ORDER_TAX + " DOUBLE,"
				+ COL_ORDER_DISCOUNT + " DOUBLE,"
				+ COL_ORDER_AMOUNT + " DOUBLE,"
				+ COL_ORDER_USER_ID + " TEXT,"
				+ COL_ORDER_BRANCH_ID + " TEXT"
			+ ");";
	
	String TBL_PRODUCT_ORDER_DETAIL = "product_order_detail";
	String COL_PRODUCT_ORDER_DETAIL_CODE = "product_order_detail_id";
	String COL_PRODUCT_ORDER_DETAIL_PRODUCT_CODE = "product_id";
	String COL_PRODUCT_ORDER_DETAIL_ORDER_CODE = "order_id";
	String COL_PRODUCT_ORDER_DETAIL_PRICE = "price";
	String COL_PRODUCT_ORDER_DETAIL_DISCOUNT = "discount";
	String COL_PRODUCT_ORDER_DETAIL_QTY = "qty";
	
	String CREATE_TBL_PRODUCT_ORDER_DETAIL = "CREATE TABLE "
			+ TBL_PRODUCT_ORDER_DETAIL
			+ "(" 
				+ COL_PRODUCT_ORDER_DETAIL_CODE  + " TEXT PRIMARY KEY,"
				+ COL_PRODUCT_ORDER_DETAIL_PRODUCT_CODE + " TEXT," 
				+ COL_PRODUCT_ORDER_DETAIL_ORDER_CODE + " TEXT," 
				+ COL_PRODUCT_ORDER_DETAIL_PRICE + " DOUBLE," 
				+ COL_PRODUCT_ORDER_DETAIL_QTY + " DOUBLE,"
				+ COL_PRODUCT_ORDER_DETAIL_DISCOUNT + " DOUBLE"
			+ ");";
	
	String DROP_TBL_PRODUCT_CATEGORY = "DROP TABLE IF EXISTS "+ TBL_PRODUCT_CATEGORY;
	String DROP_TBL_PRODUCT = "DROP TABLE IF EXISTS "+ TBL_PRODUCT;	
	String DROP_TBL_SETTING = "DROP TABLE IF EXISTS "+ TBL_SETTING;
	String DROP_TBL_USER = "DROP TABLE IF EXISTS "+ TBL_USER;
	String DROP_TBL_ORDER = "DROP TABLE IF EXISTS "+ TBL_ORDER;
	String DROP_TBL_PRODUCT_ORDER_DETAIL = "DROP TABLE IF EXISTS "+ TBL_PRODUCT_ORDER_DETAIL;

}
