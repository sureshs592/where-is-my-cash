package com.suresh.whereismycash;

import java.util.HashMap;
import java.util.LinkedList;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class NewEditAdapter extends BaseAdapter {
	
	public static final int TYPE_ITEM = 0;
    public static final int TYPE_SEPARATOR = 1;
    private static final int TYPE_MAX_COUNT = TYPE_SEPARATOR + 1;
	
	private LinkedList<HashMap<String, Object>> items;
	private String name;
	private DbHelper dbHelper;
	
	public NewEditAdapter(LinkedList<HashMap<String, Object>> loans, String name, DbHelper dbHelper) {
		this.items = loans;
		this.name = name;
		this.dbHelper = dbHelper;
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		return items.get(position);
	}
	
	@Override
	public int getItemViewType(int position) {
		return (Integer) items.get(position).get("viewType");
	}
	
	@Override
	public int getViewTypeCount() {
		return TYPE_MAX_COUNT;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		return convertView;
	}

}