package com.suresh.whereismycash;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.widget.AutoCompleteTextView;

import com.actionbarsherlock.app.SherlockActivity;

public class CreateActivity extends SherlockActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create);
		AutoCompleteTextView auto = (AutoCompleteTextView) findViewById(R.id.autoEtName);
		Cursor c = new DbHelper(this).getDistinctNames();
		startManagingCursor(c);
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, c,
				new String[]{DbHelper.KEY_NAME}, new int[]{android.R.id.text1},
				CursorAdapter.FLAG_AUTO_REQUERY);
		auto.setAdapter(adapter);
	}
}
