package org.coursera.capstone.ui;

import java.util.List;

import org.coursera.capstone.R;
import org.coursera.capstone.connector.CheckInListAdapter;
import org.coursera.capstone.model.CheckIn;
import org.coursera.capstone.model.Prescription;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DoctorCheckInActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_checkin_doctor);
		
		Intent i = getIntent();
		CheckIn checkin = i.getParcelableExtra(CheckInListAdapter.CHECKIN);
		String patientName = i.getStringExtra(CheckInListAdapter.PATIENT_NAME);
		
		TextView checkinTitleTxtView = (TextView) findViewById(R.id.checkinTitleTxtView);
		checkinTitleTxtView.append(" " + patientName);
		
		TextView checkinDateTxtView = (TextView) findViewById(R.id.checkinDateTxtView);
		checkinDateTxtView.setText(checkin.getStringDate());
		
		TextView checkinTimeTxtView = (TextView) findViewById(R.id.checkinTimeTxtView);
		checkinTimeTxtView.setText(checkin.getStringTime());
		
		TextView painSeverityTxtView = (TextView) findViewById(R.id.painSeverityTxtView);
		painSeverityTxtView.setText(checkin.getPainLevel());
		
		TextView painStopTxtView = (TextView) findViewById(R.id.painStopTxtView);
		painStopTxtView.setText(checkin.getStopEating());
		
		TextView medicationTakenTxtView = (TextView) findViewById(R.id.medicationTakenTxtView);
		if (checkin.isPainMedication()) {
			medicationTakenTxtView.setText(R.string.yes);
			displayCheckInMedications(checkin);
		} else {
			medicationTakenTxtView.setText(R.string.no);
		}
	}
	
	private void displayCheckInMedications(CheckIn checkin) {
		List<Prescription> prescriptions = checkin.getPrescriptionList();
		LinearLayout medicationsLinearLayout = (LinearLayout) findViewById(R.id.medicationsLinearLayout);
		
		for (Prescription p : prescriptions) {
			RelativeLayout medicationLayout = (RelativeLayout) LayoutInflater
					.from(this)
					.inflate(R.layout.medication_layout, medicationsLinearLayout, false);
			
			TextView medicationNameTxtView = (TextView) medicationLayout.findViewById(R.id.medicationNameTxtView);
			medicationNameTxtView.setText(p.getName());
			
			TextView medicationTakenTxtView = (TextView) medicationLayout.findViewById(R.id.medicationTakenTxtView);
			if (p.isTaken()) {
				medicationTakenTxtView.setText(R.string.yes);
				TextView medicationTimestampTxtView = (TextView)
						medicationLayout.findViewById(R.id.medicationTimestampTxtView);
				medicationTimestampTxtView.setText(p.getStringTimeStamp());
			} else {
				medicationTakenTxtView.setText(R.string.no);
				TextView medicationTimestampLabelTxtView = (TextView) 
						medicationLayout.findViewById(R.id.medicationTimestampLabelTxtView);
				medicationLayout.removeView(medicationTimestampLabelTxtView);
				TextView medicationTimestampTxtView = (TextView)
						medicationLayout.findViewById(R.id.medicationTimestampTxtView);
				medicationLayout.removeView(medicationTimestampTxtView);
			}
			
			medicationsLinearLayout.addView(medicationLayout);
		}
	}
}
