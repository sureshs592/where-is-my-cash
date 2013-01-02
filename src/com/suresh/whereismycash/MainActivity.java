package com.suresh.whereismycash;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.suresh.whereismycash.DbHelper.PaymentType;

public class MainActivity extends SherlockListActivity implements OnClickListener{
	
	private DbHelper dbHelper = new DbHelper(this);
	private static final int ACTION_ADD_ENTRY = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    
    public void initializeList() {
    	Cursor c = dbHelper.getAllLoans();
        startManagingCursor(c);
        MainAdapter adapter = new MainAdapter(this, c, CursorAdapter.FLAG_AUTO_REQUERY, dbHelper);
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
		
		float getSum = dbHelper.getSumByType(PaymentType.GET);
		TextView tvGetAmount = (TextView) findViewById(R.id.tvGetAmount);
		tvGetAmount.setText(String.valueOf(getSum));
		
		float paySum = dbHelper.getSumByType(PaymentType.PAY);
		TextView tvPayAmount = (TextView) findViewById(R.id.tvPayAmount);
		tvPayAmount.setText(String.valueOf(paySum));
	}
    
}
