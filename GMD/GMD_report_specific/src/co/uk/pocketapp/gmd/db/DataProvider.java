package co.uk.pocketapp.gmd.db;

import android.net.Uri;
import android.provider.BaseColumns;

public class DataProvider {

	public static final String AUTHORITY = GMDContentProvider.class.getName();

	private DataProvider() {
	}

	public static final Uri CUSTOMQUERY = Uri.parse("content://" + AUTHORITY
			+ "/customquery");

	// start of old structure

	public static final class Main implements BaseColumns {
		private Main() {
		}

		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ AUTHORITY + "/mainparent");
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.pocketapp.main";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.pocketapp.main";
		public static final String ID = "_id";
		public static final String CHILD = "child";
	}

	public static final class Second_Generation implements BaseColumns {
		private Second_Generation() {
		}

		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ AUTHORITY + "/secondgeneration");
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.pocketapp.secondgeneration";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.pocketapp.secondgeneration";
		public static final String ID = "_id";
		public static final String MAIN_CHILD_ID = "mainchildid";
		public static final String CHILD_SECOND_GEN = "childsecondgen";
	}

	public static final class Third_Generation implements BaseColumns {
		private Third_Generation() {
		}

		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ AUTHORITY + "/thirdgeneration");
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.pocketapp.thirdgeneration";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.pocketapp.thirdgeneration";
		public static final String ID = "_id";
		public static final String CHILD_SECOND_GEN_ID = "childsecondgenid";
		public static final String CHILD_THIRD_GEN = "childthirdgen";
	}

	public static final class Leaf_Node implements BaseColumns {
		private Leaf_Node() {
		}

		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ AUTHORITY + "/leafnode");
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.pocketapp.leafnode";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.pocketapp.leafnode";
		public static final String ID = "_id";
		public static final String CHILD_THIRD_GEN_ID = "childthirdgenid";
		public static final String CHILD_LEAF_NODE = "childleafnode";
	}

	public static final class Summary_Values implements BaseColumns {
		private Summary_Values() {
		}

		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ AUTHORITY + "/summaryvalues");
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.pocketapp.summaryvalues";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.pocketapp.summaryvalues";
		public static final String ID = "_id";
		public static final String REPORT_ID = "reportid";
		public static final String CHILD_SECOND_GEN_ID = "childsecondgenid";
		public static final String CHILD_THIRD_GEN_ID = "childthirdgenid";
		public static final String CHILD_LEAF_NODE_ID = "childleafnodeid";
		public static final String CHILD_MAIN_ID = "childmainid";
		public static final String CHILD_SECOND_GEN = "childsecondgen";
		public static final String CHILD_THIRD_GEN = "childthirdgen";
		public static final String CHILD_LEAF_NODE = "childleafnode";
		public static final String CHILD_MAIN = "childmain";
		public static final String CHILD = "child";
		public static final String VALUE = "value";
	}

	// end of old structure

	public static final class Material_Log implements BaseColumns {
		private Material_Log() {
		}

		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ AUTHORITY + "/materiallog");
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.pocketapp.materiallog";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.pocketapp.materiallog";
		public static final String ID = "_id";
		public static final String REPORT_ID = "reportid";
		public static final String MATERIAL_CATEGORY = "category";
		public static final String MATERIAL_NAME = "name";
		public static final String MATERIAL_REPORT_DATE = "reportdate";
		public static final String MATERIAL_INFO = "info";
	}

	// start of new structure

	public static final class Mill implements BaseColumns {
		private Mill() {
		}

		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ AUTHORITY + "/mill");
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.pocketapp.mill";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.pocketapp.mill";
		public static final String ID = "_id";
		public static final String MILL_ID = "millid";
		public static final String MILL_NAME = "name";
		public static final String GMD_TYPE = "gmdtype";
		public static final String REPORT_ID = "reportid";
	}

	public static final class Services implements BaseColumns {
		private Services() {
		}

		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ AUTHORITY + "/services");
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.pocketapp.services";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.pocketapp.services";
		public static final String ID = "_id";
		public static final String REPORT_ID = "reportid";
		public static final String MILL_ID = "millid";
		public static final String ITEM_ID = "itemid";
		public static final String PARENT_ID = "parentid";
		public static final String ITEM_NAME = "name";
	}

	public static final class Tasks implements BaseColumns {
		private Tasks() {
		}

		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ AUTHORITY + "/tasks");
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.pocketapp.tasks";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.pocketapp.tasks";
		public static final String ID = "_id";
		public static final String REPORT_ID = "reportid";
		public static final String MILL_ID = "millid";
		public static final String SERVICES_ITEM_ID = "servicesitemid";
		public static final String SERVICES_PARENT_ID = "servicesparentid";
		public static final String TASK_NAME = "taskname";
		public static final String TASK_CONTENT = "taskcontent";
		public static final String TASK_COMMENT = "taskcomment";
		public static final String TASK_COMPLETED = "taskcompleted";
		public static final String SERVICES_FIRSTGEN_ID = "servicesfirstgenid";
		public static final String SERVICES_SECONDGEN_ID = "servicessecondgenid";
		public static final String SERVICES_FIRSTGEN_PARENT_ID = "servicesfirstgenparentid";
		public static final String SERVICES_SECONDGEN_PARENT_ID = "servicessecondgenparentid";
	}

	public static final class Child_Leaf implements BaseColumns {
		private Child_Leaf() {
		}

		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ AUTHORITY + "/childleaf");
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.pocketapp.childleaf";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.pocketapp.childleaf";
		public static final String ID = "_id";
		public static final String REPORT_ID = "reportid";
		public static final String MILL_ID = "millid";
		public static final String CHILD_LEAF = "childleaf";
	}
	// end of new structure

}
