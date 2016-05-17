package adcar.com.cache;

import android.content.Context;

import java.util.Map;

import adcar.com.database.dao.AreaDAO;
import adcar.com.model.Ad;
import adcar.com.model.servertalkers.Areas;
import adcar.com.utility.Utility;

/**
 * Created by aditya on 02/02/16.
 */
public class Cache {

    public static Cache cache = new Cache();

    public Areas areas;

    public Map<Integer, Ad> adMap;

    public static Integer LAST_AD = null;

    public static String deviceId = Utility.getDeviceId();

    private Cache(){
    }

    public Areas getAreas(){
        return areas;
    }



    public static  Cache getCache(){
        return cache;
    }

    public void initialize(Context context){
        AreaDAO areaDAO = new AreaDAO(context);
        areas = new Areas();
        areas.setAreas(areaDAO.getAreas());
    }

    public static void setCache(Cache cache) {
        Cache.cache = cache;
    }

    public void setArea(Areas areas) {
        this.areas = areas;
    }

    public void setAdMap(Map<Integer, Ad> adMap) {
        this.adMap = adMap;
    }
}
