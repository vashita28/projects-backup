package co.uk.android.lldc.utils;

import java.io.IOException;

import android.content.Context;
import android.util.Log;

public class GetMediaListUrl {
	Connection connection;
	String sSocialHandle = "";

	public GetMediaListUrl(Context context, String szSocialHandle) {
		// TODO Auto-generated constructor stub
		connection = new Connection(context);
		this.sSocialHandle = szSocialHandle;
	}

	public String getMediaListResponse() {
		connection.szSocialHandle = sSocialHandle;
		connection.connect(Constants.GET_MEDIA_LIST);
		String szResponse = "";
		if (connection.mInputStream != null) {
			try {
				szResponse = connection
						.inputStreamToString(connection.mInputStream);

				// Log.e("GetMediaListUrl", "szResponse:: " + szResponse);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return szResponse;
	}

}
