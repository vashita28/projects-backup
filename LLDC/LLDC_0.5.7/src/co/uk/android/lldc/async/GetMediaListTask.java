package co.uk.android.lldc.async;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import co.uk.android.lldc.fragments.EventSocialFragment;
import co.uk.android.lldc.models.EventMediaModel;
import co.uk.android.lldc.utils.GetMediaListUrl;

public class GetMediaListTask extends AsyncTask<Void, Void, String> {
	private static final String TAG = GetMediaListTask.class.getSimpleName();

	public String szSocialHandle = "";
	public Context mContext;
	ProgressDialog pDialog;

	public GetMediaListTask(Context context, String sSocialHandle) {
		this.mContext = context;
		EventSocialFragment.eventList = new ArrayList<EventMediaModel>();
		this.szSocialHandle = sSocialHandle;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		pDialog = new ProgressDialog(mContext);
		pDialog.setMessage("Loading...");
		pDialog.setCancelable(false);
		pDialog.show();
	}

	@Override
	protected String doInBackground(Void... params) {
		// TODO Auto-generated method stub

		GetMediaListUrl getMediaURL = new GetMediaListUrl(mContext,
				szSocialHandle);

		JSONObject jPaymentResponseObject = null;
		try {
			jPaymentResponseObject = new JSONObject(
					getMediaURL.getMediaListResponse());

			Log.e(TAG, "getMediaURL.getMediaListResponse()   "
					+ getMediaURL.getMediaListResponse().toString());

			JSONArray jsonArray = jPaymentResponseObject.getJSONArray("data");
			for (int i = 0; i < jsonArray.length(); i++) {

				EventMediaModel eventMediaModel = new EventMediaModel();

				int likes = jsonArray.getJSONObject(i).getJSONObject("likes")
						.getInt("count");
				eventMediaModel.setNoOfLikes(likes);

				String szBannerImage = jsonArray.getJSONObject(i)
						.getJSONObject("images")
						.getJSONObject("standard_resolution").getString("url");
				eventMediaModel.setUrlBanner(szBannerImage);

				String szUserName = jsonArray.getJSONObject(i)
						.getJSONObject("user").getString("username");
				eventMediaModel.setName(szUserName);

				String szProfilePictureUrl = jsonArray.getJSONObject(i)
						.getJSONObject("user").getString("profile_picture");
				eventMediaModel.setUrlProfile(szProfilePictureUrl);

				EventSocialFragment.eventList.add(eventMediaModel);

			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return "error";
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);

		if (pDialog != null) {
			pDialog.dismiss();
			pDialog = null;
		}
		EventSocialFragment.mHandler.sendEmptyMessage(0);
	}
}
