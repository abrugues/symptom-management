package org.coursera.capstone.server.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Prescription {
	
	@Id
	@Column(name = "PRESC_ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private String name;
	private boolean taken;
	private long timeStamp;
	
	@ManyToOne
	@JoinColumn(name = "CHK_ID")
	@JsonIgnore
	private CheckIn checkin;
	
	public Prescription() {
		
	}

	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public boolean isTaken() {
		return taken;
	}
	
	public void setTaken(boolean taken) {
		this.taken = taken;
	}

	public long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}
	
	public CheckIn getCheckin() {
		return checkin;
	}
	
	public void setCheckIn(CheckIn checkin) {
		this.checkin = checkin;
	}
	
	@Override
	public String toString() {
		return "Prescription [name=" + name + ", taken=" + taken + ", timeStamp=" + timeStamp + "]";
	}
}