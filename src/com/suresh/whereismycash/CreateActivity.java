package com.suresh.whereismycash;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.FilterQueryProvider;

import com.actionbarsherlock.app.SherlockActivity;

public class CreateActivity extends SherlockActivity implements FilterQueryProvider {
	private DbHelper dbHelper;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create);
		dbHelper = new DbHelper(this);
		AutoCompleteTextView auto = (AutoCompleteTextView) findViewById(R.id.autoEtName);
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(
				this, android.R.layout.simple_list_item_1, null,
				new String[]{DbHelper.KEY_NAME}, new int[]{android.R.id.text1},
				CursorAdapter.FLAG_AUTO_REQUERY);
		adapter.setFilterQueryProvider(this);
		auto.setAdapter(adapter);
	}

	@Override
	public Cursor runQuery(CharSequence constraint) {
		return (constraint == null)
			? null
			: dbHelper.getMatchingNames(constraint.toString());
	}
}
