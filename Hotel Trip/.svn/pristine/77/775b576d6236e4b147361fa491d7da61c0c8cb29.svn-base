package com.hoteltrip.android;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.hoteltrip.android.util.Const;
import com.hoteltrip.android.util.NumberPicker;

public class RoomOccupancyDetailsActivity extends BaseActivity {

	private int UPPER_LIMIT = 6;
	private int LOWER_LIMIT = 1;
	NumberPicker pickerNumberOfRooms;
	LinearLayout ll_room_details;
	Handler mHandler;
	Handler childCountChangedHandler;
	static CurrentValueForRoomOccupancyDetails currentRoomsRequired;
	ScrollView roomDetailsScrollView;
	CurrentValueForChildCount currentChildCount;

	// TextView roomNumberText;

	ImageButton applyButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_room_occupancy_details);
		super.init("Room Details");
		btnMap.setVisibility(View.GONE);

		applyButton = (ImageButton) findViewById(R.id.imgbtn_apply);
		roomDetailsScrollView = (ScrollView) findViewById(R.id.sv_room_details);
		ll_room_details = (LinearLayout) findViewById(R.id.ll_room_details);

		// roomNumberText = (TextView) findViewById(R.id.tv_room_number);

		// String szRoomOccupancyDetails =
		// "<font color=#ce5f02>Room #%s</font> <font color=#000></font>";
		// roomNumberText.setText(Html.fromHtml(String.format(
		// szRoomOccupancyDetails, 1)));

		// View roomFirstOccupancyDetails = (View)
		// findViewById(R.id.room_first_occupancy_details);

		// NumberPicker pickerNumberOfAdults = (NumberPicker)
		// roomFirstOccupancyDetails
		// .findViewById(R.id.picker_number_of_adults);
		// pickerNumberOfAdults.setRange(1, 4);
		// pickerNumberOfAdults.setFragmentManager(getSupportFragmentManager());
		//
		// NumberPicker pickerNumberOfChild = (NumberPicker)
		// roomFirstOccupancyDetails
		// .findViewById(R.id.picker_number_of_childs);
		// pickerNumberOfChild.setRange(0, 2);
		// pickerNumberOfChild.setFragmentManager(getSupportFragmentManager());
		//
		// Spinner firstChildSpinner = (Spinner)
		// findViewById(R.id.spinner_child1_age);
		// ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
		// this, R.array.childages, R.layout.spinner_age);
		// adapter.setDropDownViewResource(R.layout.spinner_age);
		// firstChildSpinner.setAdapter(adapter);
		//
		// Spinner secondChildSpinner = (Spinner)
		// findViewById(R.id.spinner_child2_age);
		// secondChildSpinner.setAdapter(adapter);

		mHandler = new Handler() {
			@Override
			public void handleMessage(Message message) {
				super.handleMessage(message);
				if (message.what == Const.INCREMENT) {
					inflateAndAddLayout(
							currentRoomsRequired.currentSelectedValue - 1, null);
				} else if (message.what == Const.DECREMENT) {
					ll_room_details
							.removeView(ll_room_details
									.getChildAt(currentRoomsRequired.currentSelectedValue));
				}
				// ll_room_details.removeAllViews();
				// for (int i = 0; i <
				// currentRoomsRequired.currentSelectedValue; i++) {
				// inflateAndAddLayout(currentRoomsRequired.currentSelectedValue);
				// }

				roomDetailsScrollView.post(new Runnable() {
					public void run() {
						roomDetailsScrollView.scrollTo(0,
								roomDetailsScrollView.getBottom());
					}
				});

			}
		};

		pickerNumberOfRooms = (NumberPicker) findViewById(R.id.picker_number_of_rooms);

		pickerNumberOfRooms.setRange(LOWER_LIMIT, UPPER_LIMIT);
		currentRoomsRequired = new CurrentValueForRoomOccupancyDetails();
		pickerNumberOfRooms.setCallBackAfterNumberChangeRoomOccupancyDetails(
				mHandler, currentRoomsRequired);
		pickerNumberOfRooms.setFragmentManager(getSupportFragmentManager());

		int totalNoOfRooms = 1;
		Bundle bundleRoomDetails = getIntent().getExtras();
		if (bundleRoomDetails != null)
			totalNoOfRooms = bundleRoomDetails.getInt("totalrooms");

		pickerNumberOfRooms.setCurrent(totalNoOfRooms);

		for (int i = 0; i < totalNoOfRooms; i++) {
			inflateAndAddLayout(i, bundleRoomDetails);
		}

		applyButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int totalNumberOfRooms = ll_room_details.getChildCount();

				Bundle bundleRoomDetails = new Bundle();
				bundleRoomDetails.putInt("totalrooms", totalNumberOfRooms);

				for (int i = 0; i < totalNumberOfRooms; i++) {

					View view_room_occupancy = ll_room_details.getChildAt(i);

					NumberPicker pickerNumberOfAdults = (NumberPicker) view_room_occupancy
							.findViewById(R.id.picker_number_of_adults);
					Spinner ageFirstChildSpinner = (Spinner) view_room_occupancy
							.findViewById(R.id.spinner_child1_age);
					Spinner ageSecondChildSpinner = (Spinner) view_room_occupancy
							.findViewById(R.id.spinner_child2_age);

					int numberOfAdults = pickerNumberOfAdults.getCurrent();
					int numberOfChild = 0;
					int ageOfChild1 = 0;
					if (ageFirstChildSpinner.getSelectedItemPosition() > 0) {
						ageOfChild1 = Integer.parseInt(ageFirstChildSpinner
								.getSelectedItem().toString());
						numberOfChild++;
					}

					int ageOfChild2 = 0;
					if (ageSecondChildSpinner.getSelectedItemPosition() > 0) {
						ageOfChild2 = Integer.parseInt(ageSecondChildSpinner
								.getSelectedItem().toString());
						numberOfChild++;
					}

					Log.e("RoomOccupancyDetailsActivity", " Room No " + (i + 1)
							+ ", number of adults:: " + numberOfAdults
							+ ", number of child:: " + numberOfChild
							+ ", Age of child 1:: " + ageOfChild1
							+ ", Age of Child 2:: " + ageOfChild2);
					bundleRoomDetails.putInt("numberofadults" + (i + 1),
							numberOfAdults);
					bundleRoomDetails.putInt("numberofchild" + (i + 1),
							numberOfChild);
					bundleRoomDetails.putInt("ageoffirstchild" + (i + 1),
							ageOfChild1);
					bundleRoomDetails.putInt("ageofsecondchild" + (i + 1),
							ageOfChild2);
				}

				Intent intent = new Intent(RoomOccupancyDetailsActivity.this,
						FindHotelActivity.class);
				intent.putExtras(bundleRoomDetails);
				intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				finish();
				startActivity(intent);

			}
		});
	}

	void inflateAndAddLayout(int i, Bundle bundleRoomDetails) {

		String szRoomOccupancyDetails = "<font color=#ce5f02>Room #%s</font> <font color=#3C3F38></font>";

		LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
		View view_room_occupancy_details = inflater.inflate(
				R.layout.room_adult_child_count_row, null);

		TextView roomNumberText = (TextView) view_room_occupancy_details
				.findViewById(R.id.tv_room_number);
		roomNumberText.setText(Html.fromHtml(String.format(
				szRoomOccupancyDetails, i + 1)));

		NumberPicker pickerNumberOfAdults = (NumberPicker) view_room_occupancy_details
				.findViewById(R.id.picker_number_of_adults);
		pickerNumberOfAdults.setRange(1, 4);
		pickerNumberOfAdults.setFragmentManager(getSupportFragmentManager());
		if (bundleRoomDetails != null
				&& bundleRoomDetails.containsKey("numberofadults" + (i + 1))) {
			pickerNumberOfAdults.setCurrent(bundleRoomDetails
					.getInt("numberofadults" + (i + 1)));
		}

		childCountChangedHandler = new Handler() {
			@Override
			public void handleMessage(Message message) {
				super.handleMessage(message);
				// int numberOfChildCount =
				// currentChildCount.currentNumberOfChild;
				int posInLayout = message.arg1;
				Log.e("***********", "*************" + posInLayout);
				// View view_room_occupancy_details = ll_room_details
				// .getChildAt(posInLayout);

				// Spinner ageFirstChildSpinner = (Spinner)
				// view_room_occupancy_details
				// .findViewById(R.id.spinner_child1_age);
				// Spinner ageSecondChildSpinner = (Spinner)
				// view_room_occupancy_details
				// .findViewById(R.id.spinner_child2_age);
				//
				// if (message.what == Const.INCREMENT) {
				// if (numberOfChildCount == 1) {
				// ageFirstChildSpinner.setEnabled(true);
				// } else if (numberOfChildCount == 2) {
				// ageSecondChildSpinner.setEnabled(true);
				// }
				//
				// } else if (message.what == Const.DECREMENT) {
				// if (numberOfChildCount == 1) {
				// ageSecondChildSpinner.setEnabled(false);
				// } else if (numberOfChildCount == 0) {
				// ageFirstChildSpinner.setEnabled(false);
				// }
				// }

			}
		};

		// NumberPicker pickerNumberOfChild = (NumberPicker)
		// view_room_occupancy_details
		// .findViewById(R.id.picker_number_of_childs);
		// pickerNumberOfChild.setRange(0, 2);
		// currentChildCount = new CurrentValueForChildCount();
		// pickerNumberOfChild.setPositionInLayout(i);
		// pickerNumberOfChild.setCallBackAfterNumberOfChildChange(
		// childCountChangedHandler, currentChildCount);
		// pickerNumberOfChild.setFragmentManager(getSupportFragmentManager());

		Spinner ageFirstChildSpinner = (Spinner) view_room_occupancy_details
				.findViewById(R.id.spinner_child1_age);
		// ageFirstChildSpinner.setEnabled(false);
		Spinner ageSecondChildSpinner = (Spinner) view_room_occupancy_details
				.findViewById(R.id.spinner_child2_age);
		// ageSecondChildSpinner.setEnabled(false);

		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				RoomOccupancyDetailsActivity.this, R.array.childages,
				R.layout.spinner_age);
		adapter.setDropDownViewResource(R.layout.spinner_age);
		ageFirstChildSpinner.setAdapter(adapter);

		ageSecondChildSpinner.setAdapter(adapter);

		if (bundleRoomDetails != null
				&& bundleRoomDetails.containsKey("ageoffirstchild" + (i + 1))) {
			ageFirstChildSpinner.setSelection(bundleRoomDetails
					.getInt("ageoffirstchild" + (i + 1)));
		}

		if (bundleRoomDetails != null
				&& bundleRoomDetails.containsKey("ageofsecondchild" + (i + 1))) {
			ageSecondChildSpinner.setSelection(bundleRoomDetails
					.getInt("ageofsecondchild" + (i + 1)));
		}

		ll_room_details.addView(view_room_occupancy_details);
	}

	public class CurrentValueForRoomOccupancyDetails {
		public int currentSelectedValue = 1;
	}

	public class CurrentValueForChildCount {
		public int currentNumberOfChild = 0;
	}

}
