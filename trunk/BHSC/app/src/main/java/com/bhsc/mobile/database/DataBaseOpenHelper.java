
package com.bhsc.mobile.database;


import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * @author zhaoyongguang 
 * @version 4
 */
public class DataBaseOpenHelper extends SQLiteOpenHelper {

	private static final String TAG = "DataBaseOpenHelper";
	private Context mContext;
	private static DataBaseOpenHelper dbT;
	private static SQLiteDatabase myDatabase;
	
	public static SQLiteDatabase getInstance(Context context) {
		if (dbT == null) {
			synchronized (DataBaseOpenHelper.class) {
				if (dbT == null) {
					dbT = new DataBaseOpenHelper(context);
				}
			}
		}
		if (myDatabase == null) {
			synchronized (DataBaseOpenHelper.class) {
				if (myDatabase == null && dbT != null) {
					myDatabase = dbT.getWritableDatabase();
				}
			}
		}
		return myDatabase;
	}

	private DataBaseOpenHelper(Context context) {
		super(context, Constants_DB.DB_NAME, null, Constants_DB.DB_VERSION);
		this.mContext = context;
		// myDatabase = getInstance(context);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d(TAG, "DataBaseOpenHelper onCreate()");
		this.tableCreat(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.d(TAG, "onUpgrade() oldVersion:" + oldVersion + " newVersion:" + newVersion);
		if(dropTable(db)) {
			tableCreat(db);
		}
	}

	public Boolean dropTable(SQLiteDatabase db) {

		Boolean isResult = false;

		Cursor cursor = db.rawQuery("select name from sqlite_master where type='table' order by name", null);
		while (cursor.moveToNext()) {
			// 遍历出表名
			String name = cursor.getString(0);
			Log.i("TAG", "dropTable表名称 = " + name);
			String sql = "DROP TABLE  " + name;

			try {
				db.execSQL(sql);
				isResult = true;
			} catch (SQLException e) {
				e.printStackTrace();
				isResult = false;
			} finally {

			}
		}
		return isResult;
	}


	/**
	 * @param db
	 */
	public void tableCreat(SQLiteDatabase db) {
		Log.d(TAG, "tableCreat()  start");

		createUserTable(db);
		createDiscloseTable(db);

		Log.d(TAG, "tableCreat()  end");
	}



	private void createUserTable(SQLiteDatabase db) {
		String sql = "CREATE TABLE IF NOT EXISTS "
				+ Constants_DB.TABLE_USER
				+ " ("
				+ Constants_DB.USER_PHOTOPATH + " varchar(128),"
				+ Constants_DB.USER_STATUS + " varchar(256),"
				+ Constants_DB.USER_NICKNAME + " varchar(32),"
				+ Constants_DB.USER_LASTCHANGETIME + " long(10,0),"
				+ Constants_DB.USER_PASSWORD + " varchar(64),"
				+ Constants_DB.USER_USERNAME + " varchar(64)"
				+ ");";
		try {
			db.beginTransaction();
			db.execSQL(sql);
			db.setTransactionSuccessful();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}
		Log.d(TAG, "1,tableCreat()  execSql: " + Constants_DB.TABLE_USER);
	}

	private void createDiscloseTable(SQLiteDatabase db) {
		String sql = "CREATE TABLE IF NOT EXISTS "
				+ Constants_DB.TABLE_DISCLOSE
				+ " ("
				+ Constants_DB.DISCLOSE_TITLE + " varchar(32),"
				+ Constants_DB.DISCLOSE_USERNAME + " varchar(32),"
				+ Constants_DB.DISCLOSE_CONTENT + " varchar(256),"
				+ Constants_DB.DISCLOSE_IMAGEPATHS + " varchar(512),"
				+ Constants_DB.DISCLOSE_DATAID + " varchar(256),"
				+ Constants_DB.DISCLOSE_CREATETIME + " long(10,0)"
				+ ");";
		try {
			db.beginTransaction();
			db.execSQL(sql);
			db.setTransactionSuccessful();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}
		Log.d(TAG, "1,tableCreat()  execSql: " + Constants_DB.TABLE_DISCLOSE);
	}
}
