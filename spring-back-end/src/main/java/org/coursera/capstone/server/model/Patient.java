package org.coursera.capstone.server.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity(name = "PATIENT")
public class Patient {

	@Id
	@Column(name = "PAT_ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private String userName;
	
	private String firstName;
	private String lastName;
	private Date birthDate;
	
	@Column(unique = true)
	private String recordNum;
	
	@ElementCollection
	private Set<String> medications;
	
	@OneToOne(cascade = CascadeType.ALL)
	private Alert alert;
	
	@ManyToOne
	@JoinColumn(name = "DOC_ID")
	@JsonIgnore
	private Doctor doctor;
	
	@OneToMany(mappedBy = "patient")
	@JsonIgnore
	private List<CheckIn> checkIns;
		
	public Patient() {
		
	}
	
	public Patient(String userName, String firstName, String lastName,
			Date birthDate, String recordNum, Set<String> medications) {
		this.userName = userName;
		this.firstName = firstName;
		this.lastName = lastName;
		this.birthDate = birthDate;
		this.recordNum = recordNum;
		this.medications = medications;
		this.alert = new Alert();
		this.checkIns = new ArrayList<CheckIn>();
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
	
	public Doctor getDoctor() {
		return doctor;
	}

	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
	}
	
	public List<CheckIn> getCheckIns() {
		return checkIns;
	}
	
	public void setCheckIns(List<CheckIn> checkIns) {
		this.checkIns = checkIns;
	}
}
