package PlaceItDB;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import HTTP.RequestReceiver;
import Models.LocationPlaceIt;
import Models.PlaceIt;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class PlaceItHandler extends SQLiteOpenHelper implements iPlaceItModelv2 {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 11;

	// Database Name
	private static final String DATABASE_NAME = "CSE110";

	// Contacts table name
	private static final String TABLE_PLACEITS = "placeIts";

	// Contacts Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_TITLE = "title";
	private static final String KEY_DESCRIPTION = "description";
	private static final String KEY_LONGITUDE = "longitude";
	private static final String KEY_LATITUDE = "latitude";
	private static final String KEY_ACTIVEDATE = "activeDate";

	public PlaceItHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_PLACEITS_TABLE = "CREATE TABLE " + TABLE_PLACEITS + "("
				+ KEY_ID + " INTEGER PRIMARY KEY, " + KEY_TITLE
				+ " VARCHAR(255), " + KEY_DESCRIPTION + " TEXT ,"
				+ KEY_LONGITUDE + " DOUBLE, " + KEY_LATITUDE + " DOUBLE ,"
				+ KEY_ACTIVEDATE + " BIGINT" + ")";

		db.execSQL(CREATE_PLACEITS_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLACEITS);

		// Create tables again
		onCreate(db);
	}

	@Override
	public String addPlaceIt(PlaceIt placeIt) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_TITLE, placeIt.getTitle());
		values.put(KEY_DESCRIPTION, placeIt.getDescription());
	/*	values.put(KEY_LONGITUDE, placeIt.getLongitude());
		values.put(KEY_LATITUDE, placeIt.getLatitude());*/
		Calendar cal = Calendar.getInstance();
		cal.setTime(placeIt.getActiveDate());
		cal.add(Calendar.MINUTE, 45);
		values.put(KEY_ACTIVEDATE, cal.getTime().getTime());
		// Inserting Row
		long id = db.insert(TABLE_PLACEITS, null, values);
		db.close(); // Closing database connection
		return Long.toString(id);

	}
	
	public PlaceIt getPlaceIt(String id) {
		List<PlaceIt> placeItList = new ArrayList<PlaceIt>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_PLACEITS + " WHERE " + KEY_ID + " = " + id;
		Log.d("SQL QUERY", selectQuery);
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				PlaceIt contact = new LocationPlaceIt(cursor.getString(1),cursor.getString(2));
				contact.setID(cursor.getString(0));

		//		Log.d("placeit created had id", Integer.toString(contact.getID()));
				/*contact.setLatitude(Double.valueOf(cursor.getString(4)));
				contact.setLongitude(Double.valueOf(cursor.getString(3)));*/
				double ds = Double.parseDouble(cursor.getString(5));
				long sd = (long) ds;
				contact.setActiveDate(sd);
				// Adding placeit to listr
				placeItList.add(contact);
			} while (cursor.moveToNext());
		}

		// return contact list
		if(placeItList.size() > 0)
			return placeItList.get(0);
		else 
			return null;
	}

	@Override
	public void getAllPlaceIts(RequestReceiver receiver) {
		List<PlaceIt> placeItList = new ArrayList<PlaceIt>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_PLACEITS;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				/*PlaceIt contact = new PlaceIt();
				contact.setID(Integer.parseInt(cursor.getString(0)));

				Log.d("placeit created had id", Integer.toString(contact.getID()));
				contact.setTitle(cursor.getString(1));
				contact.setDescription(cursor.getString(2));
				contact.setLatitude(Double.valueOf(cursor.getString(4)));
				contact.setLongitude(Double.valueOf(cursor.getString(3)));
				double ds = Double.parseDouble(cursor.getString(5));
				long sd = (long) ds;
				contact.setActiveDate(sd);*/
				// Adding placeit to listr
			//	placeItList.add(contact);
			} while (cursor.moveToNext());
		}

		// return contact list
		//return placeItList;
	}

	

	@Override
	public void updatePlaceIt(PlaceIt placeit, RequestReceiver receiver) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_TITLE, placeit.getTitle());
		values.put(KEY_DESCRIPTION, placeit.getDescription());
		values.put(KEY_ACTIVEDATE, placeit.getActiveDate().getTime());
		Log.d("UPDATING VALUES", values.toString());
		// updating row
	}

	@Override
	public void deleteAll() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLACEITS);
		onCreate(db);
	}

	@Override
	public void deletePlaceIt(PlaceIt placeit, RequestReceiver receiver) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		db.delete(TABLE_PLACEITS, KEY_ID + " = ?",
				new String[] { String.valueOf(placeit.getID()) });
		db.close();
	}

	@Override
	public void deactivatePlaceit(PlaceIt placeit) {
		placeit.setActiveDate(0); /* maybe... */
		//this.updatePlaceIt(placeit);

	}

	@Override
	public void getPlaceIt(String id, RequestReceiver receiver) {
		// TODO Auto-generated method stub
		
	}

}
