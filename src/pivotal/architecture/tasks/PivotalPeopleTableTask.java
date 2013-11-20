package pivotal.architecture.tasks;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;

import pivotal.architecture.PivotalApplication;
import pivotal.architecture.database.PivotalPeopleView;
import pivotal.architecture.models.PivotalPeopleModel;
import pivotal.architecture.providers.PivotalContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.net.Uri;
import android.os.RemoteException;
import android.util.Log;

import com.google.gson.Gson;

public class PivotalPeopleTableTask extends PivotalTask {

	public static final Gson GSON = new Gson();

	public PivotalPeopleTableTask(Context context, Uri uri) {
		super(context, uri);
	}

	@Override
	public void executeTask() throws Exception {
		final String peopleDirectory = getContext().getExternalCacheDir() + "/" + PivotalApplication.NETWORK_PEOPLE_DIRECTORY + "/" + PivotalApplication.NETWORK_PEOPLE_DIRECTORY;

		final File directory = new File(peopleDirectory);
		final File[] files = directory.listFiles();

		final Collection<ContentValues> contentValueList = new ArrayList<ContentValues>(files.length);
		for (final File file : files) {
			final String filename = file.getName();
			if (!file.isDirectory()) {
				FileInputStream fileInputStream = null;
				InputStreamReader inputStreamReader = null;
				try {
					fileInputStream = new FileInputStream(file);
					inputStreamReader = new InputStreamReader(fileInputStream);
					final PivotalPeopleModel pivotalModel = GSON.fromJson(inputStreamReader, PivotalPeopleModel.class);
					final ContentValues contentValues = pivotalModel.getcontentValues();
					contentValueList.add(contentValues);
				} finally {
					if (inputStreamReader != null) {
						try {
							inputStreamReader.close();
						} catch (IOException e) {
						}
					}

					if (fileInputStream != null) {
						try {
							fileInputStream.close();
						} catch (IOException e) {
						}
					}
				}
			}
		}
		updateDatabase(contentValueList);
	}

	private void updateDatabase(final Collection<ContentValues> contentValueList) throws RemoteException, OperationApplicationException {
		Log.d(PivotalApplication.DEBUG_TAG, "contentValueList.size(): " + contentValueList.size());
		final ArrayList<ContentProviderOperation> contentProviderOperations = new ArrayList<ContentProviderOperation>();

		final ContentProviderOperation deleteContentProviderOperation = ContentProviderOperation.newDelete(getUri()).build();
		contentProviderOperations.add(deleteContentProviderOperation);

		for (final ContentValues contentValues : contentValueList) {
			final ContentProviderOperation contentProviderOperation = ContentProviderOperation.newInsert(getUri()).withValues(contentValues).build();
			contentProviderOperations.add(contentProviderOperation);
		}

		final ContentResolver contentResolver = getContext().getContentResolver();
		contentResolver.applyBatch(PivotalContentProvider.AUTHORITY, contentProviderOperations);
		contentResolver.notifyChange(PivotalPeopleView.URI, null);
	}
}
