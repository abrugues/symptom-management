package org.coursera.capstone.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	
	public static final String TABLE_REMINDER = "reminders";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_HOUR = "hour";
	public static final String COLUMN_MINUTE = "minute";
	public static final String COLUMN_ACTIVE = "active";

	public static final String TABLE_MEDICATION = "medications";
	public static final String COLUMN_DRUG_NAME = "name";
	
	private static final String DATABASE_NAME = "symptoms.db";
	private static final int DATABASE_VERSION = 1;
	
	// SQL statement to create the table for the alarms
	private final static String CREATE_TABLE_ALARMS = 
			"CREATE TABLE " + TABLE_REMINDER + "(" 
			+ COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ COLUMN_HOUR + " INTEGER NOT NULL, "
			+ COLUMN_MINUTE + " INTEGER NOT NULL, "
			+ COLUMN_ACTIVE + " INTEGER NOT NULL);";
	
	private final static String CREATE_TABLE_MEDICATIONS = 
			"CREATE TABLE " + TABLE_MEDICATION + "("
			+ COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ COLUMN_DRUG_NAME + " TEXT NOT NULL);";
	
	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(CREATE_TABLE_ALARMS);
		database.execSQL(CREATE_TABLE_MEDICATIONS);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_REMINDER);
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_MEDICATION);
		onCreate(database);
	}
	
	
}
