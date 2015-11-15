package co.uk.android.lldc.fragments.tablet;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import org.sufficientlysecure.htmltextview.HtmlTextView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources.NotFoundException;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import co.uk.android.lldc.R;
import co.uk.android.lldc.tablet.HomeActivityTablet;
import co.uk.android.lldc.tablet.LLDCApplication;
import co.uk.android.lldc.utils.Constants;

import com.nostra13.universalimageloader.core.ImageLoader;

public class VenueEventOverviewFragment extends Fragment {
	private static final String TAG = VenueEventOverviewFragment.class
			.getSimpleName();

	TextView txt_eventname, txt_eventvenue, txt_eventdate;// txt_eventdetails;

	HtmlTextView txt_eventdetails;

	ImageView img_imagedetls;

	String _id, tableName;

	// Button btn_calendar;
	RelativeLayout rlCalender;
	TextView tvCalender;
	ImageView ivCalenderEvent;

	Bundle bundle = new Bundle();

	// ServerModel selectedModel = new ServerModel();

	public static Handler mVenueEventOverviewHandler = null;
	boolean bEventAdded = false;

	public VenueEventOverviewFragment() {
		// TODO Auto-generated constructor stub
		// this._id = _id;
		// this.tableName = tableName;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		mVenueEventOverviewHandler = new Handler() {
			public void handleMessage(Message message) {

				if (message.what == 1010) {
					CalenderClickBg();
				}
				;
			};
		};

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mVenueEventOverviewHandler = null;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		View rootView = null;
		Log.e(TAG, "Device is Tablet..." + TAG);
		getActivity().setRequestedOrientation(
				ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		rootView = inflater.inflate(R.layout.fragment_venue_event_overview,
				container, false);

		bundle = getArguments();
		if (bundle != null && bundle.containsKey("PAGETITLE"))
			((HomeActivityTablet) getActivity()).tvHeaderTitle.setText(bundle
					.getString("PAGETITLE"));

		img_imagedetls = (ImageView) rootView.findViewById(R.id.img_imagedetls);
		txt_eventname = (TextView) rootView.findViewById(R.id.eventname);
		txt_eventvenue = (TextView) rootView.findViewById(R.id.eventvenue);
		txt_eventdate = (TextView) rootView.findViewById(R.id.eventdate);
		txt_eventdetails = (HtmlTextView) rootView
				.findViewById(R.id.txt_eventdetails);

		txt_eventdetails.setTypeface(Typeface.createFromAsset(getActivity()
				.getAssets(), "ROBOTO-LIGHT.TTF"));

		rlCalender = (RelativeLayout) rootView
				.findViewById(R.id.rlCalenderEvent);

		tvCalender = (TextView) rootView.findViewById(R.id.tvCalender);
		ivCalenderEvent = (ImageView) rootView
				.findViewById(R.id.ivCalenderEvent);

		txt_eventname.setSelected(true);
		txt_eventvenue.setSelected(true);

		rlCalender.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (tvCalender.getText().toString().contains("I'M GOING")) {
					// if (((Button)
					// arg0).getText().toString().contains("I'M GOING")) {
					LLDCApplication.onShowToastMesssage(getActivity(),
							"Event already added...");
				} else {
					// long startDate = Long
					// .parseLong(LLDCApplication.selectedModel
					// .getStartDateTime() + "000");
					long endDate = Long.parseLong(LLDCApplication.selectedModel
							.getEndDateTime() + "000");
					long currDate = Calendar.getInstance().getTimeInMillis();
					if (currDate < endDate) {
						// onOpenAddCalEventDialog();
						onAddCalendarIntent();
					} else {
						LLDCApplication.onShowToastMesssage(getActivity(),
								"Can't add past events...");
					}
				}
			}
		});
		onGetDataFromDB();

		((HomeActivityTablet) getActivity()).rlBackBtn
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						if (Constants.IS_FROM_MAP) {
							((HomeActivityTablet) getActivity()).tvHeaderTitle
									.setText("Venues");
						} else {
							((HomeActivityTablet) getActivity())
									.hideBackButton();
							((HomeActivityTablet) getActivity()).tvHeaderTitle
									.setText("Map");
							((HomeActivityTablet) getActivity())
									.showCommonSearchIcon();
						}
						VenueFragmentTablet.pager.setVisibility(View.VISIBLE);
						VenueFragmentTablet.tabHost.setVisibility(View.VISIBLE);
						VenueFragmentTablet.frameContainerVenue
								.setVisibility(View.GONE);

						getParentFragment().getChildFragmentManager()
								.popBackStack();
						Constants.IS_FROM_MAP = false;

						/* to remove this fragment from activity */
						Fragment fragment = getParentFragment()
								.getChildFragmentManager().findFragmentByTag(
										VenueEventOverviewFragment.class
												.getName());
						if (fragment != null)
							getParentFragment().getChildFragmentManager()
									.beginTransaction().remove(fragment)
									.commit();

					}
				});

		LLDCApplication.removeSimpleProgressDialog();

		return rootView;
	}

	void CalenderClickBg() {

		try {
			if (rlCalender != null && getResources() != null) {
				if (!readCalendarEvent(LLDCApplication.selectedModel.getName())) {
					rlCalender.setBackgroundColor(getResources().getColor(
							R.color.calender_bg_unselected));
					tvCalender.setTextColor(getResources().getColor(
							R.color.black_heading));
					ivCalenderEvent.setImageResource(R.drawable.calender_icon);
					tvCalender.setText("CALENDAR");

				} else {
					rlCalender.setBackgroundColor(getResources().getColor(
							R.color.calender_bg_selected));
					tvCalender.setTextColor(getResources().getColor(
							android.R.color.white));
					ivCalenderEvent.setImageResource(R.drawable.white_cal);
					tvCalender.setText("I'M GOING!");
				}
			}
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// if (readCalendarEvent(selectedModel.getName())) {

		((HomeActivityTablet) getActivity()).tvHeaderTitle.setText("Events");

		if (bEventAdded) {
			if (mVenueEventOverviewHandler != null)
				mVenueEventOverviewHandler.sendEmptyMessageDelayed(1010, 500);
			bEventAdded = false;
		}

		rlCalender.setBackgroundColor(getResources().getColor(
				R.color.calender_bg_unselected));
		tvCalender.setTextColor(getResources().getColor(R.color.black_heading));
		ivCalenderEvent.setImageResource(R.drawable.calender_icon);
		tvCalender.setText("CALENDAR");
		CalenderClickBg();
	}

	@SuppressLint({ "ResourceAsColor", "DefaultLocale" })
	public void onGetDataFromDB() {
		try {

			txt_eventname.setText(LLDCApplication.selectedModel.getName()
					.toString().toUpperCase());
			txt_eventvenue.setText(LLDCApplication.selectedModel
					.getVenueTitle().toString().toUpperCase());
			txt_eventdate.setText(LLDCApplication.selectedModel.getActiveDays()
					.toString().toUpperCase());
			txt_eventdetails.setHtmlFromString(
					LLDCApplication.selectedModel.getLongDescription(), false);
			txt_eventname.setTextColor(Color
					.parseColor(LLDCApplication.selectedModel.getColor()));
			txt_eventvenue.setTextColor(Color
					.parseColor(LLDCApplication.selectedModel.getColor()));
			txt_eventdate.setTextColor(Color
					.parseColor(LLDCApplication.selectedModel.getColor()));
			try {
				String szImageUrl = LLDCApplication.selectedModel
						.getLargeImage();
				// + "&width="
				// + (LLDCApplication.screenWidth + 200)
				// + "&height="
				// + (LLDCApplication.screenHeight/2 + 100) + "&crop-to-fit";

				ImageLoader.getInstance().displayImage(szImageUrl,
						img_imagedetls);

				// img_imagedetls.setImageUrl(szImageUrl, ImageCacheManager
				// .getInstance().getImageLoader());
				//
				// img_imagedetls
				// .setDefaultImageResId(R.drawable.qeop_placeholder);
				// img_imagedetls.setErrorImageResId(R.drawable.imgnt_placeholder);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			CalenderClickBg();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@SuppressLint({ "SimpleDateFormat", "InflateParams" })
	public void onOpenAddCalEventDialog() {
		try {

			AlertDialog.Builder quitAlertDialog = new AlertDialog.Builder(
					getActivity());

			LayoutInflater inflater = getActivity().getLayoutInflater();
			View dialogView = inflater.inflate(R.layout.dilog_event_create,
					null);
			quitAlertDialog.setView(dialogView);

			// quitAlertDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
			TextView eventTitle = (TextView) dialogView
					.findViewById(R.id.eventname);
			TextView eventdate = (TextView) dialogView
					.findViewById(R.id.eventdate);

			eventTitle.setText("Event Name:"
					+ LLDCApplication.selectedModel.getName().toString()
							.toUpperCase());

			Calendar startcal = Calendar.getInstance();
			startcal.setTimeInMillis(Long
					.parseLong(LLDCApplication.selectedModel.getStartDateTime()
							+ "000"));
			SimpleDateFormat sdf = new SimpleDateFormat("dd MMM");
			String startdate = sdf.format(startcal.getTime());
			System.out.println(startdate);

			sdf = new SimpleDateFormat("dd MMM yyyy");
			Calendar endcal = Calendar.getInstance();
			endcal.setTimeInMillis(Long.parseLong(LLDCApplication.selectedModel
					.getEndDateTime() + "000"));
			String endate = sdf.format(endcal.getTime());
			System.out.println(endate);
			String active_days = startdate + " - " + endate;
			eventdate.setText(active_days.toUpperCase());

			quitAlertDialog
					.setTitle("Event")
					// .setMessage("Confirm exit?")
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								@SuppressWarnings("unused")
								@Override
								public void onClick(DialogInterface argDialog,
										int argWhich) {
									try {
										Uri EVENTS_URI = Uri
												.parse(getCalendarUriBase()
														+ "events");
										ContentResolver cr = getActivity()
												.getContentResolver();
										ContentValues values = new ContentValues();

										values.put(
												Events.DTSTART,
												Long.parseLong(LLDCApplication.selectedModel
														.getStartDateTime()
														+ "000"));
										values.put(
												Events.DTEND,
												Long.parseLong(LLDCApplication.selectedModel
														.getStartDateTime()
														+ "000"));
										// "QEOP::"

										values.put(
												Events.TITLE,
												"QEOP::"
														+ LLDCApplication.selectedModel
																.getName());
										values.put(Events.DESCRIPTION,
												LLDCApplication.selectedModel
														.getShortDesc());
										values.put(Events.CALENDAR_ID,
												LLDCApplication.selectedModel
														.get_id());
										values.put(Events.EVENT_TIMEZONE,
												TimeZone.getDefault().getID());
										// Events.CONTENT_URI
										Uri uri = cr.insert(EVENTS_URI, values);
										long eventID = Long.parseLong(uri
												.getLastPathSegment());

										// onAddCalendarEvent();

										LLDCApplication.onShowLogCat(
												"EVENT_URI", uri.toString()
														+ "");

										CalenderClickBg();
									} catch (NumberFormatException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									argDialog.dismiss();
								}
							})
					.setNegativeButton("No",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface argDialog,
										int argWhich) {
									argDialog.dismiss();
								}
							}).create();

			AlertDialog dialog = quitAlertDialog.create();
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean readCalendarEvent(String eventTitle) {
		boolean isPresent = false;
		try {

			Uri EVENTS_URI = Uri.parse(getCalendarUriBase() + "events");

			// String searchQuery = "title like 'QEOP::%'";
			//
			// Cursor cursor = getActivity().getContentResolver().query(
			// EVENTS_URI,
			// null, searchQuery, null,
			// null);

			Cursor cursor = getActivity().getContentResolver().query(
					EVENTS_URI,
					new String[] { "calendar_id", "title", "description",
							"dtstart", "dtend", "eventLocation" }, null, null,
					null);

			if (cursor == null || cursor.getCount() == 0)
				return isPresent;

			cursor.moveToFirst();
			// fetching calendars name
			String CNames[] = new String[cursor.getCount()];

			// fetching calendars id
			// ArrayList<String> nameOfEvent = new ArrayList<String>();
			cursor.moveToFirst();
			for (int i = 0; i < CNames.length; i++) {
				LLDCApplication.onShowLogCat("EVENT_URI", cursor.getString(0)
						.toString() + "");
				try {
					if (cursor.getString(1).toString() != null
							& eventTitle.equals(cursor.getString(1).toString())) {
						isPresent = true;
						break;
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				cursor.moveToNext();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return isPresent;
	}

	public void onAddCalendarIntent() {
		Intent intent = new Intent(Intent.ACTION_INSERT);
		intent.setType("vnd.android.cursor.item/event");
		intent.putExtra(
				"beginTime",
				Long.parseLong(LLDCApplication.selectedModel.getStartDateTime()
						+ "000"));
		intent.putExtra(
				"endTime",
				Long.parseLong(LLDCApplication.selectedModel.getStartDateTime()
						+ "000"));
		intent.putExtra("title", LLDCApplication.selectedModel.getName());
		intent.putExtra("description",
				LLDCApplication.selectedModel.getShortDesc());
		intent.putExtra("eventLocation", "Geolocation");
		startActivity(intent);
		bEventAdded = true;
	}

	public void onAddCalendarEvent() {
		ContentValues event = new ContentValues();
		event.put(CalendarContract.Events.CALENDAR_ID,
				LLDCApplication.selectedModel.get_id());
		event.put(CalendarContract.Events.TITLE,
				LLDCApplication.selectedModel.getName());
		event.put(CalendarContract.Events.DESCRIPTION,
				LLDCApplication.selectedModel.getShortDesc());
		// event.put(CalendarContract.Events.EVENT_LOCATION,
		// appointment.mAddress);

		event.put(CalendarContract.Events.DTSTART,
				LLDCApplication.selectedModel.getStartDateTime() + "000");
		event.put(CalendarContract.Events.DTEND,
				LLDCApplication.selectedModel.getEndDateTime() + "000");
		event.put(CalendarContract.Events.ALL_DAY, 1); // 0 for false, 1 for
														// true
		event.put("eventStatus", 1); // 0 for tentative, 1 for confirmed, 2 for
										// canceled
		event.put(CalendarContract.Events.HAS_ALARM, 1); // 0 for false, 1 for
															// true
		event.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault()
				.getID());
		Uri url = getActivity().getContentResolver().insert(
				CalendarContract.Events.CONTENT_URI, event);

		LLDCApplication.onShowLogCat("EVENT_URI", url.toString() + "");
	}

	@SuppressWarnings("deprecation")
	private String getCalendarUriBase() {

		String calendarUriBase = null;
		Uri calendars = Uri.parse(" content://com.android.calendar/");// ("content://calendar/calendars");
		Cursor managedCursor = null;
		try {
			managedCursor = getActivity().managedQuery(calendars, null, null,
					null, null);
		} catch (Exception e) {
		}
		if (managedCursor != null) {
			calendarUriBase = "content://calendar/";
		} else {
			calendars = Uri.parse("content://com.android.calendar/calendars");
			try {
				managedCursor = getActivity().managedQuery(calendars, null,
						null, null, null);
			} catch (Exception e) {
			}
			if (managedCursor != null) {
				calendarUriBase = "content://com.android.calendar/";
			}
		}
		return calendarUriBase;
	}

}