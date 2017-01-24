package org.coursera.capstone.connector;

import java.util.ArrayList;
import java.util.List;

import org.coursera.capstone.db.DBHelper;
import org.coursera.capstone.model.Reminder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class ReminderDAO {

	private SQLiteDatabase database;
	private DBHelper dbHelper;
	
	public ReminderDAO(Context context) {
		dbHelper = new DBHelper(context);
	}
	
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}
	
	public void close() {
		dbHelper.close();
	}
	
	public void createReminder(Reminder reminder) {
		ContentValues values = new ContentValues();
		values.put(DBHelper.COLUMN_HOUR, reminder.getHour());
		values.put(DBHelper.COLUMN_MINUTE, reminder.getMinute());
		values.put(DBHelper.COLUMN_ACTIVE, (reminder.isActive()) ? 1 : 0);
		long insertId = database.insert(DBHelper.TABLE_REMINDER,
				null, values);
		Cursor cursor = database.query(DBHelper.TABLE_REMINDER,
				null, DBHelper.COLUMN_ID + " = " + insertId, 
				null, null, null, null);
		cursor.moveToFirst();
		cursor.close();
		reminder.setId(insertId);
	}
	
	public void updateReminderStatus(Reminder reminder) {
		ContentValues values = new ContentValues();
		values.put(DBHelper.COLUMN_ACTIVE, (reminder.isActive()) ? 1 : 0);
		database.update(DBHelper.TABLE_REMINDER,
				values, 
				DBHelper.COLUMN_ID + " = " + reminder.getId(),
				null);
	}
	
	public void deleteReminder(Reminder reminder) {
		database.delete(DBHelper.TABLE_REMINDER,
				DBHelper.COLUMN_ID + " = " + reminder.getId(),
				null);
	}
	
	public List<Reminder> getAllReminders() {
		List<Reminder> items = new ArrayList<Reminder>();
		
		Cursor cursor = database.query(DBHelper.TABLE_REMINDER,
				null, null, null, null, null, null);
		cursor.moveToFirst();
		
		while (!cursor.isAfterLast()) {
			Reminder reminder = cursorToReminder(cursor);
			items.add(reminder);
			cursor.moveToNext();
		}
		cursor.close();
		return items;
	}
	
	private Reminder cursorToReminder(Cursor cursor) {
		Reminder alarm = new Reminder();
		alarm.setId(cursor.getLong(0));
		alarm.setHour(cursor.getInt(1));
		alarm.setMinute(cursor.getInt(2));
		alarm.setActive((cursor.getInt(3) == 1) ? true : false);
		return alarm;
	}
	
}
