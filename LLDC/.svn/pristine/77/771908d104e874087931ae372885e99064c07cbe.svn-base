package co.uk.android.lldc.async;

import java.io.IOException;
import java.io.InputStream;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import co.uk.android.lldc.LLDCApplication;
import co.uk.android.lldc.SplashScreen;
import co.uk.android.lldc.models.UrgentClosureModel;

public class AsyncUrgentClosure extends AsyncTask<Void, Void, Void>{

	Context mContext;
	
	public AsyncUrgentClosure(Context context) {
		// TODO Auto-generated constructor stub
		mContext = context;
	}
	
	@Override
	protected Void doInBackground(Void... params) {
		// TODO Auto-generated method stub
		
		getData();
		return null;
	}
	
	@Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if (SplashScreen.mHandler != null) {
			SplashScreen.mHandler.sendEmptyMessage(1);
		}
	}
	
	public void getData() {
		try {
			
			LLDCApplication.urgentClosureModel = new UrgentClosureModel();
			JSONObject obj = new JSONObject(loadJSONFromAsset());
			LLDCApplication.urgentClosureModel.setMsg(obj.getString("msg").toString().trim());
			LLDCApplication.urgentClosureModel.setMsgId(obj.getString("msgId").toString().trim());
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String loadJSONFromAsset() {
		String json = null;
		try {

			InputStream is = mContext.getAssets().open(
					"urgentclouser.json");

			int size = is.available();

			byte[] buffer = new byte[size];

			is.read(buffer);

			is.close();

			json = new String(buffer, "UTF-8");

		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
		return json;

	}

}
