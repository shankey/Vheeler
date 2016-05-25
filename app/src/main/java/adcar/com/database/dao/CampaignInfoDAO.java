package adcar.com.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import adcar.com.model.Ad;
import adcar.com.model.CampaignInfo;

/**
 * Created by adinema on 06/05/16.
 */
public class CampaignInfoDAO extends DAO {

    public static final String TABLE_CAMPAIGN_INFO = "campaign_info";

    public static String CREATE_CAMPAIGN_INFO_TABLE = "CREATE TABLE " + TABLE_CAMPAIGN_INFO + "("
            + KEY_ID + " INTEGER PRIMARY KEY," + KEY_CAMPAIGN_INFO_ID + " INTEGER,"
            + KEY_CAMPAIGN_ID + " INTEGER,"
            + KEY_AD_ID + " INTEGER," + KEY_AREA_ID + " INTEGER," +
            KEY_VERSION + " INTEGER," + KEY_STATUS +  " INTEGER," + KEY_ACTIVE + " INTEGER" +
            ")";

    public CampaignInfoDAO(Context context) {
        super(context);
    }

    public void saveOrUpdate(List<CampaignInfo> list){
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        for(CampaignInfo campaignInfo: list){
            CampaignInfo ci = getCampaign(campaignInfo.getCampaignInfoId().toString());
            if(ci == null){
                List<CampaignInfo> dbAdd = new ArrayList<CampaignInfo>();
                dbAdd.add(campaignInfo);
                addCampaigns(dbAdd);
            }else{
                update(campaignInfo);
            }
        }

        db.close();

    }

    public void update(CampaignInfo campaignInfo){
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        ContentValues values = convertCampaignInfoToContentValue(campaignInfo);
        String sql = "campaignInfoId=?";
        String[] params = new String[]{campaignInfo.getCampaignInfoId().toString()};
        db.update(TABLE_CAMPAIGN_INFO, values, sql, params);
        db.close();
    }

    public void updateCampaignStatus(Integer campaignInfoId, Integer status){
        SQLiteDatabase db = dbHandler.getWritableDatabase();

        String sql = String.format("update " + CampaignInfoDAO.TABLE_CAMPAIGN_INFO + " set "+ KEY_STATUS + "=" + status + " where %s=%s", KEY_CAMPAIGN_INFO_ID, campaignInfoId);
        db.execSQL(sql);
        db.close();

    }


    private ContentValues convertCampaignInfoToContentValue(CampaignInfo campaignInfo){

        ContentValues values = new ContentValues();
        values.put(KEY_AD_ID, campaignInfo.getAdId());
        values.put(KEY_CAMPAIGN_INFO_ID, campaignInfo.getCampaignInfoId());
        values.put(KEY_ACTIVE, campaignInfo.getActive());
        values.put(KEY_AREA_ID, campaignInfo.getAreaId());
        values.put(KEY_VERSION, campaignInfo.getVersion());
        values.put(KEY_CAMPAIGN_ID, campaignInfo.getCampaignId());
        values.put(KEY_STATUS, campaignInfo.getStatus());

        return values;
    }

    public void addCampaigns(List<CampaignInfo> campaigns){
        SQLiteDatabase db = dbHandler.getWritableDatabase();

        for(CampaignInfo campaign: campaigns){
            Log.i("DATABASE", campaign.toString());
            // Create a new map of values, where column names are the keys

            ContentValues values = convertCampaignInfoToContentValue(campaign);

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

    public CampaignInfo getCampaign(String campaignInfoId){
        String sql = "campaignInfoId=?";
        String[] params = new String[]{campaignInfoId};
        List<CampaignInfo> li = getCampaigns(sql, params);

        if(li.size()>1){
            // throw exception
        }

        if(li.size()==0){
            return  null;
        }

        return li.get(0);
    }

    public List<CampaignInfo> getUnSyncedCampaigns(){
        String sql="status=?";
        String[] params = new String[] {"0"};
        return getCampaigns(sql, params);
    }

    public List<CampaignInfo> getCampaigns(String sql, String[] params){
        SQLiteDatabase db = dbHandler.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CAMPAIGN_INFO, new String[]{KEY_ID, KEY_CAMPAIGN_INFO_ID , KEY_CAMPAIGN_ID, KEY_AD_ID, KEY_AREA_ID, KEY_STATUS, KEY_VERSION}
                ,sql, params, null, null, null);
        Log.i("DATABASE", sql + " " + Arrays.toString(params));

        Log.i("DATABASE", cursor.getColumnNames().toString());

        List<CampaignInfo> campaigns = getCampaignInfoFromCursor(cursor);
        db.close();
        Log.i("DATABASE", campaigns.toString());
        return  campaigns;
    }

    public List<CampaignInfo> getAdsForArea(Integer areaId, String time){
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        String sql = "SELECT * FROM "+ TABLE_CAMPAIGN_INFO +" a INNER JOIN "+ CampaignRunDAO.TABLE_CAMPAIGN_RUN +" b ON a.campaignInfoId=b.campaignInfoId WHERE a.areaId=? and b.date=? and b.active=1 order by a.id";

        String[] params = new String[]{areaId.toString(), time};
        Cursor cursor = db.rawQuery(sql, params);
        List<CampaignInfo> campaignInfoList = getCampaignInfoFromCursor(cursor);
        Log.i("ADSHOW", time);
        Log.i("ADSHOW", campaignInfoList.toString());
        db.close();

        return campaignInfoList;
    }

    private List<CampaignInfo> getCampaignInfoFromCursor(Cursor cursor){
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
                campaign.setCampaignInfoId(cursor.getInt(cursor.getColumnIndex(KEY_CAMPAIGN_INFO_ID)));
                campaign.setCampaignId(cursor.getInt(cursor.getColumnIndex(KEY_CAMPAIGN_ID)));
                campaign.setAdId(cursor.getInt(cursor.getColumnIndex(KEY_AD_ID)));
                campaign.setAreaId(cursor.getInt(cursor.getColumnIndex(KEY_AREA_ID)));
                campaign.setStatus(cursor.getInt(cursor.getColumnIndex(KEY_STATUS)));
                campaign.setVersion(cursor.getInt(cursor.getColumnIndex(KEY_VERSION)));
                campaigns.add(campaign);
            }while(cursor.moveToNext());
        }

        return campaigns;
    }

}
