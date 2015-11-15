package com.hoteltrip.android.fragment;

import java.util.Calendar;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.actionbarsherlock.app.SherlockDialogFragment;
import com.google.android.gms.internal.v;
import com.hoteltrip.android.util.Const;
import com.hoteltrip.android.util.DateFetchedListener;

public class MyDialogFragment extends SherlockDialogFragment {

	private int mYear;
	private int mMonth;
	private int mDay;
	private int mHour;
	private int mMinute;
	private Calendar calendarToday;

	private DateFetchedListener dateFetchedListener;
	private AlertDialog dialog;

	public static MyDialogFragment newInstance(int id, int value) {
		MyDialogFragment frag = new MyDialogFragment();
		Bundle bundle = new Bundle();
		bundle.putInt("id", id);
		bundle.putInt("value", value);
		frag.setArguments(bundle);
		return frag;
	}

	public void setDateFetcher(DateFetchedListener listener) {
		this.dateFetchedListener = listener;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		// Use the current time as the default values for the picker
		calendarToday = Calendar.getInstance();
		mYear = calendarToday.get(Calendar.YEAR);
		mMonth = calendarToday.get(Calendar.MONTH);
		mDay = calendarToday.get(Calendar.DAY_OF_MONTH);
		mHour = calendarToday.get(Calendar.HOUR_OF_DAY);
		mMinute = calendarToday.get(Calendar.MINUTE);

		int dialogId = getArguments().getInt("id");
		int value = getArguments().getInt("value");

		switch (dialogId) {
		case Const.DATE_PICKER_DIALOG_ID:
			// TODO: Jellybean bug workaround for
			// http://code.google.com/p/android/issues/detail?id=34833
			// http://stackoverflow.com/questions/11444238/jelly-bean-datepickerdialog-is-there-a-way-to-cancel
			dialog = new DatePickerDialog(getActivity(), dateSetListener,
					mYear, mMonth, mDay);
			break;
		case Const.TIME_PICKER_DIALOG_ID:

			break;
		case Const.SELECT_DATE_BEFORE_ERROR_ID:
			dialog = new AlertDialog.Builder(getActivity())
					.setMessage("Cannot select date before today.")
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									return;
								}
							}).create();
			break;
		case Const.SELECT_DATE_GREATER_ERROR_ID:
			dialog = new AlertDialog.Builder(getActivity())
					.setMessage("Cannot select date greater than a year.")
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									return;
								}
							}).create();
			break;
		case Const.UPPER_LIMIT_REACHED_ID:
			String message = "Max. limit of %s";
			dialog = new AlertDialog.Builder(getActivity())
					.setMessage(String.format(message, value))
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									return;
								}
							}).create();
			break;
		case Const.LOWER_LIMIT_REACHED_ID:
			message = "Min. limit of %s";
			dialog = new AlertDialog.Builder(getActivity())
					.setMessage(String.format(message, value))
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									return;
								}
							}).create();
			break;

		case Const.SEARCH_CRITERIA_NULL_ID:
			dialog = new AlertDialog.Builder(getActivity())
					.setMessage("Search criteria must not be empty!")
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									return;
								}
							}).create();

			break;
		}
		return dialog;
	}

	OnDateSetListener dateSetListener = new OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			Calendar selectedCal = Calendar.getInstance();

			selectedCal.set(year, monthOfYear, dayOfMonth, mHour, mMinute);

			if (dateFetchedListener != null) {
				dateFetchedListener.setSelectedDate(selectedCal);
			}
		}
	};

	OnTimeSetListener timePickerSet = new OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

		}
	};

}
