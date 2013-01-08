package com.suresh.whereismycash;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainAdapter extends CursorAdapter implements OnClickListener {
	
	private DbHelper dbHelper;
	private OnClickListener parentActivity;

	public MainAdapter(Context context, Cursor c, int flags, DbHelper dbHelper) {
		super(context, c, flags);
		parentActivity = (OnClickListener) context;
		this.dbHelper = dbHelper;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		view.findViewById(R.id.btDelete).setOnClickListener(this);
		view.setOnClickListener(parentActivity);
		float amount = cursor.getFloat(cursor.getColumnIndex(DbHelper.KEY_AMOUNT));
		amount = (float) (Math.round(amount*100.0)/100.0);
		String name = cursor.getString(cursor.getColumnIndex(DbHelper.KEY_NAME));
		
		TextView tvName = (TextView) view.findViewById(R.id.tvName);
		tvName.setText(name);
		
		TextView tvAmount = (TextView) view.findViewById(R.id.tvAmount);
		DbHelper.setTextandColor(context, tvAmount, amount);
		
		view.findViewById(R.id.btDelete).setTag(name);
		//int id = cursor.getInt(cursor.getColumnIndex(DbHelper.KEY_ID));
		view.setTag(name);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.main_list_item, null);
		bindView(v, context, cursor);
		return v;
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btDelete:
			displayDialog(v);
			break;
		}
	}
	
	public void displayDialog(final View v) {
		View parent = (View) v.getParent();
		TextView tvName = (TextView) parent.findViewById(R.id.tvName);
		AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
		builder.setMessage("Delete entry for " + tvName.getText() + "?");
		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dbHelper.delete((String)v.getTag());
				swapCursor(dbHelper.getAllLoans());
				updateParentTotal(v);
			}
		});
		builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {}
		});
		builder.show();
	}
	
	public void updateParentTotal(View v) {
		View grandParent = (View) v.getParent().getParent().getParent();
		
		float netSum = dbHelper.getNetSum();
		TextView tvNetAmount = (TextView) grandParent.findViewById(R.id.tvNetAmount);
		DbHelper.setTextandColor(grandParent.getContext(), tvNetAmount, netSum);
	}

}
