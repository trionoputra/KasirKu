package com.chipo.cashier.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.LayoutInflater.Filter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.chipo.cashier.R;
import com.chipo.cashier.entity.Cart;
import com.chipo.cashier.utils.Constants;
import com.chipo.cashier.utils.Shared;

public class CartListAdapter extends BaseAdapter {
 
    private List<Cart> dtList = new ArrayList<Cart>();
    private Activity context;
    private LayoutInflater inflater;
    private CartListener listener;
    public CartListAdapter(Activity context) {
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    
    public CartListAdapter(Activity context, List<Cart> data) {
     
        this.context = context;
        this.dtList = data;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    
    private class ViewHolder {
        TextView name;
        TextView price;
        TextView qty;
        ImageButton btnMinus;
        ImageButton btnPlus;
    }
 
    public int getCount() {
        return dtList.size();
    }
    
    public void set(List<Cart> list) {
    	dtList = list;
        notifyDataSetChanged();
    }
    
    public void remove(Cart user) {
    	dtList.remove(user);
        notifyDataSetChanged();
    }
    public void removeAll() {
    	dtList = new ArrayList<Cart>();
        notifyDataSetChanged();
    }
    
    public void removeByID(String code) {
    	for (int i = 0; i < dtList.size(); i++) {
			if(dtList.get(i).getProductID().equals(code))
				dtList.remove(i);
		};
        notifyDataSetChanged();
    }
    
    public void add(Cart user) {
    	dtList.add(user);
        notifyDataSetChanged();
    }
    
    public void insert(Cart user,int index) {
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
    public void notifyDataSetChanged() {
    	// TODO Auto-generated method stub
    	super.notifyDataSetChanged();
		if(listener != null)
			listener.onChange(dtList);
    }
 
    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        ViewHolder holder;
        
        if (convertView == null) {
        	vi = inflater.inflate(R.layout.cart_list_item, null);
            holder = new ViewHolder();
 
            holder.name = (TextView) vi.findViewById(R.id.textView1);
            holder.price = (TextView) vi.findViewById(R.id.textView2);
            holder.qty = (TextView) vi.findViewById(R.id.textView3);
            holder.btnMinus = (ImageButton) vi.findViewById(R.id.imageButton2);
            holder.btnPlus = (ImageButton)vi.findViewById(R.id.imageButton1);
            
            vi.setTag(holder);
        } else {
        	 holder=(ViewHolder)vi.getTag();
        }
        
        final Cart cart = (Cart) getItem(position);
        holder.name.setText(cart.getProductName());
        holder.price.setText(Shared.read(Constants.KEY_SETTING_CURRENCY_SYMBOL,Constants.VAL_DEFAULT_CURRENCY_SYMBOL) + ""+ Shared.decimalformat.format(cart.getSubtotal()));
        holder.qty.setText(String.valueOf(cart.getQty()));
        
        holder.name.setTypeface(Shared.OpenSansRegular);
        holder.price.setTypeface(Shared.OpenSansSemibold);
        holder.qty.setTypeface(Shared.OpenSansRegular);
        
        if(cart.getQty() <= 0)
        {
        	holder.btnMinus.setEnabled(false);
        }
        
        holder.btnMinus.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				cart.setQty(cart.getQty() - 1);
				
				double discount =  (cart.getPrice()*cart.getQty()) * (cart.getDiscount()/100);
				double subtotal = (cart.getPrice()*cart.getQty()) - discount;
				cart.setSubtotal(subtotal);
				dtList.set(position, cart);
				
				if(cart.getQty() <= 0)
		        {
					dtList.remove(position);
					if(listener != null)
						listener.onRemove(cart.getProductID());
						
		        }
				notifyDataSetChanged();
			}
		});
        
        holder.btnPlus.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				cart.setQty(cart.getQty() + 1);
			
				double discount = (cart.getPrice()*cart.getQty()) * (cart.getDiscount()/100);
				double subtotal = (cart.getPrice()*cart.getQty()) - discount;
				cart.setSubtotal(subtotal);
				
				dtList.set(position, cart);
				notifyDataSetChanged();
			}
		});
        
        return vi;
    }
    

    
    public void setCartListener(CartListener listener)
    {
    	this.listener = listener;
    }
    
    public interface CartListener {
        public void onRemove(String result);
        public void onChange(List<Cart> list);
    }
}