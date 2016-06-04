package adcar.com.cache;

import android.content.Context;
import android.graphics.Bitmap;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import adcar.com.database.dao.AreaDAO;
import adcar.com.model.Ad;
import adcar.com.model.servertalkers.Areas;
import adcar.com.model.servertalkers.ExhaustedCampaign;
import adcar.com.utility.Utility;

/**
 * Created by aditya on 02/02/16.
 */
public class Cache {

    public static Cache cache = new Cache();

    public Areas areas;

    public Map<Integer, Ad> adMap;

    public static Integer LAST_AD = null;

    public static Integer LAST_AREA = null;

    public static Integer LAST_CAMPAIGN_INFO_ID = null;

    public static String deviceId = Utility.getDeviceId();

    public Map<Integer, Bitmap> imageMap = new HashMap<Integer, Bitmap>();

    public Set<ExhaustedCampaign> campaignRunSet = new HashSet<ExhaustedCampaign>();

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

    public Set<ExhaustedCampaign> getCampaignRunSet() {
        return campaignRunSet;
    }

    public void setCampaignRunSet(Set<ExhaustedCampaign> campaignRunSet) {
        this.campaignRunSet = campaignRunSet;
    }

    public Map<Integer, Bitmap> getImageMap() {
        return imageMap;
    }

    public void setImageMap(Map imageMap) {
        this.imageMap = imageMap;
    }
}
