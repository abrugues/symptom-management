package org.coursera.capstone.model;

public class Alert {

	private boolean twelveHoursPain = false;
	private boolean sixteenHoursPain = false;
	private boolean twelveHoursEat = false;
	
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
	
	@Override
	public String toString() {
		return "Alert [12hPain=" + twelveHoursPain + ", 16hPain=" + sixteenHoursPain  + ", 12hCantEat=" + twelveHoursEat + "]";
	}
	
}
