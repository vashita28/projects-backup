package com.handpoint.headstart.android;

import java.util.Collection;
import java.util.Iterator;

import android.os.Parcel;
import android.os.Parcelable;

import com.handpoint.headstart.api.DeviceDescriptor;
/**
 * 
 * Parcel for DeviceDescriptor object for passing it in intents.
 *
 */
public class ParcelDeviceDescriptor implements Parcelable {

    public static final Creator CREATOR = new Creator();

    private DeviceDescriptor deviceDescriptor;
    
    public ParcelDeviceDescriptor(DeviceDescriptor deviceDescriptor) {
    	this.deviceDescriptor = deviceDescriptor;
    }
    
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        CREATOR.writeToParcel(this, parcel, flags);
    }

    public DeviceDescriptor getResult() {
        return this.deviceDescriptor;
    }
    
    public static class Creator implements Parcelable.Creator<ParcelDeviceDescriptor> {

		public ParcelDeviceDescriptor createFromParcel(Parcel source) {
			String type = source.readString();
			String name = source.readString();
			int attrCount = source.readInt();
			DeviceDescriptor result = new DeviceDescriptor(type, name);
			if (attrCount > 0 ) {
				String [] keys = new String[attrCount];
				source.readStringArray(keys);
				String [] values = new String[attrCount];;
				source.readStringArray(values);
				for (int i = 0; i < keys.length; i++) {
					result.setAttribute(keys[i], values[i]);
				}
			}
			return new ParcelDeviceDescriptor(result);
		}
		
        public void writeToParcel(ParcelDeviceDescriptor data, Parcel parcel, int flags) {
        	parcel.writeString(data.deviceDescriptor.getType());
        	parcel.writeString(data.deviceDescriptor.getName());
        	Collection<String> attributes = data.deviceDescriptor.attributes();
        	if (null != attributes) {
        		parcel.writeInt(attributes.size());
        		if (attributes.size() > 0) {
	        		String[] keys = new String[attributes.size()];
	        		String[] values = new String[attributes.size()];
	        		int i = 0;
	        		for (Iterator<String> iterator = attributes.iterator(); iterator.hasNext(); i++) {
						String key = iterator.next();
						keys[i] = key;
						values[i] = data.deviceDescriptor.getAttribute(key);
					}
	        		parcel.writeStringArray(keys);
	        		parcel.writeStringArray(values);
        		}
        	} else {
        		parcel.writeInt(0);
        	}
        }

		public ParcelDeviceDescriptor[] newArray(int size) {
			return new ParcelDeviceDescriptor[size];
		}
    	
    }
}
