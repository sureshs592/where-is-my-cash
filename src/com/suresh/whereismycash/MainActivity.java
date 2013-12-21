package com.suresh.whereismycash;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class MainActivity extends SherlockListActivity implements OnClickListener{
	
	private DbHelper dbHelper = new DbHelper(this);
	private static final int ACTION_ADD_ENTRY = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        displayOneTimeInfoDialog();
    }
    
    public void initializeList() {
    	Cursor c = dbHelper.getAllLoans();
        startManagingCursor(c);
        MainAdapter adapter = new MainAdapter(this, getListView(), c, CursorAdapter.FLAG_AUTO_REQUERY, dbHelper);
        setListAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	getSupportMenuInflater().inflate(R.menu.activity_main, menu);
    	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()) {
    	case R.id.menu_add:
    		startActivityForResult(new Intent(this, CreateActivity.class), ACTION_ADD_ENTRY);
    		break;
    	default:
    		return super.onOptionsItemSelected(item);
    	}
    	return true;
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	switch (requestCode) {
    	case ACTION_ADD_ENTRY:
    		if (resultCode == RESULT_OK) {
    			initializeList();
    		}
    		break;
    	default:
    		super.onActivityResult(requestCode, resultCode, data);
    	}
    }

	@Override
	public void onClick(View v) {
		String name = (String) v.getTag();
    	Intent i = new Intent(this, EditActivity.class);
    	i.putExtra("name", name);
    	startActivity(i);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		initializeList();
		
		float netSum = dbHelper.getNetSum();
		TextView tvNetAmount = (TextView) findViewById(R.id.tvNetAmount);
		DbHelper.setTextandColor(this, tvNetAmount, netSum);
	}
	
	public void displayOneTimeInfoDialog() {
		try {
			SharedPreferences info = getSharedPreferences("info", MODE_PRIVATE);
			int storedVersionCode = info.getInt("versionCode", -1);
			int currentVersionCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
			
			if (storedVersionCode < currentVersionCode && MiscUtil.phoneSupportsSwipe()) { //Display information dialog
				AlertDialog.Builder builder = new Builder(this);
				builder.setTitle(R.string.update_dialog_title);
				builder.setMessage(R.string.update_info);
				builder.setNeutralButton(R.string.dialog_ok, null);
				AlertDialog dialog = builder.create();
				dialog.show();
			}
			
			Editor editor = info.edit();
			editor.putInt("versionCode", currentVersionCode);
			editor.commit();
		} catch (NameNotFoundException ignore) { }
	}
    
}
