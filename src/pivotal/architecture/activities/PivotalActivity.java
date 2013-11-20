package pivotal.architecture.activities;

import pivotal.architecture.R;
import pivotal.architecture.callbacks.PivotalLoaderCallbacksListener;
import pivotal.architecture.loaders.PivotalPeopleViewLoaderCallbacks;
import android.app.Activity;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.widget.ListView;
import android.widget.Toast;

public class PivotalActivity extends Activity implements PivotalLoaderCallbacksListener {

	private ListView mListView;
	private PivotalPeopleViewLoaderCallbacks mPeopleCursorLoader;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_pivotal);
		super.onCreate(savedInstanceState);
		mListView = (ListView) findViewById(R.id.activity_pivotal_list_view);
		mPeopleCursorLoader = new PivotalPeopleViewLoaderCallbacks(getApplicationContext(), getLoaderManager(), this);
	}

	@Override
	protected void onStart() {
		super.onStart();
		mPeopleCursorLoader.onStart(getApplicationContext());
	}

	@Override
	protected void onStop() {
		mPeopleCursorLoader.onStop(getApplicationContext());
		super.onStop();
	}

	@Override
	public void onLoadFinished(final Uri uri, final Cursor cursor) {
		Toast.makeText(getApplicationContext(), "We Got Data!", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onLoaderReset(final Loader<Cursor> loader) {
		mListView.setAdapter(null);
	}

}
