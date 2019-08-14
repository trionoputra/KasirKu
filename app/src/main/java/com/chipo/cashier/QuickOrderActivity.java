package com.chipo.cashier;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.chipo.cashier.adapter.CartListAdapter;
import com.chipo.cashier.adapter.CartListAdapter.CartListener;
import com.chipo.cashier.adapter.CategorySpinnerMenuAdapter;
import com.chipo.cashier.adapter.ProductGridAdapter;
import com.chipo.cashier.entity.Cart;
import com.chipo.cashier.entity.Order;
import com.chipo.cashier.entity.OrderDetails;
import com.chipo.cashier.entity.Product;
import com.chipo.cashier.entity.ProductCategory;
import com.chipo.cashier.printer.DeviceListActivity;
import com.chipo.cashier.sqlite.DatabaseHelper;
import com.chipo.cashier.sqlite.DatabaseManager;
import com.chipo.cashier.sqlite.ds.ProductCategoryDataSource;
import com.chipo.cashier.sqlite.ds.ProductDataSource;
import com.chipo.cashier.utils.Constants;
import com.chipo.cashier.utils.Shared;
import com.chipo.cashier.widget.CustomConfirm;
import com.chipo.cashier.widget.CustomConfirm.ConfirmListener;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.zj.btsdk.BluetoothService;

public class QuickOrderActivity extends Activity {
	private RelativeLayout menuWrapper;
	private RelativeLayout cartWrapper;
	private GridView menuGrid;
	private ProductGridAdapter menuadapter;
	private CartListAdapter cartadapter;
	private ListView cartList;
	
	private TextView txtTotal;
	private TextView txtTotal2;
	private EditText txtKeyword;
	private EditText txtPayment;
	private TextView txtChange;
	private TextView txtempty;
	
	private Button btnCancel;
	private Button btnOrder;
	private Button btnSave;
	private Button btnCancelCheckout;
	private Button btnPay;
	private ImageButton btnToggleList;
	
	private Spinner spinnerCategory;
	private CategorySpinnerMenuAdapter spinneradapter;
	
	private ScrollView scroll;
	private ImageView arrow;
	
	private RelativeLayout cartContainer;
	private RelativeLayout checkOutContainer;
	
	private boolean isCheckout = false;
	private Double total = 0.0;
	
	private BluetoothService mService = null;
	private BluetoothDevice con_dev = null;
	private ProductDataSource DS;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Shared.initialize(getBaseContext());
		
		setContentView(R.layout.activity_quick_order);
		
		menuWrapper = (RelativeLayout)findViewById(R.id.bgMenu);
		cartWrapper = (RelativeLayout)findViewById(R.id.bgCart);
		
		menuGrid = (GridView)findViewById(R.id.gridView1);
		menuadapter = new ProductGridAdapter(this);
		
		menuGrid.setAdapter(menuadapter);
		
		initLayout();
		
		DatabaseManager.initializeInstance(new DatabaseHelper(this));
		SQLiteDatabase db =  DatabaseManager.getInstance().openDatabase();
		DS = new ProductDataSource(db);
		menuadapter.set(DS.getAll("",""));
		
		spinnerCategory = (Spinner)findViewById(R.id.spinner1);
		spinneradapter = new CategorySpinnerMenuAdapter(this);
		spinnerCategory.setAdapter(spinneradapter);
		
		ProductCategoryDataSource catds = new ProductCategoryDataSource(db);
		ArrayList<ProductCategory> catList = catds.getAll();
		ProductCategory ct = new ProductCategory();
		ct.setCategoryID("");
		ct.setCategoryName("All");
		catList.add(0, ct);
		spinneradapter.set(catList);
		
		//DatabaseManager.getInstance().closeDatabase();
		
		cartadapter = new CartListAdapter(this);
		cartList = (ListView)findViewById(R.id.listView1);
		cartList.setAdapter(cartadapter);

		txtTotal = (TextView)findViewById(R.id.textView3);
		txtTotal2 = (TextView)findViewById(R.id.textView5);
		txtKeyword = (EditText)findViewById(R.id.editText1);
		txtPayment = (EditText)findViewById(R.id.editText2);
		txtChange = (TextView)findViewById(R.id.textView8);
		
