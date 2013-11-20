package pivotal.architecture.models;

import pivotal.architecture.database.PivotalPeopleTable;
import android.content.ContentValues;

import com.google.gson.annotations.SerializedName;

public class PivotalPeopleModel {

	public static final class Keys {
		public static final String ID = "id";
		public static final String LAST_NAME = "lastName";
		public static final String FIRST_NAME = "firstName";
		public static final String ADDRESS = "address";
		public static final String CITY = "city";
	}

	@SerializedName(Keys.ID)
	private final Long mId;
	@SerializedName(Keys.LAST_NAME)
	private final String mLastName;
	@SerializedName(Keys.FIRST_NAME)
	private final String mFirstName;
	@SerializedName(Keys.ADDRESS)
	private final String mAddress;
	@SerializedName(Keys.CITY)
	private final String mCity;

	public PivotalPeopleModel(final Long id, final String lastName, final String firstName, final String address, final String city) {
		mId = id;
		mLastName = lastName;
		mFirstName = firstName;
		mAddress = address;
		mCity = city;
	}

	public Long getId(){
		return mId;
	}
	
	public String getLastName() {
		return mLastName;
	}

	public String getFirstName() {
		return mFirstName;
	}

	public String getAddress() {
		return mAddress;
	}

	public String getCity() {
		return mCity;
	}

	public ContentValues getcontentValues() {
		final ContentValues value = new ContentValues();
		value.put(PivotalPeopleTable.Columns.ID, getId());
		value.put(PivotalPeopleTable.Columns.FIRST_NAME, getFirstName());
		value.put(PivotalPeopleTable.Columns.LAST_NAME, getLastName());
		value.put(PivotalPeopleTable.Columns.ADDRESS, getAddress());
		value.put(PivotalPeopleTable.Columns.CITY, getCity());
		return value;
	}

	public static ContentValues getContentValues(PivotalPeopleModel item) {
		return item.getcontentValues();
	}
}
