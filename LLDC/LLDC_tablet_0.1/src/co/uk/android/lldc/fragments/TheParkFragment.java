package co.uk.android.lldc.fragments;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import co.uk.android.lldc.R;
import co.uk.android.lldc.adapters.ParkGridAdapter;
import co.uk.android.lldc.models.DashboardModel;
import co.uk.android.lldc.models.TheParkModel;
import co.uk.android.lldc.tablet.HomeActivityTablet;
import co.uk.android.lldc.tablet.LLDCApplication;
import co.uk.android.lldc.utils.Constants;

import com.nostra13.universalimageloader.core.ImageLoader;

public class TheParkFragment extends Fragment implements OnClickListener {
	private static final String TAG = TheParkFragment.class.getSimpleName();

	ArrayList<TheParkModel> theParkList = null;

	public static TheParkFragment sParkFragmentTablet;

	TextView tvOverview, tvVenueOpeningTimes, tvGettingToPark,
			tvNearestTubeAndTrain, tvAccessibility, tvOurAccess;

	// GridView gvPark;
	ParkGridAdapter parklistAdapter = null;
	FragmentManager mFragmentManager;
	LinearLayout llPark;

	Bundle nBundle;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onAttach(Activity activity) {
		sParkFragmentTablet = this;
		super.onAttach(activity);
	}

	@Override
	public void onDetach() {
		sParkFragmentTablet = null;
		super.onDetach();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		((HomeActivityTablet) getActivity()).tvHeaderTitle.setText("The Park");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = null;

		Log.e(TAG, "Device is Tablet...");
		getActivity().setRequestedOrientation(
				ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		rootView = inflater.inflate(R.layout.fragment_the_park_tablet,
				container, false);

		nBundle = new Bundle();

		// gvPark = (GridView) rootView.findViewById(R.id.gridPark);
		llPark = (LinearLayout) rootView.findViewById(R.id.llPark);
		onGetDB();

		((HomeActivityTablet) getActivity()).ivBack
				.setVisibility(View.INVISIBLE);
		((HomeActivityTablet) getActivity()).rlBackBtn.setEnabled(false);
		((HomeActivityTablet) getActivity()).tvHeaderTitle.setText("The Park");

		// gvPark.setOnItemClickListener(new OnItemClickListener() {
		//
		// @Override
		// public void onItemClick(AdapterView<?> arg0, View v, int arg2,
		// long arg3) {
		// // TODO Auto-generated method stub
		//
		// if (v.getTag().toString().equals("-1")) {
		// DashboardModel dashboardModel = LLDCApplication.DBHelper
		// .onGetDashBoardData();
		//
		// sendEmail(dashboardModel.getReportEmailTo(),
		// dashboardModel.getReportEmailSubject(), "");
		//
		// } else {
		// if (theParkList.size() > 0) {
		// // int index = Integer.parseInt(v.getTag().toString());
		// int index = arg2;
		// LLDCApplication.selectedParkModel = theParkList
		// .get(index);
		//
		// ((HomeActivityTablet) getActivity()).tvHeaderTitle
		// .setText(theParkList.get(index).getTitle());
		// nBundle.putString("PAGETITLE", theParkList.get(index)
		// .getTitle());
		// // Constants.PageTitle =
		// // theParkList.get(index).getTitle();
		// displayView(Constants.OVERVIEW);
		// } else {
		// LLDCApplication.onShowToastMesssage(getActivity(),
		// "Unable to retrive the data...");
		// }
		//
		// }
		// }
		// });

		LLDCApplication.removeSimpleProgressDialog();
		return rootView;
	}

	// public void onGetDB() {
	//
	// theParkList = LLDCApplication.DBHelper.onGetTheParkData();
	// Log.e(TAG, "theParkList: " + theParkList.size());
	// if (theParkList.size() > 0) {
	// // set gridview adapter
	// parklistAdapter = new ParkGridAdapter(getActivity(), theParkList);
	// gvPark.setAdapter(parklistAdapter);
	// // setServerData();
	// }
	// }

	public void onGetDB() {

		theParkList = LLDCApplication.DBHelper.onGetTheParkData();

		if (theParkList.size() > 0) {
			setServerData();
		}
	}

	@SuppressLint({ "DefaultLocale", "InflateParams" })
	public void setServerData() {
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		int nLoopCount = 0, n = theParkList.size();

		if (n % 2 == 1)
			nLoopCount = (n / 2) + 1;
		else
			nLoopCount = n / 2;

		for (int i = 0; i < nLoopCount; i++) {
			// continue;
			View horizontalItem = inflater
					.inflate(R.layout.parkslist_row, null);

			if (horizontalItem != null) {

				int j = i + 1;
				LinearLayout llItem1 = (LinearLayout) horizontalItem
						.findViewById(R.id.llItem1);
				ImageView parklist_image = (ImageView) horizontalItem
						.findViewById(R.id.parklist_image);
				TextView parklist_header = (TextView) horizontalItem
						.findViewById(R.id.parklist_header);
				TextView parklist_normal = (TextView) horizontalItem
						.findViewById(R.id.parklist_normal);
				String szImageUrl = theParkList.get((j * 2) - 2).getImageUrl();

				// parklist_image.setImageUrl(szImageUrl, ImageCacheManager
				// .getInstance().getImageLoader());

				try {
					ImageLoader.getInstance().displayImage(szImageUrl,
							parklist_image);
				} catch (Exception e) {
					e.printStackTrace();
				}

				parklist_header.setText(theParkList.get((j * 2) - 2).getTitle()
						.toString().toUpperCase());
				parklist_normal.setText(theParkList.get((j * 2) - 2)
						.getSubTitle().toString().toUpperCase());
				llItem1.setTag((j * 2) - 2 + "");
				llItem1.setOnClickListener(this);

				LinearLayout llItem2 = (LinearLayout) horizontalItem
						.findViewById(R.id.llItem2);
				if (((j * 2) - 1) < n) {

					ImageView parklist_image2 = (ImageView) horizontalItem
							.findViewById(R.id.parklist_image2);
					TextView parklist_header2 = (TextView) horizontalItem
							.findViewById(R.id.parklist_header2);
					TextView parklist_normal2 = (TextView) horizontalItem
							.findViewById(R.id.parklist_normal2);
					String szImageUrl2 = theParkList.get((j * 2) - 1)
							.getImageUrl();

					// parklist_image2.setImageUrl(szImageUrl2,
					// ImageCacheManager
					// .getInstance().getImageLoader());

					try {
						ImageLoader.getInstance().displayImage(szImageUrl2,
								parklist_image2);
					} catch (Exception e) {
						e.printStackTrace();
					}
					parklist_header2.setText(theParkList.get((j * 2) - 1)
							.getTitle().toString().toUpperCase());
					parklist_normal2.setText(theParkList.get((j * 2) - 1)
							.getSubTitle().toString().toUpperCase());
					llItem2.setTag((j * 2) - 1 + "");
					llItem2.setOnClickListener(this);
				} else {
					llItem2.setVisibility(View.GONE);
				}

				// NetworkImageView parklist_image = (NetworkImageView)
				// horizontalItem
				// .findViewById(R.id.parklist_image);
				// TextView parklist_header = (TextView) horizontalItem
				// .findViewById(R.id.parklist_header);
				// TextView parklist_normal = (TextView) horizontalItem
				// .findViewById(R.id.parklist_normal);
				// String szImageUrl = theParkList.get(i).getImageUrl();
				//
				// parklist_image.setImageUrl(szImageUrl, ImageCacheManager
				// .getInstance().getImageLoader());
				//
				// parklist_header.setText(theParkList.get(i).getTitle()
				// .toString().toUpperCase());
				// parklist_normal.setText(theParkList.get(i).getSubTitle()
				// .toString().toUpperCase());

				// horizontalItem.setTag(i + "");
				// horizontalItem.setOnClickListener(this);
				llPark.addView(horizontalItem);
				View view = new View(getActivity());
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, 5);
				view.setLayoutParams(params);
				view.setBackgroundColor(Color.TRANSPARENT);
				llPark.addView(view);
			}

		}

	}

