package be.ap.edu.mobiledevelopmentexamen;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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
        values.put("beschrijving", "test");

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

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
