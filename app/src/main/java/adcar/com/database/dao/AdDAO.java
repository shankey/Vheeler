package adcar.com.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import adcar.com.model.Ad;
import adcar.com.model.servertalkers.Ads;


/**
 * Created by aditya on 09/02/16.
 */
public class AdDAO extends DAO {

    private static final String TABLE_ADS = "ads";

    public static String CREATE_ADS_TABLE = "CREATE TABLE " + TABLE_ADS + "("
            + KEY_ID + " INTEGER PRIMARY KEY," + KEY_AD_ID + " INTEGER,"
            + KEY_AD_URL + " TEXT," + KEY_STATUS + " INTEGER" + ")";

    public AdDAO(Context context){
        super(context);
    }

    public void addAd(Ad ad){
        SQLiteDatabase db = dbHandler.getWritableDatabase();


            ContentValues values = new ContentValues();
            values.put(KEY_AD_ID, ad.getAdId());
            values.put(KEY_AD_URL, ad.getUrl());
            values.put(KEY_STATUS, ad.getStatus());

// Insert the new row, returning the primary key value of the new row
            long newRowId;
            newRowId = db.insert(TABLE_ADS, null , values);
            Log.i("DATABASE", "row inserted ad= " + newRowId);


        db.close();
    }


    public void addAds(Ads ads){
        SQLiteDatabase db = dbHandler.getWritableDatabase();

        for(Ad ad: ads.getAds()){
            ContentValues values = new ContentValues();

            values.put(KEY_ID, ad.getAdId());

            values.put(KEY_AD_URL, ad.getUrl());



// Insert the new row, returning the primary key value of the new row
            long newRowId;
            newRowId = db.insert(TABLE_ADS, null , values);
            Log.i("DATABASE", "row inserted area= " + newRowId);

        }
        db.close();
    }

    public void updateAdStatus(Ad ad){
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        Log.i("ADSYNC", "update ad status in db " + ad.getId());
        String sql = String.format("update " + TABLE_ADS + " set status=1 where %s=%s", KEY_ID, ad.getId());
        db.execSQL(sql);
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

    public Ad getAd(Integer adId){
        String sql = "adId=?";
        String[] params = new String[]{adId.toString()};

        List<Ad> li = getAds(sql, params);

        if(li.size()>1){
            // throw exception
        }

        if(li.size()==0){
            return null;
        }

        return li.get(0);
    }

    public List<Ad> getAds(String sql, String[] params){
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        Log.i("DATABASE", "ads query = " + sql + " " + Arrays.toString(params));

        Cursor cursor = db.query(TABLE_ADS, new String[]{KEY_ID, KEY_AD_ID, KEY_AD_URL, KEY_STATUS}
                , sql, params, null, null, KEY_ID + " DESC");

        List<Ad> adList = new ArrayList<Ad>();

        if(cursor.moveToFirst()){
            do{
                Log.i("DB QUERY", "" + cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                Log.i("DB QUERY", "" + cursor.getInt(cursor.getColumnIndex(KEY_AD_ID)));
                Log.i("DB QUERY", "" + cursor.getString(cursor.getColumnIndex(KEY_AD_URL)));
                Log.i("DB QUERY", "" + cursor.getString(cursor.getColumnIndex(KEY_STATUS)));

                Ad ad = new Ad();
                ad.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                ad.setAdId(cursor.getInt(cursor.getColumnIndex(KEY_AD_ID)));
                ad.setUrl(cursor.getString(cursor.getColumnIndex(KEY_AD_URL)));
                ad.setStatus(cursor.getInt(cursor.getColumnIndex(KEY_STATUS)));

                adList.add(ad);

            }while(cursor.moveToNext());
        }

        Log.i("DATABASE", "ads return = " + adList.toString());
        return adList;

    }

    public List<Ad> getUnsyncedAds(Integer status){
        String sql = "status=?";
        String[] params = new String[]{status.toString()};

        return getAds(sql, params);
    }


}
