package com.example.cabapppassenger.fragments;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.cabapppassenger.R;
import com.example.cabapppassenger.activity.MainFragmentActivity;
import com.example.cabapppassenger.activity.MainFragmentActivity.OnCustomBackPressedListener;
import com.example.cabapppassenger.adapter.MyBookingsAdapter;
import com.example.cabapppassenger.datastruct.json.Bookings;
import com.example.cabapppassenger.datastruct.json.BookingsList;
import com.example.cabapppassenger.task.GetBookingsTask;
import com.example.cabapppassenger.util.Constant;
import com.google.gson.Gson;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class MyBookingsFragment extends RootFragment implements
		OnCustomBackPressedListener {

	TextView tvAll, tvCard, tvCash, tvHistory, tvSearch, tvActive;
	EditText etSearch;
	boolean bIsActive = true, bIsHistory = false, bIsAll = true,
			bIsCash = false, bIsCard = false;
	static TextView tvEmpty;
	public static List<Bookings> bookingsList;
	public static List<Bookings> new_bookingsList;
	ListView lvMyBookings;
	Handler mHandler, mHandlerToggleEmptyView, mhandler;
	ProgressBar myBookingsProgress;
	MyBookingsAdapter myBookingsAdapter;
	// airport icon of topbar
	ImageView ivAirport, ivCancelSearch;
	RelativeLayout rlHistory, rlActive, rlSearchView;
	LinearLayout llTopBar;
	ArrayList<Bookings> filteredBookingsList;
	String PREF_NAME = "CabApp_Passenger", accessToken;
	SharedPreferences sharedPreferences;
	int activeBookingsCount = 0;
	DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	Fragment fragment;
	Context mContext;

	public MyBookingsFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_mybookings,
				container, false);
		mContext = getActivity();
		fragment = this;
		tvAll = (TextView) rootView.findViewById(R.id.tvAll);
		tvCard = (TextView) rootView.findViewById(R.id.tvCard);
		tvCash = (TextView) rootView.findViewById(R.id.tvCash);
		tvActive = (TextView) rootView.findViewById(R.id.tvActive);
		tvHistory = (TextView) rootView.findViewById(R.id.tv_history);
		rlHistory = (RelativeLayout) rootView.findViewById(R.id.rl_history);
		rlActive = (RelativeLayout) rootView.findViewById(R.id.rl_active);
		tvEmpty = (TextView) rootView.findViewById(R.id.tvEmpty);
		lvMyBookings = (ListView) rootView.findViewById(R.id.lvMyBookings);
		ivAirport = (ImageView) rootView.findViewById(R.id.iv_airport);
		etSearch = (EditText) rootView.findViewById(R.id.editTextSearch);
		llTopBar = (LinearLayout) rootView.findViewById(R.id.topbarTabs);
		ivCancelSearch = (ImageView) rootView.findViewById(R.id.ivCancelSearch);
		ivCancelSearch.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				etSearch.setText("");
				if (tvActive.getCompoundDrawables()[0].getConstantState()
						.equals((getActivity().getResources()
								.getDrawable(R.drawable.activeselected))
								.getConstantState())) {
					llTopBar.setVisibility(View.GONE);
					bIsActive = true;
					bIsHistory = false;
					if (myBookingsAdapter != null) {
						sortBookingsListAndUpdateAdapter("filterbyactive");
					}

					toggleTextViewBackgroundBottom(tvActive);
					rlSearchView.setVisibility(View.GONE);
					hideSoftKeyBoard();

				} else {

					hideKeybord(v);
					bIsActive = false;
					bIsHistory = true;

					llTopBar.setVisibility(View.VISIBLE);
					if (myBookingsAdapter != null) {
						sortBookingsListAndUpdateAdapter("filterbyhistory");
					}
					toggleTextViewBackgroundBottom(tvHistory);
					rlSearchView.setVisibility(View.GONE);
					hideSoftKeyBoard();
				}

			}
		});
		llTopBar.setVisibility(View.GONE);
		sharedPreferences = getActivity().getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		accessToken = sharedPreferences.getString("AccessToken", "");
		rlSearchView = (RelativeLayout) rootView
				.findViewById(R.id.rlSearchLayout);
		tvAll.setOnTouchListener(new TextTouchListenerTop());
		tvCard.setOnTouchListener(new TextTouchListenerTop());
		tvCash.setOnTouchListener(new TextTouchListenerTop());
		rlHistory.setOnTouchListener(new TextTouchListenerBottom());
		rlActive.setOnTouchListener(new TextTouchListenerBottom());

		ivAirport.setImageResource(R.drawable.searchselected);
		ivAirport.setOnTouchListener(new TextTouchListenerBottom());

		mhandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg != null && msg.peekData() != null) {
					Bundle bundleData = (Bundle) msg.obj;

					if (bundleData.containsKey("CancelBooking")) {
						// Log.e("My Booking Frag", "Inside cancel handler");
						// GetBookingsTask getBookingsTask = new
						// GetBookingsTask(
						// getActivity(),
						// Constant.passengerURL
						// + "ws/v2/passenger/bookings/?accessToken="
						// + accessToken, mHandler, false);
						// getBookingsTask.execute();
						alertdialog("bookingcancelled", mContext);
					}
				}
			}
		};
		etSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

				myBookingSearch(s.toString());
				// hideSoftKeyBoard();
				llTopBar.setVisibility(View.GONE);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				// hideSoftKeyBoard();
			}
		});
		super.initWidgets(rootView, this.getClass().getName());
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		mHandler = new Handler() {
			@Override
			public void handleMessage(Message message) {
				super.handleMessage(message);
				String bookingData;
				if (message != null && message.peekData() != null) {
					Bundle bundleData = (Bundle) message.obj;
					bookingData = bundleData.containsKey("bookingsData") ? bundleData
							.getString("bookingsData") : "";
					BookingsList response = new BookingsList();
					Gson gson = new Gson();
					if (bookingData != null)
						response = gson.fromJson(bookingData,
								BookingsList.class);
					if (response != null) {
						bookingsList = response.getBookings();
						new_bookingsList = response.getBookings();
					}

				}
				if (myBookingsProgress != null)
					myBookingsProgress.setVisibility(View.GONE);

				// preparing list data
				if (bookingsList != null)
					myBookingsAdapter = new MyBookingsAdapter(fragment,
							getActivity(), bookingsList,
							mHandlerToggleEmptyView, mhandler);

				lvMyBookings.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						if (myBookingsAdapter != null) {
							sortBookingsListAndUpdateAdapter("filterbyactive");
							// setting list adapter
							lvMyBookings.setAdapter(myBookingsAdapter);
						}
					}
				});
			}
		};

		GetBookingsTask getBookingsTask = new GetBookingsTask(getActivity(),
				Constant.passengerURL
						+ "ws/v2/passenger/bookings/?accessToken="
						+ accessToken, mHandler, false);
		getBookingsTask.execute();

		mHandlerToggleEmptyView = new Handler() {
			@Override
			public void handleMessage(Message message) {
				super.handleMessage(message);

				lvMyBookings.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						if (myBookingsAdapter == null
								|| myBookingsAdapter.getCount() == 0)
							tvEmpty.setVisibility(View.VISIBLE);
						else
							tvEmpty.setVisibility(View.GONE);
					}
				});
			}
		};

		// not doing anything
		lvMyBookings.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				if (myBookingsAdapter != null) {
					Bookings booking = myBookingsAdapter.getCurrentList().get(
							position);
				}
			}
		});
	}

	@Override
	public void onResume() {

		((MainFragmentActivity) getActivity()).setCustomBackPressListener(this);
		super.onResume();
		bIsActive = true;
		getActivity().getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
	}

	@Override
	public void onStop() {
		super.onStop();
		if (MainFragmentActivity.slidingMenu != null)
			MainFragmentActivity.slidingMenu
					.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

	}

	void hideSoftKeyBoard() {
		try {
			InputMethodManager imm = (InputMethodManager) getActivity()
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(getActivity().getCurrentFocus()
					.getWindowToken(), 0);
			// imm.hideSoftInputFromWindow(tv.getWindowToken(), 0);
		} catch (Exception e) {

		}
	}

	public void myBookingSearch(CharSequence s) {
		// Code to get active fragment
		if (filteredBookingsList != null && filteredBookingsList.size() > 0)
			myBookingsAdapter.setUpdatedList(filteredBookingsList);
		if (bIsActive) {

			if (myBookingsAdapter != null) {
				myBookingsAdapter.getFilter(false).filter(s);
			}
		} else {

			if (myBookingsAdapter != null) {
				myBookingsAdapter.getFilter(true).filter(s);
			}
		}

	}

	void sortBookingsListAndUpdateAdapter(CharSequence constraint) {
		filteredBookingsList = new ArrayList<Bookings>();
		String paymentMethod;
		for (int i = 0; i < bookingsList.size(); i++) {
			paymentMethod = bookingsList.get(i).getPaymentType().toString();
			if (constraint.equals("filterbycard")) {
				if (paymentMethod.toLowerCase().contains("both")) {
					addBookingsAccordingtoType(bookingsList.get(i));
				}
			} else if (constraint.equals("filterbycash")) {
				if (paymentMethod.toLowerCase().contains("cash")) {
					addBookingsAccordingtoType(bookingsList.get(i));
				}
			} else if (constraint.equals("filterbyhistory")) {
				if (isBookingHistory(bookingsList.get(i))) {
					addBookingsAccordingtoType(bookingsList.get(i));
				}
				ArrayList<Bookings> newOrderedHistoryList = new ArrayList<Bookings>();
				for (int j = filteredBookingsList.size() - 1; j >= 0; j--) {

					newOrderedHistoryList.add(filteredBookingsList.get(j));
				}
				filteredBookingsList = newOrderedHistoryList;
				// myBookingsAdapter.setUpdatedList(filteredBookingsList);
			} else if (constraint.equals("filterbyactive")) {
				if (isBookingActive(bookingsList.get(i))) {
					addBookingsAccordingtoType(bookingsList.get(i));
				}
			} else if (constraint.equals("filterbyall")) {
				addBookingsAccordingtoType(bookingsList.get(i));
			}
		}
		// if (constraint.equals("filterbyhistory")) {
		// ArrayList<Bookings> newOrderedHistoryList = new
		// ArrayList<Bookings>();
		// for (int i = filteredBookingsList.size() - 1; i >= 0; i--) {
		//
		// newOrderedHistoryList.add(filteredBookingsList.get(i));
		// }
		// filteredBookingsList = newOrderedHistoryList;
		myBookingsAdapter.setUpdatedList(filteredBookingsList);
		// }

		if (constraint.equals("filterbyactive")) {
			activeBookingsCount = filteredBookingsList.size();
			// ((MainActivity) getActivity())
			// .updateAndSetMenuItems(activeBookingsCount);
			// updateBookingsCountBadge(activeBookingsCount);
			// AppValues.nBookingsCount = activeBookingsCount;
			Log.e("MyBookingsFragment", " Filtered Booking count:: "
					+ activeBookingsCount);
			myBookingsAdapter.setUpdatedList(filteredBookingsList);
		}

		mHandlerToggleEmptyView.sendEmptyMessage(0);
	}

	void addBookingsAccordingtoType(Bookings Booking) {

		if (bIsActive) {
			if (isBookingActive(Booking)) {
				filteredBookingsList.add(Booking);
			}
		}
		if (bIsHistory) {
			if (isBookingHistory(Booking))
				filteredBookingsList.add(Booking);

		}

	}

	void addBookingsAccordingToPayment(Bookings Booking) {
		String paymentType = Booking.getPaymentType().toString();

		if (bIsCash) {
			if (paymentType.toLowerCase().contains("cash")) {
				filteredBookingsList.add(Booking);
			}
		} else if (bIsCard) {
			if (paymentType.toLowerCase().contains("both")) {
				filteredBookingsList.add(Booking);
			}
		} else if (bIsAll)
			filteredBookingsList.add(Booking);
	}

	boolean isBookingActive(Bookings Booking) {

		Log.i(getClass().getSimpleName(),
				Booking.getStatus().get("currentStatus") + " "
						+ Booking.getCompletedAt());

		if (Booking.getCompletedAt().equals("")
				&& !Booking.getStatus().get("currentStatus").getAsString()
						.toLowerCase().equals("cancelled")) // because active
															// booking
		// will have empty completedAt field
		{
			return true;
		} else {
			return false;
		}
	}

	boolean isBookingHistory(Bookings Booking) {
		Log.i(getClass().getSimpleName(),
				Booking.getStatus().get("currentStatus") + " "
						+ Booking.getCompletedAt());

		if (!Booking.getCompletedAt().equals("")
				|| Booking.getStatus().get("currentStatus").getAsString()
						.toLowerCase().equals("cancelled")) // because completed
															// booking
		// will have empty completedAt field
		{
			return true;
		} else {
			return false;
		}

	}

	class TextTouchListenerBottom implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.iv_airport: // it is search button

				showSearchScreen();
				break;
			case R.id.rl_history:
				bIsActive = false;
				bIsHistory = true;
				llTopBar.setVisibility(View.VISIBLE);

				if (myBookingsAdapter != null) {
					myBookingsAdapter.isHistory_global = true;
					sortBookingsListAndUpdateAdapter("filterbyhistory");
				}

				toggleTextViewBackgroundBottom(tvHistory);
				rlSearchView.setVisibility(View.GONE);
				toggleTextViewBackgroundTop(tvAll);
				/*
				 * bookingsList= new ArrayList<Bookings>(); for(int
				 * i=new_bookingsList.size()-1;i>=0;i++) {
				 * bookingsList.add(new_bookingsList.get(i)); }
				 */
				bIsAll = true;
				bIsCash = false;
				bIsCard = false;

				break;
			case R.id.rl_active:
				llTopBar.setVisibility(View.GONE);
				bIsActive = true;
				bIsHistory = false;

				/* bookingsList=new_bookingsList; */
				if (myBookingsAdapter != null) {
					myBookingsAdapter.isHistory_global = false;
					sortBookingsListAndUpdateAdapter("filterbyactive");
				}
				toggleTextViewBackgroundBottom(tvActive);
				rlSearchView.setVisibility(View.GONE);
				hideSoftKeyBoard();
				break;
			}
			return true;
		}
	}

	void showSearchScreen() {
		rlSearchView.setVisibility(View.VISIBLE);
	}

	void toggleTextViewBackgroundTop(TextView textViewSelected) {
		tvAll.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.allicon, 0,
				0);
		tvCard.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.card2, 0,
				0);
		tvCash.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.cash, 0, 0);

		tvAll.setTextColor(getResources().getColor(R.color.textcolor_grey));
		tvCard.setTextColor(getResources().getColor(R.color.textcolor_grey));
		tvCash.setTextColor(getResources().getColor(R.color.textcolor_grey));

		if (textViewSelected == tvAll)
			textViewSelected.setCompoundDrawablesWithIntrinsicBounds(0,
					R.drawable.alliconselected, 0, 0);
		else if (textViewSelected == tvCard)
			textViewSelected.setCompoundDrawablesWithIntrinsicBounds(0,
					R.drawable.card2selected, 0, 0);
		else if (textViewSelected == tvCash)
			textViewSelected.setCompoundDrawablesWithIntrinsicBounds(0,
					R.drawable.cashselected, 0, 0);

		textViewSelected.setTextColor(getResources().getColor(
				R.color.textview_selected));

	}

	void toggleTextViewBackgroundBottom(TextView textViewSelected) {

		tvHistory.setCompoundDrawablesWithIntrinsicBounds(R.drawable.time2, 0,
				0, 0);
		/*
		 * tvSearch.setCompoundDrawablesWithIntrinsicBounds(R.drawable.search,
		 * 0, 0, 0);
		 */
		tvActive.setCompoundDrawablesWithIntrinsicBounds(R.drawable.active, 0,
				0, 0);
		tvHistory.setTextColor(getResources().getColor(R.color.textcolor_grey));
		/*
		 * tvSearch.setTextColor(getResources().getColor(R.color.textcolor_grey))
		 * ;
		 */
		tvActive.setTextColor(getResources().getColor(R.color.textcolor_grey));

		if (textViewSelected == tvHistory)
			textViewSelected.setCompoundDrawablesWithIntrinsicBounds(
					R.drawable.time, 0, 0, 0);
		// else if (textViewSelected == tvSearch)
		// textViewSelected.setCompoundDrawablesWithIntrinsicBounds(
		// R.drawable.searchselected, 0, 0, 0);
		else if (textViewSelected == tvActive)
			textViewSelected.setCompoundDrawablesWithIntrinsicBounds(
					R.drawable.activeselected, 0, 0, 0);

		textViewSelected.setTextColor(getResources().getColor(
				R.color.textview_selected));

	}

	class TextTouchListenerTop implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub

			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				toggleTextViewBackgroundTop((TextView) v);
			}

			switch (v.getId()) {
			case R.id.tvAll:
				bIsAll = true;
				bIsCash = false;
				bIsCard = false;
				if (myBookingsAdapter != null) {
					sortBookingsListAndUpdateAdapter("filterbyall");
				}
				break;
			case R.id.tvCard:
				bIsAll = false;
				bIsCash = false;
				bIsCard = true;
				if (myBookingsAdapter != null) {
					sortBookingsListAndUpdateAdapter("filterbycard");
				}
				break;
			case R.id.tvCash:
				bIsAll = false;
				bIsCash = true;
				bIsCard = false;
				if (myBookingsAdapter != null) {
					sortBookingsListAndUpdateAdapter("filterbycash");
				}
				break;
			}
			return true;
		}
	}

	@Override
	public void onCustomBackPressed() {
		Log.i(getClass().getSimpleName(), "onCustomBackPressed");
		if (MainFragmentActivity.slidingMenu.isMenuShowing())
			MainFragmentActivity.slidingMenu.toggle();
		else
			((MainFragmentActivity) getActivity()).showQuitDialog();

	}

	@Override
	public void onPause() {
		((MainFragmentActivity) getActivity()).setCustomBackPressListener(null);
		super.onPause();
	}

	public void alertdialog(final String message, Context mContext) {
		TextView title = new TextView(mContext);
		title.setTextColor(Color.parseColor("#d16413"));
		title.setText("Alert");
		title.setPadding(10, 10, 10, 10);
		title.setGravity(Gravity.CENTER);
		title.setTextColor(Color.BLACK);
		title.setTextSize(20);

		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setCustomTitle(title);

		if (message == "bookingcancelled") {
			builder.setMessage("Your Booking has been cancelled.");
		}

		builder.setCancelable(false);
		builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
				GetBookingsTask getBookingsTask = new GetBookingsTask(
						getActivity(), Constant.passengerURL
								+ "ws/v2/passenger/bookings/?accessToken="
								+ accessToken, mHandler, false);
				getBookingsTask.execute();
			}
		});
		AlertDialog alert = builder.show();

	}
}
