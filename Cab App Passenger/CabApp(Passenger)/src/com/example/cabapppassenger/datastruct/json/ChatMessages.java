package com.example.cabapppassenger.datastruct.json;

public class ChatMessages {

	public String source, message;

	public ChatMessages(String source, String message) {
		super();
		this.source = source;
		this.message = message;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
