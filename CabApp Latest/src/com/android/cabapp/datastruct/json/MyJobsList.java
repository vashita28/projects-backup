package com.android.cabapp.datastruct.json;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

public class MyJobsList {

	@Expose
	private List<Job> jobs = new ArrayList<Job>();
	@Expose
	private String currentTime;

	public List<Job> getJobs() {
		return jobs;
	}

	public void setJobs(List<Job> jobs) {
		this.jobs = jobs;
	}

	public String getCurrentTime() {
		return currentTime;
	}

	public void setCurrentTime(String currentTime) {
		this.currentTime = currentTime;
	}

}
