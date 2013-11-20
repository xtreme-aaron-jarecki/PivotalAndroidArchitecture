package pivotal.architecture.services;

import java.util.concurrent.ScheduledThreadPoolExecutor;

import pivotal.architecture.PivotalApplication;
import pivotal.architecture.database.PivotalPeopleTable;
import pivotal.architecture.tasks.PivotalPeopleTableTask;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

public class PivotalService extends Service {

	private static final int THREAD_POOL_SIZE = 4;
	private static final ScheduledThreadPoolExecutor mSheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(THREAD_POOL_SIZE);

	private static final class EXTRAS {
		public static final String URI = "uri";
	}

	public static void startTask(final Context context, final Uri uri){
		Log.d(PivotalApplication.DEBUG_TAG, "StartTask uri: " + uri.toString());
		final String uriString = uri.toString();
		
		final Intent intent = new Intent(context, PivotalService.class);
		intent.putExtra(EXTRAS.URI, uriString);
		context.startService(intent);
		
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(PivotalApplication.DEBUG_TAG, "onStartCommand intent: " + intent);
		handleOnStart(intent);
		return super.onStartCommand(intent, flags, startId);
	}

	private void handleOnStart(Intent intent) {
		if (intent == null)
			return;

		final String uriString = intent.getStringExtra(EXTRAS.URI);
		final Uri uri = Uri.parse(uriString);
		if (uri.equals(PivotalPeopleTable.URI)) {
			final PivotalPeopleTableTask peopleTableTask = new PivotalPeopleTableTask(getApplicationContext(), uri);
			mSheduledThreadPoolExecutor.execute(peopleTableTask);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}