		btnCancel = (Button)findViewById(R.id.btnCancel);
		btnSave = (Button)findViewById(R.id.btnSave);
		btnOrder = (Button)findViewById(R.id.btnOrder);
		btnCancelCheckout = (Button)findViewById(R.id.btnCancelCheckout);
		btnPay = (Button)findViewById(R.id.btnCheckout);
		btnToggleList = (ImageButton)findViewById(R.id.imageView2);
		
		menuGrid.setOnItemClickListener(gridOnlick);
		btnCancel.setOnClickListener(cancelOnlick);
		btnSave.setOnClickListener(saveOnlick);
		btnCancelCheckout.setOnClickListener(cancelCheckOutOnlick);
		btnPay.setOnClickListener(payOnlick);
		btnOrder.setOnClickListener(orderOnlick);
		btnToggleList.setOnClickListener(toogleOnclick);
		
		cartContainer = (RelativeLayout)findViewById(R.id.cartContainer);
		checkOutContainer = (RelativeLayout)findViewById(R.id.checkOutContainer);
		
		spinnerCategory.setOnItemSelectedListener(spinnerCategoryOnChange);
		
		
		cartadapter.setCartListener(new CartListener() {
			
			@Override
			public void onRemove(String result) {
				// TODO Auto-generated method stub
				menuadapter.setSelection(result);
				if(cartadapter.getCount() == 0)
					txtempty.setVisibility(View.VISIBLE);
				else
					txtempty.setVisibility(View.GONE);
			}

			@Override
			public void onChange(List<Cart> list) {
				// TODO Auto-generated method stub
				double mtotal = 0;
				for (int i = 0; i < list.size(); i++) {
					double sub = (list.get(i).getPrice()*list.get(i).getQty());
					double discount =  sub * (list.get(i).getDiscount()/100);
					double subtotal = sub - discount;
					mtotal += subtotal;
				}
				
				total = mtotal;
				txtTotal.setText(Shared.read(Constants.KEY_SETTING_CURRENCY_SYMBOL,Constants.VAL_DEFAULT_CURRENCY_SYMBOL) + ""+ Shared.decimalformat.format(mtotal));
				txtTotal2.setText(txtTotal.getText().toString());
				
				if(cartadapter.getCount() == 0)
					txtempty.setVisibility(View.VISIBLE);
				else
					txtempty.setVisibility(View.GONE);
			}
		});
		
		
		txtKeyword.addTextChangedListener(keywordOnchange);
		txtPayment.addTextChangedListener(paymentOnchange);
		
		
		scroll = (ScrollView)findViewById(R.id.scrollView1);
		arrow = (ImageView)findViewById(R.id.imageView4);
		
		
		TextView t1 =(TextView)findViewById(R.id.textView1);
		TextView t2 =(TextView)findViewById(R.id.textView2);
		TextView t4 =(TextView)findViewById(R.id.textView4);
		TextView t6 =(TextView)findViewById(R.id.textView6);
		TextView t7 =(TextView)findViewById(R.id.textView7);
		txtempty =(TextView)findViewById(R.id.textView9);
		
		t1.setTypeface(Shared.OpenSansSemibold);
		t2.setTypeface(Shared.OpenSansBold);
		t4.setTypeface(Shared.OpenSansBold);
		t6.setTypeface(Shared.OpenSansBold);
		t7.setTypeface(Shared.OpenSansBold);
		txtempty.setTypeface(Shared.openSansLightItalic);
		
		txtKeyword.setTypeface(Shared.openSansLightItalic);
		txtTotal.setTypeface(Shared.OpenSansBold);
		txtTotal2.setTypeface(Shared.OpenSansBold);
		txtChange.setTypeface(Shared.OpenSansBold);
		
