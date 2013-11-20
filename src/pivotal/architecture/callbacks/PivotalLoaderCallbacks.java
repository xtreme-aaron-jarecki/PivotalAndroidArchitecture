package pivotal.architecture.callbacks;

import pivotal.architecture.loaders.PivotalCursorLoader;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Loader;
import android.content.Loader.ForceLoadContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

public abstract class PivotalLoaderCallbacks implements LoaderCallbacks<Cursor> {

	public abstract Uri getUri();

	public abstract int getLoaderId();

	private final PivotalLoaderCallbacksListener mPivotalCursorLoaderCallbacks;
	private final Context mContext;
	private LoaderManager mLoaderManager;
	private ForceLoadContentObserver mForceLoadContentObserver;

	public PivotalLoaderCallbacks(Context context, final LoaderManager loaderManager, PivotalLoaderCallbacksListener pivotalCursorLoaderCallbacks) {
		mLoaderManager = loaderManager;
		mPivotalCursorLoaderCallbacks = pivotalCursorLoaderCallbacks;
		mContext = context;
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		final PivotalCursorLoader pivotalCursorLoader = new PivotalCursorLoader(mContext);
		pivotalCursorLoader.setUri(getUri());
		mForceLoadContentObserver = pivotalCursorLoader.getForceLoadContentObserver();
		mContext.getContentResolver().registerContentObserver(getUri(), false, mForceLoadContentObserver);
		return pivotalCursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		cursor.moveToFirst();
		mPivotalCursorLoaderCallbacks.onLoadFinished(getUri(), cursor);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		mPivotalCursorLoaderCallbacks.onLoaderReset(loader);
	}

	public void onStart(Context context) {
		final Loader<?> loader = mLoaderManager.getLoader(getLoaderId());
		if (loader == null)
			mLoaderManager.initLoader(getLoaderId(), null, this);
		else
			mLoaderManager.restartLoader(getLoaderId(), null, this);
	}

	public void onStop(Context context) {
		if (mForceLoadContentObserver != null)
			mContext.getContentResolver().unregisterContentObserver(mForceLoadContentObserver);
	}

}