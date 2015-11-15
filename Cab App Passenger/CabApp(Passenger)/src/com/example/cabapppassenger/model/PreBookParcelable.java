package com.example.cabapppassenger.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.example.cabapppassenger.fragments.Pre_BookFragment.EnumLocations;
import com.example.cabapppassenger.fragments.Pre_BookFragment.EnumPaymentType;
import com.example.cabapppassenger.fragments.Pre_BookFragment.EnumWhen;

public class PreBookParcelable implements Parcelable, Cloneable {

	LocationParcelable presentLocation, mapPickUpLocation, mapDropOffLocation,
			favouritePickUpLocation, favouriteDropOffLocation,
			recentPickUpLocation, recentDropOffLocation, defaultPickUpLocation,
			defaultDropOffLocation;

	int selectedIndexPassengerCount;

	String comment, pickUpDate, pickUpTime, pickUpDateToShow,
			fixedPriceCurrency, bookingId, truncatedPan;

	Double fixedPrice;

	EnumLocations ePickUpSelected, eDropOffSelected;

	EnumWhen eWhen;

	EnumPaymentType ePaymentType;

	boolean isWheelChairSelected, isHearingImpairedSelected,
			isAccessibiltyExpanded, isCommentExpanded, isFixedPrice,
			isValuesUpdated, isPaymentTypeUpdated, hailNowPopUpVisible,
			isBackPressed;

