package adcar.com.database.dao;

import android.content.Context;
import android.provider.ContactsContract;

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
    public static final String DEVICE_ID = "deviceId";

    private Context context = null;
    DatabaseHandler dbHandler = null;

    public DAO(Context context){
        this.context = context;
        this.dbHandler = new DatabaseHandler(this.context);
    }
}
