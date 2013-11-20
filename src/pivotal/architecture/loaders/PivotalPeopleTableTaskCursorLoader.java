package pivotal.architecture.loaders;

import pivotal.architecture.callbacks.PivotalLoaderCallbacks;
import pivotal.architecture.callbacks.PivotalLoaderCallbacksListener;
import pivotal.architecture.database.PivotalPeopleTable;
import pivotal.architecture.database.PivotalTasksTable;
import pivotal.architecture.providers.PivotalContentProvider;
import android.app.LoaderManager;
import android.content.Context;
import android.net.Uri;

public class PivotalPeopleTableTaskCursorLoader extends PivotalLoaderCallbacks {

	private static final Uri URI = PivotalTasksTable.URI.buildUpon().appendQueryParameter(PivotalContentProvider.TASK_URI, PivotalPeopleTable.URI.toString()).build();
	private static final int LOADER_ID = 1;

	public PivotalPeopleTableTaskCursorLoader(Context context, LoaderManager loaderManager, PivotalLoaderCallbacksListener pivotalCursorLoaderCallbacks) {
		super(context, loaderManager, pivotalCursorLoaderCallbacks);
	}

	@Override
	public Uri getUri() {
		return URI;
	}

	@Override
	public int getLoaderId() {
		return LOADER_ID;
	}

}
