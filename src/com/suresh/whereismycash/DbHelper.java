package com.suresh.whereismycash;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbHelper extends SQLiteOpenHelper {
	
	private static final String TAG = "DbHelper";
	
    /**
     * Database values and creation statement
     */
    private static final String DATABASE_NAME = "data";
    private static final String DATABASE_TABLE = "loans";
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_CREATE =
	    "create table " + DATABASE_TABLE + " (_id integer primary key autoincrement, "
	    + "name varchar(100) not null, amount float not null, paid float not null default 0, reason text, " +
	    "receive tinyint(1) not null, created_at timestamp not null default current_timestamp);";
    
    /**
     * SQL Column Keys
     */
    public static final String KEY_ID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_AMOUNT = "amount";
    public static final String KEY_PAID = "paid";
    public static final String KEY_RECEIVE = "receive";
    public static final String KEY_REASON = "reason";
    public static final String KEY_CREATED_AT = "created_at";

	public DbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		initDummyData();
	}
	
	private void initDummyData() {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues val = new ContentValues();
		val.put(KEY_NAME, "Suresh");
		val.put(KEY_AMOUNT, 100);
		val.put(KEY_RECEIVE, true);
		db.insert(DATABASE_TABLE, null, val);
		ContentValues val2 = new ContentValues();
		val2.put(KEY_NAME, "Akshay");
		val2.put(KEY_AMOUNT, 200);
		val2.put(KEY_RECEIVE, true);
		db.insert(DATABASE_TABLE, null, val2);
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
	
	public Cursor getAllLoans(boolean receive) {
		int check = receive ? 1 : 0;
		SQLiteDatabase db = getWritableDatabase();
		String[] columns = {KEY_ID, KEY_NAME, "SUM(" + KEY_AMOUNT + ")", KEY_PAID};
		Cursor c = db.query(DATABASE_TABLE, columns, KEY_RECEIVE + " = " + check, null, KEY_NAME, null, KEY_CREATED_AT + " desc");
		return c;
	}

}
