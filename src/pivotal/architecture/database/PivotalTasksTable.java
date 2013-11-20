package pivotal.architecture.database;

import pivotal.architecture.providers.PivotalContentProvider;
import android.net.Uri;

public class PivotalTasksTable {

	public static final class Columns {
		public static final String TASK_ID = "taskId";
		public static final String STATE = "state";
		public static final String TIME = "time";
	}
	
	public static final class State {
		public static final String RUNNING = "running";
		public static final String SUCCESS = "success";
		public static final String FAIL = "fail";
	}

	public static String TABLE_NAME = "tasks";
	
	public static final String DROP = "DROP TABLE IF EXISTS "+ TABLE_NAME;
	
	public static final String CREATE = "CREATE TABLE " + TABLE_NAME + " ( " + 
	Columns.TASK_ID + " TEXT, " + 
	Columns.STATE + " TEXT, " + 
	Columns.TIME + " INTEGER " + ");";

	public static final String URI_PATH = TABLE_NAME;
	public static final Uri URI = Uri.parse(PivotalContentProvider.CONTENT + PivotalContentProvider.AUTHORITY + "/" +  TABLE_NAME); 
	public static final int CODE = 3;
}
