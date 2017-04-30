package org.coursera.capstone.connector;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.coursera.capstone.db.DBHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class MedicationDAO {
	
	private SQLiteDatabase database;
	private DBHelper dbHelper;
	
	public MedicationDAO(Context context) {
		dbHelper = new DBHelper(context);
	}
	
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}
	
	public void close() {
		dbHelper.close();
	}
	
	public void updateMedication(Set<String> medications) {
		
		database.execSQL("DELETE FROM " + DBHelper.TABLE_MEDICATION);
		
		if (!medications.isEmpty()) {
			ContentValues values = new ContentValues();
			long insertId;
			Cursor cursor = null;
			
			for (String medication : medications) {
				values.put(DBHelper.COLUMN_DRUG_NAME, medication);
				insertId = database.insert(DBHelper.TABLE_MEDICATION,
						null, values);
				cursor = database.query(DBHelper.TABLE_MEDICATION,
						null, DBHelper.COLUMN_ID + " = " + insertId, 
						null, null, null, null);
				cursor.moveToFirst();
			}
			
			cursor.close();
		}
	}
	
	public List<String> getAllMedications() {
		List<String> medications = new ArrayList<String>();
		
		Cursor cursor = database.query(DBHelper.TABLE_MEDICATION,
				null, null, null, null, null, null);
		cursor.moveToFirst();
		
		while (!cursor.isAfterLast()) {
			medications.add(cursor.getString(1));
			cursor.moveToNext();
		}
		cursor.close();
		return medications;
	}
}
