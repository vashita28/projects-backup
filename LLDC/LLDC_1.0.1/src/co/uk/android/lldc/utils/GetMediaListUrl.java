package co.uk.android.lldc.utils;

import java.io.IOException;

import android.content.Context;
import android.util.Log;

public class GetMediaListUrl {
	Connection connection;
	String sSocialHandle = "";
	String pageURL = "";

	public GetMediaListUrl(Context context, String szSocialHandle, String pageUrl) {
		// TODO Auto-generated constructor stub
		connection = new Connection(context);
		this.sSocialHandle = szSocialHandle;
		this.pageURL= pageUrl; 
	}

	public String getMediaListResponse() {
		connection.szSocialHandle = sSocialHandle;
		if (pageURL.equals("")) {
			connection.connect(Constants.GET_MEDIA_LIST,"");
		}else{
			connection.connect(Constants.GET_MEDIA_NEXT_PAGE,pageURL);
		}
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
