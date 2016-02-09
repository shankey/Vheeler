package adcar.com.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import adcar.com.model.Coordinate;
import adcar.com.model.CoordinatesEntity;

/**
 * Created by aditya on 02/02/16.
 */
public class CoordinateDAO extends DAO {

    private static final String TABLE_COORDINATES = "coordinates";

    public static String CREATE_COORDINATES_TABLE = "CREATE TABLE " + TABLE_COORDINATES + "("
            + KEY_ID + " INTEGER PRIMARY KEY," + KEY_LATITUDE + " REAL,"
            + KEY_LONGITUDE + " REAL,"+ KEY_TIMESTAMP + " TIMESTAMP" + ")";


    public CoordinateDAO(Context context){
        super(context);
    }

    public void deleteCoordinate(List<CoordinatesEntity> coordinates){
        SQLiteDatabase db = dbHandler.getWritableDatabase();

        List<String> idList = new ArrayList<>();
        for(CoordinatesEntity coordinate: coordinates){
            idList.add(coordinate.getId().toString());
        }

        String args = TextUtils.join(", ", idList);
        db.execSQL(String.format("DELETE FROM "+ TABLE_COORDINATES +" WHERE id IN (%s);", args));
        db.close();
    }

    public void addCoordinate(CoordinatesEntity coordinatesEntity){
        SQLiteDatabase db = dbHandler.getWritableDatabase();

        ContentValues values = new ContentValues();


        // Create a new map of values, where column names are the keys

        values.put(KEY_LATITUDE, coordinatesEntity.getCoordinate().getLatitude());
        values.put(KEY_LONGITUDE, coordinatesEntity.getCoordinate().getLongitude());
        values.put(KEY_TIMESTAMP, new Date().getTime());

// Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(TABLE_COORDINATES, null , values);
        db.close();

        Log.i("DATABASE", "row inserted = " + newRowId);
    }

    public ArrayList<CoordinatesEntity> getCoordinates(){
        SQLiteDatabase db = dbHandler.getReadableDatabase();

        Cursor cursor = db.query(TABLE_COORDINATES, new String[]{KEY_ID, KEY_LATITUDE, KEY_LONGITUDE, KEY_TIMESTAMP}
                ,null, null, null, null, KEY_TIMESTAMP+ " DESC", "700");

        ArrayList<CoordinatesEntity> coordinates = new ArrayList<CoordinatesEntity>();

        if(cursor.moveToFirst()){
            do{
                Log.i("DB QUERY", cursor.getString(cursor.getColumnIndex(KEY_ID)));
                Log.i("DB QUERY", cursor.getString(cursor.getColumnIndex(KEY_LATITUDE)));
                Log.i("DB QUERY", cursor.getString(cursor.getColumnIndex(KEY_LONGITUDE)));
                Log.i("DB QUERY", cursor.getString(cursor.getColumnIndex(KEY_TIMESTAMP)));

                CoordinatesEntity coordinate = new CoordinatesEntity();
                coordinate.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                coordinate.setTimestamp(new Timestamp(cursor.getLong(cursor.getColumnIndex(KEY_TIMESTAMP))));
                Coordinate co = new Coordinate(cursor.getDouble(cursor.getColumnIndex(KEY_LATITUDE)), cursor.getDouble(cursor.getColumnIndex(KEY_LONGITUDE)));
                coordinate.setCoordinate(co);
                coordinates.add(coordinate);
            }while(cursor.moveToNext());
        }
        db.close();
        return  coordinates;
    }



}
