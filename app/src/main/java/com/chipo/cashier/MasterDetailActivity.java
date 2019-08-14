package com.chipo.cashier;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;

import com.chipo.cashier.fragment.CategoryListFragment;
import com.chipo.cashier.fragment.ProductListFragment;
import com.chipo.cashier.fragment.UserListFragment;
import com.chipo.cashier.utils.Constants;

/**
 * An activity representing a single Master detail screen. This activity is only
 * used on handset devices. On tablet-size devices, item details are presented
 * side-by-side with a list of items in a {@link MasterListActivity}.
 * <p>
 * This activity is mostly just a 'shell' activity containing nothing more than
 * a {@link MasterDetailFragment}.
 */
public class MasterDetailActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_master_detail);

		// Show the Up button in the action bar.
	//	getActionBar().setDisplayHomeAsUpEnabled(true);

		// savedInstanceState is non-null when there is fragment state
		// saved from previous configurations of this activity
		// (e.g. when rotating the screen from portrait to landscape).
		// In this case, the fragment will automatically be re-added
		// to its container so we don't need to manually add it.
		// For more information, see the Fragments API guide at:
		//
		// http://developer.android.com/guide/components/fragments.html
		//
		if (savedInstanceState == null) {
			// Create the detail fragment and add it to the activity
			// using a fragment transaction.
		/*	Bundle arguments = new Bundle();
			arguments.putString(MasterDetailFragment.ARG_ITEM_ID, getIntent()
					.getStringExtra(MasterDetailFragment.ARG_ITEM_ID));
			MasterDetailFragment fragment = new MasterDetailFragment();
			fragment.setArguments(arguments);
			getFragmentManager().beginTransaction()
					.add(R.id.master_detail_container, fragment).commit();
			*/
			setScreen(getIntent().getStringExtra(MasterDetailFragment.ARG_ITEM_ID));
		}
	}
	
	private void setScreen(String id)
	{
		while (getSupportFragmentManager().getBackStackEntryCount() > 0){
		    getSupportFragmentManager().popBackStackImmediate();
		}
		
		Bundle arguments = new Bundle();
		arguments.putString(Constants.ARG_ITEM_ID, id);
		
		Fragment fragment  = new UserListFragment();
		String tag = "";
		
		if(id.equals("1"))
			fragment = new UserListFragment();
		else if(id.equals("2"))
			fragment = new CategoryListFragment();
		else if(id.equals("3"))
			fragment = new ProductListFragment();
	
		fragment.setArguments(arguments);
		getSupportFragmentManager()
		.beginTransaction()
		.setTransition(android.R.anim.fade_in)
		.replace(R.id.master_detail_container, fragment,tag)
		.commit();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			navigateUpTo(new Intent(this, MasterListActivity.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
