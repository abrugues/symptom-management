package org.coursera.capstone.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.os.Parcel;
import android.os.Parcelable;

public class CheckIn implements Parcelable {
	
	private long id = 0;
	private long timeStamp;
	
	private String painLevel;
	private String stopEating;
	private boolean painMedication;
	private List<Prescription> prescriptionList;
		
	public CheckIn() {

	}
	
	public String getStringTimeStamp() {
		Date date = new Date(timeStamp);
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/y @ HH:mm", Locale.getDefault());
		return sdf.format(date);
	}
	
	public String getStringDate() {
		Date date = new Date(timeStamp);
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/y", Locale.getDefault());
		return sdf.format(date);
	}
	
	public String getStringTime() {
		Date date = new Date(timeStamp);
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
		return sdf.format(date);
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

	public void setPrescriptionList(List<Prescription> medicationsList) {
		this.prescriptionList = medicationsList;
	}
	
	public static final Parcelable.Creator<CheckIn> CREATOR = new Creator<CheckIn>() {
		
		@Override
		public CheckIn createFromParcel(Parcel source) {			
			CheckIn checkin = new CheckIn();
			checkin.timeStamp = source.readLong();
			checkin.painLevel = source.readString();
			checkin.stopEating = source.readString();
			checkin.painMedication = (source.readByte() == 1? true : false);
			checkin.prescriptionList = source.readArrayList(Prescription.class.getClassLoader());
			return checkin;
		}

		@Override
		public CheckIn[] newArray(int size) {
			return new CheckIn[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(this.timeStamp);
		dest.writeString(this.painLevel);
		dest.writeString(this.stopEating);
		dest.writeByte((byte) (this.painMedication? 1 : 0));
		dest.writeList(prescriptionList);
	}
}
