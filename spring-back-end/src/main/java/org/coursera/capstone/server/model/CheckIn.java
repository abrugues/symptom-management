package org.coursera.capstone.server.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity(name = "CHECKIN")
public class CheckIn {
	
	public static final String PAIN_LVL_WELL = "Well-controlled";
	public static final String PAIN_LVL_SEVERE = "Severe";
	public static final String CANT_EAT = "I can't eat";
	
	@Id
	@Column(name = "CHK_ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private long timeStamp;
	
	private String painLevel;
	
	private String stopEating;
	
	private boolean painMedication;
	
	@OneToMany(mappedBy = "checkin", cascade = CascadeType.ALL)
	private List<Prescription> prescriptionList;
	
	@ManyToOne
	@JoinColumn(name = "PAT_ID")
	@JsonIgnore
	private Patient patient;

	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getPainLevel() {
		return painLevel;
	}

	public void setPainLevel(String painLevel) {
		this.painLevel = painLevel;
	}
	
	public String getStopEating() {
		return stopEating;
	}

	public void setStopEating(String stopEating) {
		this.stopEating = stopEating;
	}

	public boolean isPainMedication() {
		return painMedication;
	}

	public void setPainMedication(boolean painMedication) {
		this.painMedication = painMedication;
	}

	public List<Prescription> getPrescriptionList() {
		return prescriptionList;
	}

	public void setPrescriptionList(List<Prescription> prescriptionList) {
		this.prescriptionList = prescriptionList;
	}
	
	public Patient getPatient() {
		return patient;
	}
	
	public void setPatient(Patient patient) {
		this.patient = patient;
	}
	
	@Override
	public String toString() {
		return "CheckIn [id=" + id + ", prescriptionList=" + prescriptionList.toString() + "]";
	}
}