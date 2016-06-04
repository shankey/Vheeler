package adcar.com.handler;

import android.graphics.Bitmap;
import android.location.Location;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;


import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import adcar.com.adcar.MainActivity;
import adcar.com.algorithms.AdAlgorithms;
import adcar.com.cache.Cache;
import adcar.com.algorithms.CoordinateAlgorithms;
import adcar.com.database.dao.CampaignRunDAO;
import adcar.com.factory.Factory;
import adcar.com.database.dao.CoordinateDAO;
import adcar.com.model.CampaignInfo;
import adcar.com.model.CampaignRun;
import adcar.com.model.CoordinatesEntity;
import adcar.com.model.servertalkers.Coordinate;
import adcar.com.model.servertalkers.CoordinateBatchEntity;
import adcar.com.model.servertalkers.ExhaustedCampaign;
import adcar.com.network.CustomStringRequest;
import adcar.com.network.NetworkManager;
import adcar.com.network.UrlPaths;
import adcar.com.utility.Strings;
import adcar.com.utility.Utility;

/**
 * Created by aditya on 06/02/16.
 */
public class CoordinatesHandler extends Handler {

    public static CoordinateDAO coordinateDAO = (CoordinateDAO)Factory.getInstance().get(Factory.DAO_COORDINATE);
    public static CampaignRunDAO campaignRunDAO = (CampaignRunDAO)Factory.getInstance().get(Factory.DAO_CAMPAIGN_RUN);

    public void sendCoordinatesToServer(final CoordinateBatchEntity coordinateBatchEntity){

        Map<String, String> map = new HashMap<>();
        map.put("json", gson.toJson(coordinateBatchEntity));
        CustomStringRequest request = new CustomStringRequest(Request.Method.POST, null, map, UrlPaths.POST_COORDINATES_BATCH,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        coordinateDAO.deleteCoordinate(coordinateBatchEntity.getLi());
                        Log.i("GPSPROCESSOR", response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("GPSPROCESSOR", "inside first line onErrorResponse");
                        //saveCoordinatesToDatabase(coordinatesEntity);
                        Log.i("GPSPROCESSOR", error.toString());
                    }
                }
        );
        NetworkManager nm = (NetworkManager) Factory.getInstance().get(Factory.NETWORK_MANAGER);
        nm.addToRequestQueue(request);
    }

    private void saveCoordinatesToDatabase(CoordinatesEntity coordinatesEntity){
        coordinateDAO.addCoordinate(coordinatesEntity);
    }

    private void addCampaignRunToCache(Integer campaignInfoId, String time){
        ExhaustedCampaign cr = new ExhaustedCampaign();
        cr.setCampaignInfoId(campaignInfoId);
        cr.setDate(time);
        Cache.getCache().getCampaignRunSet().add(cr);
    }

    public void sendCoordinatesToServer(final CoordinatesEntity coordinatesEntity){
        Log.i("GPSPROCESSOR", "inside first line sendCoordinatesToServer");
        Map<String, String> map = new HashMap<>();
        map.put("json", gson.toJson(coordinatesEntity));
        CustomStringRequest request = new CustomStringRequest(Request.Method.POST, null, map, UrlPaths.POST_COORDINATES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("GPSPROCESSOR", response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("GPSPROCESSOR", "inside first line onErrorResponse");
                        saveCoordinatesToDatabase(coordinatesEntity);
                        Log.i("GPSPROCESSOR", error.toString());
                    }
                }
        );
        NetworkManager nm = (NetworkManager) Factory.getInstance().get(Factory.NETWORK_MANAGER);
        nm.addToRequestQueue(request);
    }

    public void makeUseOfNewLocation(Location location) {
        Calendar calendar = Calendar.getInstance();
        Timestamp tTime = new Timestamp(calendar.getTime().getTime());
        String time = sdf.format(tTime).toString();

        Log.i("GPSPROCESSOR", " insdie makeUseOfNewLocation " + location.getLatitude() + " - " + location.getLongitude());

        Integer areaId = CoordinateAlgorithms.getInsideAreaId(location);
        CampaignInfo campaignInfo = AdAlgorithms.getAdIdToDisplay(areaId, time);
        Integer adId = null;
        Integer campaignInfoId = null;
        if(campaignInfo == null){
            adId = 7;
            campaignInfoId = 68;
        }else{
            adId = campaignInfo.getAdId();
            campaignInfoId=campaignInfo.getCampaignInfoId();
        }

        CampaignRun crExahusted = campaignRunDAO.getCampaignRunsFromInfoIdAndDate(Cache.LAST_CAMPAIGN_INFO_ID, time);
        if(crExahusted !=null){
            Log.i("GPSPROCESSOR", "coordinate ad change = " + crExahusted.toString());
        }

        Log.i("GPSPROCESSOR", String.format("trying to change display ad with adId=%s areaId=%s campaignInfoId=%s ", adId, areaId, campaignInfoId));
        Log.i("GPSPROCESSOR", String.format("previous ad was adId=%s areaId=%s campaignInfoId=%s ", Cache.LAST_AD, Cache.LAST_AREA, Cache.LAST_CAMPAIGN_INFO_ID));

        Bitmap bm = null;
        if((adId==7 || Cache.LAST_AREA==null || Cache.LAST_AREA!=areaId || Cache.LAST_CAMPAIGN_INFO_ID==null || Cache.LAST_CAMPAIGN_INFO_ID!=campaignInfoId) || (crExahusted==null || crExahusted.getActive()==0)){

            try{
                bm = Utility.getFromInternalStorage(adId);

                if(bm!=null){
                    Cache.LAST_AD = adId;
                    Cache.LAST_AREA = areaId;
                    Cache.LAST_CAMPAIGN_INFO_ID = campaignInfoId;
                    Utility.sendMessageToHandler(MainActivity.getHandler(), Strings.IMAGE_UPDATE, bm);
                }else{
                    Log.i("GPSPROCESSOR", "Ad Image is null = " + bm);
                }

                String obj = "Showing AdId = "+adId + " AreaId = " + areaId + "campaignInfoId = " + campaignInfoId;
                Utility.sendMessageToHandler(MainActivity.getHandler(), Strings.TOAST, obj);

            }catch (Exception e){
                Log.e("GPSPROCESSOR", "Could Not Retrieve Ad" + e.getStackTrace());
            }
        }

        if(!(bm==null || Cache.LAST_AD==null || Cache.LAST_AREA==null || Cache.LAST_CAMPAIGN_INFO_ID==null)){
            CoordinatesEntity coordinatesEntity = new CoordinatesEntity();
            Coordinate coordinate = new Coordinate(location.getLatitude(), location.getLongitude());
            coordinatesEntity.setCoordinate(coordinate);
            coordinatesEntity.setTimestamp(tTime);
            coordinatesEntity.setAreaId(Cache.LAST_AREA);
            coordinatesEntity.setAdId(Cache.LAST_AD);
            coordinatesEntity.setDeviceId(Cache.deviceId);
            coordinatesEntity.setCampaignInfoId(Cache.LAST_CAMPAIGN_INFO_ID);

            Log.i("GPSPROCESSOR", "Calling sendCoordinateToSErver now with = " + coordinatesEntity.toString());
            sendCoordinatesToServer(coordinatesEntity);
            addCampaignRunToCache(Cache.LAST_CAMPAIGN_INFO_ID, time);
        }
    }
}
