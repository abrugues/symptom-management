package org.coursera.capstone.server.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;

import org.coursera.capstone.server.model.Doctor;
import org.coursera.capstone.server.model.Patient;
import org.coursera.capstone.server.repo.DoctorRepo;
import org.coursera.capstone.server.repo.PatientRepo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class InitController implements InitializingBean {
	
	private static final String LORTAB = "Lortab";
	private static final String OXYCONTIN = "OxyContin";
	
	@Autowired
	private DoctorRepo doctors;
	
	@Autowired
	private PatientRepo patients;

	@Override
	public void afterPropertiesSet() throws Exception {
		//Create doctor 1
		Doctor d  = new Doctor("jwatson", "John", "Watson", "D-S00000H");
		doctors.save(d);
		
		//Create doctor 2
		Doctor d2  = new Doctor("ghouse", "Gregory", "House", "D-S00000I");
		doctors.save(d2);
		
		//Create patient 1
		//Birthday
		Date birthDateP1 = parseDate("23/04/1973");
		//Set of medications for the patient
		HashSet<String> setMed1 = new HashSet<String>();
		setMed1.add(LORTAB);
		setMed1.add(OXYCONTIN);
		Patient p1 = new Patient("jodoe", "John", "Doe",
				birthDateP1, "P-X12345S", setMed1);
		p1.setDoctor(d);
		patients.save(p1);
		
		//Create patient 2
		Date birthDateP2 = parseDate("12/08/1977");
		HashSet<String> setMed2 = new HashSet<String>();
		setMed2.add(OXYCONTIN);
		Patient p2 = new Patient("jadoe", "Jane", "Doe",
				birthDateP2, "P-S54321X", setMed2);
		p2.setDoctor(d);
		patients.save(p2);
				
		//Create patient 3
		Date birthDateP3 = parseDate("09/06/1975");
		Patient p3 = new Patient("sseguin", "Sydney", "Seguin",
				birthDateP3, "P-A99999A", new HashSet<String>());
		p3.setDoctor(d2);
		patients.save(p3);
		
	}
	
	private Date parseDate(String dateString) {
		Date date = new Date();
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			date = formatter.parse(dateString);
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
}