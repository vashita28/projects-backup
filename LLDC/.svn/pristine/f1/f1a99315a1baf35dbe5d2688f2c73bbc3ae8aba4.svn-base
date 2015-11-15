package co.uk.android.lldc.tablet;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import co.uk.android.lldc.R;
import co.uk.android.lldc.adapters.AutocompleteCustomArrayAdapter;
import co.uk.android.lldc.broadcastreceiver.CustomAutoCompleteTextChangedListener;
import co.uk.android.lldc.models.ServerModel;

public class SearchActivity extends Activity {
	private static final String TAG = SearchActivity.class.getSimpleName();

	FragmentManager mFragmentManager;

	RelativeLayout rlCancel, rlRelax, rlEntertain, rlActive, rlLeftSide;
	ImageView ivMenuAtm, ivMenuToilet, ivMenuEating, ivMenuDisabled,
			ivMenuViewPoint, ivMenuInformation;

	ImageView ivRelax, ivEntertain, ivActive;
	TextView tvRelax, tvEntertain, tvActive;

	public AutoCompleteTextView etSearch;

	// adapter for auto-complete
	public ArrayAdapter<ServerModel> myAdapter;

	public static int MENU_ATM = 1;
	public static int MENU_TOILET = 2;
	public static int MENU_EATING = 3;
	public static int MENU_DISABLED = 4;
	public static int MENU_VIEWPOINT = 5;
	public static int MENU_INFORMATION = 6;

	public static boolean bAtmSelected = false, bToiletSelected = false,
			bEatingSelected = false, bDisabledSelected = false,
			bViewPointSelected = false, bInformationSelected = false,
			bRelaxSelected = false, bEntertainSelected = false,
			bActiveSelected = false;

	Bundle bundle = new Bundle();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		Log.e(TAG, "Device is Tablet...");
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		setContentView(R.layout.activity_search_tablet);

		rlCancel = (RelativeLayout) findViewById(R.id.rlCancel);
		rlLeftSide = (RelativeLayout) findViewById(R.id.screen);
		rlRelax = (RelativeLayout) findViewById(R.id.rlRelax);
		rlEntertain = (RelativeLayout) findViewById(R.id.rlEntertain);
		rlActive = (RelativeLayout) findViewById(R.id.rlActive);
		ivMenuAtm = (ImageView) findViewById(R.id.ivMenuAtm);
		ivMenuToilet = (ImageView) findViewById(R.id.ivMenuToilet);
		ivMenuEating = (ImageView) findViewById(R.id.ivMenuEating);
		ivMenuDisabled = (ImageView) findViewById(R.id.ivMenuDisabled);
		ivMenuViewPoint = (ImageView) findViewById(R.id.ivMenuViewPoint);
		ivMenuInformation = (ImageView) findViewById(R.id.ivMenuInformation);
		ivRelax = (ImageView) findViewById(R.id.ivRelax);
		ivEntertain = (ImageView) findViewById(R.id.ivEntertain);
		ivActive = (ImageView) findViewById(R.id.ivActive);
		tvRelax = (TextView) findViewById(R.id.tvRelax);
		tvEntertain = (TextView) findViewById(R.id.tvEntertain);
		tvActive = (TextView) findViewById(R.id.tvActive);

		etSearch = (AutoCompleteTextView) findViewById(R.id.autoCompleteSearch);

		etSearch.setTypeface(Typeface.DEFAULT_BOLD);

