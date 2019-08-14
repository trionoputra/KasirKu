package com.chipo.cashier;

import com.chipo.cashier.fragment.CategoryListFragment;
import com.chipo.cashier.fragment.ProductListFragment;
import com.chipo.cashier.fragment.UserListFragment;
import com.chipo.cashier.utils.Constants;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

/**
 * An activity representing a list of Items. This activity has different
 * presentations for handset and tablet-size devices. On handsets, the activity
 * presents a list of items, which when touched, lead to a
 * {@link MasterDetailActivity} representing item details. On tablets, the
 * activity presents the list of items and item details side-by-side using two
 * vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link MasterListFragment} and the item details (if present) is a
 * {@link MasterDetailFragment}.
 * <p>
 * This activity also implements the required
 * {@link MasterListFragment.Callbacks} interface to listen for item selections.
 */
public class MasterListActivity extends FragmentActivity implements
		MasterListFragment.Callbacks {

	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */
	private boolean mTwoPane;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_master_list);

		if (findViewById(R.id.master_detail_container) != null) {
			// The detail container view will be present only in the
			// large-screen layouts (res/values-large and
			// res/values-sw600dp). If this view is present, then the
			// activity should be in two-pane mode.
			mTwoPane = true;

			// In two-pane mode, list items should be given the
			// 'activated' state when touched.
			((MasterListFragment) getFragmentManager().findFragmentById(
					R.id.master_list)).setActivateOnItemClick(true);
			
			setScreen("1");
		}

		// TODO: If exposing deep links into your app, handle intents here.
	}

	/**
	 * Callback method from {@link MasterListFragment.Callbacks} indicating that
	 * the item with the given ID was selected.
	 */
	@Override
	public void onItemSelected(String id) {
		if (mTwoPane) {
			// In two-pane mode, show the detail view in this activity by
			// adding or replacing the detail fragment using a
			// fragment transaction.
			/*Bundle arguments = new Bundle();
			arguments.putString(MasterDetailFragment.ARG_ITEM_ID, id);
			MasterDetailFragment fragment = new MasterDetailFragment();
			fragment.setArguments(arguments);
			getFragmentManager().beginTransaction()
					.replace(R.id.master_detail_container, fragment).commit();
			*/
			setScreen(id);
		} else {
			// In single-pane mode, simply start the detail activity
			// for the selected item ID.
			Intent detailIntent = new Intent(this, MasterDetailActivity.class);
			detailIntent.putExtra(MasterDetailFragment.ARG_ITEM_ID, id);
			startActivity(detailIntent);
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
}
