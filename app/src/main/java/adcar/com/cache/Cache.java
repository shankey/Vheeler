package adcar.com.cache;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adcar.com.database.dao.AdDAO;
import adcar.com.database.dao.AreaDAO;
import adcar.com.model.Ad;
import adcar.com.model.Area;

/**
 * Created by aditya on 02/02/16.
 */
public class Cache {

    public static Cache cache = new Cache();

    public List<Area> area;

    public Map<Integer, Ad> adMap;

    private Cache(){
    }

    public List<Area> getArea(){
        return area;
    }

    public Map<Integer, Ad> getAds(){
        return adMap;
    }

    public static  Cache getCache(){
        return cache;
    }

    public void initialize(Context context){
        AreaDAO areaDAO = new AreaDAO(context);
        area = areaDAO.getAreas();
        AdDAO adDAO = new AdDAO(context);
        adMap = adDAO.getAds();
    }
}