	public PreBookParcelable getClone() {
		PreBookParcelable newParcelable = this;
		try {
			newParcelable = (PreBookParcelable) this.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		newParcelable.setPresentLocation(this.getPresentLocation().getClone());
		newParcelable.setMapPickUpLocation(this.getMapPickUpLocation()
				.getClone());
		newParcelable.setMapDropOffLocation(this.getMapDropOffLocation()
				.getClone());
		newParcelable.setRecentDropOffLocation(this.getRecentDropOffLocation()
				.getClone());
		newParcelable.setRecentPickUpLocation(this.getRecentPickUpLocation()
				.getClone());
		newParcelable.setFavouriteDropOffLocation(this
				.getFavouriteDropOffLocation().getClone());
		newParcelable.setFavouritePickUpLocation(this
				.getFavouritePickUpLocation().getClone());
		newParcelable.setDefaultDropOffLocation(this
				.getDefaultDropOffLocation().getClone());
		newParcelable.setDefaultPickUpLocation(this.getDefaultPickUpLocation()
				.getClone());

		return newParcelable;

	}

	public static final Parcelable.Creator<PreBookParcelable> CREATOR = new Parcelable.Creator<PreBookParcelable>() {
		public PreBookParcelable createFromParcel(Parcel in) {
			return new PreBookParcelable(in);
		}

		public PreBookParcelable[] newArray(int size) {
			return new PreBookParcelable[size];
		}
	};

	public String getTruncatedPan() {
		return truncatedPan;
	}

	public void setTruncatedPan(String truncatedPan) {
		this.truncatedPan = truncatedPan;
	}

	public boolean isHailNowPopUpVisible() {
		return hailNowPopUpVisible;
	}

	public void setHailNowPopUpVisible(boolean hailNowPopUpVisible) {
		this.hailNowPopUpVisible = hailNowPopUpVisible;
	}

	public boolean isBackPressed() {
		return isBackPressed;
	}

	public void setBackPressed(boolean isBackPressed) {
		this.isBackPressed = isBackPressed;
	}

	public LocationParcelable getDefaultPickUpLocation() {
		return defaultPickUpLocation;
	}

	public void setDefaultPickUpLocation(
			LocationParcelable defaultPickUpLocation) {
		this.defaultPickUpLocation = defaultPickUpLocation;
	}

	public LocationParcelable getDefaultDropOffLocation() {
		return defaultDropOffLocation;
	}

	public void setDefaultDropOffLocation(
			LocationParcelable defaultDropOffLocation) {
		this.defaultDropOffLocation = defaultDropOffLocation;
	}

	public boolean isPaymentTypeUpdated() {
		return isPaymentTypeUpdated;
	}

	public void setPaymentTypeUpdated(boolean isPaymentTypeUpdated) {
		this.isPaymentTypeUpdated = isPaymentTypeUpdated;
	}

	public boolean isValuesUpdated() {
		return isValuesUpdated;
	}

	public void setValuesUpdated(boolean isValuesUpdated) {
		this.isValuesUpdated = isValuesUpdated;
	}

	public PreBookParcelable(Parcel in) {
		readFromParcel(in);
	}

	@SuppressLint("SimpleDateFormat")
	public PreBookParcelable() {
		presentLocation = new LocationParcelable();
		mapPickUpLocation = new LocationParcelable();
		;
		favouritePickUpLocation = new LocationParcelable();
		recentPickUpLocation = new LocationParcelable();
		defaultPickUpLocation = new LocationParcelable();

		mapDropOffLocation = new LocationParcelable();

		favouriteDropOffLocation = new LocationParcelable();
		recentDropOffLocation = new LocationParcelable();
		defaultDropOffLocation = new LocationParcelable();
		defaultDropOffLocation.setAddress1("");
		selectedIndexPassengerCount = 0;
		comment = "";
		SimpleDateFormat sdf = new SimpleDateFormat("E dd LLL");
		pickUpDateToShow = sdf.format(new Date());
		sdf = new SimpleDateFormat("dd/MM/yyyy");
		pickUpDate = sdf.format(new Date());
		sdf = new SimpleDateFormat("HH:mm");
		pickUpTime = sdf.format(new Date());
		ePickUpSelected = EnumLocations.PRESENTLOCATION;
		eDropOffSelected = EnumLocations.DEFAULT;
		eWhen = EnumWhen.PREBOOK;
		ePaymentType = EnumPaymentType.CASH;
		isWheelChairSelected = false;

		isHearingImpairedSelected = false;
		isAccessibiltyExpanded = false;
		isCommentExpanded = false;
		isValuesUpdated = false;
		fixedPrice = 0.00;
		fixedPriceCurrency = "";
		bookingId = "";
		isFixedPrice = false;
		isPaymentTypeUpdated = false;
		hailNowPopUpVisible = false;
		truncatedPan = "";

	}

	/*
	 * public String getStateSelected() { return stateSelected; }
	 * 
	 * public void setStateSelected(String stateSelected) { this.stateSelected =
	 * stateSelected; }
	 */

	public String getPickUpDate() {
		return pickUpDate;
	}

	public void setPickUpDate(String pickUpDate) {
		this.pickUpDate = pickUpDate;
	}

	public String getPickUpTime() {
		return pickUpTime;
	}

	public void setPickUpTime(String pickUpTime) {
		this.pickUpTime = pickUpTime;
	}

	public LocationParcelable getPresentLocation() {
		return presentLocation;
	}

	public void setPresentLocation(LocationParcelable presentPickUpLocation) {
		this.presentLocation = presentPickUpLocation;
	}

	public LocationParcelable getMapPickUpLocation() {
		return mapPickUpLocation;
	}

	public void setMapPickUpLocation(LocationParcelable mapPickUpLocation) {
		this.mapPickUpLocation = mapPickUpLocation;
	}

	public LocationParcelable getMapDropOffLocation() {
		return mapDropOffLocation;
	}

	public void setMapDropOffLocation(LocationParcelable mapDropOffLocation) {
		this.mapDropOffLocation = mapDropOffLocation;
	}

	public LocationParcelable getFavouritePickUpLocation() {
		return favouritePickUpLocation;
	}

	public void setFavouritePickUpLocation(
			LocationParcelable favouritePickUpLocation) {
		this.favouritePickUpLocation = favouritePickUpLocation;
	}

	public LocationParcelable getFavouriteDropOffLocation() {
		return favouriteDropOffLocation;
	}

	public void setFavouriteDropOffLocation(
			LocationParcelable favouriteDropOffLocation) {
		this.favouriteDropOffLocation = favouriteDropOffLocation;
	}

	public LocationParcelable getRecentPickUpLocation() {
		return recentPickUpLocation;
	}

	public void setRecentPickUpLocation(LocationParcelable recentPickUpLocation) {
		this.recentPickUpLocation = recentPickUpLocation;
	}

	public LocationParcelable getRecentDropOffLocation() {
		return recentDropOffLocation;
	}

	public void setRecentDropOffLocation(
			LocationParcelable recentDropOffLocation) {
		this.recentDropOffLocation = recentDropOffLocation;
	}

	public int getPassengerCount() {
		return selectedIndexPassengerCount;
	}

	public void setPassengerCount(int passengerCount) {
		this.selectedIndexPassengerCount = passengerCount;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public EnumLocations getePickUpSelected() {
		return ePickUpSelected;
	}

	public void setePickUpSelected(EnumLocations ePickUpSelected) {
		this.ePickUpSelected = ePickUpSelected;
	}

	public EnumLocations geteDropOffSelected() {
		return eDropOffSelected;
	}

	public void seteDropOffSelected(EnumLocations eDropOffSelected) {
		this.eDropOffSelected = eDropOffSelected;
	}

	public EnumWhen geteWhen() {
		return eWhen;
	}

	public void seteWhen(EnumWhen eWhen) {
		this.eWhen = eWhen;
	}

	public boolean isWheelChairSelected() {
		return isWheelChairSelected;
	}

	public void setWheelChairSelected(boolean isWheelChairSelected) {
		this.isWheelChairSelected = isWheelChairSelected;
	}

	public boolean isHearingImpairedSelected() {
		return isHearingImpairedSelected;
	}

	public void setHearingImpairedSelected(boolean isHearingImpairedSelected) {
		this.isHearingImpairedSelected = isHearingImpairedSelected;
	}

	public boolean isAccessibiltyExpanded() {
		return isAccessibiltyExpanded;
	}

	public void setAccessibiltyExpanded(boolean isAccessibiltyExpanded) {
		this.isAccessibiltyExpanded = isAccessibiltyExpanded;
	}

	public boolean isCommentExpanded() {
		return isCommentExpanded;
	}

	public void setCommentExpanded(boolean isCommentExpanded) {
		this.isCommentExpanded = isCommentExpanded;
	}

	public String getPickUpDateToShow() {
		return pickUpDateToShow;
	}

	public void setPickUpDateToShow(String pickUpDateToShow) {
		this.pickUpDateToShow = pickUpDateToShow;
	}

	public String getFixedPriceCurrency() {
		return fixedPriceCurrency;
	}

	public void setFixedPriceCurrency(String fixedPriceCurrency) {
		this.fixedPriceCurrency = fixedPriceCurrency;
	}

	public Double getFixedPrice() {
		return fixedPrice;
	}

	public void setFixedPrice(Double fixedPrice) {
		this.fixedPrice = fixedPrice;
	}

	public boolean isFixedPrice() {
		return isFixedPrice;
	}

	public void setIsFixedPrice(boolean isFixedPrice) {
		this.isFixedPrice = isFixedPrice;
	}

	public EnumPaymentType getePaymentType() {
		return ePaymentType;
	}

	public void setePaymentType(EnumPaymentType ePaymentType) {
		this.ePaymentType = ePaymentType;
	}

	public String getBookingId() {
		return bookingId;
	}

	public void setBookingId(String bookingId) {
		this.bookingId = bookingId;
	}

	public void readFromParcel(Parcel in) {

		presentLocation = in.readParcelable(LocationParcelable.class
				.getClassLoader());
		mapPickUpLocation = in.readParcelable(LocationParcelable.class
				.getClassLoader());
		mapDropOffLocation = in.readParcelable(LocationParcelable.class
				.getClassLoader());
		favouritePickUpLocation = in.readParcelable(LocationParcelable.class
				.getClassLoader());
		favouriteDropOffLocation = in.readParcelable(LocationParcelable.class
				.getClassLoader());
		recentPickUpLocation = in.readParcelable(LocationParcelable.class
				.getClassLoader());
		recentDropOffLocation = in.readParcelable(LocationParcelable.class
				.getClassLoader());
		defaultPickUpLocation = in.readParcelable(LocationParcelable.class
				.getClassLoader());
		defaultDropOffLocation = in.readParcelable(LocationParcelable.class
				.getClassLoader());
		selectedIndexPassengerCount = in.readInt();
		comment = in.readString();
		pickUpDate = in.readString();
		pickUpTime = in.readString();
		pickUpDateToShow = in.readString();
		fixedPriceCurrency = in.readString();
		bookingId = in.readString();
		truncatedPan = in.readString();
		fixedPrice = in.readDouble();

		try {
			ePickUpSelected = EnumLocations.valueOf(in.readString());
			eDropOffSelected = EnumLocations.valueOf(in.readString());
			eWhen = EnumWhen.valueOf(in.readString());
			ePaymentType = EnumPaymentType.valueOf(in.readString());
		} catch (IllegalArgumentException x) {
			Log.i(getClass().getSimpleName(), "IllegalArgumentException");
		}
		isWheelChairSelected = (in.readInt() == 0) ? false : true;
		isHearingImpairedSelected = (in.readInt() == 0) ? false : true;
		isAccessibiltyExpanded = (in.readInt() == 0) ? false : true;
		isCommentExpanded = (in.readInt() == 0) ? false : true;
		isFixedPrice = (in.readInt() == 0) ? false : true;
		isValuesUpdated = (in.readInt() == 0) ? false : true;
		isPaymentTypeUpdated = (in.readInt() == 0) ? false : true;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {

		dest.writeParcelable(presentLocation, flags);
		dest.writeParcelable(mapPickUpLocation, flags);
		dest.writeParcelable(mapDropOffLocation, flags);
		dest.writeParcelable(favouritePickUpLocation, flags);
		dest.writeParcelable(favouriteDropOffLocation, flags);
		dest.writeParcelable(recentPickUpLocation, flags);
		dest.writeParcelable(recentDropOffLocation, flags);
		dest.writeParcelable(defaultPickUpLocation, flags);
		dest.writeParcelable(defaultDropOffLocation, flags);
		dest.writeInt(selectedIndexPassengerCount);
		dest.writeString(comment);
		dest.writeString(pickUpDate);
		dest.writeString(pickUpTime);
		dest.writeString(pickUpDateToShow);
		dest.writeString(fixedPriceCurrency);
		dest.writeString(bookingId);
		dest.writeString(truncatedPan);
		dest.writeDouble(fixedPrice);
		dest.writeString((ePickUpSelected == null) ? "" : ePickUpSelected
				.name());
		dest.writeString((eDropOffSelected == null) ? "" : eDropOffSelected
				.name());
		dest.writeString((eWhen == null) ? "" : eWhen.name());
		dest.writeString((ePaymentType == null) ? "" : ePaymentType.name());
		dest.writeInt(isWheelChairSelected ? 1 : 0);
		dest.writeInt(isHearingImpairedSelected ? 1 : 0);
		dest.writeInt(isAccessibiltyExpanded ? 1 : 0);
		dest.writeInt(isCommentExpanded ? 1 : 0);
		dest.writeInt(isFixedPrice ? 1 : 0);
		dest.writeInt(isValuesUpdated ? 1 : 0);
		dest.writeInt(isPaymentTypeUpdated ? 1 : 0);

	}

}
