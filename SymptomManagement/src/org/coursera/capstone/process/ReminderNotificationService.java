package org.coursera.capstone.process;

import org.coursera.capstone.PatientCheckInActivity;
import org.coursera.capstone.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.IBinder;

/**
 * This service is called by the ReminderReceiver at the specified time
 * to create a Notification that is shown in the notification area 
 * 
 * @author ?
 *
 */
public class ReminderNotificationService extends Service {

	private static final int NOTIFICATION_ID = 1;
	
	// Notification Text Elements
	private final CharSequence tickerText = "Reminder to check-in your symptoms";
	private final CharSequence contentTitle = "Check-in reminder";
	private final CharSequence contentText = "Remember to check-in your symptoms";
	
	private final long[] mVibratePattern = { 0, 200, 200, 300 };
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		Intent i = new Intent(getApplicationContext(), PatientCheckInActivity.class);
		PendingIntent pi = PendingIntent.getActivity(
				getApplicationContext(),
				NOTIFICATION_ID,
				i,
				PendingIntent.FLAG_ONE_SHOT);
		
		Notification.Builder notificationBuilder = new Notification.Builder(getApplicationContext())
				.setContentIntent(pi)
				.setTicker(tickerText)
				.setSmallIcon(R.drawable.appointment_reminders)
				.setAutoCancel(true)
				.setContentTitle(contentTitle)
				.setContentText(contentText)
				.setVibrate(mVibratePattern)
				.setLights(Color.BLUE, 100, 100);

		// Get the NotificationManager
		NotificationManager mNotificationManager = (NotificationManager) getApplicationContext()
				.getSystemService(Context.NOTIFICATION_SERVICE);

		// Pass the Notification to the NotificationManager:
		mNotificationManager.notify(NOTIFICATION_ID,
				notificationBuilder.build());
		
		return START_NOT_STICKY;
	}

}
