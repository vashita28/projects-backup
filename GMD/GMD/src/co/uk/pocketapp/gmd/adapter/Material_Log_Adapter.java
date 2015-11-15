package co.uk.pocketapp.gmd.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import co.uk.pocketapp.gmd.R;
import co.uk.pocketapp.gmd.db.DataProvider;

public class Material_Log_Adapter extends CursorAdapter {

	private LayoutInflater inflater;
	private Context context;
	private Cursor cursor;

	public Material_Log_Adapter(Context argContext, Cursor argCursor,
			boolean autoRequery) {
		super(argContext, argCursor);
		context = argContext;
		inflater = LayoutInflater.from(context);
		cursor = argCursor;
	}

	@Override
	public void bindView(View argView, Context arg1, Cursor argCursor) {

		TextView material_Category = (TextView) argView
				.findViewById(R.id.material_category_TextView);
		TextView material_Name = (TextView) argView
				.findViewById(R.id.Material_Name_TextView);
		TextView dateNtime = (TextView) argView.findViewById(R.id.date_time);
		TextView materialID = (TextView) argView
				.findViewById(R.id.textview_material_rowID);

		materialID.setText(argCursor.getString(argCursor
				.getColumnIndex(DataProvider.Material_Log.ID)));
		material_Category.setText(argCursor.getString(argCursor
				.getColumnIndex(DataProvider.Material_Log.MATERIAL_CATEGORY)));
		material_Name.setText(argCursor.getString(argCursor
				.getColumnIndex(DataProvider.Material_Log.MATERIAL_NAME)));
		dateNtime
				.setText(argCursor.getString(argCursor
						.getColumnIndex(DataProvider.Material_Log.MATERIAL_REPORT_DATE)));

	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		final View view = inflater.inflate(R.layout.site_conditions_simplerow,
				parent, false);
		return view;

	}

}
