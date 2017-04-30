package org.coursera.capstone.server.controller;

import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.coursera.capstone.server.model.CheckIn;
import org.coursera.capstone.server.model.Patient;
import org.coursera.capstone.server.model.Prescription;
import org.coursera.capstone.server.repo.CheckInRepo;
import org.coursera.capstone.server.repo.PatientRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PatientController {

	private static final long TWELVE_HOURS = 12 * 60 * 60 * 1000;
	private static final long SIXTEEN_HOURS = 16 * 60 * 60 * 1000;
	
	private static final String PATIENT_ID = "patId";
	
	private static final String PATIENT_SVC = "/patient";
	private static final String PATIENT_SVC_NEW_CHK = PATIENT_SVC + "/{" + PATIENT_ID + "}";
	
	@Autowired
	private PatientRepo patients;
	
	@Autowired
	private CheckInRepo checkins;
	
	@PreAuthorize("hasRole('PATIENT')")
	@RequestMapping(value = PATIENT_SVC, method = RequestMethod.GET)
	public @ResponseBody Patient getPatient(Principal principal) {
		String userName = principal.getName();
		return patients.findByUserName(userName);
	}
	
	@PreAuthorize("hasRole('PATIENT')")
	@RequestMapping(value = PATIENT_SVC_NEW_CHK, method = RequestMethod.POST)
	public @ResponseBody CheckIn addCheckIn(
			@PathVariable(PATIENT_ID) long patientID,
			@RequestBody CheckIn checkin,
			HttpServletResponse response) {
		
		if (!patients.exists(patientID)) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return null;
		}
		
		Patient patient = patients.findOne(patientID);
		checkin.setPatient(patient);
		for (Prescription prescription : checkin.getPrescriptionList()) {
			prescription.setCheckIn(checkin);
		}
			
		// Store the checkin before computing the alerts, to take it into account
		CheckIn checkInStored = checkins.save(checkin);
		computeAlerts(checkInStored);
				
		// Update the entry with the computed alerts
		return checkins.save(checkInStored);
	}
	
	
	private void computeAlerts(CheckIn checkin) {
		Patient patient = checkin.getPatient();
		
		long endTimeStamp = checkin.getTimeStamp();
		long init12TimeStamp = endTimeStamp - TWELVE_HOURS;
		long init16TimeStamp = endTimeStamp - SIXTEEN_HOURS;
		
		List<CheckIn> allCheckinsBefore12h = checkins
				.findByPatientAndTimeStampLessThan(patient, init12TimeStamp);
		int numCheckinsBefore12h = allCheckinsBefore12h.size();
		List<CheckIn> allCheckinsBefore16h = checkins
				.findByPatientAndTimeStampLessThan(patient, init16TimeStamp);
		int numCheckinsBefore16h = allCheckinsBefore16h.size();
		
		// All checkins from the last 12 hours
		List<CheckIn> allCheckinsLast12h = checkins
				.findByPatientAndTimeStampGreaterThan(patient, init12TimeStamp);
		int numCheckins12h = allCheckinsLast12h.size();
		
		// 12 hours of severe pain alert
		List<CheckIn> checkinsLast12hSevere = checkins
				.findByPatientAndTimeStampGreaterThanAndPainLevelEquals(
						patient, init12TimeStamp, CheckIn.PAIN_LVL_SEVERE);
		if ( (numCheckinsBefore12h > 0) && (numCheckins12h > 0) &&
				(checkinsLast12hSevere.size() == numCheckins12h) ) {
			patient.getAlert().setTwelveHoursPain(true);
		}
		
		// 12 hours of "I can't eat" alert
		List<CheckIn> checkinsLast12hCantEat = checkins
				.findByPatientAndTimeStampGreaterThanAndStopEatingEquals(
						patient, init12TimeStamp, CheckIn.CANT_EAT);
		if ( (numCheckinsBefore12h > 0) && (numCheckins12h > 0) &&
				(checkinsLast12hCantEat.size() == numCheckins12h) ) {
			patient.getAlert().setTwelveHoursEat(true);
		}
		
		// All checkins from the last 16 hours
		List<CheckIn> allCheckins16h = checkins
				.findByPatientAndTimeStampGreaterThan(patient, init16TimeStamp);
		int numCheckins16h = allCheckins16h.size();
		
		// 16 hours of at least moderate pain alert
		List<CheckIn> checkins16hNotWell = checkins
				.findByPatientAndTimeStampGreaterThanAndPainLevelNotLike(
						patient, init16TimeStamp, CheckIn.PAIN_LVL_WELL);
		
		if ( (numCheckinsBefore16h > 0) && (numCheckins16h > 0) &&
				(checkins16hNotWell.size() == numCheckins16h) ) {
			patient.getAlert().setSixteenHoursPain(true);
		}
						
	}
	
}
