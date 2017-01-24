package org.coursera.capstone.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.coursera.capstone.R;
import org.coursera.capstone.connector.ReminderDAO;
import org.coursera.capstone.connector.ReminderListAdapter;
import org.coursera.capstone.model.Reminder;
import org.coursera.capstone.process.ReminderSchedulerReceiver;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ListActivity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


public class PatientReminderActivity extends ListActivity {
		
	private ReminderListAdapter mAdapter;
	
	private ReminderDAO mReminderDAO;
		
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        if (savedInstanceState !=null) {
        	Reminder[] reminders = (Reminder[]) savedInstanceState.getParcelableArray("items");
        	List<Reminder> items = new ArrayList<Reminder>();
        	for (int i = 0; i < reminders.length; i++) {
        		items.add(reminders[i]);
        	}
        	mAdapter = new ReminderListAdapter(PatientReminderActivity.this, items);
        } else {
        	// Create the Adapter for this AlarmActivity's ListView
            mAdapter = new ReminderListAdapter(PatientReminderActivity.this);
        }
        
        // Create the DAO for the Reminders
        mReminderDAO = new ReminderDAO(this);
        
        // Put a divider between HeaderView and Alarms 
        getListView().setHeaderDividersEnabled(true);

        // Inflate headerView for header_view.xml file
        TextView headerView = (TextView) LayoutInflater
        		.from(PatientReminderActivity.this)
        		.inflate(R.layout.header_view_reminder, getListView(), false);

        // Add headerView to the ListView 
        getListView().addHeaderView(headerView);

        // Show a TimePickerDialog when headerView is pressed
        headerView.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		showTimePickerDialog();
        	}
        
        });

        // Attach the adapter to this ListActivity's ListView
        this.setListAdapter(mAdapter);        
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	
    	mReminderDAO.open();
    	// Load saved AlarmItems if necessary
    	if (mAdapter.getCount() == 0) {
    		loadItems();
    	}
    }
    
    @Override
    public void onPause() {
    	super.onPause();
    	// Save AlarmItems
    	saveItems();
    	mReminderDAO.close();
    }
    
    @Override
    public void onSaveInstanceState(Bundle outState) {
    	super.onSaveInstanceState(outState);
    	Reminder[] items = mAdapter.getValues();
    	outState.putParcelableArray("items", items);
    }
    
    private class TimePickerFragment extends DialogFragment
    	implements TimePickerDialog.OnTimeSetListener {
    	
    	@Override 
    	public Dialog onCreateDialog(Bundle savedInstance) {
    		// Use the current time as the default values for the picker
    		final Calendar c = Calendar.getInstance();
    		int hour = c.get(Calendar.HOUR_OF_DAY);    		
    		int minute = c.get(Calendar.MINUTE);
    		
    		Date d = c.getTime();
    		
    		// Class extending TimePickerDialog to set a custom title
    		final class MyTimePickerDialog extends TimePickerDialog {
    			
    			public MyTimePickerDialog(Context context,
						OnTimeSetListener callBack, int hourOfDay, int minute,
						boolean is24HourView) {
					super(context, callBack, hourOfDay, minute, is24HourView);
				}
    			
    			// Custom implementation 
    			@Override
    			public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
    				c.set(Calendar.HOUR_OF_DAY, hourOfDay);
    				c.set(Calendar.MINUTE, minute);
    				Date d = c.getTime();
    				setTitle("New reminder at " + new SimpleDateFormat("HH:mm", Locale.getDefault()).format(d));
    			}
    		}
    		
    		// Create a new instance of TimePickerDialog and return it
    		MyTimePickerDialog dialog = new MyTimePickerDialog(getActivity(), this, hour, minute, true);
    		dialog.setTitle("New reminder at " + new SimpleDateFormat("HH:mm", Locale.getDefault()).format(d));
    		return dialog;
    	}
    	
    	@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
    		Reminder newReminder = new Reminder();
    		newReminder.setHour(hourOfDay);
    		newReminder.setMinute(minute);
    		newReminder.setActive(true);
    		addReminder(newReminder);
		}
    }
    
    private void showTimePickerDialog() {
    	DialogFragment newFragment = new TimePickerFragment();
    	newFragment.show(getFragmentManager(), "timePicker");
    }
    
    // Add the time chosen to the AlarmActivity's mAdapter
    private void addReminder(Reminder item) {
    	if (mAdapter.containsItem(item)) {
    		Toast.makeText(getApplicationContext(), 
    				"Alarm at " + item.toString() + " already exists", 
    				Toast.LENGTH_LONG)
    				.show();
    	} else {
    		// Add the Reminder to the adapter and the database
    		mAdapter.addItem(item);
    		mReminderDAO.createReminder(item);
    		
    		// Call the Broadcast Receiver
    		Intent i = new Intent(PatientReminderActivity.this, ReminderSchedulerReceiver.class);
    		i.setAction(ReminderSchedulerReceiver.REMINDER_NEW);
    		i.putExtra(ReminderSchedulerReceiver.EXTRA, item);
    		sendBroadcast(i);
    	}
    }
    
    public void deleteReminder(Reminder reminder) {
    	// Delete from the database
    	mReminderDAO.deleteReminder(reminder);
    	// Notify the Broadcast Receiver
    	Intent i = new Intent(PatientReminderActivity.this, ReminderSchedulerReceiver.class);
    	i.setAction(ReminderSchedulerReceiver.REMINDER_DELETE);
    	i.putExtra(ReminderSchedulerReceiver.EXTRA, reminder);
    	sendBroadcast(i);
    }
    
    // Load stored TimeItems
    private void loadItems() {
    	mAdapter.addAllItems(mReminderDAO.getAllReminders());
    }
    
    // Save AlarmItems in the database
    private void saveItems() {
    	Reminder[] items = mAdapter.getValues();
    	for (int i = 0; i < items.length; i++) {
    		mReminderDAO.updateReminderStatus(items[i]);
    	}
    }
}