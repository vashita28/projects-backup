package com.rassaapp.deliveryapp;

import java.util.ArrayList;

import com.get2gether.deliveryapp.R;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;

public class Menu extends Activity {
	Context context = this;
	LinearLayout order;
	ImageButton startorder;
	LinearLayout menu, categories_list;
	TextView texthint;

	String[] item_image;
	String[] item_title;
	String[] item_shortdesc;
	String[] item_desc;
	String[] item_price;
	int[] item_category;

	String[] MenuCategories;
	String sign = "";

	ArrayList<Integer> items_in_order = new ArrayList<Integer>();

	TextView buy;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);

		//load data from strings.xml
		sign = getResources().getString(R.string.CurrencySign);
		item_title = getResources().getStringArray(R.array.ItemTitle);
		item_image = getResources().getStringArray(R.array.ItemImage);
		item_desc = getResources().getStringArray(R.array.ItemDescription);
		item_shortdesc = getResources().getStringArray(R.array.ItemShortDescription);
		item_price = getResources().getStringArray(R.array.ItemPrice);
		item_category = getResources().getIntArray(R.array.ItemCategory);
		MenuCategories = getResources().getStringArray(R.array.MenuCategories);

		//load resources
		TextView menu_title = (TextView) findViewById(R.id.menu_title);
		TextView yourorder = (TextView) findViewById(R.id.yourorder);
		TextView hinttext = (TextView) findViewById(R.id.hinttext);
		TextView categories = (TextView) findViewById(R.id.categories);
		menu = (LinearLayout) findViewById(R.id.menuscroll);
		startorder = (ImageButton) findViewById(R.id.startorder);
		order = (LinearLayout) findViewById(R.id.orderscroll);
		texthint = (TextView) findViewById(R.id.hinttext);
		categories_list = (LinearLayout) findViewById(R.id.categories_list);

		//force portrait
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		//set textsizes according to resolution
		menu_title.setTextSize(fontpercent_screenheight(5));
		yourorder.setTextSize(fontpercent_screenheight(2.5));
		hinttext.setTextSize(fontpercent_screenwidth(3.5));
		categories.setTextSize(fontpercent_screenwidth(3.5));

		//insert menu items
		RefreshMenuItems(0);

		//category setting
		for (int i = 0; i < MenuCategories.length; i++) {
			final int currenti = i;

			TextView category = new TextView(this);
			category.setText(MenuCategories[currenti]);
			category.setPadding(dp(10), dp(0), dp(10), dp(0));
			if (i == 0)
				category.setTextColor(0xffff0000);
			else
				category.setTextColor(0xffa0a0a0);
			category.setTextSize(fontpercent_screenheight(2.5));
			category.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					for (int i = 1; i < categories_list.getChildCount(); i++)
						((TextView) categories_list.getChildAt(i)).setTextColor(0xffa0a0a0);
					((TextView) v).setTextColor(0xffff0000);
					while (menu.getChildCount() > 1)
						menu.removeViewAt(1);

					RefreshMenuItems(currenti);
				}
			});
			category.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						((TextView) v).setTextColor(0xffffffff);
					} else if (event.getAction() == MotionEvent.ACTION_UP) {
						((TextView) v).setTextColor(0xffa0a0a0);
					}
					return false;
				}
			});
			categories_list.addView(category);
		}

		//Enter your address button clicked (the Truck button)
		startorder.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				//enter address
				if (order.getChildCount() > 0) {
					if (order.getChildAt(0) != texthint) {
						Intent intent = new Intent(context, AddressPage.class).putExtra("items_in_order", items_in_order);
						startActivity(intent);
					} else
						Toast.makeText(context, R.string.MenuNoItemsInOrder, Toast.LENGTH_LONG).show();
				}
			}
		});

	}

	public void RefreshMenuItems(int category) {
		//Create menu scroller
		LinearLayout.LayoutParams card_lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
		card_lp.setMargins(dp(5), 0, dp(5), 0);

		LinearLayout.LayoutParams image_lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 0);
		image_lp.setMargins(dp(40), dp(5), dp(40), dp(5));
		image_lp.weight = 0.5f;

		LinearLayout.LayoutParams desc_lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
		desc_lp.weight = 0.32f;

		LinearLayout.LayoutParams title_lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
		title_lp.weight = 0.08f;

		RelativeLayout.LayoutParams price_rp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		price_rp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

		RelativeLayout.LayoutParams buy_rp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		buy_rp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

		LinearLayout.LayoutParams bottom_lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
		bottom_lp.weight = 0.1f;

		//fill menu
		for (int i = 0; i < item_title.length; i++) {
			final int currenti = i;

			if ((item_category[i] == category) || category == 0) {
				TextView title = new TextView(this);
				title.setText(item_title[currenti]);
				title.setGravity(Gravity.CENTER);
				title.setTextColor(0xff212121);
				title.setPadding(dp(10), dp(0), dp(10), dp(0));
				title.setTypeface(null, Typeface.BOLD);
				title.setLayoutParams(title_lp);
				title.setBackgroundResource(R.drawable.borderradius2);
				title.setTextSize(fontpercent_screenheight(2.5));

				ImageView item = new ImageView(this);
				item.setImageResource(getResources().getIdentifier(item_image[i], "drawable", getPackageName()));
				item.setScaleType(ScaleType.FIT_CENTER);
				item.setAdjustViewBounds(true);
				item.setLayoutParams(image_lp);

				TextView desc = new TextView(this);
				desc.setText(item_shortdesc[currenti]);
				desc.setPadding(dp(10), dp(5), dp(10), dp(5));
				desc.setLayoutParams(desc_lp);
				desc.setTextSize(fontpercent_screenheight(2.5));

				TextView price = new TextView(this);
				price.setText(String.format(sign + "%.2f", Double.parseDouble(item_price[currenti])));
				price.setPadding(0, dp(5), dp(10), dp(5));
				price.setLayoutParams(price_rp);
				price.setTextSize(fontpercent_screenheight(2.5));

				buy = new TextView(this);
				buy.setText(R.string.MenuAddToOrderButton);
				buy.setPadding(dp(10), dp(5), 0, dp(5));
				buy.setTextColor(0xffff0000);
				buy.setLayoutParams(buy_rp);
				buy.setTextSize(fontpercent_screenheight(2.5));
				buy.setClickable(true);
				buy.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						addItemToOrder(currenti);
					}
				});
				buy.setOnTouchListener(new OnTouchListener() {
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						if (event.getAction() == MotionEvent.ACTION_DOWN) {
							((TextView) v).setTextColor(0xff212121);
						} else if (event.getAction() == MotionEvent.ACTION_UP) {
							((TextView) v).setTextColor(0xffff0000);
						}
						return false;
					}
				});

				RelativeLayout bottom = new RelativeLayout(this);
				bottom.setLayoutParams(bottom_lp);
				bottom.addView(buy);
				bottom.addView(price);

				LinearLayout card = new LinearLayout(this);
				card.setOrientation(LinearLayout.VERTICAL);
				card.setLayoutParams(card_lp);
				card.setBackgroundResource(R.drawable.borderradius1);
				card.setWeightSum(1f);
				card.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(context, ItemProfile.class).putExtra("item_image", getResources().getIdentifier(item_image[currenti], "drawable", getPackageName())).putExtra("item_title", item_title[currenti]).putExtra("item_desc", item_desc[currenti]).putExtra("item_price", Double.parseDouble(item_price[currenti]));
						startActivity(intent);
					}
				});

				card.addView(title);
				card.addView(item);
				card.addView(desc);
				card.addView(bottom);

				menu.addView(card);
			}
		}
	}

	//called when user adds an item to order.
	public void addItemToOrder(final int item) {

		LinearLayout.LayoutParams order_lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
		order_lp.setMargins(dp(10), 0, dp(10), 0);

		FrameLayout.LayoutParams newitem_fp = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

		FrameLayout.LayoutParams remove_fp = new FrameLayout.LayoutParams(fontpercent_screenwidth(8), fontpercent_screenwidth(8), Gravity.RIGHT | Gravity.TOP);

		ImageView newitem = new ImageView(this);
		newitem.setImageResource(getResources().getIdentifier(item_image[item], "drawable", getPackageName()));
		newitem.setScaleType(ScaleType.FIT_CENTER);
		newitem.setAdjustViewBounds(true);
		newitem.setLayoutParams(newitem_fp);

		ImageView remove = new ImageView(this);
		remove.setImageResource(R.drawable.remove);
		remove.setScaleType(ScaleType.FIT_CENTER);
		remove.setAdjustViewBounds(true);
		remove.setLayoutParams(remove_fp);
		remove.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				items_in_order.remove(order.indexOfChild((View) v.getParent()));
				order.removeView((View) v.getParent());
				if (order.getChildCount() == 0)
					order.addView(texthint);
			}
		});

		FrameLayout layout = new FrameLayout(this);
		layout.setLayoutParams(order_lp);
		layout.addView(newitem);
		layout.addView(remove);

		order.removeView(texthint);
		order.addView(layout);
		items_in_order.add(item);

		//scroll order to last item
		((HorizontalScrollView) order.getParent()).post(new Runnable() {

			@Override
			public void run() {
				((HorizontalScrollView) order.getParent()).fullScroll(ScrollView.FOCUS_RIGHT);
			}
		});
	}

	//the functions below are used to make sure fonts and dimensions look good on all screens
	public int dp(int size) {
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		return (size * metrics.densityDpi) / DisplayMetrics.DENSITY_DEFAULT;
	}

	public int fontpercent_screenheight(double d) {
		//get resolution
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);

		int px = (int) ((float) dm.heightPixels * ((float) d / 100));
		float dp = px / dm.density;
		return (int) dp;
	}

	public int fontpercent_screenwidth(double d) {
		//get resolution
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);

		int px = (int) ((float) dm.widthPixels * ((float) d / 100));
		float dp = px / dm.density;
		return (int) dp;
	}
}
