package com.suresh.whereismycash;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.FilterQueryProvider;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.suresh.whereismycash.DbHelper.PaymentType;

public class CreateActivity extends SherlockActivity implements
FilterQueryProvider, OnClickListener, OnItemClickListener, OnCheckedChangeListener {
	private DbHelper dbHelper;
	private AutoCompleteTextView auto;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create);
		setTitle("Add Entry");
		dbHelper = new DbHelper(this);
		auto = (AutoCompleteTextView) findViewById(R.id.autoEtName);
		String name = getIntent().getStringExtra("name");
		if (name == null) {
			SimpleCursorAdapter adapter = new SimpleCursorAdapter(
					this, android.R.layout.simple_list_item_1, null,
					new String[]{DbHelper.KEY_NAME}, new int[]{android.R.id.text1},
					CursorAdapter.FLAG_AUTO_REQUERY);
			adapter.setFilterQueryProvider(this);
			auto.setAdapter(adapter);
			auto.setOnItemClickListener(this);
			auto.setEnabled(true);
		} else {
			auto.setText(name);
			auto.setEnabled(false);
		}
		findViewById(R.id.btAdd).setOnClickListener(this);
		((RadioGroup) findViewById(R.id.radioGroupType)).setOnCheckedChangeListener(this);
	}
	
	public void create() {
		RadioGroup radioType = (RadioGroup) findViewById(R.id.radioGroupType);
		int checked = radioType.getCheckedRadioButtonId();
		PaymentType type = (checked == R.id.radioGet)
				? DbHelper.PaymentType.GET : DbHelper.PaymentType.PAY;
		
		String inputAmount = ((TextView) findViewById(R.id.etAmount)).getText().toString();
		float amount = 0f;
		try {
			amount = Float.parseFloat(inputAmount);	
		} catch (NumberFormatException e) {
			Toast.makeText(this, "Please enter an amount!", Toast.LENGTH_SHORT).show();
			return;
		}
		
		
		String name = ((TextView) findViewById(R.id.autoEtName)).getText().toString();
		if (name == null || name.equals("")) {
			Toast.makeText(this, "Please enter a name!", Toast.LENGTH_SHORT).show();
			return;
		}
		
		String note = ((TextView) findViewById(R.id.etNote)).getText().toString();
		
		boolean result = dbHelper.addEntry(type, amount, name, note);
		
		String text = (result) ? "Entry added successfully" : "Failed to add entry" ;
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
		setResult(RESULT_OK);
		finish();
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btAdd:
			create();
			break;
		}
	}

	@Override
	public Cursor runQuery(CharSequence constraint) {
		return (constraint == null)
			? null
			: dbHelper.getMatchingNames(constraint.toString());
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		TextView tv = (TextView) view.findViewById(android.R.id.text1);
		auto.setText(tv.getText());
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		int id = 0;
		switch (checkedId) {
		case R.id.radioGet:
			id = R.string.from_hint;
			break;
		case R.id.radioPay:
			id = R.string.to_hint;
			break;
		}
		auto.setHint(id);
	}
}
