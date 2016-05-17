package adcar.com.database.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import java.util.List;

import adcar.com.database.DatabaseHandler;

/**
 * Created by aditya on 02/02/16.
 */
public class DAO {

    public static final String KEY_ID = "id";
    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_LONGITUDE = "longitude";
    public static final String KEY_TIMESTAMP = "timestamp";
    public static final String KEY_AREA_ID = "areaId";
    public static final String KEY_AD_URL = "adlocation";
    public static final String KEY_STATUS = "status";
    public static final String KEY_AD_ID = "adId";
    public static final String KEY_DEVICE_ID = "deviceId";
    public static final String KEY_CAMPAIGN_ID = "campaignId";
    public static final String KEY_VERSION = "version";
    public static final String KEY_CAMPAIGN_INFO_ID = "campaignInfoId";
    public static final String KEY_ACTIVE = "active";
    public static final String KEY_DATE = "date";



    private Context context = null;
    DatabaseHandler dbHandler = null;

    public DAO(Context context){
        this.context = context;
        this.dbHandler = new DatabaseHandler(this.context);
    }

    public void delete(List<String> idList, String table){
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        String args = TextUtils.join(", ", idList);
        db.execSQL(String.format("DELETE FROM "+ table +" WHERE id IN (%s);", args));
        db.close();
    }
}
