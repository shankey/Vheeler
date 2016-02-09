package adcar.com.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import adcar.com.database.dao.AdDAO;
import adcar.com.database.dao.AreaDAO;
import adcar.com.database.dao.CoordinateDAO;

/**
 * Created by aditya on 25/01/16.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "vheeler";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CoordinateDAO.CREATE_COORDINATES_TABLE);
        db.execSQL(AreaDAO.CREATE_AREAS_TABLE);
        db.execSQL(AdDAO.CREATE_ADS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

}
