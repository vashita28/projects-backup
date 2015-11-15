package com.example.cabapppassenger.fragments;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.cabapppassenger.R;
import com.example.cabapppassenger.activity.MainFragmentActivity;
import com.example.cabapppassenger.activity.MainFragmentActivity.OnCustomBackPressedListener;
import com.example.cabapppassenger.listeners.PreBookListener;
import com.example.cabapppassenger.model.PreBookParcelable;
import com.example.cabapppassenger.receivers.NetworkBroadcastReceiver;
import com.example.cabapppassenger.task.AddNewCardTask;
import com.example.cabapppassenger.task.EditUserDetailsTask;
import com.example.cabapppassenger.task.GetUserDetailsTask;
import com.example.cabapppassenger.util.Constant;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class MyAccountFragment extends RootFragment implements
		OnCustomBackPressedListener {

	TextView tvAccessibilityOptions, tvAoHearingImpaired, tvAoWheelchairUser,
			tv_pick_up_top_right, tv_card, tv_cash, tvusername, tvcountry,
			tvmobile_no, tvbiling_address, tvemailadd, tv_addnew_card;
	PreBookListener listener;
	Context mContext;
	ImageView iv_airport;
	String wheelchair, hearingimpaired;
	boolean flag_card = false, flag_cash = true;
	Spinner spinner_preferredmethod;
	ArrayList<String> arr_preferredmethods = new ArrayList<String>();
	ArrayAdapter<String> condadapter;
	RelativeLayout rl_done;
	Editor editor;
	View[] viewAddCard;
	SharedPreferences shared_pref;
	PreBookParcelable pbParceable;
	LinearLayout lv_cardetails;
	Handler mhandler;
	ArrayList<ArrayList<String>> arr_usercard_pass = new ArrayList<ArrayList<String>>();
	String PREF_NAME = "CabApp_Passenger";
	String text_truncatedPan = "";
	ArrayList<String> arr_passenger_card = new ArrayList<String>();

	public MyAccountFragment() {
	}

	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		if (arr_preferredmethods.size() <= 0) {
			arr_preferredmethods.add("Cash");
			arr_preferredmethods.add("Card");
		}

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_myaccount,
				container, false);
		mContext = getActivity();
		pbParceable = new PreBookParcelable();

		lv_cardetails = (LinearLayout) rootView
				.findViewById(R.id.ll_cardetails);
		if (((LinearLayout) lv_cardetails).getChildCount() > 0)
			((LinearLayout) lv_cardetails).removeAllViews();
		tv_addnew_card = (TextView) rootView.findViewById(R.id.tv_addnew_card);
		listener = new PreBookListener(mContext, rootView, pbParceable);
		iv_airport = (ImageView) rootView.findViewById(R.id.iv_airport);
		iv_airport.setVisibility(View.GONE);
		rl_done = (RelativeLayout) rootView.findViewById(R.id.rl_done);
		tvAoHearingImpaired = (TextView) rootView
				.findViewById(R.id.tv_ao_hearing_impaired);

		tvcountry = (TextView) rootView.findViewById(R.id.tvcountry);
		tvmobile_no = (TextView) rootView.findViewById(R.id.tvmobile_no);
		tvbiling_address = (TextView) rootView
				.findViewById(R.id.tvbiling_address);
		tvemailadd = (TextView) rootView.findViewById(R.id.tvemailadd);
		tvusername = (TextView) rootView.findViewById(R.id.tvusername);
		tv_card = (TextView) rootView.findViewById(R.id.tv_card);
		tv_cash = (TextView) rootView.findViewById(R.id.tv_cash);
		tvAccessibilityOptions = (TextView) rootView
				.findViewById(R.id.tv_ao_header);
		spinner_preferredmethod = (Spinner) rootView
				.findViewById(R.id.spinner_preferredmethod);
		tvAoWheelchairUser = (TextView) rootView
				.findViewById(R.id.tv_ao_wheelchair_user);
		tv_pick_up_top_right = (TextView) rootView
				.findViewById(R.id.tv_pick_up_top_right);
		condadapter = new ArrayAdapter<String>(mContext,
				android.R.layout.simple_spinner_item, arr_preferredmethods);

		condadapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		tvAoHearingImpaired.setOnClickListener(listener);
		tvAoWheelchairUser.setOnClickListener(listener);
		tv_pick_up_top_right.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				EditProfileFragment edit = new EditProfileFragment();
				FragmentManager fm = getParentFragment()
						.getChildFragmentManager();
				FragmentTransaction ft = fm.beginTransaction();
				ft.replace(R.id.frame_container, edit);
				ft.addToBackStack("editProfileFragment");
				ft.commit();
			}
		});
		tv_addnew_card.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				alertdialog(mContext);
			}
		});
		tv_card.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// hideKeybord(v);
				if (flag_cash) {
					if (((TextView) v).getCompoundDrawables()[1]
							.getConstantState().equals(
									(mContext.getResources()
											.getDrawable(R.drawable.card2))
											.getConstantState())) {

						setTopDrawbleAndText(((TextView) v),
								R.drawable.card2selected,
								R.color.textview_selected);
						tv_addnew_card.setVisibility(View.VISIBLE);
						lv_cardetails.setVisibility(View.VISIBLE);
						flag_cash = false;
						flag_card = true;
						setTopDrawbleAndText(((TextView) tv_cash),
								R.drawable.cash_myaccount,
								R.color.textcolor_grey);
						spinner_preferredmethod.setSelection(1);

					} else {
						lv_cardetails.setVisibility(View.GONE);
						setTopDrawbleAndText(((TextView) v), R.drawable.card2,
								R.color.textcolor_grey);
						tv_addnew_card.setVisibility(View.GONE);
						spinner_preferredmethod.setSelection(0);

					}
				}
			}
		});
		spinner_preferredmethod
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View v,
							int position, long arg3) {
						// TODO Auto-generated method stub
						if (position == 1) {
							setTopDrawbleAndText(tv_card,
									R.drawable.card2selected,
									R.color.textview_selected);
							tv_addnew_card.setVisibility(View.VISIBLE);
							lv_cardetails.setVisibility(View.VISIBLE);
							flag_cash = false;
							flag_card = true;
							setTopDrawbleAndText(((TextView) tv_cash),
									R.drawable.cash_myaccount,
									R.color.textcolor_grey);
						} else {

							setTopDrawbleAndText(tv_cash,
									R.drawable.cash_myaccountselected,
									R.color.textview_selected);
							tv_addnew_card.setVisibility(View.GONE);
							lv_cardetails.setVisibility(View.GONE);
							flag_card = false;
							flag_cash = true;
							setTopDrawbleAndText(((TextView) tv_card),
									R.drawable.card2, R.color.textcolor_grey);
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}
				});
		tv_cash.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (flag_card) {
					if (((TextView) v).getCompoundDrawables()[1]
							.getConstantState()
							.equals((mContext.getResources()
									.getDrawable(R.drawable.cash_myaccount))
									.getConstantState())) {

						setTopDrawbleAndText(((TextView) v),
								R.drawable.cash_myaccountselected,
								R.color.textview_selected);
						lv_cardetails.setVisibility(View.GONE);
						tv_addnew_card.setVisibility(View.GONE);
						flag_card = false;
						flag_cash = true;
						spinner_preferredmethod.setSelection(0);
						setTopDrawbleAndText(((TextView) tv_card),
								R.drawable.card2, R.color.textcolor_grey);

					} else {
						lv_cardetails.setVisibility(View.VISIBLE);

						setTopDrawbleAndText(((TextView) v), R.drawable.cash,
								R.color.textcolor_grey);
						tv_addnew_card.setVisibility(View.VISIBLE);
						spinner_preferredmethod.setSelection(1);
					}
				}
			}
		});
		mhandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 0) {
					updatevalues();
					if (Constant.hashmap_passengercard != null) {
						arr_usercard_pass = Constant.hashmap_passengercard
								.get(Constant.USER_CARD_KEY);
						if (arr_usercard_pass != null)
							user_carddetails(arr_usercard_pass);
						Log.e("Card Details", "");
					}

				}

				if (msg.what == 1) {
					if (NetworkBroadcastReceiver.ms_bIsNetworkAvailable) {

					} else {
						Toast.makeText(mContext, "No network connection",
								Toast.LENGTH_SHORT).show();
					}
				}

				if (msg != null && msg.peekData() != null) {

					Bundle bundledata = (Bundle) msg.obj;
					if (bundledata.containsKey("URL")) {
						String url = bundledata.getString("URL");

						AddNewCardFragment card = new AddNewCardFragment();
						if (url != null) {
							Bundle bundle = new Bundle();
							bundle.putString("URL", url);
							card.setArguments(bundle);
						}
						FragmentTransaction ft = getParentFragment()
								.getChildFragmentManager().beginTransaction();
						ft.addToBackStack("bookingDetailsFragment");
						ft.replace(R.id.frame_container, card, "newCard");
						ft.commit();
					}
				}
			}

		};

		rl_done.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				hideKeybord(v);

				if (tv_card.getCompoundDrawables()[1].getConstantState()
						.equals((mContext.getResources()
								.getDrawable(R.drawable.card2selected))
								.getConstantState())
						&& text_truncatedPan == null
						&& text_truncatedPan.length() == 0) {

					Constant.alertdialog("nocardadded", mContext);

					setTopDrawbleAndText(tv_cash,
							R.drawable.cash_myaccountselected,
							R.color.textview_selected);
					lv_cardetails.setVisibility(View.GONE);
					tv_addnew_card.setVisibility(View.GONE);
					flag_card = false;
					flag_cash = true;
					spinner_preferredmethod.setSelection(0);
					setTopDrawbleAndText(tv_card, R.drawable.card2,
							R.color.textcolor_grey);

					return;

				}
				if (tvAoWheelchairUser.getCompoundDrawables()[1]
						.getConstantState()
						.equals(mContext.getResources()
								.getDrawable(R.drawable.wheelchair_user)
								.getConstantState()))
					wheelchair = "0";

				else
					wheelchair = "1";

				if (tvAoHearingImpaired.getCompoundDrawables()[1]
						.getConstantState().equals(
								mContext.getResources()
										.getDrawable(
												R.drawable.hearing_impaired)
										.getConstantState()))
					hearingimpaired = "false";

				else
					hearingimpaired = "true";

				confirm_dialog("confirm_sure", mContext);
			}
		});
		spinner_preferredmethod.setAdapter(condadapter);
		super.initWidgets(rootView, this.getClass().getName());
		return rootView;
	}

	public void confirm_dialog(final String message, final Context mContext) {
		TextView title = new TextView(mContext);
		title.setTextColor(Color.parseColor("#d16413"));
		title.setText("Alert");
		title.setPadding(10, 10, 10, 10);
		title.setGravity(Gravity.CENTER);
		title.setTextColor(Color.BLACK);
		title.setTextSize(20);

		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setCustomTitle(title);

		if (message == "confirm_sure") {
			builder.setMessage("Are you sure you want to save your details?");
		}
		builder.setCancelable(false);
		builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();

			}
		});
		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				if (NetworkBroadcastReceiver.ms_bIsNetworkAvailable) {

					EditUserDetailsTask task = new EditUserDetailsTask(mContext);
					task.firstname = shared_pref.getString("FirstName", "");
					task.lastname = shared_pref.getString("LastName", "");
					task.wheelchair = wheelchair;
					task.hearingimpaired = hearingimpaired;
					task.truncatedPan = text_truncatedPan.trim();
					if (spinner_preferredmethod.getSelectedItem().toString()
							.matches("Card"))
						task.payingmethod = "cash and card";
					else
						task.payingmethod = "cash";

					Log.e("Pafjkdsnf", task.payingmethod);
					Log.e("PreferredMethod", spinner_preferredmethod
							.getSelectedItem().toString());
					task.handler = mhandler;
					task.execute(Constant.passengerURL
							+ "ws/v2/passenger/account/?accessToken="
							+ shared_pref.getString("AccessToken", ""));
				} else {
					Toast.makeText(mContext, "No network connection",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		AlertDialog alert = builder.show();
		if (alert != null) {
			TextView messageText = (TextView) alert
					.findViewById(android.R.id.message);
			if (messageText != null) {
				messageText.setGravity(Gravity.CENTER_VERTICAL);
				messageText.setTextColor(Color.BLACK);
				messageText.setTextSize(16);
			}
		}

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// if (NetworkBroadcastReceiver.ms_bIsNetworkAvailable) {
		// if (Constant.arr_dialingcode != null
		// & Constant.arr_dialingcode.size() > 0)
		// Constant.arr_dialingcode.clear();
		//
		// GetCountryCodeTask task = new GetCountryCodeTask(mContext);
		// task.fromlanding_flag = true;
		// task.execute();
		//
		// } else {
		// Toast.makeText(mContext, "No network connection.",
		// Toast.LENGTH_SHORT).show();
		// }
		((MainFragmentActivity) getActivity()).setCustomBackPressListener(this);
		shared_pref = mContext.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		editor = shared_pref.edit();
		getActivity().getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

		updatevalues();

		if (Constant.flag_added_newcard) {
			if (NetworkBroadcastReceiver.ms_bIsNetworkAvailable) {
				GetUserDetailsTask task = new GetUserDetailsTask(mContext);
				task.handler = mhandler;
				task.execute("");
			} else {
				Toast.makeText(mContext, "No network connection",
						Toast.LENGTH_SHORT).show();
			}
		}
		if (Constant.hashmap_passengercard != null) {
			arr_usercard_pass = Constant.hashmap_passengercard
					.get(Constant.USER_CARD_KEY);
			if (arr_usercard_pass != null)
				user_carddetails(arr_usercard_pass);
			Log.e("Card Details", "");
		}

		// viewAddCard.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// int position = -1;
		// if (v.getTag() instanceof Integer) {
		// position = (Integer) v.getTag();
		// Log.e("Position", "" + position);
		// } else {
		// Log.e("Position", "" + position);
		// }
		// }
		// });
	}

	@Override
	public void onStop() {
		super.onStop();
		if (MainFragmentActivity.slidingMenu != null)
			MainFragmentActivity.slidingMenu
					.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		if (lv_cardetails != null)
			lv_cardetails.removeAllViews();
	}

	private void setTopDrawbleAndText(TextView textView, int topDrawableId,
			int textColorId) {
		textView.setCompoundDrawablesWithIntrinsicBounds(0, topDrawableId, 0, 0);
		textView.setTextColor(mContext.getResources().getColor(textColorId));
	}

	public void updatevalues() {
		// TODO Auto-generated method stub

		// username
		if (shared_pref.getString("FirstName", "") != null
				&& !shared_pref.getString("FirstName", "").matches("null")
				&& shared_pref.getString("LastName", "") != null
				&& !shared_pref.getString("LastName", "").matches("null")) {

			tvusername.setText(shared_pref.getString("FirstName", "") + " "
					+ shared_pref.getString("LastName", ""));
		} else
			tvusername.setText("");

		// mobileno
		if (shared_pref.getString("MobileNo", "") != null
				&& !shared_pref.getString("MobileNo", "").matches("null")) {

			tvmobile_no.setText(shared_pref.getString("MobileNo", ""));
		} else
			tvmobile_no.setText("");

		// country
		if (shared_pref.getString("Country", "") != null
				&& !shared_pref.getString("Country", "").matches("null")) {

			tvcountry.setText(shared_pref.getString("Country", ""));
		} else
			tvcountry.setText("");

		// billing address
		if (shared_pref.getString("Address", "") != null
				&& !shared_pref.getString("Address", "").matches("null")) {

			tvbiling_address.setText(shared_pref.getString("Address", ""));

		} else
			tvbiling_address.setText("");

		// email id
		if (shared_pref.getString("Email", "") != null
				&& !shared_pref.getString("Email", "").matches("null")) {

			tvemailadd.setText(shared_pref.getString("Email", ""));

		} else
			tvemailadd.setText("");

		// wheel chair
		if (shared_pref.getString("WheelChair", "").equals("1")) {

			setTopDrawbleAndText(tvAoWheelchairUser,
					R.drawable.wheelchair_userselected,
					R.color.textview_selected);
		} else {
			setTopDrawbleAndText(tvAoWheelchairUser,
					R.drawable.wheelchair_user, R.color.textcolor_grey);
		}

		// hearing impaired
		if (shared_pref.getString("HearingImpaired", "").equals("true")) {

			setTopDrawbleAndText(tvAoHearingImpaired,
					R.drawable.hearing_impairedselected,
					R.color.textview_selected);
		} else {
			setTopDrawbleAndText(tvAoHearingImpaired,
					R.drawable.hearing_impaired, R.color.textcolor_grey);
		}

		// preferredCard
		if (!shared_pref.getString("truncatedPan", "").equals("")) {
			text_truncatedPan = shared_pref.getString("truncatedPan", "");

		}

		// paying method
		if (shared_pref.getString("PayingMethod", "") != null) {

			if (shared_pref.getString("PayingMethod", "").equals("Cash")
					|| shared_pref.getString("PayingMethod", "").equals("cash")) {
				spinner_preferredmethod.setSelection(0);
				lv_cardetails.setVisibility(View.GONE);

			} else if (shared_pref.getString("PayingMethod", "").equals("Card")
					|| shared_pref.getString("PayingMethod", "").equals("card")
					|| shared_pref.getString("PayingMethod", "").equals("both")
					|| shared_pref.getString("PayingMethod", "").equals(
							"cash and card")) {
				lv_cardetails.setVisibility(View.VISIBLE);
				spinner_preferredmethod.setSelection(1);
			} else if (shared_pref.getString("PayingMethod", "").matches(" ")) {

				Log.e("Paying Method",
						shared_pref.getString("PayingMethod", ""));
				spinner_preferredmethod.setSelection(0);
				lv_cardetails.setVisibility(View.GONE);

			}
		}
		((MainFragmentActivity) getActivity()).onsetdata();

	}

	public void alertdialog(final Context mContext) {
		TextView title = new TextView(mContext);
		title.setTextColor(Color.parseColor("#d16413"));
		title.setText("Alert");
		title.setPadding(10, 10, 10, 10);
		title.setGravity(Gravity.CENTER);
		title.setTextColor(Color.BLACK);
		title.setTextSize(20);

		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setCustomTitle(title);
		builder.setMessage("£0.12 will be deducted from your account. Do you want to continue?");
		builder.setCancelable(false);

		builder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
				if (NetworkBroadcastReceiver.ms_bIsNetworkAvailable) {
					AddNewCardTask task = new AddNewCardTask(mContext);
					task.handler = mhandler;
					task.execute("");
				} else {
					Toast.makeText(mContext, "No network connection.",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		AlertDialog alert = builder.show();
		TextView messageText = (TextView) alert
				.findViewById(android.R.id.message);
		messageText.setGravity(Gravity.CENTER_VERTICAL);
		messageText.setTextColor(Color.BLACK);
		messageText.setTextSize(16);

	}

	protected void user_carddetails(ArrayList<ArrayList<String>> arr_usercard) {
		// TODO Auto-generated method stub

		if (arr_usercard != null && arr_usercard.size() > 0) {
			lv_cardetails.removeAllViews();
			arr_passenger_card.clear();
			for (int i = 0; i < arr_usercard.size(); i++) {
				arr_passenger_card.add("**** **** **** "
						+ arr_usercard.get(i).get(1));

			}
			viewAddCard = new View[arr_usercard.size()];
			final ToggleButton[] cb_card = new ToggleButton[arr_usercard.size()];
			for (int j = 0; j < arr_passenger_card.size(); j++) {
				// Create LinearLayout
				LayoutInflater inflater = LayoutInflater.from(getActivity());

				viewAddCard[j] = inflater.inflate(R.layout.myaccount_card_row,
						null);

				TextView tvCard = (TextView) viewAddCard[j]
						.findViewById(R.id.tv_card_no);

				TextView tvCard_type = (TextView) viewAddCard[j]
						.findViewById(R.id.tv_cardtype);

				if (arr_usercard != null)
					tvCard_type.setText(arr_usercard.get(j).get(2));
				tvCard.setText(arr_passenger_card.get(j));
				lv_cardetails.addView(viewAddCard[j]);
				viewAddCard[j].setTag(j);

				cb_card[j] = (ToggleButton) viewAddCard[j]
						.findViewById(R.id.cb_card);
				if (arr_passenger_card.size() == 1)
					cb_card[j].setChecked(true);
				if (!text_truncatedPan.equals("")
						&& arr_passenger_card
								.get(j)
								.substring(
										arr_passenger_card.get(j).length() - 5,
										arr_passenger_card.get(j).length())
								.trim().equalsIgnoreCase(text_truncatedPan)) {
					cb_card[j].setChecked(true);
				}
				viewAddCard[j].setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						int position = -1;

						if (v.getTag() instanceof Integer) {
							position = (Integer) v.getTag();
							Log.e("Position", "" + position);
							for (int i = 0; i < arr_passenger_card.size(); i++) {

								/* cb_card[i].toggle(); */

								if (i == position) {
									cb_card[i].setChecked(true);
									text_truncatedPan = arr_passenger_card.get(
											i)
											.substring(
													arr_passenger_card.get(i)
															.length() - 5,
													arr_passenger_card.get(i)
															.length());
									Log.e("Selected Truncated Pan",
											text_truncatedPan);

								} else
									cb_card[i].setChecked(false);
							}
							lv_cardetails.refreshDrawableState();
						}
					}
				});
			}

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
		// TODO Auto-generated method stub
		super.onPause();
		((MainFragmentActivity) getActivity()).setCustomBackPressListener(null);
		if (lv_cardetails != null)
			lv_cardetails.removeAllViews();
	}

}