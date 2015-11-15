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
import co.uk.pocketapp.gmd.util.Util;

public class SC_Mill_Adapter extends CursorAdapter {
	private LayoutInflater inflater;
	private Context context;
	private Cursor cursor;

	public SC_Mill_Adapter(Context argContext, Cursor argCursor,
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
				.getColumnIndex(DataProvider.Mill.MILL_NAME));

		textview_child.setText(szMainChildID);

		String szMillID = cursor.getString(cursor
				.getColumnIndex(DataProvider.Mill.MILL_ID));
		textview_child_row_id.setText(szMillID);

		int nTaskCompletedCount = 0;
		int nTaskTotalCount = 0;

		Cursor taskCursor = context.getContentResolver()
				.query(DataProvider.Tasks.CONTENT_URI,
						null,
						DataProvider.Tasks.MILL_ID + " = ?" + " AND "
								+ DataProvider.Tasks.REPORT_ID + " = ?"
								+ " AND " + DataProvider.Tasks.TASK_COMPLETED
								+ " = ? AND " + DataProvider.Tasks.TASK_NAME
								+ " != ?",
						new String[] { szMillID, Util.getReportID(context),
								"true", "" }, null);

		if (taskCursor != null && taskCursor.moveToFirst())
			nTaskCompletedCount = taskCursor.getCount();

		taskCursor.close();

		Cursor taskCursorNew = context.getContentResolver().query(
				DataProvider.Tasks.CONTENT_URI,
				null,
				DataProvider.Tasks.MILL_ID + " = ?" + " AND "
						+ DataProvider.Tasks.REPORT_ID + " = ?" + " AND "
						+ DataProvider.Tasks.TASK_COMPLETED + " != ?AND "
						+ DataProvider.Tasks.TASK_NAME + " != ?",
				new String[] { szMillID, Util.getReportID(context), "", "" },
				null);
		if (taskCursorNew != null && taskCursorNew.moveToFirst())
			nTaskTotalCount = taskCursorNew.getCount();

		taskCursorNew.close();

		textview_completed_tasks.setText(nTaskCompletedCount + "/"
				+ nTaskTotalCount);

		if (nTaskCompletedCount == 0) {
			imageview_color.setBackgroundDrawable(arg1.getResources()
					.getDrawable(R.drawable.purple));
		} else if (nTaskCompletedCount == nTaskTotalCount)
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