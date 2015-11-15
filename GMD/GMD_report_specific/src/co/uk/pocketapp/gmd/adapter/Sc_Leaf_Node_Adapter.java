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

public class Sc_Leaf_Node_Adapter extends CursorAdapter {

	private LayoutInflater inflater;
	private Context context;
	private Cursor cursor;

	String m_szLeafItemID = "", m_szThirdGenItemID = "",
			m_szSecondGenItemID = "", m_szMainItemID = "";

	public Sc_Leaf_Node_Adapter(Context argContext, Cursor argCursor,
			boolean autoRequery, String szMainItemID, String szSecondGenItemID,
			String szThirdGenItemID) {
		super(argContext, argCursor);
		context = argContext;
		inflater = LayoutInflater.from(context);
		cursor = argCursor;

		m_szThirdGenItemID = szThirdGenItemID;
		m_szSecondGenItemID = szSecondGenItemID;
		m_szMainItemID = szMainItemID;
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

		textview_completed_tasks.setText("");
		textview_child.setText(cursor.getString(cursor
				.getColumnIndex(DataProvider.Leaf_Node.CHILD_LEAF_NODE)));

		m_szLeafItemID = cursor.getString(cursor
				.getColumnIndex(DataProvider.Leaf_Node.ID));
		textview_child_row_id.setText(m_szLeafItemID);

		Cursor cursorTask = arg1.getContentResolver().query(
				DataProvider.Summary_Values.CONTENT_URI,
				null,
				DataProvider.Summary_Values.CHILD_LEAF_NODE_ID + " ='"
						+ m_szLeafItemID + "' AND "
						+ DataProvider.Summary_Values.CHILD_SECOND_GEN_ID
						+ " ='" + m_szSecondGenItemID + "' AND "
						+ DataProvider.Summary_Values.CHILD_THIRD_GEN_ID
						+ " ='" + m_szThirdGenItemID + "' AND "
						+ DataProvider.Summary_Values.CHILD_MAIN_ID + " ='"
						+ m_szMainItemID + "' AND "
						+ DataProvider.Summary_Values.REPORT_ID + " ='"
						+ Util.getReportID(arg1) + "'", null, null);
		cursorTask.moveToFirst();

		if (cursorTask.getCount() > 0) {
			imageview_color.setBackgroundDrawable(arg1.getResources().getDrawable(
					R.drawable.green));
		} else
			imageview_color.setBackgroundDrawable(arg1.getResources().getDrawable(
					R.drawable.purple));
		cursorTask.close();
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		final View view = inflater.inflate(
				R.layout.service_checklist_simplerow, parent, false);
		return view;
	}

}