		Thread thread = new Thread() {
			@Override
			public void run() {
				try {
					Thread.sleep(600);
				} catch (InterruptedException e) {
				}
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						rlLeftSide.setVisibility(View.VISIBLE);
					}
				});
			}
		};
		thread.start();

		rlCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				overridePendingTransition(R.anim.right_out, R.anim.left_in);
				rlLeftSide.setVisibility(View.INVISIBLE);
				LLDCApplication.removeSimpleProgressDialog();
			}
		});
		rlRelax.setOnClickListener(new onClickListener());
		rlEntertain.setOnClickListener(new onClickListener());
		rlActive.setOnClickListener(new onClickListener());
		ivMenuAtm.setOnClickListener(new onClickListener());
		ivMenuToilet.setOnClickListener(new onClickListener());
		ivMenuEating.setOnClickListener(new onClickListener());
		ivMenuDisabled.setOnClickListener(new onClickListener());
		ivMenuViewPoint.setOnClickListener(new onClickListener());
		ivMenuInformation.setOnClickListener(new onClickListener());
		ArrayList<ServerModel> searchResult = new ArrayList<ServerModel>();

		etSearch.addTextChangedListener(new CustomAutoCompleteTextChangedListener(
				this));

		myAdapter = new AutocompleteCustomArrayAdapter(this,
				R.layout.autocomplete_row, searchResult, "");
		etSearch.setAdapter(myAdapter);

		etSearch.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				RelativeLayout rl = (RelativeLayout) arg1;
				TextView tv = (TextView) rl.getChildAt(0);
				String tag = tv.getTag().toString();
				String[] tagarr = tag.split(",");
				// data.get(position).get_id() + "," +
				// data.get(position).getModelType()
				if (tagarr[0].equals("-1")) {
					etSearch.setText("");
					return;
				}
				Bundle bundle = new Bundle();
				bundle.putStringArray("tagarr", tagarr);
				etSearch.setText("");
				Intent intent = new Intent(SearchActivity.this,
						SearchMapActivity.class);
				intent.putExtras(bundle);
				startActivity(intent);
				SearchActivity.this.finish();

			}
		});

		etSearch.setOnEditorActionListener(new OnEditorActionListener() {
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER))
						|| (actionId == EditorInfo.IME_ACTION_DONE)) {
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);
				}
				return false;
			}
		});

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	class onClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent msgIntent = new Intent();
			switch (v.getId()) {
			case R.id.rlCancel:
				finish();
				overridePendingTransition(R.anim.right_out, R.anim.left_in);
				rlLeftSide.setVisibility(View.INVISIBLE);
				LLDCApplication.removeSimpleProgressDialog();
				break;

			case R.id.rlRelax:

				LLDCApplication.showSimpleProgressDialog(SearchActivity.this);
				if (!bRelaxSelected)
					bRelaxSelected = ChangeBg(ivRelax, false,
							R.drawable.relax_sel, R.drawable.relax_unsel);
				else
					bRelaxSelected = ChangeBg(ivRelax, true,
							R.drawable.relax_sel, R.drawable.relax_unsel);

				msgIntent = new Intent(SearchActivity.this,
						SearchMapActivity.class);
				msgIntent.putExtra("PAGETITLE", "Search");
				msgIntent.putExtra("PAGESUBTITLE", "SHOWME");
				msgIntent.putExtra("SELID", "1");

				break;

			case R.id.rlEntertain:
				LLDCApplication.showSimpleProgressDialog(SearchActivity.this);
				if (!bEntertainSelected)
					bEntertainSelected = ChangeBg(ivEntertain, false,
							R.drawable.entertain_sel,
							R.drawable.entertain_unsel);
				else
					bEntertainSelected = ChangeBg(ivEntertain, true,
							R.drawable.entertain_sel,
							R.drawable.entertain_unsel);
				msgIntent = new Intent(SearchActivity.this,
						SearchMapActivity.class);
				msgIntent.putExtra("PAGETITLE", "Search");
				msgIntent.putExtra("PAGESUBTITLE", "SHOWME");
				msgIntent.putExtra("SELID", "2");

				break;

			case R.id.rlActive:
				LLDCApplication.showSimpleProgressDialog(SearchActivity.this);
				if (!bActiveSelected)
					bActiveSelected = ChangeBg(ivActive, false,
							R.drawable.active_sel, R.drawable.active_unsel);
				else
					bActiveSelected = ChangeBg(ivActive, true,
							R.drawable.active_sel, R.drawable.active_unsel);

				msgIntent = new Intent(SearchActivity.this,
						SearchMapActivity.class);
				msgIntent.putExtra("PAGETITLE", "Search");
				msgIntent.putExtra("PAGESUBTITLE", "SHOWME");
				msgIntent.putExtra("SELID", "4");

				break;

			case R.id.ivMenuAtm:
				LLDCApplication.showSimpleProgressDialog(SearchActivity.this);
				if (!bAtmSelected)
					bAtmSelected = ChangeBg(ivMenuAtm, false,
							R.drawable.menu_atm_sel, R.drawable.menu_atm);
				else
					bAtmSelected = ChangeBg(ivMenuAtm, true,
							R.drawable.menu_atm_sel, R.drawable.menu_atm);

				msgIntent = new Intent(SearchActivity.this,
						SearchMapActivity.class);
				msgIntent.putExtra("PAGETITLE", "Search");
				msgIntent.putExtra("PAGESUBTITLE", "IMLookingFor");
				msgIntent.putExtra("SELID", "2");

				break;

			case R.id.ivMenuToilet:
				LLDCApplication.showSimpleProgressDialog(SearchActivity.this);
				if (!bToiletSelected)
					bToiletSelected = ChangeBg(ivMenuToilet, false,
							R.drawable.menu_toilet_sel, R.drawable.menu_toilet);
				else
					bToiletSelected = ChangeBg(ivMenuToilet, true,
							R.drawable.menu_toilet_sel, R.drawable.menu_toilet);

				msgIntent = new Intent(SearchActivity.this,
						SearchMapActivity.class);
				msgIntent.putExtra("PAGETITLE", "Search");
				msgIntent.putExtra("PAGESUBTITLE", "IMLookingFor");
				msgIntent.putExtra("SELID", "1");

				break;

			case R.id.ivMenuEating:
				LLDCApplication.showSimpleProgressDialog(SearchActivity.this);
				if (!bEatingSelected)
					bEatingSelected = ChangeBg(ivMenuEating, false,
							R.drawable.menu_food_sel, R.drawable.menu_food);
				else
					bEatingSelected = ChangeBg(ivMenuEating, true,
							R.drawable.menu_food_sel, R.drawable.menu_food);

				msgIntent = new Intent(SearchActivity.this,
						SearchMapActivity.class);
				msgIntent.putExtra("PAGETITLE", "Search");
				msgIntent.putExtra("PAGESUBTITLE", "IMLookingFor");
				msgIntent.putExtra("SELID", "6");

				break;

			case R.id.ivMenuDisabled:
				LLDCApplication.showSimpleProgressDialog(SearchActivity.this);
				if (!bDisabledSelected)
					bDisabledSelected = ChangeBg(ivMenuDisabled, false,
							R.drawable.menu_dis_parking_sel,
							R.drawable.menu_dis_parking);
				else
					bDisabledSelected = ChangeBg(ivMenuDisabled, true,
							R.drawable.menu_dis_parking_sel,
							R.drawable.menu_dis_parking);

				msgIntent = new Intent(SearchActivity.this,
						SearchMapActivity.class);
				msgIntent.putExtra("PAGETITLE", "Search");
				msgIntent.putExtra("PAGESUBTITLE", "IMLookingFor");
				msgIntent.putExtra("SELID", "3");
				break;

			case R.id.ivMenuViewPoint:
				LLDCApplication.showSimpleProgressDialog(SearchActivity.this);
				if (!bViewPointSelected)
					bViewPointSelected = ChangeBg(ivMenuViewPoint, false,
							R.drawable.menu_parking_sel,
							R.drawable.menu_parking);
				else
					bViewPointSelected = ChangeBg(ivMenuViewPoint, true,
							R.drawable.menu_parking_sel,
							R.drawable.menu_parking);

				msgIntent = new Intent(SearchActivity.this,
						SearchMapActivity.class);
				msgIntent.putExtra("PAGETITLE", "Search");
				msgIntent.putExtra("PAGESUBTITLE", "IMLookingFor");
				msgIntent.putExtra("SELID", "5");

				break;

			case R.id.ivMenuInformation:
				LLDCApplication.showSimpleProgressDialog(SearchActivity.this);
				if (!bInformationSelected)
					bInformationSelected = ChangeBg(ivMenuInformation, false,
							R.drawable.menu_play_area_sel,
							R.drawable.menu_play_area);
				else
					bInformationSelected = ChangeBg(ivMenuInformation, true,
							R.drawable.menu_play_area_sel,
							R.drawable.menu_play_area);

				msgIntent = new Intent(SearchActivity.this,
						SearchMapActivity.class);
				msgIntent.putExtra("PAGETITLE", "Search");
				msgIntent.putExtra("PAGESUBTITLE", "IMLookingFor");
				msgIntent.putExtra("SELID", "4");

				break;

			}
			if (msgIntent != null) {
				startActivity(msgIntent);
				SearchActivity.this.finish();
				SearchActivity.this.overridePendingTransition(R.anim.right_out,
						R.anim.left_in);
				LLDCApplication.removeSimpleProgressDialog();
			}
		}
	}

	boolean ChangeBg(ImageView iv, boolean bIsSelected, int selectedDrawable,
			int unselectedDrawable) {

		if (!bIsSelected) {
			iv.setImageResource(selectedDrawable);
			return true;
		} else {
			iv.setImageResource(unselectedDrawable);
			return false;
		}

	}

	public ArrayList<String> getFilterArray() {
		// TODO Auto-generated method stub

		// VENUE_FAC_ID_TOILET = 1, VENUE_FAC_ID_ATM = 2,
		// VENUE_FAC_ID_DIS_PARKING = 3, VENUE_FAC_ID_FAMILY_PLAY_AREA = 4,
		// VENUE_FAC_ID_PARKING = 5, VENUE_FAC_ID_FOOD_DRINK = 6

		ArrayList<String> filterarr = new ArrayList<String>();

		filterarr.clear();

		if (bAtmSelected) {
			filterarr.add("2");
		}
		if (bToiletSelected) {
			filterarr.add("1");
		}
		if (bEatingSelected) {
			filterarr.add("6");
		}
		if (bDisabledSelected) {
			filterarr.add("3");
		}
		if (bViewPointSelected) {
			filterarr.add("5");
		}
		if (bInformationSelected) {
			filterarr.add("4");
		}

		// if (bActiveSelected) {
		// filterarr.add("7");
		// }
		// if (bEntertainSelected) {
		// filterarr.add("8");
		// }
		// if (bRelaxSelected) {
		// filterarr.add("9");
		// }

		return filterarr;

	}

	public ArrayList<String> getShowMeFilterArray() {
		// TODO Auto-generated method stub

		// VENUE_FAC_ID_TOILET = 1, VENUE_FAC_ID_ATM = 2,
		// VENUE_FAC_ID_DIS_PARKING = 3, VENUE_FAC_ID_FAMILY_PLAY_AREA = 4,
		// VENUE_FAC_ID_PARKING = 5, VENUE_FAC_ID_FOOD_DRINK = 6

		ArrayList<String> filterarr = new ArrayList<String>();

		if (bActiveSelected) {
			// filterarr.add("3");
			filterarr.add("'4','5','6','7'");
		}
		if (bEntertainSelected) {
			// filterarr.add("2");
			filterarr.add("'2','3','6','7'");
		}
		if (bRelaxSelected) {
			// filterarr.add("1");
			filterarr.add("'1','3','5','7'");
		}

		return filterarr;

	}

}