	private void displayView(int position) {

		Fragment fragment = null;

		String szFrameName = "";
		switch (position) {
		case Constants.OVERVIEW:
			fragment = new StaticTrailsFragment();
			szFrameName = StaticTrailsFragment.class.getName();
			fragment.setArguments(nBundle);
			break;

		}

		if (fragment != null) {

			mFragmentManager = getParentFragment().getChildFragmentManager();
			addFragmentOnlyOnce(mFragmentManager, fragment, szFrameName);
			// mFragmentManager = getParentFragment().getChildFragmentManager();
			// mFragmentManager
			// .beginTransaction()
			// .addToBackStack(TAG)
			// .replace(R.id.frame_container_parent, fragment, szFrameName)
			// .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
			// .commit();
		}

	}

	public void addFragmentOnlyOnce(FragmentManager fragmentManager,
			Fragment fragment, String tag) {
		// Make sure the current transaction finishes first
		fragmentManager.executePendingTransactions();

		// If there is no fragment yet with this tag...
		if (fragmentManager.findFragmentByTag(tag) == null) {
			// Add it
			mFragmentManager.beginTransaction().addToBackStack(TAG)
					.replace(R.id.frame_container_parent, fragment, tag)
					.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
					.commit();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getTag().toString().equals("-1")) {
			DashboardModel dashboardModel = LLDCApplication.DBHelper
					.onGetDashBoardData();

			sendEmail(dashboardModel.getReportEmailTo(),
					dashboardModel.getReportEmailSubject(), "");

		} else {
			if (theParkList.size() > 0) {
				int index = Integer.parseInt(v.getTag().toString());
				LLDCApplication.selectedParkModel = theParkList.get(index);

				((HomeActivityTablet) getActivity()).tvHeaderTitle
						.setText(theParkList.get(index).getTitle());
				nBundle.putString("PAGETITLE", theParkList.get(index)
						.getTitle());
				// Constants.PageTitle =
				// theParkList.get(index).getTitle();
				displayView(Constants.OVERVIEW);
			} else {
				LLDCApplication.onShowToastMesssage(getActivity(),
						"Unable to retrive the data...");
			}
			// if (theParkList.size() > 0) {
			// int index = Integer.parseInt(v.getTag().toString());
			// LLDCApplication.selectedParkModel = theParkList.get(index);
			// Intent intent = new Intent(getActivity(),
			// StaticTrailsActivity.class);
			// intent.putExtra("tvFullText", true);
			// intent.putExtra("PAGETITLE", theParkList.get(index).getTitle());
			// getActivity().startActivity(intent);
			// } else {
			// LLDCApplication.onShowToastMesssage(getActivity(),
			// "Unable to retrive the data...");
			// }
		}
	}

