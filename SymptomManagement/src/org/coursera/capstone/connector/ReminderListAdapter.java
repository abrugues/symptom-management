package org.coursera.capstone.connector;

import java.util.ArrayList;
import java.util.List;

import org.coursera.capstone.PatientReminderActivity;
import org.coursera.capstone.R;
import org.coursera.capstone.model.Reminder;
import org.coursera.capstone.process.ReminderSchedulerReceiver;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class ReminderListAdapter extends BaseAdapter {
	
	private List<Reminder> mItems = new ArrayList<Reminder>();
		
	private final Context mContext;
		
	public ReminderListAdapter(Context context) {
		this.mContext = context;
	}
	
	public ReminderListAdapter(Context context, List<Reminder> items) {
		this.mContext = context;
		this.mItems = items;
	}
	
	// Add a TimeItem to the adapter
	public void addItem(Reminder item) {
		mItems.add(item);
		notifyDataSetChanged();
	}
	
	public void addAllItems(List<Reminder> items) {
		mItems = items;
		notifyDataSetChanged();
	}
	
	public boolean containsItem(Reminder item) {
		if (mItems.contains(item)) {
			return true;
		}
		return false;
	}
	
	public Reminder[] getValues() {
		Reminder[] items = new Reminder[mItems.size()];
		for (int i = 0; i < mItems.size(); i++) {
			items[i] = mItems.get(i);
		}
		return items;
	}
	
	private void deleteItem(Reminder item) {
		this.mItems.remove(item);
		notifyDataSetChanged();
	}
	
	// Method for testing purposes
	public void flush() {
		this.mItems.clear();
		notifyDataSetChanged();
	}
	
	// Returns the number of AlarmItems
	@Override
	public int getCount() {
		return mItems.size();
	}

	// Retrieve a particular AlarmItem
	@Override
	public Object getItem(int pos) {
		return mItems.get(pos);
	}

	@Override
	public long getItemId(int pos) {
		return pos;
	}

	// Create a View to display the AlarmItem at specified position
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.item_reminder, parent, false);
		}
		
		// Get the current AlarmItem 
		final Reminder reminder = (Reminder) getItem(position);
		
		 // Format the layout with the Alarm's info
		final TextView timeTextView = (TextView) convertView.findViewById(R.id.timeTextView);
		timeTextView.setText(reminder.toString());
		
		final CheckBox activeChkBx = (CheckBox) convertView.findViewById(R.id.activeCheckBox);
		activeChkBx.setChecked(reminder.isActive());
		
		activeChkBx.setOnCheckedChangeListener(new OnCheckedChangeListener() {			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					reminder.setActive(true);
					// Notify the change to the BroadcastReceiver
					Intent i = new Intent((PatientReminderActivity) mContext, ReminderSchedulerReceiver.class);
					i.setAction(ReminderSchedulerReceiver.REMINDER_STATUS_ENABLED);
					i.putExtra(ReminderSchedulerReceiver.EXTRA, reminder);
					((PatientReminderActivity)mContext).sendBroadcast(i);
					
				} else {
					if (countActiveReminders() > 4) {
						reminder.setActive(false);
						// Notify the change to the BroadcastReceiver 
						Intent i = new Intent((PatientReminderActivity) mContext, ReminderSchedulerReceiver.class);
						i.setAction(ReminderSchedulerReceiver.REMINDER_STATUS_DISABLED);
						i.putExtra(ReminderSchedulerReceiver.EXTRA, reminder);
						((PatientReminderActivity) mContext).sendBroadcast(i);
					} else {
						activeChkBx.setChecked(true);
						showToast();
					}
				}
			}
		});
		
		convertView.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View view) {
				showDialog(reminder);
				return true;
			}
		});
		
		return convertView;
	}
	
	private int countActiveReminders() {
		int count = 0;
		for (Reminder item : mItems) {
			if (item.isActive()){
				count++;
			}
		}
		return count;
	}
	
	private void showDialog(final Reminder reminder) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				mContext);
		
		alertDialogBuilder
			.setTitle("Alarm at " + reminder.toString())
			.setItems(R.array.alarm_dialog, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if (mItems.size() <= 4) {
						showToast();
					} else if ((mItems.size() > 4) && (countActiveReminders() == 4)
							&& (reminder.isActive())) {
						showToast();			
					} else {
						deleteItem(reminder);
						((PatientReminderActivity) mContext).deleteReminder(reminder);
					}
				}
			});
		
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}
	
	private void showToast() {
		Toast.makeText(mContext, 
				"At least 4 reminders must be active", 
				Toast.LENGTH_LONG)
				.show();
	}
}