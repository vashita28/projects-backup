package com.android.cabapp.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.util.Log;

import com.android.cabapp.util.Connection;
import com.android.cabapp.util.Constants;

public class RejectJobs {
	private static final String TAG = RejectJobs.class.getSimpleName();

	Connection connection;
	List<NameValuePair> nameValuePairs;
	Context mContext;
	String jobId;

	public RejectJobs(Context context, String sJobId) {
		// TODO Auto-generated constructor stub
		connection = new Connection(context);
		nameValuePairs = new ArrayList<NameValuePair>();
		mContext = context;
		this.jobId = sJobId;
	}

	public String rejectSelectedJob() {

		nameValuePairs.add(new BasicNameValuePair("data[bookingId]", jobId));

		connection.prepareConnection(nameValuePairs);
		connection.connect(Constants.REJECT_JOBS);

		String rejectJobsResponse = "";

		if (connection.mInputStream != null) {
			try {
				{
					rejectJobsResponse = connection.inputStreamToString(
							connection.mInputStream).toString();

					if (Constants.isDebug)
						Log.e(TAG, "RejectJobs response:: "
								+ rejectJobsResponse.toString());
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return rejectJobsResponse;

	}
}