	public void sendEmail(final String mailTo, final String p_subject,
			final String p_body) {
		try {
			PackageManager pm = getActivity().getPackageManager();
			ResolveInfo selectedEmailActivity = null;

			Intent emailDummyIntent = new Intent(Intent.ACTION_SENDTO);
			emailDummyIntent.setData(Uri.parse("mailto:" + mailTo));

			List<ResolveInfo> emailActivities = pm.queryIntentActivities(
					emailDummyIntent, 0);

			if (null == emailActivities || emailActivities.size() == 0) {
				Intent emailDummyIntentRFC822 = new Intent(
						Intent.ACTION_SEND_MULTIPLE);
				emailDummyIntentRFC822.setType("message/rfc822");

				emailActivities = pm.queryIntentActivities(
						emailDummyIntentRFC822, 0);
			}

			if (null != emailActivities) {
				if (emailActivities.size() == 1) {
					selectedEmailActivity = emailActivities.get(0);
				} else {
					for (ResolveInfo currAvailableEmailActivity : emailActivities) {
						if (true == currAvailableEmailActivity.isDefault) {
							selectedEmailActivity = currAvailableEmailActivity;
						}
					}
				}

				if (null != selectedEmailActivity) {
					// Send email using the only/default email activity
					sendEmailUsingSelectedEmailApp(mailTo, p_subject, p_body,
							selectedEmailActivity);
				} else {
					final List<ResolveInfo> emailActivitiesForDialog = emailActivities;

					String[] availableEmailAppsName = new String[emailActivitiesForDialog
							.size()];
					for (int i = 0; i < emailActivitiesForDialog.size(); i++) {
						availableEmailAppsName[i] = emailActivitiesForDialog
								.get(i).activityInfo.applicationInfo.loadLabel(
								pm).toString();
					}

					AlertDialog.Builder builder = new AlertDialog.Builder(
							getActivity());
					builder.setTitle("REPORT AN ISSUE");
					builder.setItems(availableEmailAppsName,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									sendEmailUsingSelectedEmailApp(mailTo,
											p_subject, p_body,
											emailActivitiesForDialog.get(which));
								}
							});

					builder.create().show();
				}
			} else {
				sendEmailUsingSelectedEmailApp(mailTo, p_subject, p_body, null);
			}
		} catch (Exception ex) {
			Log.e("", "Can't send email", ex);
		}
	}

	public void sendEmailUsingSelectedEmailApp(String mailTo, String p_subject,
			String p_body, ResolveInfo p_selectedEmailApp) {
		try {
			Intent emailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);

			String aEmailList[] = { mailTo };

			emailIntent.putExtra(Intent.EXTRA_EMAIL, aEmailList);
			emailIntent.putExtra(Intent.EXTRA_SUBJECT,
					null != p_subject ? p_subject : "");
			emailIntent.putExtra(Intent.EXTRA_TEXT, null != p_body ? p_body
					: "");

			if (null != p_selectedEmailApp) {
				Log.d("", "Sending email using " + p_selectedEmailApp);
				emailIntent.setComponent(new ComponentName(
						p_selectedEmailApp.activityInfo.packageName,
						p_selectedEmailApp.activityInfo.name));

				getActivity().startActivityForResult(emailIntent, 0);
			} else {
				Intent emailAppChooser = Intent.createChooser(emailIntent,
						"Select Email app");

				getActivity().startActivityForResult(emailAppChooser, 0);
			}
		} catch (Exception ex) {
			Log.e("", "Error sending email", ex);
		}
	}
}
