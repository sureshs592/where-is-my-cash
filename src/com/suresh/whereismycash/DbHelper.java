package com.suresh.whereismycash;

import java.sql.Timestamp;
import java.util.Calendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbHelper extends SQLiteOpenHelper {
	
	private static final String TAG = "DbHelper";
	
	public enum PaymentType { GET, PAY }
	
    /**
     * Database values and creation statement
     */
    private static final String DATABASE_NAME = "data";
    private static final String DATABASE_TABLE = "loans";
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_CREATE =
	    "create table loans (_id integer primary key autoincrement, "
	    + "name varchar(100) not null, amount float not null, note text)";
    
    /**
     * SQL Column Keys
     */
    public static final String KEY_ID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_AMOUNT = "amount";
    public static final String KEY_NOTE = "note";

	public DbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
//		initDummyData();
	}
	
	private void initDummyData() {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues val = new ContentValues();
		val.put(KEY_NAME, "Subbu");
		val.put(KEY_AMOUNT, -50);
		db.insert(DATABASE_TABLE, null, val);
		ContentValues val2 = new ContentValues();
		val2.put(KEY_NAME, "Raji");
		val2.put(KEY_AMOUNT, 150);
		db.insert(DATABASE_TABLE, null, val2);
		db.close();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
        onCreate(db);
	}
	
	public Cursor getAllLoans() {
		SQLiteDatabase db = getWritableDatabase();
		String[] columns = {KEY_ID, KEY_NAME, "SUM(" + KEY_AMOUNT + ") as amount"};
		Cursor c = db.query(DATABASE_TABLE, columns, null, null, KEY_NAME, null, null);
		return c;
	}
	
	public Cursor getMatchingNames(String input) {
		SQLiteDatabase db = getWritableDatabase();
		String query = "SELECT " + KEY_NAME + ", _id FROM " + DATABASE_TABLE +
				" WHERE " + KEY_NAME + " LIKE ? GROUP BY " + KEY_NAME;
		Cursor c = db.rawQuery(query, new String[]{input + "%"});
		return c;
	}
	
	public boolean addEntry(PaymentType type, float amount,
			String name, String note) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues val = new ContentValues();
		float storedAmount = (float) 0.0;
		switch(type) {
		case GET:
			storedAmount = -1 * amount;
			break;
		case PAY:
			storedAmount = amount;
			break;
		}
		val.put(KEY_AMOUNT, storedAmount);
		
		val.put(KEY_NAME, name);
		if (note != null && !note.isEmpty()) val.put(KEY_NOTE, note);
		
		db.insert(DATABASE_TABLE, null, val);
		db.close();
		return true;
	}
	
	public void delete(String name) {
		SQLiteDatabase db = getWritableDatabase();
		db.delete(DATABASE_TABLE, KEY_NAME + " = ?", new String[]{name});
	}

}
