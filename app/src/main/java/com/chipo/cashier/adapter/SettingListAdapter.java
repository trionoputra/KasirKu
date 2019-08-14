package com.chipo.cashier.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chipo.cashier.R;
import com.chipo.cashier.entity.Setting;
import com.chipo.cashier.utils.Shared;

public class SettingListAdapter extends BaseAdapter {
 
    private List<Setting> dtList;
    private Activity context;
    private LayoutInflater inflater;
    public SettingListAdapter(Activity context) {
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    
    public SettingListAdapter(Activity context, List<Setting> data) {
     
        this.context = context;
        this.dtList = data;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
 
    private class ViewHolder {
        TextView title;
        TextView body;
    }
 
    public int getCount() {
        return dtList.size();
    }
    
    public void set(List<Setting> list) {
    	dtList = list;
        notifyDataSetChanged();
    }
    
    public void remove(Setting user) {
    	dtList.remove(user);
        notifyDataSetChanged();
    }
    
    public void add(Setting user) {
    	dtList.add(user);
        notifyDataSetChanged();
    }
    
    public void insert(Setting user,int index) {
    	dtList.add(index, user);
        notifyDataSetChanged();
    }
 
    public Object getItem(int position) {
        return dtList.get(position);
    }
 
    public long getItemId(int position) {
        return 0;
    }
 
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        ViewHolder holder;
        
        if (convertView == null) {
        	vi = inflater.inflate(R.layout.setting_list_item, null);
            holder = new ViewHolder();
 
            holder.title = (TextView) vi.findViewById(R.id.textView1);
            holder.body = (TextView) vi.findViewById(R.id.textView2);
            
            vi.setTag(holder);
        } else {
        	 holder=(ViewHolder)vi.getTag();
        }
        
        Setting setting = (Setting) getItem(position);
        holder.title.setText(setting.getCode());
        holder.body.setText(setting.getValue() );
        
        return vi;
    }
}