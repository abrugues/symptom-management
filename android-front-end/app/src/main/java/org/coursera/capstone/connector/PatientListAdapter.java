package org.coursera.capstone.connector;

import java.util.ArrayList;
import java.util.List;

import org.coursera.capstone.ui.DoctorCheckInListActivity;
import org.coursera.capstone.ui.DoctorMedicationActivity;
import org.coursera.capstone.R;
import org.coursera.capstone.client.SymptomSvcApi;
import org.coursera.capstone.model.Alert;
import org.coursera.capstone.model.Patient;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PatientListAdapter extends BaseAdapter {
	
	public static final String MEDICATION_LIST = "medicationList";
	public static final String PATIENT_NAME = "patientName";
	
	private List<Patient> patients = new ArrayList<Patient>();
	private List<Patient> patientsCache = new ArrayList<Patient>(); 
	private Context context;
	
	public PatientListAdapter(Context context) {
		this.context = context;
	}
	
	public void addItem(Patient item) {
		patients.add(item);
		patientsCache.add(item);
		notifyDataSetChanged();
	}
	
	public void searchPatientByName(String name) {
		Patient patient = null;
		for(Patient p : patients) {
			if (p.getFirstName().equalsIgnoreCase(name)) {
				patient = p;
			}
		}
		
		if (patient != null) {
			patients.clear();
			patients.add(patient);
			notifyDataSetChanged();
			return;
		} else {
			// Search in the cache
			for (Patient p: patientsCache) {
				if (p.getFirstName().equalsIgnoreCase(name)) {
					patients.clear();
					patients.add(p);
					notifyDataSetChanged();
					return;
				}
			}
		}
		Toast.makeText(context, "Patient with name '" + name + "' not found", Toast.LENGTH_LONG).show();
		patients = new ArrayList<Patient>(patientsCache);
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return patients.size();
	}

	@Override
	public Object getItem(int pos) {
		return patients.get(pos);
	}

	@Override
	public long getItemId(int id) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.item_patient, parent, false);
		}
		
		final Patient patient = patients.get(position);

		TextView patientNameTxtView = (TextView) convertView.findViewById(R.id.patientNameTxtView);
		patientNameTxtView.setText(patient.getFullName());
		
		// Handle the alerts of the patient by displaying the propper icons
		Alert alert = patient.getAlert();
		if (alert.isTwelveHoursEat()) {
			ImageView eatAlertImgView = (ImageView) convertView.findViewById(R.id.eatAlertImgView);
			eatAlertImgView.setVisibility(View.VISIBLE);
		}
		if (alert.isTwelveHoursPain()) {
			ImageView pain12AlertImgView = (ImageView) convertView.findViewById(R.id.pain12AlertImgView);
			pain12AlertImgView.setVisibility(View.VISIBLE);
		}
		if (alert.isSixteenHoursPain()) {
			ImageView pain16AlertImgView = (ImageView) convertView.findViewById(R.id.pain16AlertImgView);
			pain16AlertImgView.setVisibility(View.VISIBLE);
		}
		
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showActionsDialog(patient);
			}
		});
		
		return convertView;
	}
	
	private void showActionsDialog(final Patient patient) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				context);
		alertDialogBuilder
			.setTitle("Patient " + patient.getFirstName() + " " + patient.getLastName())
			.setItems(R.array.patient_dialog, new DialogInterface.OnClickListener() {	
				@Override
				public void onClick(DialogInterface dialog, int which) {
					switch (which) {
						case 0:
							Intent intentCheckIns = new Intent(context, DoctorCheckInListActivity.class);
							intentCheckIns.putExtra(
									PATIENT_NAME,
									patient.getFullName());
							intentCheckIns.putExtra(SymptomSvcApi.PAT_RECORD_NUM, patient.getRecordNum());
							context.startActivity(intentCheckIns);
							break;
						case 1:
							Intent intentMedication = new Intent(context, DoctorMedicationActivity.class);
							intentMedication.putStringArrayListExtra(
									MEDICATION_LIST,
									new ArrayList<String>(patient.getMedications())
								);
							intentMedication.putExtra(
									PATIENT_NAME,
									patient.getFullName()
								);
							intentMedication.putExtra(SymptomSvcApi.PAT_RECORD_NUM, patient.getRecordNum());
							context.startActivity(intentMedication);
							break;
					}
				}
			});
		alertDialogBuilder.create().show();
	}
}