package com.hoteltrip.android.util;

import java.util.List;

import org.springframework.web.client.RestTemplate;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class SearchTask extends AsyncTask<Void, Void, Void> {

	private Context context;
	private String criterion;
	private List<String> results;
	private RestTemplate restTemplate;
	private Double lat;
	private Double lon;
	private ProgressDialog progressDialog;
	
	
	/* So currently there are two ways we can make server calls, 
	 *  one is by passing a criterion, either from place search or nearby.
	 * */
	
	public SearchTask(Context context, String criterion) {
		super();
		this.context = context;
		this.criterion = criterion;
		init();
	}
	
	/**/
	
	public SearchTask(Context context, Double lat, Double lon){
		super();
		this.context = context;
		this.lat = lat;
		this.lon = lon;
		init();
	}
	
	
	public void init(){
		restTemplate = new RestTemplate();
	}
	

	@Override
	protected void onPreExecute() {
		Toast.makeText(context, "Sending request", Toast.LENGTH_SHORT).show();
	}

	@Override
	protected Void doInBackground(Void... params) {
		
		//TODO: do your server call here
		
		
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		Toast.makeText(context, "Data successfully retrieved", Toast.LENGTH_SHORT).show();
	}
}
