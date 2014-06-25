package com.app.NAMESPACE.base;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public abstract class BaseSqlite {

	private static final String DB_NAME = "demos.db";
	private static final int DB_VERSION = 1;
	
	protected Context ctx = null;
	protected DbHelper dbh = null;
	
	public BaseSqlite(Context context) {
		dbh = new DbHelper(context, DB_NAME, null, DB_VERSION);
	}

	public void create (ContentValues values) {
		SQLiteDatabase db = null;
		try {
			db = dbh.getWritableDatabase();
			db.insert(tableName(), null, values);
		} catch (Exception e) {
			
		}
	}
	
	public void update (ContentValues values, String where, String[] params) {
		SQLiteDatabase db = null;
		try {
			db = dbh.getWritableDatabase();
			db.update(tableName(), values, where, params);
		} catch (Exception e) {
			
		}
	}
	
	public void delete (String where, String[] params) {
		SQLiteDatabase db = null;
		try {
			db = dbh.getWritableDatabase();
			db.delete(tableName(), where, params);
		} catch (Exception e) {
			
		}
	}
	
	public Cursor query (String[] columns, String where, String[] params) {
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			db = dbh.getReadableDatabase();
			cursor = db.query(tableName(), columns, where, params, null, null, null);
		} catch (Exception e) {
			
		}
		return cursor;
	}
	
	public Cursor query (String where, String[] params) {
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			db = dbh.getReadableDatabase();
			cursor = db.query(tableName(), tableColumns(), where, params, null, null, null);
		} catch (Exception e) {
			
		}
		return cursor;
	}
	
	public boolean exists (String where, String[] params) {
		boolean result = false;
		Cursor cursor = null;
		try {
			cursor = this.query(where, params);
			result = cursor.moveToFirst();
		} catch (Exception e) {
			result = false;
		} finally {
			cursor.close();
		}
		return result;
	}
	
	abstract protected String tableName ();
	abstract protected String[] tableColumns ();
	abstract protected String createSql ();
	abstract protected String upgradeSql ();
	
	protected class DbHelper extends SQLiteOpenHelper {

		public DbHelper(Context context, String name, CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(createSql());
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL(upgradeSql());
			onCreate(db);
		}
	}
}