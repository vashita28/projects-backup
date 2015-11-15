package co.uk.pocketapp.bridgestone.activity;

import android.os.Bundle;
import android.view.Window;
import co.uk.pocketapp.bridgestone.R;

public class SettingsActivity extends ParentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_settings);
	}

}
