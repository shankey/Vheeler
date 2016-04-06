package adcar.com.factory;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.LocationManager;

import adcar.com.database.dao.AdDAO;
import adcar.com.database.dao.AreaDAO;
import adcar.com.database.dao.CoordinateDAO;
import adcar.com.gps.AndroidGpsListener;
import adcar.com.network.NetworkManager;

/**
 * Created by aditya on 06/02/16.
 */
public class Factory {

    public static Factory factory = new Factory();
    public static Context context = null;
    public static CoordinateDAO coordinateDAO = null;
    public static AreaDAO areaDAO = null;
    public static AdDAO adDAO = null;
    public static NetworkManager networkManager = null;
    public static SharedPreferences sharedPreferences = null;
    public static AndroidGpsListener androidGpsListener = null;


    public static String SHARED_PREFFERENCES_FILE = "Vheeler";

    public static Integer BASE_CONTEXT = 0;
    public static Integer DAO_COORDINATE = 1;
    public static Integer DAO_AREA = 2;
    public static Integer NETWORK_MANAGER=3;
    public static Integer SHARED_PREFERENCES=4;
    public static Integer DAO_AD = 5;
    public static Integer ANDROID_GPS_LISTENER = 6;


    private Factory(){
    }

    public void initialize(Context context){
        if(this.context == null){
            this.context = context;
            coordinateDAO = new CoordinateDAO(context);
            areaDAO = new AreaDAO(context);
            adDAO = new AdDAO(context);
            networkManager = NetworkManager.getInstance(context);
            sharedPreferences = context.getSharedPreferences(SHARED_PREFFERENCES_FILE, 0);
        }
    }

    public static Factory getInstance(){
        return factory;
    }

    public void set(Integer type, Object o){
        if(type == ANDROID_GPS_LISTENER){
            androidGpsListener = (AndroidGpsListener)o;
        }
    }

    public Object get(Integer i){

        if(i == BASE_CONTEXT){
            return context;
        }

        if(i == DAO_COORDINATE){
            return coordinateDAO;
        }

        if(i == DAO_AREA){
            return areaDAO;
        }

        if(i == NETWORK_MANAGER){
            return networkManager;
        }

        if(i == SHARED_PREFERENCES){
            return sharedPreferences;
        }

        if(i == DAO_AD){
            return adDAO;
        }

        if(i == ANDROID_GPS_LISTENER){
            return androidGpsListener;
        }

        return null;
    }
}
