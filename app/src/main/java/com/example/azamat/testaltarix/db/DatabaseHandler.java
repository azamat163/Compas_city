package com.example.azamat.testaltarix.db;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.azamat.testaltarix.FavouriteGeoListAdapter;
import com.example.azamat.testaltarix.GeoDataModel;

import java.util.ArrayList;

/**
 * Created by azamat on 25.10.16.
 */

public class DatabaseHandler extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "placesDB";
    private static final String TABLE_PLACE = "places_table";
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_DIST = "dist";
    private static final String KEY_IMAGE = "image";
    private FavouriteGeoListAdapter rvAdapter;

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CITY_TABLE = "CREATE TABLE " + TABLE_PLACE + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TITLE + " TEXT,"
                + KEY_DIST + " INTEGER," + KEY_IMAGE + " TEXT"
                + ");";
        db.execSQL(CREATE_CITY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLACE);
        onCreate(db);
    }


    public void addPlace(GeoDataModel geo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE,geo.getTitle());
        values.put(KEY_DIST,geo.getDist());
        values.put(KEY_IMAGE,geo.getImage());
        Log.d("DB", "Inserting data");
        db.insert(TABLE_PLACE,null,values);
        db.close();
    }

    public ArrayList<GeoDataModel> getAllGeoDataModel() {
        ArrayList<GeoDataModel> geoList = new ArrayList<GeoDataModel>();
        String selectAll = "SELECT * FROM " + TABLE_PLACE;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectAll,null);
        if (cursor.moveToFirst()) {
            do{
                GeoDataModel geo = new GeoDataModel();
                geo.setID(Integer.parseInt(cursor.getString(0)));
                geo.setTitle(cursor.getString(1));
                geo.setDist(Integer.parseInt(cursor.getString(2)));
                geo.setImage(cursor.getString(3));

                geoList.add(geo);

            } while (cursor.moveToNext());
        }
        return geoList;
    }

    public void deletePlace(GeoDataModel geo) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PLACE, KEY_ID + "= ?",
                new String[] {String.valueOf(geo.getID())});
        db.close();
    }

}
