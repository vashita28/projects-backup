package co.uk.pocketapp.gmd.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import co.uk.pocketapp.gmd.GMDApplication;
import co.uk.pocketapp.gmd.R;
import co.uk.pocketapp.gmd.util.AppValues;
import co.uk.pocketapp.gmd.util.Util;

public class Report_A_Problem_ReviewImage extends ParentActivity implements
		OnClickListener {
	TextView GMD_heading_TextView;
	TextView page_Title_TextView;

	// sangam comment starts
	// ImageView imageview_reviewImage;
	// Button btn_zoom_in, btn_zoom_out;
	// sangam comment ends

	Button btn_back, btn_next;
	String m_CapturedImagePath = "", m_szFileName = "", m_szDateTime = "";

	String m_szMillID = "";
	String mill_ID = "";
	String parent_ID = "";
	String Id = "";
	String m_szThirdGenItemID = "";

	Context mcontext = this;
	Boolean isChildAdded = false;
	String coordinates = "empty";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.report_a_problem_reviewimage);

		// sangam changes start
		FrameLayout main = (FrameLayout) findViewById(R.id.action_settings);

		// main.addView(new Ball(this, 50, 50, 25));

		main.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent e) {
				float x = e.getX();
				float y = e.getY();
				FrameLayout flView = (FrameLayout) v;
				if (isChildAdded) {
					flView.removeViewAt(1);
				} else {
					isChildAdded = true;
				}

				Log.d("Sangam", "X :: " + x + " Y :: " + y);
				coordinates = x + "," + y;
				flView.addView(new Ball(mcontext, x, y, 10));
				return true;
			}
		});
		// sangam changes ends

		// font implementation of main heading "GMD": textview
		GMD_heading_TextView = (TextView) findViewById(R.id.Heading);
		GMD_heading_TextView.setTypeface(GMDApplication.fontHeading);

		// font implementation of page heading: textview
		page_Title_TextView = (TextView) findViewById(R.id.pageTitle);
		page_Title_TextView.setTypeface(GMDApplication.fontText);
		page_Title_TextView.setText("Review Image");

		// sangam comments starts
		// imageview_reviewImage = (ImageView)
		// findViewById(R.id.imageview_reviewimage);
		// sangam comments ends

		// sangam comments starts
		// btn_zoom_in = (Button) findViewById(R.id.btn_zoom_in);
		// btn_zoom_out = (Button) findViewById(R.id.btn_zoom_out);

		// btn_zoom_in.setOnClickListener(this);
		// btn_zoom_out.setOnClickListener(this);
		// sangam comments ends

		btn_back = (Button) findViewById(R.id.btn_back);
		btn_next = (Button) findViewById(R.id.btn_next);
		btn_next.setOnClickListener(this);
		btn_back.setOnClickListener(this);

		// if (getIntent().getExtras() != null) {
		// imageview_reviewImage.setImageURI(Uri.parse(getIntent().getExtras()
		// .getString("capturedimageuri")));
		// m_CapturedImagePath = getIntent().getExtras().getString(
		// "capturedimageuri");
		// m_szFileName = getIntent().getExtras().getString("filename");
		// m_szDateTime = getIntent().getExtras().getString("datetime");
		// }
		if (getIntent().getExtras() != null) {
			mill_ID = getIntent().getStringExtra("millid");
			parent_ID = getIntent().getStringExtra("parent_id");
			Id = getIntent().getStringExtra("id");
			m_szThirdGenItemID = getIntent().getStringExtra("thirdgenitemid");
		}

		if (getIntent().getExtras() != null) {
			m_szMillID = getIntent().getStringExtra("millid");
		}

		// sangam comment starts
		// if (m_szMillID.equals("1"))
		// imageview_reviewImage.setImageResource(R.drawable.mill1);
		// else
		// imageview_reviewImage.setImageResource(R.drawable.mill2);
		// sangam comment ends

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		// sangam comments starts
		// case R.id.btn_zoom_in:
		// CustomImageView_New.mHandler.sendEmptyMessage(50);
		// break;
		// case R.id.btn_zoom_out:
		// CustomImageView_New.mHandler.sendEmptyMessage(100);
		// break;
		// sangam comments ends
		case R.id.btn_back:
			finish();
			break;
		case R.id.btn_next:
			// Defining the solution
			// Intent defineASolutionIntent = new Intent(
			// Report_A_Problem_ReviewImage.this,
			// Define_The_Solution.class);
			// defineASolutionIntent.putExtra("capturedimageuri",
			// m_CapturedImagePath);
			// defineASolutionIntent.putExtra("filename", m_szFileName);
			// defineASolutionIntent.putExtra("datetime",
			// m_szDateTime.toLowerCase());
			// startActivity(defineASolutionIntent);

			if (coordinates.equalsIgnoreCase("empty")) {
				AlertDialog m_AlertDialog = new AlertDialog.Builder(
						Report_A_Problem_ReviewImage.this)
						.setTitle("Alert!")
						.setMessage("Click on the image to report problem")
						.setPositiveButton("Ok",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(
											DialogInterface argDialog,
											int argWhich) {

									}
								}).create();
				m_AlertDialog.setCanceledOnTouchOutside(false);
				m_AlertDialog.show();
			} else {
				Intent myIntent = new Intent(getBaseContext(),
						Report_A_Problem.class);
				myIntent.putExtra("mill_id", mill_ID);
				myIntent.putExtra("parent_id", parent_ID);
				myIntent.putExtra("id", Id);
				myIntent.putExtra("thirdgenitemid", m_szThirdGenItemID);
				myIntent.putExtra("coordinates", coordinates);
				startActivity(myIntent);
				finish();
			}

			break;

		default:
			break;
		}

	}

	public class Ball extends View {

		private final float x;
		private final float y;
		private final int r;
		private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

		public Ball(Context context, float x, float y, int r) {
			super(context);
			mPaint.setColor(0xFFFFFF33);
			this.x = x;
			this.y = y;
			this.r = r;
		}

		@Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			canvas.drawCircle(x, y, r, mPaint);
		}

	}

	public void saveInPrefrences(String key, String value) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

}
