package com.suresh.whereismycash;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class EditAdapter extends CursorAdapter implements OnClickListener {
	
	private DbHelper dbHelper;
	private String name;

	public EditAdapter(String name, Context context, Cursor c, int flags, DbHelper dbHelper) {
		super(context, c, flags);
		this.name = name;
		this.dbHelper = dbHelper;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		view.findViewById(R.id.btDelete).setOnClickListener(this);
		float amount = cursor.getFloat(cursor.getColumnIndex(DbHelper.KEY_AMOUNT));
		amount = (float) (Math.round(amount*100.0)/100.0);
		TextView tvAmount = (TextView) view.findViewById(R.id.tvAmount);
		DbHelper.setTextandColor(context, tvAmount, amount);
		
		String note = cursor.getString(cursor.getColumnIndex(DbHelper.KEY_NOTE));
		TextView tvNote = (TextView) view.findViewById(R.id.tvNote);
		if (note != null) {
			tvNote.setVisibility(View.VISIBLE);
			tvNote.setText(note);
		} else {
			tvNote.setVisibility(View.GONE);
		}
		
		//Setting tags
		int id = cursor.getInt(cursor.getColumnIndex(DbHelper.KEY_ID));
		view.setTag(id);
		view.findViewById(R.id.btDelete).setTag(id);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.edit_list_item, null);
		bindView(v, context, cursor);
		return v;
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btDelete:
			int tag = (Integer)v.getTag();
			dbHelper.delete(tag);
			swapCursor(dbHelper.getLoansByName(name));
			updateParentTotal(v);
			break;
		}
	}
	
	public void updateParentTotal(View v) {
		View grandParent = (View) v.getParent().getParent().getParent();
		TextView tvTotal = (TextView) grandParent.findViewById(R.id.tvTotal);
		float amount = dbHelper.getLoanAmountByName(name);
		DbHelper.setTextandColor(v.getContext(), tvTotal, amount);
	}
}
