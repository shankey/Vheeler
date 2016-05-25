package adcar.com.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import adcar.com.model.CampaignRun;

/**
 * Created by adinema on 06/05/16.
 */
public class CampaignRunDAO extends DAO {

    public CampaignRunDAO(Context context) {
        super(context);
    }

        public static final String TABLE_CAMPAIGN_RUN = "campaign_run";

        public static String CREATE_TABLE_CAMPAIGN_RUN = "CREATE TABLE " + TABLE_CAMPAIGN_RUN + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_CAMPAIGN_INFO_ID + " INTEGER,"
                + KEY_DATE + " DATETIME,"
                + KEY_ACTIVE + " INTEGER" +
                ")";

        public void addCampaignRun(CampaignRun campaignRun){
            List<CampaignRun> campaignRuns = new ArrayList<CampaignRun>();
            campaignRuns.add(campaignRun);
            addCampaignRuns(campaignRuns);
        }


        public void addCampaignRuns(List<CampaignRun> campaignRuns){
        SQLiteDatabase db = dbHandler.getWritableDatabase();

        ContentValues values = new ContentValues();

        for(CampaignRun campaign: campaignRuns){
            Log.i("DATABASE", campaign.toString());
            // Create a new map of values, where column names are the keys

            values.put(KEY_CAMPAIGN_INFO_ID, campaign.getCampaignInfoId());
            values.put(KEY_DATE, campaign.getDate().toString());
            values.put(KEY_ACTIVE, 1);

// Insert the new row, returning the primary key value of the new row
            long newRowId;
            newRowId = db.insert(TABLE_CAMPAIGN_RUN, null , values);
            Log.i("DATABASE", "row inserted = " + newRowId);
        }

        db.close();

    }

    public void deleteCampaignRuns(List<CampaignRun> campaignRuns){

        List<String> idList = new ArrayList<>();
        for(CampaignRun campaignRun: campaignRuns){
            idList.add(campaignRun.getId().toString());
        }

        super.delete(idList, TABLE_CAMPAIGN_RUN);
    }

    public void deleteCampaignRunsForCampaignInfo(Integer campaignInfoId){
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        Log.i("SYNCHANDLER", "Delelting campaign run = " + campaignInfoId);
        db.execSQL(String.format("DELETE FROM "+ TABLE_CAMPAIGN_RUN +" WHERE "+KEY_CAMPAIGN_INFO_ID+ " in (select id from " + CampaignInfoDAO.TABLE_CAMPAIGN_INFO + " where " + KEY_CAMPAIGN_INFO_ID + "=%s);", campaignInfoId));
        db.close();
    }

    public List<CampaignRun> getCampaignRunsFromInfoId(Integer campaignInfoId){
        String sql = KEY_CAMPAIGN_INFO_ID+"=?";
        String[] params = new String[] {campaignInfoId.toString()};

        return getCampaignRuns(sql, params);
    }

    public ArrayList<CampaignRun> getCampaignRuns(String sql, String[] params){
        SQLiteDatabase db = dbHandler.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CAMPAIGN_RUN, new String[]{KEY_ID, KEY_CAMPAIGN_INFO_ID, KEY_DATE, KEY_ACTIVE}
                ,sql, params, null, null, null);
        Log.i("DATABASE", cursor.getColumnNames().toString());

        ArrayList<CampaignRun> campaignRuns = new ArrayList<CampaignRun>();

        if(cursor.moveToFirst()){
            do{
                Log.i("DATABASE", cursor.getString(cursor.getColumnIndex(KEY_ID)));
                Log.i("DATABASE", cursor.getString(cursor.getColumnIndex(KEY_CAMPAIGN_INFO_ID)));
                Log.i("DATABASE", cursor.getString(cursor.getColumnIndex(KEY_DATE)));
                Log.i("DATABASE", cursor.getString(cursor.getColumnIndex(KEY_ACTIVE)));


                CampaignRun campaignRun = new CampaignRun();
                campaignRun.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                campaignRun.setCampaignInfoId(cursor.getInt(cursor.getColumnIndex(KEY_CAMPAIGN_INFO_ID)));
                campaignRun.setDate((cursor.getString(cursor.getColumnIndex(KEY_DATE))));
                campaignRun.setActive(cursor.getInt(cursor.getColumnIndex(KEY_ACTIVE)));
                campaignRuns.add(campaignRun);
            }while(cursor.moveToNext());
        }
        db.close();
        return  campaignRuns;

    }

    public void updateCampaignRuns(Integer campaignInfoId, String date, Integer active){
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        Log.i("DATABASE", "Updating campaign run for status = " + campaignInfoId + " " + date);
        db.execSQL(String.format("UPDATE "+ TABLE_CAMPAIGN_RUN +" set active=%s "+" WHERE ("+KEY_CAMPAIGN_INFO_ID+ "=%s " + " and " + KEY_DATE + "='%s');", active, campaignInfoId, date));
        Log.i("EXHAUSTEDRUN", String.format("UPDATE "+ TABLE_CAMPAIGN_RUN +" set active=%s "+" WHERE ("+KEY_CAMPAIGN_INFO_ID+ "=%s " + " and " + KEY_DATE + "=%s);", active, campaignInfoId, date));
        db.close();
    }

    public CampaignRun getCampaignRunsFromInfoIdAndDate(Integer campaignInfoId, String date){

        String sql = KEY_CAMPAIGN_INFO_ID + "=? and "+KEY_DATE + "=?";
        String[] params = new String[] {campaignInfoId.toString(), date};

        List<CampaignRun> list = getCampaignRuns(sql, params);
        if(list == null || list.size()==0){
            return null;
        }else{
            return list.get(0);
        }
    }
}
