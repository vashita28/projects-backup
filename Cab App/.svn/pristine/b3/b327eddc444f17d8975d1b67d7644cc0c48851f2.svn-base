package com.handpoint.headstart.android.models;

import java.math.BigDecimal;

/**
 * 
 * Data holder for currency.
 *
 */
public class CurrencyModel {

	//ISO 4217 numeric code + one lead zero
	private String code;
	//ISO 4217 code
	private String shortName;
	//currency full name
	private String name;
	//currency sign
	private String symbol;
	//Number of digits after the decimal separator
	private int decimalPart;
	//transient field: factor for conversion
	private BigDecimal factor;
	
	public CurrencyModel(String code, String shortName, String name, String symbol, int decimalPart) {
		super();
		this.code = code;
		this.shortName = shortName;
		this.name = name;
		this.symbol = symbol;
		this.decimalPart = decimalPart;
		this.factor = new BigDecimal(Math.pow(10, this.decimalPart));
	}
	
	@Override
	public String toString() {
		return shortName + "-" + name;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 15;
		result = PRIME * result + code.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (null == o) {
			return false;
		}
		CurrencyModel other = (CurrencyModel) o;
		return code.equals(other.code);
	}

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}
	
	public String getShortName() {
		return shortName;
	}

	public String getSymbol() {
		return symbol;
	}

	public int getDecimalPart() {
		return decimalPart;
	}

	public BigDecimal getFactor() {		
		return factor;
	}
}
