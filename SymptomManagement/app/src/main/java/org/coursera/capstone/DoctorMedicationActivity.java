package org.coursera.capstone;

import java.util.List;

import org.coursera.capstone.client.SecuredRestBuilder;
import org.coursera.capstone.client.SymptomSvcApi;
import org.coursera.capstone.client.UnsafeHttpsClient;
import org.coursera.capstone.connector.MedicationListAdapter;
import org.coursera.capstone.connector.PatientListAdapter;

import retrofit.RestAdapter.LogLevel;
import retrofit.client.ApacheClient;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DoctorMedicationActivity extends ListActivity {

	private MedicationListAdapter medicationsAdapter;
	private String patientRecordNum;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent intent = getIntent();
		
		List<String> medications = intent.getStringArrayListExtra(PatientListAdapter.MEDICATION_LIST);
		medicationsAdapter = new MedicationListAdapter(this, medications);
		patientRecordNum = intent.getStringExtra(SymptomSvcApi.PAT_RECORD_NUM);
				
		// Set the header
		TextView headerView = (TextView) LayoutInflater
				.from(DoctorMedicationActivity.this)
				.inflate(R.layout.header_view_medication, getListView(), false);
		headerView.append(intent.getStringExtra(PatientListAdapter.PATIENT_NAME));
		getListView().addHeaderView(headerView, null, false);
		
		// Set the footer
		LinearLayout layout = (LinearLayout) LayoutInflater
				.from(DoctorMedicationActivity.this)
				.inflate(R.layout.footer_view_medication, getListView(), false);
		configureFooter(layout);
		getListView().addFooterView(layout);
		
		getListView().setEnabled(false);
		
		setListAdapter(medicationsAdapter);
	}
	
	private void configureFooter(LinearLayout layout) {
		final EditText medicationEditTxt = (EditText) layout.findViewById(R.id.medicationNameEditTxt);
		
		final Button addMedicationBtn = (Button) layout.findViewById(R.id.addMedicationBtn);
		addMedicationBtn.setEnabled(false);
		addMedicationBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				
				SharedPreferences settings = getSharedPreferences(LoginMainActivity.PREFS_NAME, Context.MODE_PRIVATE);
				String username = settings.getString(LoginMainActivity.USERNAME, "");
				String password = settings.getString(LoginMainActivity.PASSWORD, "");
				final long doctorId = settings.getLong(LoginMainActivity.ROLE_ID, 0);
				
				final SymptomSvcApi symptomService = new SecuredRestBuilder()
						.setLoginEndpoint(LoginMainActivity.SERVICE_URL + SymptomSvcApi.TOKEN_PATH)
						.setUsername(username)
						.setPassword(password)
						.setClientId("mobile")
						.setClient(new ApacheClient(UnsafeHttpsClient.createUnsafeClient()))
						.setEndpoint(LoginMainActivity.SERVICE_URL).setLogLevel(LogLevel.FULL).build()
						.create(SymptomSvcApi.class);
				
				Thread t = new Thread(new Runnable() {
					@Override
					public void run() {
						final String medicationName = medicationEditTxt.getText().toString();						
						symptomService.addMedication(doctorId, patientRecordNum, medicationName);
						DoctorMedicationActivity.this.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								medicationEditTxt.clearFocus();
								medicationEditTxt.setText("");
								medicationsAdapter.addMedication(medicationName);
							}
						});
					}					
				});
				t.start();
			}
		});
		
		medicationEditTxt.addTextChangedListener(new TextWatcher() {			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (!s.toString().equals("")) {
					addMedicationBtn.setEnabled(true);
				} else {
					addMedicationBtn.setEnabled(false);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
			}
		});
	}
}
