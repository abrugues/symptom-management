package org.coursera.capstone;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Date;

import org.coursera.capstone.client.SecuredRestBuilder;
import org.coursera.capstone.client.SymptomSvcApi;
import org.coursera.capstone.client.UnsafeHttpsClient;
import org.coursera.capstone.connector.MedicationDAO;
import org.coursera.capstone.model.Doctor;
import org.coursera.capstone.model.Patient;

import retrofit.RestAdapter.LogLevel;
import retrofit.client.ApacheClient;
import retrofit.converter.GsonConverter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class LoginMainActivity extends Activity {
	
	public static final String PREFS_NAME = "LoginPref";
	public static final String ROLE_LOGGED = "roleLogged";
	public static final String ROLE_ID = "id";
	public static final String USERNAME = "username";
	public static final String PASSWORD = "password";
	public static final String DOC_LAST_NAME = "docLastName";
	
	public static final String SERVICE_URL = "https://10.0.2.2:8443";
	private final String PATIENT_ROLE = "PATIENT";
	private final String DOCTOR_ROLE = "DOCTOR";
	
	private EditText usernameEditText;
	private EditText passwordEditText;
	private Button loginBtn;
	private Button cancelBtn;
	
	private SharedPreferences settings;
	
	private MedicationDAO medicationDAO;
		
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Check if there is a user already logged
		settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		String lastRole = settings.getString(ROLE_LOGGED, null);
		
		if (lastRole != null) {
			if (lastRole.equals(PATIENT_ROLE)) {
				showPatientMainMenu();
			} else if (lastRole.equals(DOCTOR_ROLE)) {
				showDoctorMainMenu();
			}
		}
		
		setContentView(R.layout.activity_main);
		
		usernameEditText = (EditText) findViewById(R.id.usernameEditTxt);
		passwordEditText = (EditText) findViewById(R.id.passwordEditTxt);
		loginBtn = (Button) findViewById(R.id.loginBtn);
		cancelBtn = (Button) findViewById(R.id.cancelBtn);
		
		enableButtonsOnTextChange();
		
		medicationDAO = new MedicationDAO(this); 
		
	}
	
	@Override
	public void onResume() {
		super.onResume();	
		medicationDAO.open();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		medicationDAO.close();
	}
	
	// Method called when Cancel button is pressed
	public void deleteFields(View v) {
		usernameEditText.setText("");
		passwordEditText.setText("");
	}
	
	// Method called when Login button is pressed
	public void doLogin(View v) {
		
		final SymptomSvcApi symptomService = new SecuredRestBuilder()
				.setLoginEndpoint(SERVICE_URL + SymptomSvcApi.TOKEN_PATH)
				.setUsername(usernameEditText.getText().toString())
				.setPassword(passwordEditText.getText().toString())
				.setClientId("mobile")
				.setClient(new ApacheClient(UnsafeHttpsClient.createUnsafeClient()))
				.setConverter(new GsonConverter(getCustomGsonBuilder()))
				.setEndpoint(SERVICE_URL).setLogLevel(LogLevel.FULL).build()
				.create(SymptomSvcApi.class);
		
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				SharedPreferences.Editor editor = settings.edit();
				
				Collection<String> role = symptomService.getRoles();
						
				if (role.contains("PATIENT")) {
					
					// Save the patient that log in
					Patient p = symptomService.getPatient();
					
					editor.putString(LoginMainActivity.ROLE_LOGGED, PATIENT_ROLE);
					editor.putLong(ROLE_ID, p.getId());
					editor.putString(USERNAME, usernameEditText.getText().toString());
					editor.putString(PASSWORD, passwordEditText.getText().toString());
					editor.commit();
					
					// Save the medications of the patient
					medicationDAO.updateMedication(p.getMedications());
					
					// Start the main menu for the patient
					showPatientMainMenu();
					
				} else if (role.contains("DOCTOR")) {
					
					Doctor d = symptomService.getDoctor();
					
					editor.putString(LoginMainActivity.ROLE_LOGGED, DOCTOR_ROLE);
					editor.putLong(ROLE_ID, d.getId());
					editor.putString(USERNAME, usernameEditText.getText().toString());
					editor.putString(PASSWORD, passwordEditText.getText().toString());
					editor.putString(DOC_LAST_NAME, d.getLastName());
					editor.commit();
					
					showDoctorMainMenu();
				}
			}	
		});
		t.start();
		
	}

	private void enableButtonsOnTextChange() {
		usernameEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {				
				if (s.length() > 0) {
					cancelBtn.setEnabled(true);
					if (passwordEditText.getText().length() > 0) {
						loginBtn.setEnabled(true);
					}
				} else if (s.length() == 0) {
					loginBtn.setEnabled(false);
					if (passwordEditText.getText().length() == 0) {
						cancelBtn.setEnabled(false);
					}
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
		
		passwordEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (s.length() > 0) {
					cancelBtn.setEnabled(true);
					if (usernameEditText.getText().length() > 0) {
						loginBtn.setEnabled(true);
					}
				} else if (s.length() == 0) {
					loginBtn.setEnabled(false);
					if (usernameEditText.getText().length() == 0){
						cancelBtn.setEnabled(false);
					}
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
	
	private void showPatientMainMenu() {
		Intent i = new Intent(LoginMainActivity.this, PatientMainMenuActivity.class);
		startActivity(i);
	}
	
	private void showDoctorMainMenu() {
		Intent i = new Intent(LoginMainActivity.this, DoctorMainMenuActivity.class);
		startActivity(i);
	}
	
	public static Gson getCustomGsonBuilder() {
		
		return new GsonBuilder()
			.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
				public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
							throws JsonParseException {
					return new Date(json.getAsJsonPrimitive().getAsLong());
				}
			})
			.create();	
	}
}
