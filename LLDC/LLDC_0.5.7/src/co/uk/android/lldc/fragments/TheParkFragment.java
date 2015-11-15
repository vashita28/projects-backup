package co.uk.android.lldc.fragments;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import co.uk.android.lldc.LLDCApplication;
import co.uk.android.lldc.R;
import co.uk.android.lldc.StaticTrailsActivity;
import co.uk.android.lldc.models.DashboardModel;
import co.uk.android.lldc.models.TheParkModel;

import com.nostra13.universalimageloader.core.ImageLoader;

public class TheParkFragment extends Fragment implements OnClickListener {

	// LinearLayout rlOverview, rlGettingToPark, rlAccessibility,
	// rlReportAnIssue;

	ImageView ivClock, ivGettingToPark, ivAccessibility;

	ArrayList<TheParkModel> theParkList = null;

	TextView tvOverview, tvVenueOpeningTimes, tvGettingToPark,
			tvNearestTubeAndTrain, tvAccessibility, tvOurAccess;

	LinearLayout llPark;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_the_park, container,
				false);

		llPark = (LinearLayout) rootView.findViewById(R.id.llPark);

		onGetDB();

		return rootView;
	}

	public void onGetDB() {

		theParkList = LLDCApplication.DBHelper.onGetTheParkData();

		if (theParkList.size() > 0) {
			setServerData();
		}
	}

	@SuppressLint({ "DefaultLocale", "InflateParams" })
	public void setServerData() {
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		for (int i = 0; i < theParkList.size(); i++) {
			// continue;
			View horizontalItem = inflater
					.inflate(R.layout.parkslist_row, null);

			if (horizontalItem != null) {
				ImageView parklist_image = (ImageView) horizontalItem
						.findViewById(R.id.parklist_image);
				TextView parklist_header = (TextView) horizontalItem
						.findViewById(R.id.parklist_header);
				TextView parklist_normal = (TextView) horizontalItem
						.findViewById(R.id.parklist_normal);
				String szImageUrl = theParkList.get(i).getImageUrl();

				if (szImageUrl.contains("overview_icon.png")) {
					parklist_image.setImageResource(R.drawable.overview);
				} else if (szImageUrl.contains("infopark_icon.png")) {
					parklist_image.setImageResource(R.drawable.infopark_icon);
				} else if (szImageUrl.contains("accessibilty.png")) {
					parklist_image
							.setImageResource(R.drawable.accessibilty);
				} else if (szImageUrl.contains("help_icon.png")) {
					parklist_image.setImageResource(R.drawable.issue_icon2x);
				} else {
					ImageLoader.getInstance().displayImage(szImageUrl,
							parklist_image);
				}

				parklist_header.setText(theParkList.get(i).getTitle()
						.toString().toUpperCase());
				parklist_normal.setText(theParkList.get(i).getSubTitle()
						.toString().toUpperCase());
				horizontalItem.setTag(i + "");
				horizontalItem.setOnClickListener(this);
				llPark.addView(horizontalItem);
				View view = new View(getActivity());
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, 5);
				view.setLayoutParams(params);
				view.setBackgroundColor(Color.TRANSPARENT);
				llPark.addView(view);
			}

		}

		// View horizontalItem = inflater.inflate(R.layout.parkslist_row, null);

		// if (horizontalItem != null) {
		// @SuppressWarnings("unused")
		// ImageView parklist_image = (ImageView) horizontalItem
		// .findViewById(R.id.parklist_image);
		// TextView parklist_header = (TextView) horizontalItem
		// .findViewById(R.id.parklist_header);
		// TextView parklist_normal = (TextView) horizontalItem
		// .findViewById(R.id.parklist_normal);
		// parklist_header.setText("REPORT AN ISSUE");
		// parklist_normal.setText("HELP US KEEP THE PARK SAFE");
		// horizontalItem.setTag("-1");
		// horizontalItem.setOnClickListener(this);
		// llPark.addView(horizontalItem);
		// View view = new View(getActivity());
		// LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
		// LayoutParams.MATCH_PARENT, 3);
		// view.setLayoutParams(params);
		// view.setBackgroundColor(Color.TRANSPARENT);
		// llPark.addView(view);
		// }
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
				Intent intent = new Intent(getActivity(),
						StaticTrailsActivity.class);
				intent.putExtra("tvFullText", true);
				intent.putExtra("PAGETITLE", theParkList.get(index).getTitle());
				getActivity().startActivity(intent);
			} else {
				LLDCApplication.onShowToastMesssage(getActivity(),
						"Unable to retrive the data...");
			}
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
