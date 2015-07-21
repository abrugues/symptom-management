package org.coursera.capstone.server.repo;

import java.util.List;

import org.coursera.capstone.server.model.CheckIn;
import org.coursera.capstone.server.model.Patient;
import org.springframework.data.repository.CrudRepository;

public interface CheckInRepo extends CrudRepository<CheckIn, Long> {

	public List<CheckIn> findByPatient(Patient p);
	
	public List<CheckIn> findByPatientAndTimeStampGreaterThan(Patient p, long timeStamp);
	
	public List<CheckIn> findByPatientAndTimeStampLessThan(Patient p, long timeStamp);
	
	public List<CheckIn> findByPatientAndTimeStampGreaterThanAndPainLevelEquals(
			Patient p, long timeStamp, String painLevel);
	
	public List<CheckIn> findByPatientAndTimeStampGreaterThanAndStopEatingEquals(
			Patient p, long timeStamp, String stopEating);
	
	public List<CheckIn> findByPatientAndTimeStampGreaterThanAndPainLevelNotLike(
			Patient p, long timeStamp, String painLevel);
	
}
