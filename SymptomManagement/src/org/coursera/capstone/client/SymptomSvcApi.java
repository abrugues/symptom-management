package org.coursera.capstone.client;

import java.util.Collection;
import java.util.List;

import org.coursera.capstone.model.CheckIn;
import org.coursera.capstone.model.Doctor;
import org.coursera.capstone.model.Patient;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

public interface SymptomSvcApi {
	
	public static final String PATIENT_ID = "patId";
	public static final String DOCTOR_ID = "docId";
	public static final String PAT_RECORD_NUM = "recordNum";
	
	public static final String TOKEN_PATH = "/oauth/token";
	
	public static final String USER_SVC = "/user";
	
	public static final String PATIENT_SVC = "/patient";
	public static final String PATIENT_SVC_NEW_CHK = PATIENT_SVC + "/{" + PATIENT_ID + "}";
	
	public static final String DOCTOR_SVC = "/doctor";
	public static final String DOCTOR_SVC_PAT_LIST = DOCTOR_SVC + "/{" + DOCTOR_ID + "}";
	public static final String DOCTOR_SVC_PAT_CHK_LIST = DOCTOR_SVC_PAT_LIST + "/{" + PAT_RECORD_NUM + "}";
	public static final String DOCTOR_SVC_ADD_DRUG = DOCTOR_SVC_PAT_LIST + "/{" + PAT_RECORD_NUM + "}" + "/newDrug";
	
	@GET(USER_SVC)
	public Collection<String> getRoles();
	
	@GET(PATIENT_SVC)
	public Patient getPatient();
	
	@POST(PATIENT_SVC_NEW_CHK)
	public CheckIn addCheckIn(@Path(value=PATIENT_ID) long patientId, @Body CheckIn checkin);
	
	@GET(DOCTOR_SVC)
	public Doctor getDoctor();
	
	@GET(DOCTOR_SVC_PAT_LIST)
	public List<Patient> getPatientsList(@Path(value=DOCTOR_ID) long doctorId);
	
	@GET(DOCTOR_SVC_PAT_CHK_LIST)
	public List<CheckIn> getPatientCheckIns(@Path(value=DOCTOR_ID) long doctorId,
			@Path(value=PAT_RECORD_NUM) String patientRecordNum);
	
	@POST(DOCTOR_SVC_ADD_DRUG)
	public Void addMedication(@Path(value=DOCTOR_ID) long doctorId,
			@Path(value=PAT_RECORD_NUM) String patientRecordNum,
			@Body String medicationName);
}
