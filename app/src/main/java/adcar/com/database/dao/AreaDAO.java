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

import adcar.com.model.Area;
import adcar.com.model.Areas;
import adcar.com.model.Coordinate;

/**
 * Created by aditya on 02/02/16.
 */
public class AreaDAO extends DAO {

    private static final String TABLE_AREA = "areas";

    public static String CREATE_AREAS_TABLE = "CREATE TABLE " + TABLE_AREA + "("
            + KEY_ID + " INTEGER PRIMARY KEY," + KEY_AREA_ID + " INTEGER,"
            + KEY_LATITUDE + " REAL,"
            + KEY_LONGITUDE + " REAL"+ ")";


    public AreaDAO(Context context){
        super(context);
    }

    public void addArea(Area area){
        SQLiteDatabase db = dbHandler.getWritableDatabase();

        try{
            for(Coordinate co: area.getCoordinates()){
                ContentValues values = new ContentValues();

                values.put(KEY_AREA_ID, area.getAreaId());
                values.put(KEY_LATITUDE, co.getLatitude());
                values.put(KEY_LONGITUDE, co.getLongitude());


// Insert the new row, returning the primary key value of the new row
                long newRowId;
                newRowId = db.insert(TABLE_AREA, null , values);
                Log.i("DATABASE", "row inserted area= " + newRowId);

            }
        }catch (Exception e){
            Log.i("ERROR", e.toString());
        } finally {
            db.close();
        }

    }

    public void deleteArea(){
        SQLiteDatabase db = dbHandler.getWritableDatabase();

        try{
            db.delete(TABLE_AREA, null, null);
        }catch (Exception e){
            Log.i("ERROR", e.toString());
        } finally {
            db.close();
        }

    }



    public List<Area> getAreas(){
        SQLiteDatabase db = dbHandler.getReadableDatabase();

        Cursor cursor = db.query(TABLE_AREA, new String[]{KEY_ID, KEY_AREA_ID, KEY_LATITUDE, KEY_LONGITUDE}
                , null, null, null, null, KEY_ID + " DESC");

        Map<Integer, Area> areas = new HashMap<>();

        if(cursor.moveToFirst()){
            do{

                Integer areaId = cursor.getInt(cursor.getColumnIndex(KEY_AREA_ID));

                Coordinate co = new Coordinate();
                co.setLatitude(cursor.getDouble(cursor.getColumnIndex(KEY_LATITUDE)));
                co.setLongitude(cursor.getDouble(cursor.getColumnIndex(KEY_LONGITUDE)));

                Area area;
                if(!areas.containsKey(areaId)){
                    area = new Area();
                    area.setAreaId(cursor.getInt(cursor.getColumnIndex(KEY_AREA_ID)));
                    area.setCoordinates(new ArrayList<Coordinate>());
                    areas.put(areaId, area);
                }else{
                    area = areas.get(areaId);
                }

                area.getCoordinates().add(co);
            }while(cursor.moveToNext());
        }

        db.close();
        return new ArrayList<Area>(areas.values());
    }
}
