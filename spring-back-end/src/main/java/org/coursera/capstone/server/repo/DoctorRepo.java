package org.coursera.capstone.server.repo;

import org.coursera.capstone.server.model.Doctor;
import org.springframework.data.repository.CrudRepository;

public interface DoctorRepo extends CrudRepository<Doctor, Long> {
	
	public Doctor findByUserName(String userName);
	
}
