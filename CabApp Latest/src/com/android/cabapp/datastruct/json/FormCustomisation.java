package com.android.cabapp.datastruct.json;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

public class FormCustomisation {
	@Expose
	private String type;
	@Expose
	private String fieldType;
	@Expose
	private String name;
	@Expose
	private String friendlyName;
	@Expose
	private List<List<String>> options = new ArrayList<List<String>>();
	@Expose
	private String required;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFriendlyName() {
		return friendlyName;
	}

	public void setFriendlyName(String friendlyName) {
		this.friendlyName = friendlyName;
	}

	public List<List<String>> getOptions() {
		return options;
	}

	public void setOptions(List<List<String>> options) {
		this.options = options;
	}

	public String getRequired() {
		return required;
	}

	public void setRequired(String required) {
		this.required = required;
	}
}
