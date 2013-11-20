package pivotal.architecture.activities;

import pivotal.architecture.PivotalApplication;
import pivotal.architecture.R;
import pivotal.architecture.adapters.PivotalCursorAdapter;
import pivotal.architecture.callbacks.PivotalLoaderCallbacksListener;
import pivotal.architecture.database.PivotalPeopleView;
import pivotal.architecture.database.PivotalTasksTable;
import pivotal.architecture.loaders.PivotalPeopleViewLoaderCallbacks;
import pivotal.architecture.loaders.PivotalPeopleTableTaskCursorLoader;
import android.app.Activity;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.ListView;
import android.widget.Toast;

public class PivotalActivity extends Activity implements PivotalLoaderCallbacksListener {

	private ListView mListView;
	private PivotalPeopleViewLoaderCallbacks mPeopleCursorLoader;
	private PivotalPeopleTableTaskCursorLoader mPivotalPeopleTaskCursorLoader;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_pivotal);
		super.onCreate(savedInstanceState);
		mListView = (ListView) findViewById(R.id.activity_pivotal_list_view);
		mPeopleCursorLoader = new PivotalPeopleViewLoaderCallbacks(getApplicationContext(), getLoaderManager(), this);
		mPivotalPeopleTaskCursorLoader = new PivotalPeopleTableTaskCursorLoader(getApplicationContext(), getLoaderManager(), this);
	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.d(PivotalApplication.DEBUG_TAG, "onStart");
		mPeopleCursorLoader.onStart(getApplicationContext());
		mPivotalPeopleTaskCursorLoader.onStart(getApplicationContext());
	}

	@Override
	protected void onStop() {
		Log.d(PivotalApplication.DEBUG_TAG, "onStop");
		mPeopleCursorLoader.onStop(getApplicationContext());
		mPivotalPeopleTaskCursorLoader.onStop(getApplicationContext());
		super.onStop();
	}

	@Override
	public void onLoadFinished(final Uri uri, final Cursor cursor) {
		Log.d(PivotalApplication.DEBUG_TAG, "uri: " + uri.toString() + " count: " +  cursor.getCount());
		if (uri.equals(PivotalPeopleView.URI)) {
			
			final PivotalCursorAdapter pivotalCursorAdapter = new PivotalCursorAdapter(getApplicationContext(), cursor);
			mListView.setAdapter(pivotalCursorAdapter);
		} else {
			final boolean isCursorEmpty = cursor.getCount() == 0;
			if (isCursorEmpty){
				setProgressBarIndeterminate(true);
				return;
			}
			
			final int stateColumnIndex = cursor.getColumnIndex(PivotalTasksTable.Columns.STATE);
			final String state = cursor.getString(stateColumnIndex);
			Log.d(PivotalApplication.DEBUG_TAG, "state: " + state);

			if (PivotalTasksTable.State.SUCCESS.equals(state))
				setProgressBarIndeterminate(false);
			else if (PivotalTasksTable.State.FAIL.equals(state)) {
				setProgressBarIndeterminate(false);
				Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
			} else if (PivotalTasksTable.State.RUNNING.equals(state))
				setProgressBarIndeterminate(true);
		}
	}

	@Override
	public void onLoaderReset(final Loader<Cursor> loader) {
		mListView.setAdapter(null);
	}

}
