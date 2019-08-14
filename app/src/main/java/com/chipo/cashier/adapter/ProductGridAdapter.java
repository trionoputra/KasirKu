package com.chipo.cashier.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chipo.cashier.R;
import com.chipo.cashier.entity.Product;
import com.chipo.cashier.utils.Constants;
import com.chipo.cashier.utils.Shared;

public class ProductGridAdapter extends BaseAdapter {
 
    private List<Product> dtList = new ArrayList<Product>();
    private List<String> selected = new ArrayList<>();

    private Activity context;
    private LayoutInflater inflater;
    private String menu = "GRID";

    public ProductGridAdapter(Activity context) {
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public ProductGridAdapter(Activity context,String menu) {
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.menu = menu;
    }
    
    public ProductGridAdapter(Activity context, List<Product> data) {
        this.context = context;
        this.dtList = data;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    
    private class ViewHolder {
        TextView name;
        TextView price;
        ImageView image;
        RelativeLayout selectedWrapper;
    }
 
    public int getCount() {
        return dtList.size();
    }
    
    
    public void removeSelection()
    {
    	
    }

    public void setMenu(String menu) {
        this.menu = menu;
        notifyDataSetChanged();
    }

    public void setSelection(String id) {
        int index = selected.indexOf(id);
        if(index == -1)
            selected.add(id);
        else
            selected.remove(id);

        this.notifyDataSetChanged();
    }

    public boolean isSelected(String id) {
        return selected.contains(id);
    }
    

    public void set(List<Product> list) {
    	dtList = list;
    	this.notifyDataSetChanged();
    }
    
    public void remove(Product user) {
        dtList.remove(user);
        this.notifyDataSetChanged();
    }

    
    public void unCheckAll() {
        selected.clear();
        this.notifyDataSetChanged();
    }
    
    public void reset() {
        selected.clear();
        this.notifyDataSetChanged();
    }
    
    public void add(Product product) {
    	dtList.add(product);
        this.notifyDataSetChanged();
    }
    
    public void insert(Product product,int index) {
        dtList.add(index, product);
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
            if(this.menu.equals("GRID"))
                vi = inflater.inflate(R.layout.product_grid_item, null);
            else
                vi = inflater.inflate(R.layout.product_grid_item, null);

            holder = new ViewHolder();
 
            holder.name = (TextView) vi.findViewById(R.id.textView1);
            holder.price = (TextView) vi.findViewById(R.id.textView2);
            holder.image = (ImageView) vi.findViewById(R.id.imageView1);
            holder.selectedWrapper = (RelativeLayout)vi.findViewById(R.id.relativeLayout1);
            
            vi.setTag(holder);
        } else {
        	 holder=(ViewHolder)vi.getTag();
        }
        
        final Product product = (Product) getItem(position);
        holder.name.setText(product.getCategoryName());
        holder.price.setText(Shared.read(Constants.KEY_SETTING_CURRENCY_SYMBOL,Constants.VAL_DEFAULT_CURRENCY_SYMBOL) + ""+ Shared.decimalformat.format(product.getPrice()));
        
        holder.selectedWrapper.setVisibility(View.GONE);

        if(selected.contains(product.getProductID()))
        	 holder.selectedWrapper.setVisibility(View.VISIBLE);
        
        holder.name.setTypeface(Shared.OpenSansRegular);
        holder.price.setTypeface(Shared.OpenSansBold);
        
        if(product.getImage() != null)
    	{
        	File imgFile = new  File(product.getImage());
    		if(imgFile.exists()){
    			holder.image.setImageDrawable(Drawable.createFromPath(imgFile.getAbsolutePath()));
    		}
    	}
        
        return vi;
    }
}