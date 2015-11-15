package com.handpoint.headstart.android;

import android.os.Parcel;
import android.os.Parcelable;

import com.handpoint.headstart.eft.DeviceState;

/**
 * 
 * Parcel for DeviceDescriptor object for passing it in intents.
 *
 */
public class ParcelDeviceState implements Parcelable {

    public static final Creator CREATOR = new Creator();

    private DeviceState deviceState;
    
    public ParcelDeviceState(DeviceState deviceState) {
    	this.deviceState = deviceState;
    }
    
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        CREATOR.writeToParcel(this, parcel, flags);
    }

    public DeviceState getResult() {
        return this.deviceState;
    }
    
    public static class Creator implements Parcelable.Creator<ParcelDeviceState> {

		public ParcelDeviceState createFromParcel(Parcel source) {
			int state = source.readInt();
			String statusMessage = source.readString();
			boolean cancelAllowed = source.readInt() == 1;
			DeviceState result = new DeviceState(state, statusMessage, cancelAllowed);
			return new ParcelDeviceState(result);
		}
		
        public void writeToParcel(ParcelDeviceState data, Parcel parcel, int flags) {
        	parcel.writeInt(data.deviceState.getState());
        	parcel.writeString(data.deviceState.getStatusMessage());
        	parcel.writeInt(data.deviceState.isCancelAllowed() ? 1 : 0);
        }

		public ParcelDeviceState[] newArray(int size) {
			return new ParcelDeviceState[size];
		}
    	
    }
}
