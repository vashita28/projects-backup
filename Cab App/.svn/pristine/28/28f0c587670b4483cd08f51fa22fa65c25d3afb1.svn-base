package com.handpoint.headstart.android.tests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.handpoint.headstart.android.Application;
import com.handpoint.headstart.android.models.CurrencyModel;

public class MockTestApplication extends Application {

	private Map<String, CurrencyModel> currencies = new HashMap<String, CurrencyModel>();

	public MockTestApplication() {
		super();
		currencies.put("0840", new CurrencyModel("0840", "USD", "Americal dollar", "$", 2));
		currencies.put("0826", new CurrencyModel("0826", "GBP", "British pound", "£", 2));		
	}

	@Override
	public List<CurrencyModel> getCurrencies() {
		return new ArrayList<CurrencyModel>(currencies.values());
	}
	
	@Override
	public CurrencyModel getCurrency(String code) {
		return currencies.get(code);
	}
}
