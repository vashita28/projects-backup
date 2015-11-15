package co.uk.android.lldc.models;

import android.content.Context;
import co.uk.android.lldc.utils.Connection;
import co.uk.android.lldc.utils.Constants;

public class WiFiModel {
	private static final String TAG = WiFiModel.class.getSimpleName();

	Connection connection;
	Context mContext;

	public WiFiModel(Context context) {
		// TODO Auto-generated constructor stub
		this.mContext = context;
		connection = new Connection(mContext);
	}

	public String getWiFiList(Context context) {
		connection.connect(Constants.GET_WIFI_LIST, "");

		String szResponse = "";
		if (connection.mInputStream != null) {
			try {
				szResponse = connection
						.inputStreamToString(connection.mInputStream);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return szResponse;

	}

}
