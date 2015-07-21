package org.coursera.capstone.server.repo;

import java.util.List;

import org.coursera.capstone.server.model.Doctor;
import org.coursera.capstone.server.model.Patient;
import org.springframework.data.repository.CrudRepository;

public interface PatientRepo extends CrudRepository<Patient, Long> {

	public Patient findByUserName(String userName);
	
	public List<Patient> findByFirstNameAndDoctor(String firstName, Doctor doctor);
	
	public Patient findByRecordNumAndDoctor(String recordNum, Doctor doctor);
	
}
