package com.example.cabapppassenger.model;

import android.os.Parcel;
import android.os.Parcelable;

public class LocationParcelable implements Parcelable, Cloneable {

	private String address1;
	private String pinCode;
	private double latitude;
	private double longitude;

	public LocationParcelable getClone() {
		try {
			return (LocationParcelable) this.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public LocationParcelable() {
		address1 = "";
		pinCode = "";
		latitude = 0;
		longitude = 0;
	}

	/** Ctor from Parcel, reads back fields IN THE ORDER they were written */
	public LocationParcelable(Parcel pc) {
		address1 = pc.readString();
		pinCode = pc.readString();
		latitude = pc.readDouble();
		longitude = pc.readDouble();
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		if (address1 != null)
			this.address1 = address1;
	}

	public String getPinCode() {

		return pinCode;
	}

	public void setPinCode(String pinCode) {
		if (pinCode != null)
			this.pinCode = pinCode;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {

		dest.writeString(address1);
		dest.writeString(pinCode);
		dest.writeDouble(latitude);
		dest.writeDouble(longitude);

	}

	/** Static field used to regenerate object, individually or as arrays */
	public static final Parcelable.Creator<LocationParcelable> CREATOR = new Parcelable.Creator<LocationParcelable>() {
		public LocationParcelable createFromParcel(Parcel pc) {
			return new LocationParcelable(pc);
		}

		public LocationParcelable[] newArray(int size) {
			return new LocationParcelable[size];
		}
	};

}
