package org.coursera.capstone.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.os.Parcel;
import android.os.Parcelable;

public class Reminder implements Parcelable {
	
	private long mId;
	private int mHour;
	private int mMinute;
	private boolean mActive;

	public long getId() {
		return mId;
	}
	
	public void setId(long id) {
		this.mId = id;
	}
	
	public int getHour() {
		return mHour;
	}

	public void setHour(int hour) {
		this.mHour = hour;
	}

	public int getMinute() {
		return mMinute;
	}

	public void setMinute(int minute) {
		this.mMinute = minute;
	}

	public boolean isActive() {
		return mActive;
	}

	public void setActive(boolean active) {
		this.mActive = active;
	}
	
	public long getTimeNextAlarm() {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(System.currentTimeMillis());
		
		// Add one day depending on the current time
		int currentHour = c.get(Calendar.HOUR_OF_DAY);
		int currentMinute = c.get(Calendar.MINUTE);
		if ((mHour <= currentHour) && (mMinute <= currentMinute)) {
			c.set(Calendar.DAY_OF_YEAR, c.get(Calendar.DAY_OF_YEAR) + 1);
		}
		c.set(Calendar.HOUR_OF_DAY, mHour);
		c.set(Calendar.MINUTE, mMinute);
		c.set(Calendar.SECOND, 0);
		return c.getTimeInMillis();
	}
	
	@Override
	public String toString() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, mHour);
		c.set(Calendar.MINUTE, mMinute);
		
		Date d = c.getTime();
		return new SimpleDateFormat("HH:mm", Locale.getDefault()).format(d);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Reminder) {
			Reminder other = (Reminder) obj;
			if ((other.getHour() == this.mHour) && 
					(other.getMinute() == this.mMinute)) {
				return true;
			}
		}
		return false;
	}
	
	public static final Parcelable.Creator<Reminder> CREATOR = new Creator<Reminder>() {
		
		@Override
		public Reminder createFromParcel(Parcel source) {
			Reminder mReminder = new Reminder();
			mReminder.mId = source.readLong();
			mReminder.mHour = source.readInt();
			mReminder.mMinute = source.readInt();
			mReminder.mActive = (source.readByte() == 1? true : false);
			return mReminder;
		}

		@Override
		public Reminder[] newArray(int size) {
			return new Reminder[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(mId);
		dest.writeInt(mHour);
		dest.writeInt(mMinute);
		dest.writeByte((byte) (mActive? 1 : 0));  		
	}
}