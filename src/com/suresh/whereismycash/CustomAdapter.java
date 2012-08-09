package com.suresh.whereismycash;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CustomAdapter extends CursorAdapter {


	public CustomAdapter(Context context, Cursor c, int flags) {
		super(context, c, flags);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		float amount = cursor.getFloat(cursor.getColumnIndex(DbHelper.KEY_AMOUNT));
		int color = 0;
		if (amount < 0) {
			amount *= -1;
			color = R.color.amount_green;
		} else if (amount == 0) {
			color = R.color.amount_blue;
		} else if (amount > 0) {
			color = R.color.amount_red;
		}
		String name = cursor.getString(cursor.getColumnIndex(DbHelper.KEY_NAME));
		
		TextView tvName = (TextView) view.findViewById(R.id.tvName);
		tvName.setText(name);
		
		TextView tvAmount = (TextView) view.findViewById(R.id.tvAmount);
		tvAmount.setText(String.valueOf(amount));
		tvAmount.setTextColor(context.getResources().getColor(color));
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.main_list_item, null);
		bindView(v, context, cursor);
		return v;
	}

}
