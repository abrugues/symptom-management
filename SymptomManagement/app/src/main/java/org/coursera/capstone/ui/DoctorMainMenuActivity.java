package org.coursera.capstone.ui;

import java.util.ArrayList;
import java.util.List;

import org.coursera.capstone.R;
import org.coursera.capstone.client.SecuredRestBuilder;
import org.coursera.capstone.client.SymptomSvcApi;
import org.coursera.capstone.client.UnsafeHttpsClient;
import org.coursera.capstone.connector.PatientListAdapter;
import org.coursera.capstone.dialog.AboutDialog;
import org.coursera.capstone.model.Patient;

import retrofit.RestAdapter.LogLevel;
import retrofit.client.ApacheClient;
import retrofit.converter.GsonConverter;
import android.app.DialogFragment;
import android.app.ListActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DoctorMainMenuActivity extends ListActivity {

	private PatientListAdapter patientAdapter;
	private List<Patient> patients = new ArrayList<Patient>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		patientAdapter = new PatientListAdapter(this);
		
		// Get the username and password for the authentication
		SharedPreferences settings = getSharedPreferences(LoginMainActivity.PREFS_NAME, Context.MODE_PRIVATE);
		String username = settings.getString(LoginMainActivity.USERNAME, "");
		String password = settings.getString(LoginMainActivity.PASSWORD, "");
		// Get the doctor Id for the GET request
		final long doctorId = settings.getLong(LoginMainActivity.ROLE_ID, 0);
		String lastName = settings.getString(LoginMainActivity.DOC_LAST_NAME, "");
		
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
				DoctorMainMenuActivity.this.patients = symptomService.getPatientsList(doctorId);
				DoctorMainMenuActivity.this.runOnUiThread(new Runnable() {	
					@Override
					public void run() {
						loadPatients();	
					}
				});
			}
		});
		t.start();
		
		// Add the header view (welcome message to the doctor)
		TextView headerView = (TextView) LayoutInflater
        		.from(DoctorMainMenuActivity.this)
        		.inflate(R.layout.header_view_patient, getListView(), false);
		headerView.append(lastName);
		getListView().addHeaderView(headerView, null, false);
		
		// Add the footer view (patient search by name)
		LinearLayout footerView = (LinearLayout) LayoutInflater
				.from(DoctorMainMenuActivity.this)
				.inflate(R.layout.footer_view_patient, getListView(), false);
		configurePatientSearch(footerView);
		getListView().addFooterView(footerView);
		
	}
	
	private void loadPatients() {
		for (Patient patient : patients) {
			patientAdapter.addItem(patient);
		}
		this.setListAdapter(patientAdapter);
	}
	
	private void configurePatientSearch(LinearLayout searchLayout) {
		final EditText nameEditTxt = (EditText) searchLayout.findViewById(R.id.patNameEditTxt);
		
		final Button searchBtn = (Button) searchLayout.findViewById(R.id.searchPatientBtn);
		searchBtn.setEnabled(false);
		searchBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String patientName = nameEditTxt.getText().toString();
				nameEditTxt.clearFocus();
				nameEditTxt.setText("");
				patientAdapter.searchPatientByName(patientName);
			}
		});
		
		nameEditTxt.addTextChangedListener(new TextWatcher() {			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (!s.toString().equals("")) {
					searchBtn.setEnabled(true);
				} else {
					searchBtn.setEnabled(false);
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.about:
			DialogFragment newFragment = new AboutDialog();
			newFragment.show(getFragmentManager(), "aboutDialog");
			return true;
		case R.id.logout:
			SharedPreferences settings = getSharedPreferences(LoginMainActivity.PREFS_NAME, Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = settings.edit();
			editor.clear();
			editor.commit();
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
