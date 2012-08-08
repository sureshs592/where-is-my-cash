package com.suresh.whereismycash;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;

import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class MainActivity extends SherlockListActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Cursor c = new DbHelper(this).getAllLoans();
        String[] displayColumns = {"SUM(" + DbHelper.KEY_AMOUNT + ")", DbHelper.KEY_NAME};
        int[] displayViews = {R.id.tvAmount, R.id.tvName};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.main_list_item, c, displayColumns, displayViews, CursorAdapter.FLAG_AUTO_REQUERY);
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
    		startActivity(new Intent(this, CreateActivity.class));
    		break;
    	default:
    		return super.onOptionsItemSelected(item);
    	}
    	return true;
    }
    
}
