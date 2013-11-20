package pivotal.architecture.providers;

import java.util.Locale;

import pivotal.architecture.database.PivotalDatabase;
import pivotal.architecture.database.PivotalPeopleTable;
import pivotal.architecture.database.PivotalPeopleView;
import pivotal.architecture.database.PivotalTasksTable;
import pivotal.architecture.services.PivotalService;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class PivotalContentProvider extends ContentProvider {

	private static final String PIVOTAL_DATABASE = "MyDatabase";
	private static final String MIME_TYPE = "pivotal";
	private static SQLiteDatabase sDatabase;
	private static final long STALE_DATA_THRESHOLD = 1000 * 30; // 30 seconds
	
	public static final String AUTHORITY = "pivotal.authority";
	public static final String CONTENT = "content://";
	public static final String TASK_URI = "taskUri";

	private final UriMatcher mURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

	protected static synchronized SQLiteDatabase getDatabase(final Context context) {
		if (sDatabase == null) {
			final PivotalDatabase database = new PivotalDatabase(context, PIVOTAL_DATABASE);
			sDatabase = database.getWritableDatabase();
		}
		return sDatabase;
	}

	private SQLiteDatabase getDatabase() {
		return getDatabase(getContext());
	}

	private String getTableName(final Uri uri) {
		final int match = mURIMatcher.match(uri);
		switch (match) {
		case PivotalPeopleTable.CODE:
			return PivotalPeopleTable.TABLE_NAME;
		case PivotalPeopleView.CODE:
			return PivotalPeopleView.VIEW_NAME;
		case PivotalTasksTable.CODE:
			return PivotalTasksTable.TABLE_NAME;
		}
		return null;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		final int numRowsDeleted = getDatabase().delete(getTableName(uri), selection, selectionArgs);
		return numRowsDeleted;
	}

	@Override
	public String getType(Uri uri) {
		final String dataSetName = getTableName(uri);
		final String type = MIME_TYPE + "/" + AUTHORITY + "." + dataSetName;
		return type.toLowerCase(Locale.getDefault());
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		final long id = getDatabase().insert(getTableName(uri), null, values);
		return ContentUris.withAppendedId(uri, id);
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		final Cursor cursor = getDatabase().query(getTableName(uri), projection, selection, selectionArgs, sortOrder, null, null);

		launchTask(uri, cursor);

		return cursor;
	}

	private void launchTask(final Uri uri, final Cursor cursor) {
		final int match = mURIMatcher.match(uri);
		switch (match) {
		case PivotalTasksTable.CODE:
			boolean launchNetworkRequest = true;
			if (cursor.moveToFirst()) {
				final int timeColumnIndex = cursor.getColumnIndex(PivotalTasksTable.Columns.TIME);
				final long time = cursor.getLong(timeColumnIndex);
				final long duration = Math.abs(System.currentTimeMillis() - time);
				launchNetworkRequest = duration > STALE_DATA_THRESHOLD;
			}
			final String uriString = uri.getQueryParameter(TASK_URI);
			launchNetworkRequest = launchNetworkRequest && uriString != null;
			if (launchNetworkRequest) {
				final Uri taskUri = Uri.parse(uriString);
				PivotalService.startTask(getContext(), taskUri);
			}
		}
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		final int numRowsAffected = getDatabase().update(getTableName(uri), values, selection, selectionArgs);
		return numRowsAffected;
	}

	@Override
	public boolean onCreate() {
		mURIMatcher.addURI(AUTHORITY, PivotalPeopleTable.URI_PATH, PivotalPeopleTable.CODE);
		mURIMatcher.addURI(AUTHORITY, PivotalPeopleView.URI_PATH, PivotalPeopleView.CODE);
		mURIMatcher.addURI(AUTHORITY, PivotalTasksTable.URI_PATH, PivotalTasksTable.CODE);
		return true;
	}

}
