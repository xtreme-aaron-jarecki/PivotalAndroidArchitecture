package pivotal.architecture.loaders;

import android.content.Context;
import android.content.CursorLoader;
import android.net.Uri;

public class PivotalCursorLoader extends CursorLoader {

	private final ForceLoadContentObserver mForceLoadContentObserver;

	public PivotalCursorLoader(Context context) {
		this(context, null, null, null, null, null);
	}

	public PivotalCursorLoader(Context context, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		super(context, uri, projection, selection, selectionArgs, sortOrder);
		mForceLoadContentObserver = new ForceLoadContentObserver();
	}

	public ForceLoadContentObserver getForceLoadContentObserver() {
		return mForceLoadContentObserver;
	}

}
