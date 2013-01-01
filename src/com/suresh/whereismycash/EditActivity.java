package com.suresh.whereismycash;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockListActivity;

public class EditActivity extends SherlockListActivity {
	
	private DbHelper dbHelper = new DbHelper(this);
	private String name;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit);
		name = getIntent().getStringExtra("name");
		setTitle(name);
		float amount = dbHelper.getLoanAmountByName(name);
		TextView tvTotal = (TextView) findViewById(R.id.tvTotal);
		DbHelper.setTextandColor(this, tvTotal, amount);
		initializeList();
	}
	
	public void initializeList() {
    	Cursor c = dbHelper.getLoansByName(name);
        startManagingCursor(c);
        EditAdapter adapter = new EditAdapter(name, this, c, CursorAdapter.FLAG_AUTO_REQUERY, dbHelper);
        setListAdapter(adapter);
    }
}
