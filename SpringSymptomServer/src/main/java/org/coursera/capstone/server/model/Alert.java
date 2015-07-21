package org.coursera.capstone.server.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Alert {

	@Id
	@Column(name = "ALERT_ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private boolean twelveHoursPain = false;
	private boolean sixteenHoursPain = false;
	private boolean twelveHoursEat = false;
	
	@OneToOne(mappedBy = "alert")
	@JsonIgnore
	private Patient patient;
	
	public Alert() {
		
	}

	public boolean isTwelveHoursPain() {
		return twelveHoursPain;
	}

	public void setTwelveHoursPain(boolean twelveHoursPain) {
		this.twelveHoursPain = twelveHoursPain;
	}

	public boolean isSixteenHoursPain() {
		return sixteenHoursPain;
	}

	public void setSixteenHoursPain(boolean sixteenHoursPain) {
		this.sixteenHoursPain = sixteenHoursPain;
	}

	public boolean isTwelveHoursEat() {
		return twelveHoursEat;
	}

	public void setTwelveHoursEat(boolean twelveHoursEat) {
		this.twelveHoursEat = twelveHoursEat;
	}
}
