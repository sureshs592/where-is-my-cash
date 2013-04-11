package com.suresh.whereismycash;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class EditActivity extends SherlockListActivity {
	
	private DbHelper dbHelper = new DbHelper(this);
	private String name;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit);
		name = getIntent().getStringExtra("name");
		setTitle(name);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
	}
	
	public void initializeList() {
    	Cursor c = dbHelper.getLoansByName(name);
        startManagingCursor(c);
        EditAdapter adapter = new EditAdapter(name, this, c, CursorAdapter.FLAG_AUTO_REQUERY, dbHelper);
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
    		Intent i = new Intent(this, CreateActivity.class);
    		i.putExtra("name", name);
    		startActivity(i);
    		break;
    	case android.R.id.home:
    		finish();
    		break;
    	default:
    		return super.onOptionsItemSelected(item);
		}
		
		return true;
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		float amount = dbHelper.getLoanAmountByName(name);
		TextView tvTotal = (TextView) findViewById(R.id.tvTotal);
		DbHelper.setTextandColor(this, tvTotal, amount);
		initializeList();
	}
}
