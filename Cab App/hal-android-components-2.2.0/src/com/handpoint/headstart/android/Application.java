package com.handpoint.headstart.android;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.util.Log;

import com.handpoint.headstart.android.models.CurrencyModel;
import com.handpoint.headstart.spi.ISO4217CurrencyCodes;

public class Application extends android.app.Application {
	
	private static final BigDecimal HUNDRED = new BigDecimal(100);
	
	private static Application sInstance;

	public static Application getInstance() {
		return sInstance;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		sInstance = this;
		sInstance.initializeInstance();
	}
	
	// do all you initialization here
	protected void initializeInstance() {
		//trying to load currencies from resource file
		currencies = loadCurrencies(getCurrenciesResource());
		//if currencies were not loaded, load default set
		if (currencies.size() == 0) {
			currencies = loadCurrencies();
		}
	}

	protected int getCurrenciesResource() {
		return 0;
	}
	
	private Map<String, CurrencyModel> currencies;
	
	private Map<String, CurrencyModel> loadCurrencies() {
		Map<String, CurrencyModel> currencies = new ConcurrentHashMap<String, CurrencyModel>();
		Resources resources  = getResources();
		int defaultDecimalSubUnit = 2;
		for (Object oCode : ISO4217CurrencyCodes.getSupportedAlphaCodes()) {
			String code = (String) oCode;
			int symbolId = resources.getIdentifier("currency_symbol_" + code, "string", getApplicationContext().getPackageName());
			String symbol = symbolId == 0 ? code : getApplicationContext().getString(symbolId);
			int titleId = resources.getIdentifier("currency_title_" + code, "string", getApplicationContext().getPackageName());
			String title = titleId == 0 ? code : getApplicationContext().getString(titleId);
			int decimalSubUnit = "ISK".equals(code) ? 0 : defaultDecimalSubUnit;
			CurrencyModel cm = new CurrencyModel(ISO4217CurrencyCodes.getNumericCode(code), code, title, symbol, decimalSubUnit);
			currencies.put(cm.getCode(), cm);
		}
		return currencies;
	}
	
	private Map<String, CurrencyModel> loadCurrencies(int resourceId) {
		Map<String, CurrencyModel> currencies = new ConcurrentHashMap<String, CurrencyModel>();
		try {
			Resources resources  = getResources();
			XmlResourceParser parser = resources.getXml(resourceId);
			parser.next();
			while (XmlResourceParser.END_DOCUMENT != parser.getEventType()) {
				if (parser.getAttributeCount() > 0) {
					String code = parser.getAttributeValue(0);
					String shortName = parser.getAttributeValue(1);
					int decimalSubUnit = parser.getAttributeIntValue(2, 2);
					String symbol = parser.getAttributeValue(3);
					int nameId = resources.getIdentifier("currency_title_" + shortName, "string", getApplicationContext().getPackageName());
					String name = nameId == 0 ? code : getApplicationContext().getString(nameId);
					CurrencyModel cm = new CurrencyModel(code, shortName, name, symbol, decimalSubUnit);
					currencies.put(code, cm);
				}
				if (1 == parser.getDepth()
						&& XmlResourceParser.END_TAG == parser.getEventType()) {
					parser.next();
				} else {
					parser.nextTag();
				}
			}
		} catch (Exception e) {
			Log.e("Application", "Error load currencies from resources", e);
		}
		return currencies;
	}

	/**
	 * Returns list of available currencies
	 * @return list of available currencies
	 */
	public List<CurrencyModel> getCurrencies() {
		return new ArrayList<CurrencyModel>(currencies.values());
	}
	/**
	 * Returns currency by code
	 * @param code - currency code
	 * @return currency
	 */
	public CurrencyModel getCurrency(String code) {
		return currencies.get(code);
	}

	public String getCurrencySymbol(String code) {
		CurrencyModel currency = getCurrency(code);
		return null == currency ?  "" : currency.getSymbol();
	}
	
	public int getCurrencyDecimalPart(String code) {
		CurrencyModel currency = getCurrency(code);
		return null == currency ?  2 : currency.getDecimalPart();
	}

	public String getFormattedAmount(int totalAmount, String currencyCode) {
		CurrencyModel cm = getCurrency(currencyCode);
		if (null != cm) {
			return getFormattedAmount(
					new BigDecimal(totalAmount).divide(HUNDRED, cm.getDecimalPart(), RoundingMode.FLOOR), currencyCode); 
		}
		return ""; 
	}

	public String getFormattedAmount(BigDecimal totalAmount, String currencyCode) {
		if (null != totalAmount && null != currencyCode) {
			return getDecimalFormat(currencyCode, false).format(totalAmount);
		}
		return null;
	}

	public DecimalFormat getDecimalFormat(String currencyCode, boolean useOneCharSymbolOnly) {
		DecimalFormat df = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
		DecimalFormatSymbols dfs = df.getDecimalFormatSymbols();
		String currencySymbol = getCurrencySymbol(currencyCode);
		if (null != currencySymbol && currencySymbol.length() > 1 && useOneCharSymbolOnly) {
			currencySymbol = "";
		}
		dfs.setCurrencySymbol(currencySymbol);
		df.setDecimalFormatSymbols(dfs);
		df.setGroupingUsed(false);
		int decimalPart = getCurrencyDecimalPart(currencyCode);
		df.setMaximumFractionDigits(decimalPart);
		df.setMinimumFractionDigits(decimalPart);
		return df; 
	}
}
