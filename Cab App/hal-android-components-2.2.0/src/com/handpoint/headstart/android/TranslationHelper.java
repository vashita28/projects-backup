package com.handpoint.headstart.android;

import android.content.Context;

import com.handpoint.headstart.R;
/**
 * 
 * Helper class for getting messages for customer.
 *
 */
public class TranslationHelper {

	/**
	 * Util method that returns description type of financial transaction.
	 * @param type
	 * @return
	 */
	public static final String getTypeDescription(Context ctx, int type) {
		try {
			return ctx.getResources().getStringArray(R.array.transaction_types)[type];
		} catch (Exception e) {
			return ctx.getResources().getStringArray(R.array.transaction_types)[0];
		}
	};
	
	/**
	 * Util method that returns description result status of financial transaction.
	 * @param type
	 * @return
	 */
	public static final String getTransactionStatusDescription(Context ctx, int status) {
		try {
			return ctx.getResources().getStringArray(R.array.transaction_statuses)[status];
		} catch (Exception e) {
			return ctx.getResources().getStringArray(R.array.transaction_statuses)[0];
		}
	};

}
