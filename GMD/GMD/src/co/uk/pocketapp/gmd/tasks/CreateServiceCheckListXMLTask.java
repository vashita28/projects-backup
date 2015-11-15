package co.uk.pocketapp.gmd.tasks;

import java.io.FileOutputStream;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;
import co.uk.pocketapp.gmd.db.DataProvider;
import co.uk.pocketapp.gmd.util.AppValues;
import co.uk.pocketapp.gmd.util.Util;

public class CreateServiceCheckListXMLTask extends AsyncTask<Void, Void, Void> {

	public Context mContext;
	StringBuffer bufItems, bufMill;

	@Override
	protected Void doInBackground(Void... params) {
		// TODO Auto-generated method stub
		bufItems = new StringBuffer();
		bufMill = new StringBuffer();

		Cursor millCursor = mContext.getContentResolver().query(
				DataProvider.Mill.CONTENT_URI,
				null,
				DataProvider.Mill.REPORT_ID + " ='"
						+ Util.getReportID(mContext) + "'", null, null);
		millCursor.moveToFirst();

		do {
			String szMillID = millCursor.getString(millCursor
					.getColumnIndex(DataProvider.Mill.MILL_ID));
			String szMillName = millCursor.getString(millCursor
					.getColumnIndex(DataProvider.Mill.MILL_NAME));
			String szGMDType = millCursor.getString(millCursor
					.getColumnIndex(DataProvider.Mill.GMD_TYPE));

			String szWorkScope = "";
			String szWorkCarriedOut = "";
			String szConditions = "";
			String szRecommendations = "";
			String szComments = "";
			String szPhotograph = "";

			String szSpecialTypeCompleted = "";
			String szWorkScopeCompleted = "";
			String szWorkCarriedOutCompleted = "";
			String szConditionsCompleted = "";
			String szRecommendationsCompleted = "";
			String szCommentsCompleted = "";
			String szPhotographCompleted = "";
			String szHelpSupportCompleted = "";

			Cursor tasksCursor = mContext.getContentResolver().query(
					DataProvider.Tasks.CONTENT_URI,
					null,
					DataProvider.Tasks.MILL_ID + " ='" + szMillID + "' AND "
							+ DataProvider.Services.REPORT_ID + " ='"
							+ Util.getReportID(mContext) + "'", null, null);
			tasksCursor.moveToFirst();

			do {
				String szTaskName = tasksCursor.getString(tasksCursor
						.getColumnIndex(DataProvider.Tasks.TASK_NAME));
				String szTaskContent = tasksCursor.getString(tasksCursor
						.getColumnIndex(DataProvider.Tasks.TASK_CONTENT));
				String szServicesItemID = tasksCursor.getString(tasksCursor
						.getColumnIndex(DataProvider.Tasks.SERVICES_ITEM_ID));
				String szParentID = tasksCursor.getString(tasksCursor
						.getColumnIndex(DataProvider.Tasks.SERVICES_PARENT_ID));

				if (szTaskContent.equals("Special Type")) {
					szSpecialTypeCompleted = tasksCursor.getString(tasksCursor
							.getColumnIndex(DataProvider.Tasks.TASK_COMPLETED));
				}

				if (szTaskName.equals("Work scope")) {
					szWorkScope = tasksCursor.getString(tasksCursor
							.getColumnIndex(DataProvider.Tasks.TASK_CONTENT));
					szWorkScopeCompleted = tasksCursor.getString(tasksCursor
							.getColumnIndex(DataProvider.Tasks.TASK_COMPLETED));
				} else if (szTaskName.equals("Work carried out")) {
					szWorkCarriedOut = tasksCursor.getString(tasksCursor
							.getColumnIndex(DataProvider.Tasks.TASK_CONTENT));
					szWorkCarriedOutCompleted = tasksCursor
							.getString(tasksCursor
									.getColumnIndex(DataProvider.Tasks.TASK_COMPLETED));
				} else if (szTaskName.equals("Conditions")) {
					szConditions = tasksCursor.getString(tasksCursor
							.getColumnIndex(DataProvider.Tasks.TASK_CONTENT));
					szConditionsCompleted = tasksCursor.getString(tasksCursor
							.getColumnIndex(DataProvider.Tasks.TASK_COMPLETED));
				} else if (szTaskName.equals("Recommendations")) {
					szRecommendations = tasksCursor.getString(tasksCursor
							.getColumnIndex(DataProvider.Tasks.TASK_CONTENT));
					szRecommendationsCompleted = tasksCursor
							.getString(tasksCursor
									.getColumnIndex(DataProvider.Tasks.TASK_COMPLETED));
				} else if (szTaskName.equals("Comments")) {
					szComments = tasksCursor.getString(tasksCursor
							.getColumnIndex(DataProvider.Tasks.TASK_CONTENT));
					szCommentsCompleted = tasksCursor.getString(tasksCursor
							.getColumnIndex(DataProvider.Tasks.TASK_COMPLETED));
				} else if (szTaskName.equals("Photograph")) {
					szPhotograph = tasksCursor.getString(tasksCursor
							.getColumnIndex(DataProvider.Tasks.TASK_CONTENT));
					szPhotographCompleted = tasksCursor.getString(tasksCursor
							.getColumnIndex(DataProvider.Tasks.TASK_COMPLETED));
				} else if (szTaskName.equals("Help-Support")) {
					szHelpSupportCompleted = tasksCursor.getString(tasksCursor
							.getColumnIndex(DataProvider.Tasks.TASK_COMPLETED));
				} else if (szTaskName.equals("Report a problem")) {

					String szName = "";
					Cursor cursorServices = mContext.getContentResolver()
							.query(DataProvider.Services.CONTENT_URI,
									null,
									DataProvider.Services.ITEM_ID + " ='"
											+ szServicesItemID + "' AND "
											+ DataProvider.Services.MILL_ID
											+ " ='" + szMillID + "' AND "
											+ DataProvider.Services.REPORT_ID
											+ " ='"
											+ Util.getReportID(mContext) + "'",
									null, null);
					cursorServices.moveToFirst();
					szName = EncodeXML(cursorServices.getString(cursorServices
							.getColumnIndex(DataProvider.Services.ITEM_NAME)));
					bufItems.append(getItemsXMLStructure(szServicesItemID,
							szParentID, szName, szWorkScopeCompleted,
							EncodeXML(szWorkScope), szSpecialTypeCompleted,
							szWorkCarriedOutCompleted,
							EncodeXML(szWorkCarriedOut), szConditionsCompleted,
							EncodeXML(szConditions),
							szRecommendationsCompleted,
							EncodeXML(szRecommendations), szCommentsCompleted,
							EncodeXML(szComments), szPhotographCompleted,
							szPhotograph, szHelpSupportCompleted));
					cursorServices.close();
				}
			} while (tasksCursor.moveToNext());
			tasksCursor.close();

			bufMill.append(getMillXMLStructure(szMillID, szMillName, szGMDType,
					getServicesXMLStructure(bufItems.toString())));
		} while (millCursor.moveToNext());
		millCursor.close();
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);

