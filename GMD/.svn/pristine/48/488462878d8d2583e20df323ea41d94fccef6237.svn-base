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

public class SC_Services_Leaf_Adapter extends CursorAdapter {

	private LayoutInflater inflater;
	private Context context;
	private Cursor cursor;

	String szParentID = "", szMillID = "", szItemID = "",
			szSpecialTypeName = "";

	public SC_Services_Leaf_Adapter(Context argContext, Cursor argCursor,
			boolean autoRequery, String itemID, String millID,
			String SpecialTypeName) {
		super(argContext, argCursor);
		context = argContext;
		inflater = LayoutInflater.from(context);
		cursor = argCursor;
		// szParentID = parentID;
		szMillID = millID;
		szItemID = itemID;
		szSpecialTypeName = SpecialTypeName;
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

		imageview_color.setVisibility(View.VISIBLE);
		textview_completed_tasks.setVisibility(View.INVISIBLE);

		textview_child.setText(cursor.getString(cursor
				.getColumnIndex(DataProvider.Child_Leaf.CHILD_LEAF)));

		if (cursor.getString(
				cursor.getColumnIndex(DataProvider.Child_Leaf.CHILD_LEAF))
				.equals("Special Type")) {
			textview_child.setText(szSpecialTypeName);

			Cursor taskCursor = context.getContentResolver().query(
					DataProvider.Tasks.CONTENT_URI,
					null,
					DataProvider.Tasks.MILL_ID + " = ?" + " AND "
							+ DataProvider.Tasks.REPORT_ID + " = ?" + " AND "
							+ DataProvider.Tasks.SERVICES_ITEM_ID + " = ?"
							+ " AND " + DataProvider.Tasks.TASK_NAME + " = ?",
					new String[] { szMillID, Util.getReportID(context),
							szItemID, szSpecialTypeName }, null);

			if (taskCursor != null && taskCursor.moveToFirst()) {
				String szTaskCompleted = taskCursor.getString(taskCursor
						.getColumnIndex(DataProvider.Tasks.TASK_COMPLETED));
				if (szTaskCompleted.equals("true")) {
					imageview_color.setBackgroundDrawable(arg1.getResources()
							.getDrawable(R.drawable.green));
				} else {
					imageview_color.setBackgroundDrawable(arg1.getResources()
							.getDrawable(R.drawable.purple));
				}
			}
			taskCursor.close();
		}

		textview_child_row_id.setText(szItemID);

		String szChild = cursor.getString(cursor
				.getColumnIndex(DataProvider.Child_Leaf.CHILD_LEAF));
		if (szChild.equals("Report a problem")) {
			imageview_color.setVisibility(View.INVISIBLE);
		} else if (szChild.equals("Help-Support")) {
			imageview_color.setVisibility(View.VISIBLE);
		}

		Cursor taskCursor = context.getContentResolver().query(
				DataProvider.Tasks.CONTENT_URI,
				null,
				DataProvider.Tasks.MILL_ID + " = ?" + " AND "
						+ DataProvider.Tasks.REPORT_ID + " = ?" + " AND "
						+ DataProvider.Tasks.SERVICES_ITEM_ID + " = ?"
						+ " AND " + DataProvider.Tasks.TASK_NAME + " = ?",
				new String[] { szMillID, Util.getReportID(context), szItemID,
						szChild }, null);

		if (taskCursor != null && taskCursor.moveToFirst()) {
			String szTaskCompleted = taskCursor.getString(taskCursor
					.getColumnIndex(DataProvider.Tasks.TASK_COMPLETED));
			if (szTaskCompleted.equals("true")) {
				imageview_color.setBackgroundDrawable(arg1.getResources()
						.getDrawable(R.drawable.green));
			} else {
				imageview_color.setBackgroundDrawable(arg1.getResources()
						.getDrawable(R.drawable.purple));
			}
		}
		taskCursor.close();

	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		final View view = inflater.inflate(
				R.layout.service_checklist_simplerow, parent, false);
		return view;
	}
}
