package pivotal.architecture.tasks;

import pivotal.architecture.PivotalApplication;
import pivotal.architecture.database.PivotalTasksTable;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public abstract class PivotalTask implements Runnable {

	private final Context mContext;
	private final Uri mTaskId;

	public PivotalTask(final Context context, final Uri taskId) {
		mContext = context;
		mTaskId = taskId;
	}

	public Context getContext() {
		return mContext;
	}

	public Uri getUri() {
		return mTaskId;
	}

	@Override
	public void run() {
		try {
			notifyRunning();
			executeTask();
			onSuccess();
		} catch (final Exception exception) {
			onFailure();
		} finally{
			Log.d(PivotalApplication.DEBUG_TAG, "complete");
		}
	}

	private void notifyRunning() {
		final ContentResolver contentResolver = getContext().getContentResolver();
		final String whereClause = PivotalTasksTable.Columns.TASK_ID + "=? AND " + PivotalTasksTable.Columns.STATE + "<>?";
		final String[] whereArguments = new String[] { mTaskId.toString(), PivotalTasksTable.State.RUNNING };
		final ContentValues contentValues = new ContentValues();
		contentValues.put(PivotalTasksTable.Columns.STATE, PivotalTasksTable.State.RUNNING);
		contentValues.put(PivotalTasksTable.Columns.TASK_ID, mTaskId.toString());
		contentValues.put(PivotalTasksTable.Columns.TIME, System.currentTimeMillis());
		// THE FOLLOWING NEEDS TO BE ATOMIC
		final int rows = contentResolver.update(PivotalTasksTable.URI, contentValues, whereClause, whereArguments);
		if (rows == 0) {
			final String queryWhereClause = PivotalTasksTable.Columns.TASK_ID + "=? AND " + PivotalTasksTable.Columns.STATE + "=?";
			final String[] queryWhereArguments = new String[] { mTaskId.toString(), PivotalTasksTable.State.RUNNING };

			final Cursor cursor = contentResolver.query(PivotalTasksTable.URI, null, queryWhereClause, queryWhereArguments, null);
			try {
				if (cursor.getCount() != 0)
					return;
			} finally {
				cursor.close();
			}
			contentResolver.insert(PivotalTasksTable.URI, contentValues);

		}
		contentResolver.notifyChange(PivotalTasksTable.URI, null);
	}

	private void onFailure() {
		notifyState(PivotalTasksTable.State.FAIL);
		Log.d(PivotalApplication.DEBUG_TAG, "fail");
	}

	private void onSuccess() {
		notifyState(PivotalTasksTable.State.SUCCESS);
		Log.d(PivotalApplication.DEBUG_TAG, "success");
	}

	private void notifyState(final String state) {
		final ContentResolver contentResolver = getContext().getContentResolver();
		final String whereClause = PivotalTasksTable.Columns.TASK_ID + "=?";
		final String[] whereArguments = new String[] { mTaskId.toString() };
		final ContentValues contentValues = new ContentValues();
		contentValues.put(PivotalTasksTable.Columns.STATE, state);
		contentValues.put(PivotalTasksTable.Columns.TASK_ID, mTaskId.toString());
		contentValues.put(PivotalTasksTable.Columns.TIME, System.currentTimeMillis());
		contentResolver.update(PivotalTasksTable.URI, contentValues, whereClause, whereArguments);
		contentResolver.notifyChange(PivotalTasksTable.URI, null);
	}

	public abstract void executeTask() throws Exception;

}
