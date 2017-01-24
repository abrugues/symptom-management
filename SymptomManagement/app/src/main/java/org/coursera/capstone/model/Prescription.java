package org.coursera.capstone.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.os.Parcel;
import android.os.Parcelable;


public class Prescription implements Parcelable {

	private long id = 0;
	
	private String name;
	private boolean taken;
	private long timeStamp;
	
	public Prescription() {
		
	}
	
	public Prescription(String name, long timeStamp) {
		this.name = name;
		this.taken = true;
		this.timeStamp = timeStamp;
	}
	
	public Prescription(String name) {
		this.name = name;
		this.taken = false;
		this.timeStamp = 0;
	}
	
	public String getStringTimeStamp() {
		Date d = new Date(timeStamp);
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/y @ HH:mm", Locale.getDefault());
		return sdf.format(d);
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
	
	@Override
	public boolean equals(Object obj) {
		return (obj instanceof Prescription)
				&& ((Prescription)obj).getName().equals(name);
	}
	
	@Override
	public int hashCode() {
		return (int) name.hashCode();
	}
	
	public static final Parcelable.Creator<Prescription> CREATOR = new Creator<Prescription>() {

		@Override
		public Prescription createFromParcel(Parcel source) {
			Prescription prescription = new Prescription();
			prescription.name = source.readString();
			prescription.taken = (source.readByte() == 1? true : false);
			prescription.timeStamp = source.readLong();
			return prescription;
		}

		@Override
		public Prescription[] newArray(int size) {
			return new Prescription[size];
		}
		
	};
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.name);
		dest.writeByte((byte) (this.taken? 1 : 0));
		dest.writeLong(this.timeStamp);
	}
}