		// Log.d("ServiceCheckListXML",
		// "ServiceCheckListXML ::  MILL XML :: "+bufMill);

		if (!Util.getServiceCheckListCreated(mContext)) {

			// Log.d("CreateServiceCheckListXMLTask",
			// "CreateServiceCheckListXMLTask :: Total XML :: "
			// + getServiceCheckListStructure(bufMill.toString()));

			try {
				Util.DecryptServiceCheckListXML();

				FileOutputStream fos = new FileOutputStream(
						AppValues.getServiceCheckListtXMLFile());
				fos.write(getServiceCheckListStructure(bufMill.toString())
						.getBytes());
				fos.close();

				Util.EncryptServiceCheckListXML();

				// temp encrypt file
				// EncodeDecodeAES encrypt = new EncodeDecodeAES();
				// encrypt.encrypt(AppValues.getServiceCheckListtXMLFile());
				// encrypt.decrypt(AppValues.getServiceCheckListtXMLFileTemp());

				Util.setServiceCheckListCreated(mContext, true);

			} catch (Exception e) {
				e.printStackTrace();
			}

			// new XML_Creation().add_Service_Checklist();

			// to delete the xml after completion
			// if(file.exists())
			// file.delete();

		}

		// else {
		//
		// new XML_Creation().add_Service_Checklist();
		// }
	}

	String getItemsXMLStructure(String szID, String szParentID, String szName,
			String szWorkScopeCompleted, String szWorkScope,
			String szSpecialTypeTaskCompleted,
			String szWorkCarriedOutCompleted, String szWorkCarriedOut,
			String szConditionsCompleted, String szConditions,
			String szRecommendationsCompleted, String szRecommendations,
			String szCommentsCompleted, String szComments,
			String szPhotographCompleted, String szPhotograph,
			String szHelpSupportCompleted) {

		String szItemsXML = "<item>\r\n"
				+ "						<id>%s</id>\r\n"
				+ "						<parent_id>%s</parent_id>\r\n"
				+ "						<name>%s</name>\r\n"
				+ "						<tasks>\r\n"
				+ "							<work_scope task_completed = \"%s\">%s</work_scope>\r\n"
				+ "							<special_type task_completed = \"%s\"></special_type>\r\n"
				+ "							<work_carried_out task_completed = \"%s\">%s</work_carried_out>\r\n"
				+ "							<conditions task_completed = \"%s\">%s</conditions>\r\n"
				+ "							<recommendations task_completed = \"%s\">%s</recommendations>\r\n"
				+ "							<comments task_completed = \"%s\">%s</comments>\r\n"
				+ "							<photograph task_completed = \"%s\">%s</photograph>\r\n"
				+ "							<helpsupport task_completed = \"%s\"></helpsupport>\r\n"
				+ "						</tasks>\r\n" + "					</item>";

		szItemsXML = String.format(szItemsXML, szID, szParentID, szName,
				szWorkScopeCompleted, szWorkScope, szSpecialTypeTaskCompleted,
				szWorkCarriedOutCompleted, szWorkCarriedOut,
				szConditionsCompleted, szConditions,
				szRecommendationsCompleted, szRecommendations,
				szCommentsCompleted, szComments, szPhotographCompleted,
				szPhotograph, szHelpSupportCompleted);

		return szItemsXML;
	}

	String getServicesXMLStructure(String szItemsXML) {

		String szServicesXML = "<services>\r\n" + "%s" + "</services>";
		szServicesXML = String.format(szServicesXML, szItemsXML);

		return szServicesXML;
	}

	String getMillXMLStructure(String szMillID, String szMillName,
			String szGMDType, String szServicesXML) {
		String szMillXML = "<mill>\r\n" + "	<mill_id>%s</mill_id>\r\n"
				+ "	<mill_name>%s</mill_name>\r\n"
				+ "	<gmd_type>%s</gmd_type>\r\n" + "	%s\r\n" + "</mill>";
		szMillXML = String.format(szMillXML, szMillID, szMillName, szGMDType,
				szServicesXML);

		return szMillXML;
	}

	String getServiceCheckListStructure(String szMillXML) {

		String szServiceCheckListXML = "<callback xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"schema.xsd\">\r\n"
				+ "	<service_checklist>\r\n"
				+ "%s"
				+ "	</service_checklist>\r\n" + "</callback>";

		szServiceCheckListXML = String.format(szServiceCheckListXML, szMillXML);

		return szServiceCheckListXML;
	}

	String EncodeXML(String szString) {
		szString = szString.replaceAll("&", "&amp;");
		szString = szString.replaceAll("<", "&lt;");
		szString = szString.replaceAll(">", "&gt;");
		szString = szString.replaceAll("'", "&apos;");
		szString = szString.replaceAll("\"", "&quot;");
		return szString;
	}
}
