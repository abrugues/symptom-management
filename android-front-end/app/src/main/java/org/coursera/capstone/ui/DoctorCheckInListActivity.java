package org.coursera.capstone.ui;

import java.util.List;

import org.coursera.capstone.R;
import org.coursera.capstone.client.SecuredRestBuilder;
import org.coursera.capstone.client.SymptomSvcApi;
import org.coursera.capstone.client.UnsafeHttpsClient;
import org.coursera.capstone.connector.CheckInListAdapter;
import org.coursera.capstone.connector.PatientListAdapter;
import org.coursera.capstone.model.CheckIn;

import retrofit.RestAdapter.LogLevel;
import retrofit.client.ApacheClient;
import retrofit.converter.GsonConverter;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.TextView;

public class DoctorCheckInListActivity extends ListActivity {

	private CheckInListAdapter checkinListAdapter;
	private List<CheckIn> checkins;
	private String patientName;
	private String patientRecordNum;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_checkin_list_doctor);
		
		Intent intent = getIntent();
		patientName = intent.getStringExtra(PatientListAdapter.PATIENT_NAME);
		patientRecordNum = intent.getStringExtra(SymptomSvcApi.PAT_RECORD_NUM); 
		
		downloadCheckIns();
		
		checkinListAdapter = new CheckInListAdapter(this);		
	}
	
	public String getPatientName() {
		return patientName;
	}
	
	private void downloadCheckIns() {
		
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
				.setConverter(new GsonConverter(LoginMainActivity.getCustomGsonBuilder()))
				.setEndpoint(LoginMainActivity.SERVICE_URL).setLogLevel(LogLevel.FULL).build()
				.create(SymptomSvcApi.class);
		
		Thread t = new Thread(new Runnable() {			
			@Override
			public void run() {
				// Get the checkins from the server
				checkins = symptomService.getPatientCheckIns(doctorId, patientRecordNum);
				
				if (!checkins.isEmpty()) {
					DoctorCheckInListActivity.this.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							
							TextView headerView = (TextView) LayoutInflater
									.from(DoctorCheckInListActivity.this)
									.inflate(R.layout.header_view_checkin, getListView(), false);
							headerView.append(" " + patientName);
							getListView().addHeaderView(headerView, null, false);
							checkinListAdapter.loadCheckins(checkins);
							DoctorCheckInListActivity.this.setListAdapter(checkinListAdapter);
						}
					});
				} else {
					DoctorCheckInListActivity.this.setListAdapter(checkinListAdapter);
				}
			}
		});
		t.start();
		
	}
}
