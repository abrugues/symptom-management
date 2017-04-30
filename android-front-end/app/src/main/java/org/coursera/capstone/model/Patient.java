package org.coursera.capstone.model;

import java.util.Date;
import java.util.Set;

public class Patient {
	
	private long id;
	
	private String userName;
	
	private String firstName;
	private String lastName;
	private Date birthDate;
	
	private String recordNum;
	
	private Set<String> medications;
	
	private Alert alert;
	
		
	public Patient() {
		
	}
	
	public String getFullName() {
		return firstName + " " + lastName;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public String getRecordNum() {
		return recordNum;
	}

	public void setRecordNum(String recordNum) {
		this.recordNum = recordNum;
	}

	public Set<String> getMedications() {
		return medications;
	}

	public void setMedications(Set<String> medications) {
		this.medications = medications;
	}
	
	public Alert getAlert() {
		return alert;
	}
	
	public void setAlerts(Alert alert) {
		this.alert = alert;
	}
}