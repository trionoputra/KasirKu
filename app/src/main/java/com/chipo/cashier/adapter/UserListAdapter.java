package com.chipo.cashier.adapter;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.chipo.cashier.LoginActivity;
import com.chipo.cashier.MainActivity;
import com.chipo.cashier.R;
import com.chipo.cashier.entity.User;
import com.chipo.cashier.fragment.CategoryAddFragment;
import com.chipo.cashier.fragment.UserAddFragment;
import com.chipo.cashier.sqlite.DatabaseManager;
import com.chipo.cashier.sqlite.ds.ProductCategoryDataSource;
import com.chipo.cashier.sqlite.ds.UserDataSource;
import com.chipo.cashier.utils.Constants;
import com.chipo.cashier.utils.Shared;

public class UserListAdapter extends BaseAdapter {
 
    private List<User> dtList;
    private FragmentActivity context;
    private LayoutInflater inflater;
    private String itemID;
    public UserListAdapter(FragmentActivity context) {
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    
    public UserListAdapter(FragmentActivity context, List<User> data) {
     
        this.context = context;
        this.dtList = data;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    
    public UserListAdapter(FragmentActivity context,String itemid) {
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.itemID = itemid;
    }
 
    private class ViewHolder {
        TextView title;
        TextView body;
        TextView desc1;
        ImageButton edit;
	    ImageButton delete;
    }
 
    public int getCount() {
        return dtList.size();
    }
    
    public void set(List<User> list) {
    	dtList = list;
        notifyDataSetChanged();
    }
    
    public void remove(User user) {
    	dtList.remove(user);
        notifyDataSetChanged();
    }
    
    public void add(User user) {
    	dtList.add(user);
        notifyDataSetChanged();
    }
    
    public void insert(User user,int index) {
    	dtList.add(index, user);
        notifyDataSetChanged();
    }
 
    public Object getItem(int position) {
        return dtList.get(position);
    }
 
    public long getItemId(int position) {
        return 0;
    }
 
    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        ViewHolder holder;
        
        if (convertView == null) {
        	vi = inflater.inflate(R.layout.user_list_item, null);
            holder = new ViewHolder();
 
            holder.title = (TextView) vi.findViewById(R.id.textView1);
            holder.body = (TextView) vi.findViewById(R.id.textView2);
            holder.desc1 = (TextView) vi.findViewById(R.id.textView3);
            holder.edit = (ImageButton)vi.findViewById(R.id.imageView2);
            holder.delete = (ImageButton)vi.findViewById(R.id.imageView1);
            vi.setTag(holder);
        } else {
        	 holder=(ViewHolder)vi.getTag();
        }
        
      
        final User user = (User) getItem(position);
        holder.title.setText(user.getUserName());
        holder.body.setText(user.getLevel().equals("1") ? "administrator" : "cashier" );
        if(user.getLastLogin() != null)
        	holder.desc1.setText("last login: " + Shared.dateformatID.format(user.getLastLogin()));
        else
        	holder.desc1.setText("last login: - ");
        
        holder.edit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Fragment fragment  = new UserAddFragment();
				Bundle arguments = new Bundle();
				arguments.putString(Constants.ARG_USER_ID, user.getUserID());
				
				if(itemID != null)
					arguments.putString(Constants.ARG_ITEM_ID, itemID);
				
				
				fragment.setArguments(arguments);
				context.getSupportFragmentManager().beginTransaction()
				.setTransition(android.R.anim.slide_in_left)
				.addToBackStack("add")
				.replace(R.id.master_detail_container, fragment).commit();
			}
		});
        
        holder.delete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setMessage(R.string.delete_message).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
		               @Override
		               public void onClick(DialogInterface dialog, int id) {
		            	    if(user.getUserID().equals(MainActivity.SesID))
		            	    {
		            	    	Toast.makeText(context, context.getString(R.string.data_being_used), Toast.LENGTH_SHORT).show();
		            	    }
		            	    else
		            	    {
		            	    	
		            	    	SQLiteDatabase db =  DatabaseManager.getInstance().openDatabase();
		            	    	UserDataSource ds = new UserDataSource(db);
		            	    	if(!ds.cekAvailable(user.getUserID()))
		            	    	{
		            	    		ds.delete(user.getUserID());
				        	        dtList.remove(position);
				        	        notifyDataSetChanged();
				        	      
		            	    	}
		            	    	else
		            	    	{
		            	    		Toast.makeText(context, context.getString(R.string.data_being_used), Toast.LENGTH_SHORT).show();
		            	    	}
		            	    	
		            	    	  DatabaseManager.getInstance().closeDatabase();
			        	       
		            	    }
		            		
		               }
		           }).setNegativeButton(R.string.cancel, null);
				
				builder.show();
				
			}
		});
        
        return vi;
    }
}