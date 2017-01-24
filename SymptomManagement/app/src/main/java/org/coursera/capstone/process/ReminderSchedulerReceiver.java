package org.coursera.capstone.process;

import org.coursera.capstone.model.Reminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * This BroadcastReceiver is responsible for scheduling the alarms, 
 * according to what the user does in the GUI. Through the AlarmManager
 * it calls the ReminderService to show the Notification to the user.
 * 
 * @author ?
 *
 */
public class ReminderSchedulerReceiver extends BroadcastReceiver {

	public final String TAG = getClass().getName();
	
	public static final String EXTRA = "reminder";
	
	public static final String REMINDER_NEW = "org.coursera.capstone.broadcast.REMINDER_NEW";
	public static final String REMINDER_DELETE = "org.coursera.capstone.broadcast.REMINDER_DELETE";
	public static final String REMINDER_STATUS_ENABLED = "org.coursera.capstone.broadcast.REMINDER_STATUS_ENABLED";
	public static final String REMINDER_STATUS_DISABLED = "org.coursera.capstone.broadcast.REMINDER_STATUS_DISABLED";
		
	@Override
	public void onReceive(Context context, Intent intent) {
		// Process the intent received
		String action = intent.getAction();
		Reminder r = (Reminder) intent.getParcelableExtra(EXTRA);
	   
		Log.i(TAG, "Intent's action: " + action);
		Toast.makeText(context, "Intent's action: " + action, Toast.LENGTH_LONG).show();
		
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		
		// Intent that will be encapsulated by the PendingIntent
		Intent i = new Intent(context, ReminderNotificationService.class);
		
		if ((action.equals(REMINDER_NEW)) || (action.equals(REMINDER_STATUS_ENABLED))) {
			// Create the PendingIntent for the AlarmManager
			PendingIntent alarmIntent = PendingIntent.getService(
					context,
					(int) r.getId(),
					i,
					PendingIntent.FLAG_UPDATE_CURRENT);
			
			// Set a new alarm with a repetition interval of one day
			alarmManager.setRepeating(
					AlarmManager.RTC_WAKEUP,
					r.getTimeNextAlarm(),
					AlarmManager.INTERVAL_DAY,
					alarmIntent);
			
		} else if (action.equals(REMINDER_DELETE)) {			
			PendingIntent cancelIntent = PendingIntent.getService(
					context,
					(int) r.getId(),
					i,
					PendingIntent.FLAG_CANCEL_CURRENT);
			
			alarmManager.cancel(cancelIntent);
			
		} else if (action.equals(REMINDER_STATUS_DISABLED)) {
			PendingIntent disableIntent = PendingIntent.getService(
					context,
					(int) r.getId(),
					i,
					PendingIntent.FLAG_UPDATE_CURRENT);
			
			alarmManager.cancel(disableIntent);
		}
	}	
}