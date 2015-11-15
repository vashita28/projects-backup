/*
 *
 *  * This file is subject to the terms and conditions defined in
 *  * file 'license.txt', which is part of this source code package.
 *
 */

package com.handpoint.headstart.android;

import android.os.Parcel;
import android.os.Parcelable;
import com.handpoint.headstart.api.BluetoothInformation;
import com.handpoint.headstart.api.XMLCommandResult;
/**
 *
 * Parcel for XML Command Result object for passing it in intents.
 *
 */
public class ParcelXmlCommandResult
    implements Parcelable {

        public static final Creator CREATOR = new Creator();

        private XMLCommandResult result;

        public ParcelXmlCommandResult(XMLCommandResult result) {
            this.result = result;
        }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        CREATOR.writeToParcel(this, parcel, flags);
    }

    public XMLCommandResult getResult() {
        return this.result;
    }

    public static class Creator
            implements Parcelable.Creator<ParcelXmlCommandResult> {

        public ParcelXmlCommandResult createFromParcel(Parcel parcel) {
            int type = parcel.readInt();
            String statusMessage = parcel.readString();
            String serialNumber = parcel.readString();
            int batteryStatus = parcel.readInt();
            int batteryMv = parcel.readInt();
            boolean batteryCharging = parcel.readByte() == 1;
            boolean externalPower= parcel.readByte() == 1;

            BluetoothInformation btInfo = new BluetoothInformation();
            btInfo.bTReconnectRequired = parcel.readByte() == 1;
            btInfo.bluetoothName = parcel.readString();
            btInfo.oldBluetoothName = parcel.readString();

            XMLCommandResult result =
                    new XMLCommandResult(type, btInfo, statusMessage, serialNumber, batteryStatus, batteryMv, batteryCharging, externalPower);

            return new ParcelXmlCommandResult(result);
        }

        public void writeToParcel(ParcelXmlCommandResult data, Parcel parcel, int flags) {
            parcel.writeInt(data.result.type);
            parcel.writeString(data.result.statusMessage);
            parcel.writeString(data.result.serialNumber);
            parcel.writeInt(data.result.batteryStatus);
            parcel.writeInt(data.result.batteryMv);
            parcel.writeByte((byte) (data.result.batteryCharging ? 1 : 0));
            parcel.writeByte((byte)(data.result.externalPower ? 1 : 0));

            if(data.result.btInfo != null && data.result.btInfo.bTReconnectRequired != null)
                parcel.writeByte((byte)(data.result.btInfo.bTReconnectRequired ? 1 : 0));
            else
                parcel.writeByte((byte)0);

            if(data.result.btInfo != null && data.result.btInfo.bluetoothName != null)
                parcel.writeString(data.result.btInfo.bluetoothName);
            else
                parcel.writeString("");

            if(data.result.btInfo != null && data.result.btInfo.oldBluetoothName != null)
                parcel.writeString(data.result.btInfo.oldBluetoothName);
            else
                parcel.writeString("");

        }

        public ParcelXmlCommandResult[] newArray(int size) {
            return new ParcelXmlCommandResult[size];
        }
    }

}
