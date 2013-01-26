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
import android.widget.Button;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.suresh.whereismycash.DbHelper.PaymentType;
import com.suresh.whereismycash.R.string;

public class CreateActivity extends SherlockActivity implements
FilterQueryProvider, OnClickListener, OnItemClickListener, OnCheckedChangeListener {
	private DbHelper dbHelper;
	private int entryId = 0;
	private AutoCompleteTextView auto;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create);
		dbHelper = new DbHelper(this);
		auto = (AutoCompleteTextView) findViewById(R.id.autoEtName);
		Intent i = getIntent();
		String name = i.getStringExtra("name");
		if (name == null) {
			setTitle(getResources().getString(R.string.btn_add));
			SimpleCursorAdapter adapter = new SimpleCursorAdapter(
					this, android.R.layout.simple_list_item_1, null,
					new String[]{DbHelper.KEY_NAME}, new int[]{android.R.id.text1},
					CursorAdapter.FLAG_AUTO_REQUERY);
			adapter.setFilterQueryProvider(this);
			auto.setAdapter(adapter);
			auto.setOnItemClickListener(this);
			auto.setEnabled(true);
		} else {
			if (i.hasExtra("id")) {
				entryId = i.getIntExtra("id", 0);
				setTitle(getResources().getString(R.string.title_edit) + " " + name);
				
				String paymentType = i.getStringExtra("paymentType");
				RadioGroup radioType = (RadioGroup) findViewById(R.id.radioGroupType);
				int checkedId = (paymentType.equals(PaymentType.GET.name()))
						? R.id.radioGet : R.id.radioPay ;
				radioType.check(checkedId);
				
				String amount = i.getStringExtra("amount");
				EditText etAmount = (EditText) findViewById(R.id.etAmount);
				etAmount.setText(amount);
				
				String note = i.getStringExtra("note");
				EditText etNote = (EditText) findViewById(R.id.etNote);
				etNote.setText(note);
				
				Button btAction = (Button) findViewById(R.id.btAction);
				btAction.setText(getResources().getString(R.string.btn_update));
			} else {
				setTitle(getResources().getString(R.string.title_add_person) + " " + name);
			}
			auto.setText(name);
			auto.setEnabled(false);
		}
		findViewById(R.id.btAction).setOnClickListener(this);
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
			Toast.makeText(this, getResources().getString(R.string.error_amount), Toast.LENGTH_SHORT).show();
			return;
		}
		
		
		String name = ((TextView) findViewById(R.id.autoEtName)).getText().toString();
		if (name == null || name.equals("")) {
			Toast.makeText(this, getResources().getString(R.string.error_name), Toast.LENGTH_SHORT).show();
			return;
		}
		
		String note = ((TextView) findViewById(R.id.etNote)).getText().toString();
		
		boolean result = dbHelper.addEntry(type, amount, name, note);
		
		String text = (result) ? getResources().getString(R.string.notify_success)
				: getResources().getString(R.string.notify_fail) ;
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
		setResult(RESULT_OK);
		finish();
	}
	
	public void update() {
		RadioGroup radioType = (RadioGroup) findViewById(R.id.radioGroupType);
		int checked = radioType.getCheckedRadioButtonId();
		PaymentType type = (checked == R.id.radioGet)
				? DbHelper.PaymentType.GET : DbHelper.PaymentType.PAY;
		
		String inputAmount = ((TextView) findViewById(R.id.etAmount)).getText().toString();
		float amount = 0f;
		try {
			amount = Float.parseFloat(inputAmount);	
		} catch (NumberFormatException e) {
			Toast.makeText(this, getResources().getString(R.string.error_amount), Toast.LENGTH_SHORT).show();
			return;
		}
		
		String note = ((TextView) findViewById(R.id.etNote)).getText().toString();
		
		boolean result = dbHelper.updateEntry(entryId, type, amount, note);
		
		String text = (result) ? getResources().getString(R.string.notify_success)
				: getResources().getString(R.string.notify_fail);
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
		setResult(RESULT_OK);
		finish();
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btAction:
			String text = (String) ((Button)v).getText();
			if (text.equals(getResources().getString(R.string.btn_add))) {
				create();	
			} else {
				update();
			}
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
