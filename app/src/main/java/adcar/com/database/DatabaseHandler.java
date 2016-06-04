package adcar.com.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import adcar.com.database.dao.AdDAO;
import adcar.com.database.dao.AreaDAO;
import adcar.com.database.dao.CampaignInfoDAO;
import adcar.com.database.dao.CampaignRunDAO;
import adcar.com.database.dao.CoordinateDAO;

/**
 * Created by aditya on 25/01/16.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;

    // Database Name
    private static final String DATABASE_NAME = "vheeler";
    private static final String BETA_DATABASE_NAME = "/sdcard/vheeler.db";

    private static DatabaseHandler dbHandler=null;

    public static DatabaseHandler getDbHandler(Context context){
        if(dbHandler == null){
            dbHandler = new DatabaseHandler(context);
        }
        return dbHandler;
    }

    private DatabaseHandler(Context context) {
        super(context, BETA_DATABASE_NAME, null, DATABASE_VERSION);
        Log.i("DATABASE", "path = "+context.getExternalFilesDir(null).getAbsolutePath() + "/" +DATABASE_NAME);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CoordinateDAO.CREATE_COORDINATES_TABLE);
        db.execSQL(AreaDAO.CREATE_AREAS_TABLE);
        db.execSQL(AdDAO.CREATE_ADS_TABLE);
        db.execSQL(CampaignInfoDAO.CREATE_CAMPAIGN_INFO_TABLE);
        db.execSQL(CampaignRunDAO.CREATE_TABLE_CAMPAIGN_RUN);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("UPGRADE", "oldversion is" + oldVersion);
        switch(oldVersion) {

            case 1:
                db.execSQL(CoordinateDAO.ALTER_COORDINATES_TABLE_AD_ID_1);
                db.execSQL(CoordinateDAO.ALTER_COORDINATES_TABLE_AREA_ID_1);
            case 2:

            case 3:
                break;
            default:
                throw new IllegalStateException(
                        "onUpgrade() with unknown oldVersion " + oldVersion);
        }
    }

}
