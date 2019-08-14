package com.chipo.cashier.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.chipo.cashier.R;
import com.chipo.cashier.entity.ProductCategory;
import com.chipo.cashier.fragment.CategoryAddFragment;
import com.chipo.cashier.sqlite.DatabaseManager;
import com.chipo.cashier.sqlite.ds.ProductCategoryDataSource;
import com.chipo.cashier.utils.Constants;
import com.chipo.cashier.utils.Shared;

public class CategorySpinnerAdapter extends BaseAdapter {
 
    private List<ProductCategory> dtList = new ArrayList<ProductCategory>();
    private Activity context;
    private LayoutInflater inflater;
    public CategorySpinnerAdapter(Activity context) {
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    
    public CategorySpinnerAdapter(Activity context, List<ProductCategory> data) {
     
        this.context = context;
        this.dtList = data;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    
 
    private class ViewHolder {
        TextView title;
    }
 
    public int getCount() {
        return dtList.size();
    }
    
    public void set(List<ProductCategory> list) {
    	dtList = list;
        notifyDataSetChanged();
    }
    
    public int indexOf(String code) {
    	int index = -1;
    	for(int i = 0;i < dtList.size();i++) {
			if(code.equals(dtList.get(i).getCategoryID()))
				index = i;
		}
    	return index;
    }
    
    public void remove(ProductCategory user) {
    	dtList.remove(user);
        notifyDataSetChanged();
    }
    
    public void add(ProductCategory user) {
    	dtList.add(user);
        notifyDataSetChanged();
    }
    
    public void insert(ProductCategory user,int index) {
    	dtList.add(index, user);
        notifyDataSetChanged();
    }
 
    public Object getItem(int position) {
        return dtList.get(position);
    }
 
    public long getItemId(int position) {
        return 0;
    }
 
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
    	// TODO Auto-generated method stub
    	View vi = convertView;
        ViewHolder holder;
        
        if (convertView == null) {
        	vi = inflater.inflate(R.layout.category_spinner_item, null);
            holder = new ViewHolder();
 
            holder.title = (TextView) vi.findViewById(R.id.textView1);
            
            vi.setTag(holder);
        } else {
        	 holder=(ViewHolder)vi.getTag();
        }
        
        final ProductCategory category = (ProductCategory) getItem(position);
        holder.title.setText(category.getCategoryName());
        holder.title.setTypeface(Shared.OpenSansRegular);
        
        return vi;
    }

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View vi = convertView;
        ViewHolder holder;
        
        if (convertView == null) {
        	vi = inflater.inflate(R.layout.category_spinner_item, null);
            holder = new ViewHolder();
 
            holder.title = (TextView) vi.findViewById(R.id.textView1);
            
            vi.setTag(holder);
        } else {
        	 holder=(ViewHolder)vi.getTag();
        }
        
        final ProductCategory category = (ProductCategory) getItem(position);
        holder.title.setText(category.getCategoryName());
        holder.title.setTypeface(Shared.OpenSansRegular);
        
        return vi;
	}
    
}