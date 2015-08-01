package org.coursera.capstone;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.coursera.capstone.client.SecuredRestBuilder;
import org.coursera.capstone.client.SymptomSvcApi;
import org.coursera.capstone.client.UnsafeHttpsClient;
import org.coursera.capstone.connector.MedicationDAO;
import org.coursera.capstone.dialog.DateTimeDialogFragment;
import org.coursera.capstone.model.CheckIn;
import org.coursera.capstone.model.Prescription;

import retrofit.RestAdapter.LogLevel;
import retrofit.client.ApacheClient;
import retrofit.converter.GsonConverter;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class PatientCheckInActivity extends Activity
		implements DateTimeDialogFragment.DateTimePickerListener,
				DialogInterface.OnDismissListener{
	
	private SharedPreferences settings;
	private String username;
	private String password;
	private long patientId = 0;
	
	// The object to transmit to the server
	private CheckIn checkIn = new CheckIn();
	// The different medications taken
	private List<Prescription> prescriptions = new ArrayList<Prescription>();
	
	private MedicationDAO medicationDAO;
	private List<String> medications;
	
	private ViewFlipper viewFlipper;
	
	// The different layouts added programmatically according to the medications
	private List<View> layoutsQuestFour = new ArrayList<View>();
	// The TextView where the date of the medication is displayed
	private TextView dateTxtView;
	// The name of the medication
	private String medicationName;
	
	private boolean isQuestOneAnswered = false;
	private boolean isQuestTwoAnswered = false;
	private boolean isQuestThreeAnsweredYes = false;
	private boolean isQuestThreeAnsweredNo = false;
	private float lastX;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_checkin_patient);
		
		//Check if the patient is logged in 
		getCredentials();
		
		// Read the medications stored in the database
		medicationDAO = new MedicationDAO(this);
		medicationDAO.open();
		medications = medicationDAO.getAllMedications();
			
		// Set the FlipperView
		viewFlipper = (ViewFlipper) findViewById(R.id.surveyViewFlipper);
		setFlipperContent();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		medicationDAO.close();
	}
	
	// Called when the user selects time and date for a medication
	@Override
	public void onDialogPositiveClick(DialogFragment dialog) {
		DateTimeDialogFragment dateTimeDF = (DateTimeDialogFragment) dialog;
		dateTxtView.setText("Date: " + dateTimeDF.getDateAndTimeString());
		
		// Create the prescription associated with the click
		Prescription p = new Prescription(medicationName, dateTimeDF.getDate().getTime());
		if (prescriptions.contains(p)) {
			prescriptions.remove(p);
		}
		prescriptions.add(p);
	}
	
	// Called when the dialog to select time and date is dimissed
	@Override
	public void onDismiss(DialogInterface dialog) {
		if (isAllMedicationFilled()) {
			viewFlipper.setInAnimation(this, R.anim.slide_in_from_right);
			viewFlipper.setOutAnimation(this, R.anim.slide_out_to_left);
			viewFlipper.showNext();
		}
	}
	
	
	public void onSubmitCheckin(View view) {
		checkIn.setPrescriptionList(prescriptions);
		checkIn.setTimeStamp(new Date().getTime());
		
		final SymptomSvcApi symptomService = new SecuredRestBuilder()
				.setLoginEndpoint(LoginMainActivity.SERVICE_URL + SymptomSvcApi.TOKEN_PATH)
				.setUsername(username)
				.setPassword(password)
				.setClientId("mobile")
				.setClient(new ApacheClient(UnsafeHttpsClient.createUnsafeClient()))
				.setConverter(new GsonConverter(LoginMainActivity.getCustomGsonBuilder()))
				.setEndpoint(LoginMainActivity.SERVICE_URL).setLogLevel(LogLevel.FULL).build()
				.create(SymptomSvcApi.class);
		
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				// HTTP POST the check-in to the server
				symptomService.addCheckIn(patientId, checkIn);
				PatientCheckInActivity.this.finish();
			}
			
		});
		t.start();
	}
	
	public void onCancelCheckin(View view) {
		finish();
	}
	
	public void onQuestionOneClicked(View view) {
		isQuestOneAnswered = true;
		// Set the value selected
		RadioButton radio = (RadioButton) view;
		checkIn.setPainLevel(radio.getText().toString());
		
		viewFlipper.setInAnimation(this, R.anim.slide_in_from_right);
		viewFlipper.setOutAnimation(this, R.anim.slide_out_to_left);
		viewFlipper.showNext();
	}
	
	public void onQuestionTwoClicked(View view) {
		isQuestTwoAnswered = true;
		
		RadioButton radio = (RadioButton) view;
		checkIn.setStopEating(radio.getText().toString());
		
		viewFlipper.setInAnimation(this, R.anim.slide_in_from_right);
		viewFlipper.setOutAnimation(this, R.anim.slide_out_to_left);
		viewFlipper.showNext();
	}
	
	public void onQuestionThreeYesClicked(View view) {
		isQuestThreeAnsweredYes = true;
		isQuestThreeAnsweredNo = false;
		
		checkIn.setPainMedication(true);
		
		viewFlipper.setInAnimation(this, R.anim.slide_in_from_right);
		viewFlipper.setOutAnimation(this, R.anim.slide_out_to_left);
		viewFlipper.showNext();
	}
	
	public void onQuestionThreeNoClicked(View view) {
		isQuestThreeAnsweredNo = true;
		isQuestThreeAnsweredYes = false;
		
		checkIn.setPainMedication(false);
		checkIn.setPrescriptionList(new ArrayList<Prescription>());
		
		viewFlipper.setInAnimation(this, R.anim.slide_in_from_right);
		viewFlipper.setOutAnimation(this, R.anim.slide_out_to_left);
		viewFlipper.setDisplayedChild(4);
	}
	
	public void onQuestionFourYesClicked(View view) {
		showDateTimePickerDialog();
		
		// Get the proper TextView that will be updated once the dialod is closed 
		RelativeLayout layout = (RelativeLayout) view.getParent().getParent();
		dateTxtView = (TextView) layout.findViewById(R.id.drugDateTxtView);
		
		// Get the name of the medication used for the prescription
		TextView questionTxtView = (TextView) layout.findViewById(R.id.questFourTxtView);
		int length = questionTxtView.getText().length();
		medicationName = questionTxtView.getText().subSequence(18, length - 1).toString();		
	}
	
	public void onQuestionFourNoClicked(View view) {
		RelativeLayout layout = (RelativeLayout) view.getParent().getParent();
		dateTxtView = (TextView) layout.findViewById(R.id.drugDateTxtView);
		dateTxtView.setText("Date: -");
				
		// Get the name of the medication used for the prescription
		TextView questionTxtView = (TextView) layout.findViewById(R.id.questFourTxtView);
		int length = questionTxtView.getText().length();
		medicationName = questionTxtView.getText().subSequence(18, length - 1).toString();
		
		// Store the prescription
		Prescription p = new Prescription(medicationName);		
		if (prescriptions.isEmpty()) {
			prescriptions.add(p);
		} else if ( (!prescriptions.isEmpty()) && (prescriptions.contains(p)) ) {
			prescriptions.remove(p);
			prescriptions.add(p);
		} else if ( (!prescriptions.isEmpty()) && (!prescriptions.contains(p)) ) {
			prescriptions.add(p);
		}
		
		if (isAllMedicationFilled()) {
			viewFlipper.setInAnimation(this, R.anim.slide_in_from_right);
			viewFlipper.setOutAnimation(this, R.anim.slide_out_to_left);
			viewFlipper.showNext();
		}
	}
	
	private void getCredentials() {
		// Get the username and password for the authentication
		settings = getSharedPreferences(LoginMainActivity.PREFS_NAME, Context.MODE_PRIVATE);
		username = settings.getString(LoginMainActivity.USERNAME, "");
		password = settings.getString(LoginMainActivity.PASSWORD, "");
		// Get the patient Id for the POST request
		patientId = settings.getLong(LoginMainActivity.ROLE_ID, 0);
				
		// If the user is not loged in
		if ( (username.equals("")) && (password.equals("")) ) {
			Toast.makeText(this, "User is not loged in", Toast.LENGTH_LONG).show();
			finish();
		}
	}
	
	private void setFlipperContent() {
		// Child 0
		RelativeLayout fisrtQuestLayout = (RelativeLayout) LayoutInflater
				.from(PatientCheckInActivity.this)
				.inflate(R.layout.checkin_1st, viewFlipper, false);
		viewFlipper.addView(fisrtQuestLayout);
		
		// Child 1
		RelativeLayout secondQuestLayout = (RelativeLayout) LayoutInflater
				.from(PatientCheckInActivity.this)
				.inflate(R.layout.checkin_2nd, viewFlipper, false);
		viewFlipper.addView(secondQuestLayout);
		
		// Child 2
		RelativeLayout thirdQuestLayout = (RelativeLayout) LayoutInflater
				.from(PatientCheckInActivity.this)
				.inflate(R.layout.checkin_3rd, viewFlipper, false);
		viewFlipper.addView(thirdQuestLayout);
		
		// Child 3
		ListView fourthQuestLayout = (ListView) LayoutInflater
				.from(PatientCheckInActivity.this)
				.inflate(R.layout.checkin_4th, viewFlipper, false);
		addMedicaments(fourthQuestLayout);
		viewFlipper.addView(fourthQuestLayout);
		
		// Child 4
		RelativeLayout fifthQuestLayout = (RelativeLayout) LayoutInflater
				.from(PatientCheckInActivity.this)
				.inflate(R.layout.checkin_5th, viewFlipper, false);
		viewFlipper.addView(fifthQuestLayout);
	}
	
	private boolean isAllMedicationFilled() {
		int numLayouts = layoutsQuestFour.size();		
		int numLayoutsSelected = 0;
		for(View layout : layoutsQuestFour) {
			RadioGroup radioGroup = (RadioGroup) layout.findViewById(R.id.questFourRadioGroup);
			int selected = radioGroup.getCheckedRadioButtonId();
			if (selected != -1) {
				numLayoutsSelected++;
			}
		}
		if (numLayoutsSelected == numLayouts) {
			return true;
		}
		return false;
	}
	
	private void showDateTimePickerDialog() {
		DialogFragment newFragment = new DateTimeDialogFragment();
		newFragment.show(getFragmentManager(), "dateTimePicker");
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent touchevent) { 
		
		switch (touchevent.getAction()) {
			// When user first touches the screen to swap
			case MotionEvent.ACTION_DOWN: {
				lastX = touchevent.getX();
				break;
			}
			
			case MotionEvent.ACTION_UP: {
				float currentX = touchevent.getX();

				// if left to right swipe on screen
				if (lastX < currentX) {
					// If no more View/Child to flip
					int child = viewFlipper.getDisplayedChild();
					if (child == 0) {
						break;
					} else if ( (child == 4) && (isQuestThreeAnsweredNo) ) {
						viewFlipper.setInAnimation(this, R.anim.slide_in_from_left);
						viewFlipper.setOutAnimation(this, R.anim.slide_out_to_right);
						viewFlipper.setDisplayedChild(2);
						break;
					}
						
					// set the required Animation type to ViewFlipper
					// The Next screen will come in form Left and current Screen
					// will go OUT from Right
					viewFlipper.setInAnimation(this, R.anim.slide_in_from_left);
					viewFlipper.setOutAnimation(this, R.anim.slide_out_to_right);
					// Show the next Screen
					viewFlipper.showPrevious();
					
				}
				
				// Handling right to left screen swap.
				if (lastX > currentX) {
					// Prevent to flip if the question is not yet answered
					int child = viewFlipper.getDisplayedChild();
					if ( (child == viewFlipper.getChildCount() - 1) || 
							(!isQuestOneAnswered) ) {
						break;
					} else if ( (child == 1) && (!isQuestTwoAnswered) ) {
						break;
					} else if ( (child == 2) && (!isQuestThreeAnsweredYes) && 
							(!isQuestThreeAnsweredNo) ) {
						break;
					} else if ( (child == 2) && (isQuestThreeAnsweredNo) ) {
						viewFlipper.setInAnimation(this, R.anim.slide_in_from_right);
						viewFlipper.setOutAnimation(this, R.anim.slide_out_to_left);
						viewFlipper.setDisplayedChild(4);
						break;
					} else if ( (child == 3) && (!isAllMedicationFilled()) ) {
						break;
					}
					
					// Next screen comes in from right.
					viewFlipper.setInAnimation(this, R.anim.slide_in_from_right);
					// Current screen goes out from left.
					viewFlipper.setOutAnimation(this, R.anim.slide_out_to_left);
					// Display previous screen.
					viewFlipper.showNext();
				}
				break;
			}
		}
		return false;
	}

	private void addMedicaments(ListView layout) {
		
		MedicationAdapter medAdapter = new MedicationAdapter(medications);
		layout.setAdapter(medAdapter);
		
		// Add the gesture functionality to the layout		
		layout.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				PatientCheckInActivity.this.onTouchEvent(event);
				return false;
			}
		});
	}
	
	private class MedicationAdapter extends BaseAdapter {

		private List<String> medications;
		
		public MedicationAdapter(List<String> medications) {
			this.medications = medications;
		}
		
		@Override
		public int getCount() {
			return medications.size();
		}

		@Override
		public Object getItem(int position) {
			return medications.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) PatientCheckInActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.checkin_4th_medication, parent, false);
			}
			
			final String medication = (String) getItem(position);
			
			TextView medicationTxtView = (TextView) convertView.findViewById(R.id.questFourTxtView);
			medicationTxtView.append(" " + medication + "?");
			
			PatientCheckInActivity.this.layoutsQuestFour.add(convertView);
			
			return convertView;
		}	
	}
}
