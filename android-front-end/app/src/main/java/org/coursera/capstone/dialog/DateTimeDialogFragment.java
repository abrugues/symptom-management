package org.coursera.capstone.dialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.coursera.capstone.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TimePicker;
import android.widget.ViewSwitcher;

public class DateTimeDialogFragment extends DialogFragment {

	private DatePicker datePicker;
	private TimePicker timePicker;
	private ViewSwitcher viewSwitcher;
	
	private Button setDateBtn;
	private Button setTimeBtn;
	
	private DateTimePickerListener listener;

	public interface DateTimePickerListener {
		public void onDialogPositiveClick(DialogFragment dialog);
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		LayoutInflater inflater = getActivity().getLayoutInflater();
		
		// Inflate the pickers to instantiate them
		datePicker = (DatePicker) inflater.inflate(R.layout.date_picker_dialog, null);
		timePicker = (TimePicker) inflater.inflate(R.layout.time_picker_dialog, null);
		
		configurePickers();
		
		// Instantiate the ViewSwitcher from its layout  
		RelativeLayout dateTimePickerLayout = (RelativeLayout) inflater.inflate(R.layout.date_time_picker_dialog, null);
		viewSwitcher = (ViewSwitcher) dateTimePickerLayout.findViewById(R.id.dateTimePickerVS);
				
		// Add the pickers to the ViewSwitcher
		viewSwitcher.addView(datePicker, 0);
		viewSwitcher.addView(timePicker, 1);
		
		setSwitchButtons(dateTimePickerLayout);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());	
		
		builder.setTitle("Select the Time and Date");
		builder.setView(dateTimePickerLayout);
		
		builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				listener.onDialogPositiveClick(DateTimeDialogFragment.this);
			}
		});
		
		return builder.create();
	}
	
	@Override
    public void onDismiss(final DialogInterface dialog) {
        super.onDismiss(dialog);
        final Activity activity = getActivity();
        if (activity != null && activity instanceof DialogInterface.OnDismissListener) {
            ((DialogInterface.OnDismissListener) activity).onDismiss(dialog);
        }
    }

	public Date getDate() {
		int hour = timePicker.getCurrentHour();
		int minute = timePicker.getCurrentMinute();
		int day = datePicker.getDayOfMonth();
		int month = datePicker.getMonth();
		int year = datePicker.getYear();
		
		Calendar cal = Calendar.getInstance();
		cal.set(year, month, day, hour, minute);
		
		return new Date(cal.getTimeInMillis());
	}
	
	public String getDateAndTimeString() {
		int hour = timePicker.getCurrentHour();
		int minute = timePicker.getCurrentMinute();
		int day = datePicker.getDayOfMonth();
		int month = datePicker.getMonth();
		int year = datePicker.getYear();
		
		Calendar c = Calendar.getInstance();
		c.set(year, month, day, hour, minute);
		
		Date d = c.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/y @ HH:mm", Locale.getDefault());
		
		return sdf.format(d);
	}
	
	 @Override
	 public void onAttach(Activity activity) {
		 super.onAttach(activity);
		 // Verify that the host activity implements the callback interface
		 try {
			 // Instantiate the DateTimePickerListener so we can send events to the host
			 listener = (DateTimePickerListener) activity;
		 } catch (ClassCastException e) {
			 // The activity doesn't implement the interface, throw exception
			 throw new ClassCastException(activity.toString()
	                    + " must implement DateTimePickerListener");
	     }
	 }

	
	private void configurePickers() {
		
		timePicker.setIs24HourView(true);
		
		final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        timePicker.setCurrentHour(hour);
        timePicker.setCurrentMinute(minute);
	}
	
	private void setSwitchButtons(RelativeLayout layout) {
		setDateBtn = (Button) layout.findViewById(R.id.switchToDateBtn);
		setTimeBtn = (Button) layout.findViewById(R.id.switchToTimeBtn);
		
		setDateBtn.setVisibility(View.INVISIBLE);
		
		setDateBtn.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				setDateBtn.setVisibility(View.INVISIBLE);
				setTimeBtn.setVisibility(View.VISIBLE);
				viewSwitcher.setDisplayedChild(0);
			}
		});
		
		setTimeBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setTimeBtn.setVisibility(View.INVISIBLE);
				setDateBtn.setVisibility(View.VISIBLE);
				viewSwitcher.setDisplayedChild(1);
			}
		});
		
	}

}
