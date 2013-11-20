package pivotal.architecture.loaders;

import pivotal.architecture.callbacks.PivotalLoaderCallbacks;
import pivotal.architecture.callbacks.PivotalLoaderCallbacksListener;
import pivotal.architecture.database.PivotalPeopleView;
import android.app.LoaderManager;
import android.content.Context;
import android.net.Uri;

public class PivotalPeopleViewLoaderCallbacks extends PivotalLoaderCallbacks {

	private static final Uri URI = PivotalPeopleView.URI;
	private static final int LOADER_ID = 0;

	public PivotalPeopleViewLoaderCallbacks(Context context, LoaderManager loaderManager, PivotalLoaderCallbacksListener pivotalCursorLoaderCallbacks) {
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
