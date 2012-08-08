package com.suresh.whereismycash;

import java.util.Calendar;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.FilterQueryProvider;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.suresh.whereismycash.DbHelper.PaymentType;

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
	
	public void create() {
		RadioGroup radioType = (RadioGroup) findViewById(R.id.radioGroupType);
		PaymentType type = (radioType.getCheckedRadioButtonId() == R.id.radioGet)
				? DbHelper.PaymentType.GET : DbHelper.PaymentType.GET;
		
		String inputAmount = ((TextView) findViewById(R.id.etAmount)).getText().toString();
		float amount = Float.parseFloat(inputAmount);
		
		String name = ((TextView) findViewById(R.id.autoEtName)).getText().toString();
		String note = ((TextView) findViewById(R.id.etNote)).getText().toString();
		
		DatePicker picker = (DatePicker) findViewById(R.id.datePicker);
		Calendar date = Calendar.getInstance();
		date.set(picker.getYear(), picker.getMonth(), picker.getDayOfMonth());
		
		boolean result = dbHelper.addEntry(type, amount, name, note, date);
		
		String text = (result) ? "Entry added successfully" : "Failed to add entry" ;
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}

	@Override
	public Cursor runQuery(CharSequence constraint) {
		return (constraint == null)
			? null
			: dbHelper.getMatchingNames(constraint.toString());
	}
}
