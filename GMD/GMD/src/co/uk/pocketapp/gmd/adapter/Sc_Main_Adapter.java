package co.uk.pocketapp.gmd.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import co.uk.pocketapp.gmd.R;
import co.uk.pocketapp.gmd.db.DataProvider;
import co.uk.pocketapp.gmd.util.AppValues;
import co.uk.pocketapp.gmd.util.Util;

public class Sc_Main_Adapter extends CursorAdapter {
	private LayoutInflater inflater;
	private Context context;
	private Cursor cursor;

	public Sc_Main_Adapter(Context argContext, Cursor argCursor,
			boolean autoRequery) {
		super(argContext, argCursor);
		context = argContext;
		inflater = LayoutInflater.from(context);
		cursor = argCursor;
	}

	@Override
	public void bindView(View argView, Context arg1, Cursor argCursor) {

		TextView textview_child = (TextView) argView
				.findViewById(R.id.textview_report_title);
		TextView textview_completed_tasks = (TextView) argView
				.findViewById(R.id.textview_completed_percentage);
		ImageView imageview_color = (ImageView) argView
				.findViewById(R.id.imageview_color_id);
		TextView textview_child_row_id = (TextView) argView
				.findViewById(R.id.textview_child_row_id);

		String szMainChildID = cursor.getString(cursor
				.getColumnIndex(DataProvider.Main.ID));
		Cursor cursorSecondGen = arg1.getContentResolver().query(
				DataProvider.Second_Generation.CONTENT_URI,
				null,
				DataProvider.Second_Generation.MAIN_CHILD_ID + " ='"
						+ szMainChildID + "'", null, null);
		cursorSecondGen.moveToFirst();

		int nThirdGenChildCount = 0;
		do {

			String szChildSecondID = cursorSecondGen.getString(cursorSecondGen
					.getColumnIndex(DataProvider.Second_Generation.ID));
			Cursor cursorThirdGen = arg1.getContentResolver().query(
					DataProvider.Third_Generation.CONTENT_URI,
					null,
					DataProvider.Third_Generation.CHILD_SECOND_GEN_ID + " ='"
							+ szChildSecondID + "'", null, null);

			nThirdGenChildCount = nThirdGenChildCount
					+ cursorThirdGen.getCount();
			cursorThirdGen.close();

		} while (cursorSecondGen.moveToNext());
		cursorSecondGen.close();

		String szTaskCount = "0";
		Cursor cursorTaskCount = arg1.getContentResolver().query(
				DataProvider.Summary_Values.CONTENT_URI,
				null,
				DataProvider.Summary_Values.CHILD_MAIN_ID + " ='"
						+ szMainChildID + "' AND "
						+ DataProvider.Summary_Values.REPORT_ID + " ='"
						+ Util.getReportID(arg1) + "'", null, null);

		if (cursorTaskCount.moveToFirst())
			szTaskCount = String.valueOf(cursorTaskCount.getCount());

		cursorTaskCount.close();

		textview_completed_tasks.setText(String.valueOf(szTaskCount + "/"
				+ nThirdGenChildCount * 7));// AppValues.nleaf_child_in_each_thirdgen

		textview_child.setText(cursor.getString(cursor
				.getColumnIndex(DataProvider.Main.CHILD)));

		textview_child_row_id.setText(cursor.getString(cursor
				.getColumnIndex(DataProvider.Main.ID)));

		if (Integer.parseInt(szTaskCount) == 0) {
			imageview_color.setBackgroundDrawable(arg1.getResources()
					.getDrawable(R.drawable.purple));
		} else if (Integer.parseInt(szTaskCount) == nThirdGenChildCount * 7)// AppValues.nleaf_child_in_each_thirdgen
			imageview_color.setBackgroundDrawable(arg1.getResources()
					.getDrawable(R.drawable.green));
		else
			imageview_color.setBackgroundDrawable(arg1.getResources()
					.getDrawable(R.drawable.yellow));

	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		final View view = inflater.inflate(
				R.layout.service_checklist_simplerow, parent, false);
		return view;
	}

}