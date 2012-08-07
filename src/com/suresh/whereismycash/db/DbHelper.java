package com.suresh.whereismycash.db;

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
	    + "name varchar(100) not null, amount float not null, paid float not null, reason text, " +
	    "receive boolean not null, created_at timestamp not null default current_timestamp);";
    
    /**
     * SQL Column Keys
     */
    private static final String KEY_ID = "_id";
    private static final String KEY_NAME = "name";
    private static final String KEY_AMOUNT = "amount";
    private static final String KEY_PAID = "paid";
    private static final String KEY_RECEIVE = "receive";
    private static final String KEY_REASON = "reason";
    private static final String KEY_CREATED_AT = "created_at";

	public DbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
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
		SQLiteDatabase db = getWritableDatabase();
		String[] columns = {KEY_ID, KEY_NAME, KEY_AMOUNT, KEY_PAID};
		Cursor c = db.query(DATABASE_TABLE, columns, KEY_RECEIVE + " = " + receive, null, null, null, KEY_CREATED_AT + " desc");
		return c;
	}

}