		btnCancel.setTypeface(Shared.OpenSansSemibold);
		btnOrder.setTypeface(Shared.OpenSansSemibold);
		btnSave.setTypeface(Shared.OpenSansSemibold);
		btnCancelCheckout.setTypeface(Shared.OpenSansSemibold);
		btnPay.setTypeface(Shared.OpenSansSemibold);
		txtPayment.setTypeface(Shared.openSansLight);
		
		mService = new BluetoothService(this, mHandler);
		if( mService.isAvailable() == false ){
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
		}

		//Intent serverIntent = new Intent(QuickOrderActivity.this,DeviceListActivity.class);      //运行另外一个类的活动
		//startActivityForResult(serverIntent,Constants.REQUEST_CONNECT_DEVICE);
	}
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		 String address = Shared.read(Constants.KEY_SETTING_MAC_ADDRESS,"0F:03:E0:C2:42:86");
		 if(!address.equals(""))
		 {
			 con_dev = mService.getDevByMac(address);
			 BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
			 if(mBluetoothAdapter.isEnabled())
			 {
				 mService.connect(con_dev);
				 Toast.makeText(QuickOrderActivity.this,"on",Toast.LENGTH_LONG).show();
			 }
		 }

	}
	
	private void initLayout()
	{

		final int width = Shared.getDisplayWidth();
		
		menuWrapper.post(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				LayoutParams param = new LayoutParams(menuWrapper.getLayoutParams());
				param.width = (width/3) * 2;
				menuWrapper.setLayoutParams(param);
			}
		});
		
		cartWrapper.post(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				LayoutParams param = new LayoutParams(cartWrapper.getLayoutParams());
				param.width = (width/3);
				cartWrapper.setLayoutParams(param);
			}
		});
	}
	
	private OnItemClickListener gridOnlick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
			// TODO Auto-generated method stub
			
			if(!isCheckout)
			{

				Product product = (Product) menuadapter.getItem(position);
				menuadapter.setSelection(product.getProductID());
				
				if(menuadapter.isSelected(product.getProductID()))
				{
					Cart cart = new Cart();
					cart.setProductID(product.getProductID());
					cart.setProductName(product.getProductName());
					cart.setPrice(product.getPrice());
					cart.setDiscount(product.getDiscount());
					cart.setQty(1);
					
					double discount = cart.getPrice() * (product.getDiscount()/100);
					double subtotal = cart.getPrice() - discount;
					cart.setSubtotal(subtotal);
					cartadapter.add(cart);
				}
				else
				{
					cartadapter.removeByID(product.getProductID());
				} 
			}
		}
	};
	
	private OnClickListener cancelOnlick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(cartadapter.getCount() != 0)
			{
				cartadapter.removeAll();
				menuadapter.reset();
			}
			else
			{
				finish();
				overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
			}
		}
	};
	
	private OnItemSelectedListener spinnerCategoryOnChange = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			// TODO Auto-generated method stub
			 ProductCategory c = (ProductCategory) spinneradapter.getItem(position);
			 //menuadapter.setFilter(txtKeyword.getText().toString(), c.getCategoryID());
			menuadapter.set(DS.getAll(txtKeyword.getText().toString(),c.getCategoryID()));
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub
		}
	};
	
	private TextWatcher keywordOnchange = new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			// TODO Auto-generated method stub
			ProductCategory cat =   (ProductCategory) spinnerCategory.getSelectedItem();
			menuadapter.set(DS.getAll(txtKeyword.getText().toString(),cat.getCategoryID()));
		}
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,int after) {
			// TODO Auto-generated method stub
		}
		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
		}
	};
	
	private boolean canScroll(ScrollView scrollView) {
	    View child = (View) scrollView.getChildAt(0);
	    if (child != null) {
	        int childHeight = (child).getHeight();
	        return scrollView.getHeight() < childHeight + scrollView.getPaddingTop() + scrollView.getPaddingBottom();
	    }
	    return false;
	}
	
	public void resizeArrow()
	{
		scroll.post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(canScroll(scroll))
				{
					android.widget.LinearLayout.LayoutParams param = new LayoutParams(arrow.getLayoutParams());
					param.height = arrow.getHeight()/2;
					param.width = arrow.getWidth()/2;
					param.bottomMargin = 16;
					param.topMargin = 16;
					param.gravity = Gravity.CENTER_HORIZONTAL;
					arrow.setLayoutParams(param);
				}
			}
		});
		
	}
	
	private OnClickListener cancelCheckOutOnlick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			isCheckout = false;
			showCart();
		}
	};
	
	
	
	private OnClickListener payOnlick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(txtPayment.getText().toString().equals(""))
			{
				Toast.makeText(QuickOrderActivity.this, getString(R.string.enter_payment), Toast.LENGTH_SHORT).show();
				return;
			}
			
			Date dt = new Date();
			
			Order order = new Order();
			order.setOrderID(Shared.getOrderID());
			order.setBranchID(Shared.read(Constants.KEY_SETTING_BRANCH_ID,""));
			order.setUserID(MainActivity.SesID);
			order.setCreatedOn(dt);
			order.setDescription("");
			order.setUpdatedOn(dt);
			
			ArrayList<OrderDetails> detaillist = new ArrayList<OrderDetails>();
			double subtotal = 0;
			double disc = 0;
			for (int i = 0; i < cartadapter.getCount(); i++) {
				Cart cart = (Cart) cartadapter.getItem(i);
				OrderDetails detail = new OrderDetails();
				detail.setDetailID(Shared.getOrderDetailID(i));
				detail.setDiscount(cart.getDiscount());
				detail.setName(cart.getProductName());
				detail.setQty(cart.getQty());
				detail.setPrice(cart.getPrice());
				detail.setProductID(cart.getProductID());
				detail.setOrderID(order.getOrderID());
				detaillist.add(detail);
				
				subtotal += detail.getQty() * detail.getPrice();
				disc += subtotal * (detail.getDiscount()/100);
			}
			
			order.setOrderDetails(detaillist);
			
			order.setDiscount(disc);
			
			double sub = subtotal - disc;
			double tax = sub * (Double.parseDouble(Shared.read(Constants.KEY_SETTING_TAX,Constants.VAL_DEFAULT_TAX))/100);
			
			order.setTax(tax);
			order.setAmount(sub + tax);
			
			
			CustomConfirm con = new CustomConfirm(QuickOrderActivity.this,order,mService);
			con.setConfirmListener(new ConfirmListener() {
				@Override
				public void onFinish(String result) {
					// TODO Auto-generated method stub
					ClearForm();
					Toast.makeText(QuickOrderActivity.this,getString(R.string.transaction_succeed), Toast.LENGTH_SHORT).show();
				}
			});
			con.show();
			
		}
	};
	
	private OnClickListener orderOnlick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(cartadapter.getCount() == 0)
			{
				Toast.makeText(QuickOrderActivity.this, getString(R.string.select_one), Toast.LENGTH_SHORT).show();
				return;
			}
			isCheckout = true;
			showCheckout();
		}
	};

	private OnClickListener saveOnlick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

		}
	};
	
	private TextWatcher paymentOnchange = new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			// TODO Auto-generated method stub
			try {
				if(!s.toString().equals(""))
				{
					double pay = Integer.valueOf(s.toString());
					double change = pay - total;
					txtChange.setText(Shared.read(Constants.KEY_SETTING_CURRENCY_SYMBOL,Constants.VAL_DEFAULT_CURRENCY_SYMBOL) + ""+ Shared.decimalformat.format(change));
				}
				else
				{
					txtChange.setText(Shared.read(Constants.KEY_SETTING_CURRENCY_SYMBOL,Constants.VAL_DEFAULT_CURRENCY_SYMBOL) + ""+ Shared.decimalformat.format(0));
				}
				
					
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			
		}
	};
	
	private final  Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case BluetoothService.MESSAGE_STATE_CHANGE:
                switch (msg.arg1) {
                case BluetoothService.STATE_CONNECTED: 
                	Toast.makeText(getApplicationContext(), "Connect successful",Toast.LENGTH_SHORT).show();
                    break;
                case BluetoothService.STATE_CONNECTING:
                    break;
                case BluetoothService.STATE_LISTEN: 
                case BluetoothService.STATE_NONE:
                    break;
                }
                break;
            case BluetoothService.MESSAGE_CONNECTION_LOST: 
                Toast.makeText(getApplicationContext(), "Device connection was lost", Toast.LENGTH_SHORT).show();
                break;
            case BluetoothService.MESSAGE_UNABLE_CONNECT:  
            	Toast.makeText(getApplicationContext(), "Unable to connect device",Toast.LENGTH_SHORT).show();
            	break;
            }
        }
    };
    
    @Override
    public void onStart() {
    	super.onStart();
    	if(mService != null)
    	{
    		if( mService.isBTopen() == false)
    		{
                Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableIntent, Constants.REQUEST_ENABLE_BT);
    		}
    	}
    }
    
    @Override
	protected void onDestroy() {
		super.onDestroy();
		if (mService != null) 
			mService.stop();
		mService = null; 
	}
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {    
        switch (requestCode) {
        case Constants.REQUEST_ENABLE_BT:
            if (resultCode == Activity.RESULT_OK) {
            	Toast.makeText(this, "Bluetooth open successful", Toast.LENGTH_LONG).show();
            } else {                
            	finish();
            }
            break;
        case  Constants.REQUEST_CONNECT_DEVICE: 
        	if (resultCode == Activity.RESULT_OK) { 
                String address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                Shared.write(Constants.KEY_SETTING_MAC_ADDRESS, address);
                con_dev = mService.getDevByMac(address);
                mService.connect(con_dev);
            }
            break;
        }
    } 
    
    private void showCart()
    {
    	YoYo.with(Techniques.FadeOutDown).duration(700).withListener(new AnimatorListener() {
			
			@Override
			public void onAnimationStart(Animator arg0) {
				// TODO Auto-generated method stub
				btnCancelCheckout.setEnabled(false);
				btnPay.setEnabled(false);
			}
			@Override
			public void onAnimationRepeat(Animator arg0) {
				// TODO Auto-generated method stub
			}
			@Override
			public void onAnimationEnd(Animator arg0) {
				// TODO Auto-generated method stub
				checkOutContainer.setVisibility(View.GONE);
				btnOrder.setEnabled(true);
				btnCancel.setEnabled(true);
			}
			
			@Override
			public void onAnimationCancel(Animator arg0) {
				// TODO Auto-generated method stub
				
			}
		}).playOn(checkOutContainer);
    }
    
    private void showCheckout()
    {
    	YoYo.with(Techniques.FadeInDown).duration(1000).withListener(new AnimatorListener() {
			
			@Override
			public void onAnimationStart(Animator arg0) {
				// TODO Auto-generated method stub
				checkOutContainer.setVisibility(View.VISIBLE);
				btnOrder.setEnabled(false);
				btnCancel.setEnabled(false);
			}
			
			@Override
			public void onAnimationRepeat(Animator arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animator arg0) {
				// TODO Auto-generated method stub
				btnPay.setEnabled(true);
				btnCancelCheckout.setEnabled(true);
			}
			
			@Override
			public void onAnimationCancel(Animator arg0) {
				// TODO Auto-generated method stub
				
			}
		}).playOn(checkOutContainer);
    }
    
    private void ClearForm()
    {
    	cartadapter.removeAll();
    	txtPayment.setText("");
    	showCart();
    	txtKeyword.setText("");
    	spinnerCategory.setSelection(0);
    	menuadapter.unCheckAll();
    	isCheckout = false;
    }

	private OnClickListener toogleOnclick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if(Shared.read(Constants.KEY_SETTING_MENU,Constants.VAL_DEFAULT_MENU).equals("GRID"))
			{
				Shared.write(Constants.KEY_SETTING_MENU,"LIST");
				btnToggleList.setImageResource(R.drawable.ic_grid_on_black_36dp);
			}
			else
			{
				Shared.write(Constants.KEY_SETTING_MENU,"GRID");
				btnToggleList.setImageResource(R.drawable.ic_list_black_36dp);
			}
		}
	};
	
    
    
	
}
