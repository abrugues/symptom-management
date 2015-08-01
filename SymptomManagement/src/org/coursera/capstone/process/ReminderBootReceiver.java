package org.coursera.capstone.process;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ReminderBootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
			// Restart the alarms
		}
	}
}
