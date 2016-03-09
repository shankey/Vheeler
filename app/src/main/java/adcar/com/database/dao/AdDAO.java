package adcar.com.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adcar.com.model.Ad;
import adcar.com.model.Ads;


/**
 * Created by aditya on 09/02/16.
 */
public class AdDAO extends DAO {

    private static final String TABLE_ADS = "ads";

    public static String CREATE_ADS_TABLE = "CREATE TABLE " + TABLE_ADS + "("
            + KEY_ID + " INTEGER PRIMARY KEY," + KEY_AREA_ID + " INTEGER,"
            + KEY_AD_URL + " TEXT," + KEY_STATUS + " INTEGER" + ")";

    public AdDAO(Context context){
        super(context);
    }

    public void addAd(Ad ad){
        SQLiteDatabase db = dbHandler.getWritableDatabase();


            ContentValues values = new ContentValues();

            values.put(KEY_ID, ad.getId());
            values.put(KEY_AREA_ID, ad.getAreaId());
            values.put(KEY_AD_URL, ad.getUrl());
            values.put(KEY_STATUS, ad.getStatus());

// Insert the new row, returning the primary key value of the new row
            long newRowId;
            newRowId = db.insert(TABLE_ADS, null , values);
            Log.i("DATABASE", "row inserted area= " + newRowId);


        db.close();
    }


    public void addAds(Ads ads){
        SQLiteDatabase db = dbHandler.getWritableDatabase();

        for(Ad ad: ads.getAds()){
            ContentValues values = new ContentValues();

            values.put(KEY_ID, ad.getId());
            values.put(KEY_AREA_ID, ad.getAreaId());
            values.put(KEY_AD_URL, ad.getUrl());
            values.put(KEY_STATUS, ad.getStatus());


// Insert the new row, returning the primary key value of the new row
            long newRowId;
            newRowId = db.insert(TABLE_ADS, null , values);
            Log.i("DATABASE", "row inserted area= " + newRowId);

        }
        db.close();
    }

    public void deleteAd(){
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        Log.i("DB QUERY", "delete all ads");
        try{
            db.delete(TABLE_ADS, null, null);
        }catch (Exception e){
            Log.i("ERROR", e.toString());
        } finally {
            db.close();
        }

    }

    public Map<Integer, Ad> getAds(){
        SQLiteDatabase db = dbHandler.getReadableDatabase();

        Cursor cursor = db.query(TABLE_ADS, new String[]{KEY_ID, KEY_AREA_ID, KEY_AD_URL, KEY_STATUS}
                , null, null, null, null, KEY_ID + " DESC");

        Map<Integer, Ad> adHashMap = new HashMap<>();

        if(cursor.moveToFirst()){
            do{
                Log.i("DB QUERY", "" + cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                Log.i("DB QUERY", "" + cursor.getInt(cursor.getColumnIndex(KEY_AREA_ID)));
                Log.i("DB QUERY", "" + cursor.getString(cursor.getColumnIndex(KEY_AD_URL)));

                Ad ad = new Ad();
                ad.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                ad.setAreaId(cursor.getInt(cursor.getColumnIndex(KEY_AREA_ID)));
                ad.setUrl(cursor.getString(cursor.getColumnIndex(KEY_AD_URL)));

                adHashMap.put(ad.getAreaId(), ad);

            }while(cursor.moveToNext());
        }

        return adHashMap;
    }
}
