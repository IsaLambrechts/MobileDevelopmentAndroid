package be.ap.edu.mobiledevelopmentexamen;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.OverlayItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static android.R.id.list;

/**
 * Created by CaruCath on 06-10-17.
 */

class MapSQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "mapsaver.db";
    private static final String TABLE_NAME = "coordinates";
    private static final int DATABASE_VERSION = 2;

    public MapSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_NAME + "(_id INTEGER PRIMARY KEY AUTOINCREMENT, longitude REAL, latitude REAL, beschrijving STRING)";
        db.execSQL(CREATE_USERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addContact(double lon, double lat, String beschrijving) {
        SQLiteDatabase db = this.getWritableDatabase();
        //db.delete(TABLE_CONTACTS, null, null);
        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        ContentValues values = new ContentValues();
        values.put("longitude", lon);
        values.put("latitude", lat);
        values.put("beschrijving", beschrijving);

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public Double getlat(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_NAME + " WHERE _id = " + id;
        Cursor cursor = db.rawQuery(query, null);

        Double lat = 0.0;
        cursor.moveToFirst();
        while(!cursor.isAfterLast())
            if(cursor.getString(cursor.getColumnIndex("latitude")) != null){
                lat = Double.parseDouble(cursor.getString(cursor.getColumnIndex("latitude")));
            }

        return lat;

    }

    public Double getLon(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_NAME + " WHERE _id = " + id;
        Cursor cursor = db.rawQuery(query, null);

        Double lon = 0.0;
        cursor.moveToFirst();
        while(!cursor.isAfterLast())
            if(cursor.getString(cursor.getColumnIndex("longitude")) != null){
                lon = Double.parseDouble(cursor.getString(cursor.getColumnIndex("longitude")));
            }

        return lon;

    }

    /*public Coords get_DatabaseObject(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[] { "_id", "latitude", "longitude", "omschrijving" }, "_id" + "=?",
                new String[] { String.valueOf(id) }, null, null,null,null);
        if (cursor != null)
            cursor.moveToFirst();

        Coords data = new Coords(Double.parseDouble(cursor.getString(1)), Double.parseDouble(cursor.getString(2)),cursor.getString(3));
        // return database object
        return data;
    }

    // Getting All database objects
    public List<Coords> getAllDatabaseObject() {
        List<Coords> contactList = new ArrayList<Coords>();
        //  Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Coords data = new Coords();
                data.set_id((Integer.parseInt(cursor.getString(0))));
                data.setLat( Double.parseDouble(cursor.getString(1)));
                data.setLon( Double.parseDouble(cursor.getString(2)));
                data.setOmschrijving(cursor.getString(3));

                //   Adding contact to list
                contactList.add(data);
            } while (cursor.moveToNext());
        }

        // return database object list
        return contactList;
    }*/

    public String getTableAsString() {
        SQLiteDatabase db = this.getWritableDatabase();

        Log.d("SQLITE", "getTableAsString called");
        String tableString = String.format("Table %s:\n", TABLE_NAME);
        Cursor allRows  = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        System.out.println(allRows.getCount());
        if (allRows.moveToFirst() ){
            String[] columnNames = allRows.getColumnNames();
            do {
                for (String name: columnNames) {
                    tableString += String.format("%s: %s\n", name,
                            allRows.getString(allRows.getColumnIndex(name)));
                }
                tableString += "\n";

            } while (allRows.moveToNext());
        }

        allRows.close();

        return tableString;
    }
}
