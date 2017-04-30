package org.coursera.capstone.server.controller;

import java.security.Principal;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.coursera.capstone.server.model.CheckIn;
import org.coursera.capstone.server.model.Doctor;
import org.coursera.capstone.server.model.Patient;
import org.coursera.capstone.server.repo.CheckInRepo;
import org.coursera.capstone.server.repo.DoctorRepo;
import org.coursera.capstone.server.repo.PatientRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;

@Controller
public class DoctorController {
	
	private static final String DOCTOR_ID = "docId";
	private static final String PAT_RECORD_NUM = "recordNum";
	
	private static final String DOCTOR_SVC = "/doctor";
	private static final String DOCTOR_SVC_PAT_LIST = DOCTOR_SVC + "/{" + DOCTOR_ID + "}";
	private static final String DOCTOR_SVC_PAT_CHK_LIST = DOCTOR_SVC_PAT_LIST + "/{" + PAT_RECORD_NUM + "}";
	private static final String DOCTOR_SVC_ADD_DRUG = DOCTOR_SVC_PAT_LIST + "/{" + PAT_RECORD_NUM + "}" + "/newDrug";
	
	@Autowired
	private DoctorRepo doctors;
	
	@Autowired
	private PatientRepo patients;
	
	@Autowired
	private CheckInRepo checkins;
	
	@PreAuthorize("hasRole('DOCTOR')")
	@RequestMapping(value=DOCTOR_SVC, method=RequestMethod.GET)
	public @ResponseBody Doctor getDoctor(Principal principal) {
		String userName = principal.getName();
		return doctors.findByUserName(userName);
	}
	
	@PreAuthorize("hasRole('DOCTOR')")
	@RequestMapping(value=DOCTOR_SVC_PAT_LIST, method=RequestMethod.GET)
	public @ResponseBody Collection<Patient> getPatientList(
			@PathVariable(DOCTOR_ID) long doctorId,
			HttpServletResponse response) {
		
		if (!doctors.exists(doctorId)) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}
		
		Doctor d = doctors.findOne(doctorId);
		return Lists.newArrayList(d.getPatients());
	}
	
	@PreAuthorize("hasRole('DOCTOR')")
	@RequestMapping(value=DOCTOR_SVC_PAT_CHK_LIST, method=RequestMethod.GET)
	public @ResponseBody List<CheckIn> getCheckinsList(
			@PathVariable(DOCTOR_ID) long doctorId,
			@PathVariable(PAT_RECORD_NUM) String recordNum,
			HttpServletResponse response) {
		
		if (!doctors.exists(doctorId)) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}
		
		Doctor d = doctors.findOne(doctorId);
		Patient p = patients.findByRecordNumAndDoctor(recordNum, d);
		
		if (p == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
		
		List<CheckIn> patientCheckins = checkins.findByPatient(p);
		
		for (CheckIn checkin : patientCheckins) {
			System.out.println(checkin.toString());
		}
		
		Collections.reverse(patientCheckins);
		
		return patientCheckins;
	}
	
	@PreAuthorize("hasRole('DOCTOR')")
	@RequestMapping(value=DOCTOR_SVC_ADD_DRUG, method=RequestMethod.POST)
	public void addMedication(
			@PathVariable(DOCTOR_ID) long doctorId,
			@PathVariable(PAT_RECORD_NUM) String recordNum, 
			@RequestBody String medication,
			HttpServletResponse response) {
		
		if (!doctors.exists(doctorId)) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		
		Doctor d = doctors.findOne(doctorId);
		Patient p = patients.findByRecordNumAndDoctor(recordNum, d);
		
		if (p == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		
		// Delete the quotation marks
		medication = medication.substring(1, medication.length() - 1);
		// Capitalize first letter
		medication = medication.substring(0, 1).toUpperCase() + medication.substring(1);
		
		p.getMedications().add(medication);
		patients.save(p);
	}
}
