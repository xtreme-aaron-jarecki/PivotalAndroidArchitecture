package pivotal.architecture.callbacks;

import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;

public interface PivotalLoaderCallbacksListener {
	public void onLoadFinished(final Uri uri, final Cursor cursor);
	public void onLoaderReset(final Loader<Cursor> loader) ;
}
