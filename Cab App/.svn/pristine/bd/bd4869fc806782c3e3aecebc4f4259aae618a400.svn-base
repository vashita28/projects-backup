package com.handpoint.headstart.android;

import android.os.Parcel;
import android.os.Parcelable;
import com.handpoint.headstart.api.FinancialTransactionResult;
/**
 * 
 * Parcel for FinancialTransactionResult object for passing it in intents.
 *
 */
public class ParcelFinancialTransactionResult
        implements Parcelable {

    public static final Creator CREATOR = new Creator();

    private FinancialTransactionResult result;

    public ParcelFinancialTransactionResult(FinancialTransactionResult result) {
        this.result = result;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        CREATOR.writeToParcel(this, parcel, flags);
    }

    public FinancialTransactionResult getResult() {
        return this.result;
    }
    
    public static class Creator
            implements Parcelable.Creator<ParcelFinancialTransactionResult> {

        public ParcelFinancialTransactionResult createFromParcel(Parcel parcel) {
            int operationStatus = parcel.readInt();
            int transactionStatus = parcel.readInt();
            int type = parcel.readInt();
            int authorizedAmount = parcel.readInt();
            String currency = parcel.readString();
            String transactionId = parcel.readString();
            String merchantReceipt = parcel.readString();
            String customerReceipt = parcel.readString();
            String statusMessage = parcel.readString();
            String transactionType = parcel.readString();
            String financialStatus = parcel.readString();
            Integer requestedAmount = (Integer) parcel.readValue(null);
            Integer gratuityAmount = (Integer) parcel.readValue(null);
            Integer gratuityPercentage = (Integer) parcel.readValue(null);
            Integer totalAmount = (Integer) parcel.readValue(null);
            String eftTransactionId = parcel.readString();
            String eftTimestamp = parcel.readString();
            String authorisationCode = parcel.readString();
            String cvm = parcel.readString();
            String cardEntryType = parcel.readString();
            String cardSchemeName = parcel.readString();
            String errorMessage = parcel.readString();
            String customerReference = parcel.readString();
            String budgetNumber = parcel.readString();
            
            FinancialTransactionResult result = 
            		new FinancialTransactionResult(
            				operationStatus, transactionStatus, authorizedAmount, transactionId, merchantReceipt, customerReceipt,
            				statusMessage, transactionType, financialStatus, requestedAmount, 
            				gratuityAmount, gratuityPercentage, totalAmount, currency, eftTransactionId, 
            				eftTimestamp, authorisationCode, cvm, cardEntryType, cardSchemeName,
            				errorMessage, customerReference, budgetNumber);
            result.type = type;
            result.currency = currency;
            return new ParcelFinancialTransactionResult(result);
        }

        public void writeToParcel(ParcelFinancialTransactionResult data, Parcel parcel, int flags) {
            parcel.writeInt(data.result.operationStatus);
            parcel.writeInt(data.result.transactionStatus);
            parcel.writeInt(data.result.type);
            parcel.writeInt(data.result.authorizedAmount);
            parcel.writeString(data.result.currency);
            parcel.writeString(data.result.transactionId);
            parcel.writeString(data.result.merchantReceipt);
            parcel.writeString(data.result.customerReceipt);
            parcel.writeString(data.result.statusMessage);
            parcel.writeString(data.result.transactionType);
            parcel.writeString(data.result.financialStatus);
            parcel.writeValue(data.result.requestAmount);
            parcel.writeValue(data.result.gratuityAmount);
            parcel.writeValue(data.result.gratuityPercentage);
            parcel.writeValue(data.result.totalAmount);
            parcel.writeString(data.result.eftTransactionId);
            parcel.writeString(data.result.eftTimestamp);
            parcel.writeString(data.result.authorisationCode);
            parcel.writeString(data.result.cvm);
            parcel.writeString(data.result.cardEntryType);
            parcel.writeString(data.result.cardSchemeName);
            parcel.writeString(data.result.errorMessage);
            parcel.writeString(data.result.customerReference);
            parcel.writeString(data.result.budgetNumber);
        }

        public ParcelFinancialTransactionResult[] newArray(int size) {
            return new ParcelFinancialTransactionResult[size];
        }
    }

}
