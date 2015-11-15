package co.uk.pocketapp.gmd.ui;

import android.util.Log;

public class Data_Model_Archive_Reports {
	private static final String TAG = "DATA MODEL ARCHIVE REPORTS";
	public String report_Title;
	public String date_Time;
	
	public Data_Model_Archive_Reports(String reportTitle, String date_N_Time){
		this.report_Title=reportTitle;
		Log.v(TAG,"REPORT TITLE"+ reportTitle);
		this.date_Time=date_N_Time;
		Log.v(TAG,"DATA AND TIME"+ date_N_Time);
	}

	public String getReport_Title() {
		return report_Title;
	}

	public void setReport_Title(String report_Title) {
		this.report_Title = report_Title;
	}

	public String getDate_Time() {
		return date_Time;
	}

	public void setDate_Time(String date_Time) {
		this.date_Time = date_Time;
	}
}
