package org.coursera.capstone.server.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity(name = "DOCTOR")
public class Doctor {

	@Id
	@Column(name = "DOC_ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private String userName;
	
	private String firstName;
	private String lastName;
	
	@Column(unique = true)
	private String docUId;
		

	@OneToMany(mappedBy = "doctor")
	@JsonIgnore
	private List<Patient> patients;

	public Doctor() {

	}
	
	public Doctor(String userName, String firstName, String lastName, String docUId) {
		this.userName = userName;
		this.firstName = firstName;
		this.lastName = lastName;
		this.docUId = docUId;
		this.patients = new ArrayList<Patient>();
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

	public String getDocUId() {
		return docUId;
	}

	public void setdocUId(String docUId) {
		this.docUId = docUId;
	}
	
	public List<Patient> getPatients() {
		return patients;
	}

	public void setPatients(List<Patient> patients) {
		this.patients = patients;
	}
}
