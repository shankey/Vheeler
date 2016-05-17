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
import adcar.com.model.CampaignInfo;

/**
 * Created by adinema on 06/05/16.
 */
public class CampaignInfoDAO extends DAO {

    public static final String TABLE_CAMPAIGN_INFO = "campaign_info";

    public static String CREATE_CAMPAIGN_INFO_TABLE = "CREATE TABLE " + TABLE_CAMPAIGN_INFO + "("
            + KEY_ID + " INTEGER PRIMARY KEY," + KEY_CAMPAIGN_ID + " INTEGER,"
            + KEY_AD_ID + " INTEGER," + KEY_AREA_ID + " INTEGER," +
            KEY_VERSION + " INTEGER," + KEY_STATUS +  " INTEGER" +
            ")";

    public CampaignInfoDAO(Context context) {
        super(context);
    }

    public void saveOrUpdate(){

    }

    public void updateCampaignStatus(Integer campaignId, Integer areaId, Integer adId, Integer status){
        SQLiteDatabase db = dbHandler.getWritableDatabase();

        String sql = String.format("update " + CampaignInfoDAO.TABLE_CAMPAIGN_INFO + " set "+ KEY_STATUS + "=" + status + " where %s=%s and %s=%s and %s=%s", KEY_CAMPAIGN_ID, campaignId, KEY_AREA_ID, areaId, KEY_AD_ID, adId);
        db.execSQL(sql);
        db.close();

    }

    public void addCampaigns(List<CampaignInfo> campaigns){
        SQLiteDatabase db = dbHandler.getWritableDatabase();

        ContentValues values = new ContentValues();

        for(CampaignInfo campaign: campaigns){
            Log.i("DATABASE", campaign.toString());
            // Create a new map of values, where column names are the keys

            values.put(KEY_AD_ID, campaign.getAdId());
            values.put(KEY_AREA_ID, campaign.getAreaId());
            values.put(KEY_VERSION, campaign.getVersion());
            values.put(KEY_CAMPAIGN_ID, campaign.getCampaignId());
            values.put(KEY_STATUS, campaign.getStatus());

// Insert the new row, returning the primary key value of the new row
            long newRowId;
            newRowId = db.insert(TABLE_CAMPAIGN_INFO, null , values);
            Log.i("DATABASE", "row inserted campaign= " + newRowId);
        }

        db.close();

    }

    public void deleteCampaigns(List<CampaignInfo> campaigns){

        List<String> idList = new ArrayList<>();
        for(CampaignInfo campaign: campaigns){
            idList.add(campaign.getId().toString());
        }

        super.delete(idList, TABLE_CAMPAIGN_INFO);
    }

    public CampaignInfo getCampaign(String campaignId, String adId, String areaId){
        String sql = "campaignId=? and adId=? and areaId=?";
        String[] params = new String[]{campaignId, adId, areaId};
        List<CampaignInfo> li = getCampaigns(sql, params);

        if(li.size()>1){
            // throw exception
        }

        if(li.size()==0){
            return  null;
        }

        return li.get(0);
    }

    public ArrayList<CampaignInfo> getUnSyncedCampaigns(){
        String sql="status=?";
        String[] params = new String[] {"0"};
        return getCampaigns(sql, params);
    }

    public ArrayList<CampaignInfo> getCampaigns(String sql, String[] params){
        SQLiteDatabase db = dbHandler.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CAMPAIGN_INFO, new String[]{KEY_ID, KEY_CAMPAIGN_ID, KEY_AD_ID, KEY_AREA_ID, KEY_STATUS, KEY_VERSION}
                ,sql, params, null, null, null);
        Log.i("DATABASE", sql + " " + Arrays.toString(params));

        Log.i("DATABASE", cursor.getColumnNames().toString());

        ArrayList<CampaignInfo> campaigns = new ArrayList<CampaignInfo>();

        if(cursor.moveToFirst()){
            do{
                Log.i("DATABASE", cursor.getString(cursor.getColumnIndex(KEY_ID)));
                Log.i("DATABASE", cursor.getString(cursor.getColumnIndex(KEY_CAMPAIGN_ID)));
                Log.i("DATABASE", cursor.getString(cursor.getColumnIndex(KEY_AREA_ID)));
                Log.i("DATABASE", cursor.getString(cursor.getColumnIndex(KEY_AD_ID)));
                Log.i("DATABASE", cursor.getString(cursor.getColumnIndex(KEY_VERSION)));

                CampaignInfo campaign = new CampaignInfo();
                campaign.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                campaign.setCampaignId(cursor.getInt(cursor.getColumnIndex(KEY_CAMPAIGN_ID)));
                campaign.setAdId(cursor.getInt(cursor.getColumnIndex(KEY_AD_ID)));
                campaign.setAreaId(cursor.getInt(cursor.getColumnIndex(KEY_AREA_ID)));
                campaign.setStatus(cursor.getInt(cursor.getColumnIndex(KEY_STATUS)));
                campaign.setVersion(cursor.getInt(cursor.getColumnIndex(KEY_VERSION)));
                campaigns.add(campaign);
            }while(cursor.moveToNext());
        }
        db.close();
        Log.i("DATABASE", campaigns.toString());
        return  campaigns;
    }

    public List<CampaignInfo> getAdsForArea(Integer areaId){
        String sql = "areaId=?";
        String[] params = new String[]{areaId.toString()};

        return getCampaigns(sql, params);
    }

}
