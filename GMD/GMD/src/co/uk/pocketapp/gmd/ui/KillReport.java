package co.uk.pocketapp.gmd.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import co.uk.pocketapp.gmd.R;
import co.uk.pocketapp.gmd.db.DataProvider;
import co.uk.pocketapp.gmd.util.AppValues;
import co.uk.pocketapp.gmd.util.Util;

public class KillReport extends Activity {
	EditText et_CurrentPasscode;
	Button btn_Confirm;
	TextView txt_Error;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.killreport);
		setTitle("Kill Report");

		et_CurrentPasscode = (EditText) findViewById(R.id.edittext_passcode);
		et_CurrentPasscode.requestFocus();
		et_CurrentPasscode.setHintTextColor(getResources().getColor(
				R.color.hint_text_color));
		et_CurrentPasscode.setTextColor(getResources().getColor(
				R.color.textview_text_color));
		btn_Confirm = (Button) findViewById(R.id.button_confirmresetpasscode);
		txt_Error = (TextView) findViewById(R.id.txtError);

		btn_Confirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (et_CurrentPasscode.getText().toString()
						.equals(AppValues.szMasterPassword)) {
					finish();
					ReportKill();
				}

				else {
					txt_Error.setVisibility(View.VISIBLE);
				}
			}
		});

	}

	void ReportKill() {
		Util.setReportKilled(KillReport.this);
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(et_CurrentPasscode.getWindowToken(), 0);

		if (MainActivity.mHandler != null)
			MainActivity.mHandler.sendEmptyMessage(0);
		if (Report_Details.mHandler != null)
			Report_Details.mHandler.sendEmptyMessage(0);

		if (AppValues.getReportXMLFile().exists())
			AppValues.getReportXMLFile().delete();

		if (AppValues.getServiceCheckListtXMLFile().exists())
			AppValues.getServiceCheckListtXMLFile().delete();

		getContentResolver().delete(DataProvider.Mill.CONTENT_URI, null, null);
		getContentResolver().delete(DataProvider.Services.CONTENT_URI, null,
				null);
		getContentResolver().delete(DataProvider.Child_Leaf.CONTENT_URI, null,
				null);
		getContentResolver().delete(DataProvider.Tasks.CONTENT_URI, null, null);
		getContentResolver().delete(DataProvider.Material_Log.CONTENT_URI,
				null, null);
	}
}
