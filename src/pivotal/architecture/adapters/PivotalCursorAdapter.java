package pivotal.architecture.adapters;

import pivotal.architecture.R;
import pivotal.architecture.database.PivotalPeopleTable;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class PivotalCursorAdapter extends CursorAdapter {

	public PivotalCursorAdapter(Context context, Cursor c) {
		super(context, c);
	}

	public PivotalCursorAdapter(Context context, Cursor c, boolean autoRequery) {
		super(context, c, autoRequery);
	}

	public PivotalCursorAdapter(Context context, Cursor c, int flags) {
		super(context, c, flags);
	}

	@Override
	public int getItemViewType(int position) {
		return super.getItemViewType(position);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		final LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View view = layoutInflater.inflate(R.layout.list_item_activity_pivotal, null);

		optimize(view, R.id.list_item_activity_pivotal_address);
		optimize(view, R.id.list_item_activity_pivotal_city);
		optimize(view, R.id.list_item_activity_pivotal_first_name);
		optimize(view, R.id.list_item_activity_pivotal_last_name);

		return view;
	}

	private void optimize(final View view, final int resourceId) {
		view.setTag(resourceId, view.findViewById(resourceId));
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		bindTextView(view, R.id.list_item_activity_pivotal_first_name, cursor, PivotalPeopleTable.Columns.FIRST_NAME);
		bindTextView(view, R.id.list_item_activity_pivotal_address, cursor, PivotalPeopleTable.Columns.ADDRESS);
		bindTextView(view, R.id.list_item_activity_pivotal_city, cursor, PivotalPeopleTable.Columns.CITY);
		bindTextView(view, R.id.list_item_activity_pivotal_last_name, cursor, PivotalPeopleTable.Columns.LAST_NAME);
	}
	
	private void bindTextView(final View view, final int resourceId, final Cursor cursor, final String columnName){
		final int columnIndex = cursor.getColumnIndex(columnName);
		final String string = cursor.getString(columnIndex);
		final TextView textView = (TextView) view.getTag(resourceId);
		textView.setText(string);
	}

}
