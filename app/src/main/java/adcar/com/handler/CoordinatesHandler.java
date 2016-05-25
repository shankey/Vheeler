package adcar.com.handler;

import android.graphics.Bitmap;
import android.location.Location;
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
                        Log.i("RESPONSE", response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("RESPONSE", "inside first line onErrorResponse");
                        //saveCoordinatesToDatabase(coordinatesEntity);
                        Log.i("RESPONSE", error.toString());
                    }
                }
        );
        NetworkManager nm = (NetworkManager) Factory.getInstance().get(Factory.NETWORK_MANAGER);
        nm.addToRequestQueue(request);
    }

    private void saveCoordinatesToDatabase(CoordinatesEntity coordinatesEntity){
        if(coordinateDAO == null) {
            Log.i("RESPONSE", "NULL hai coordinateDAO");
            if (Factory.getInstance().get(Factory.DAO_COORDINATE) == null) {
                Log.i("RESPONSE", "NULL hai Factory coordinateDAO");
            } else{
                Log.i("RESPONSE", "NULL nahi hai Factory coordinateDAO");
            }
        }else{
            Log.i("RESPONSE", "NULL nahi hai coordinateDAO" + coordinateDAO);
        }
        coordinateDAO.addCoordinate(coordinatesEntity);
    }

    private void addCampaignRunToCache(Integer campaignInfoId, String time){
        ExhaustedCampaign cr = new ExhaustedCampaign();
        cr.setCampaignInfoId(campaignInfoId);
        cr.setDate(time);
        Cache.getCache().getCampaignRunSet().add(cr);
    }

    public void sendCoordinatesToServer(final CoordinatesEntity coordinatesEntity){
        Log.i("GPS", "inside first line sendCoordinatesToServer");
        Map<String, String> map = new HashMap<>();
        map.put("json", gson.toJson(coordinatesEntity));
        CustomStringRequest request = new CustomStringRequest(Request.Method.POST, null, map, UrlPaths.POST_COORDINATES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("RESPONSE", response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("RESPONSE", "inside first line onErrorResponse");
                        saveCoordinatesToDatabase(coordinatesEntity);
                        Log.i("RESPONSE", error.toString());
                    }
                }
        );
        NetworkManager nm = (NetworkManager) Factory.getInstance().get(Factory.NETWORK_MANAGER);
        nm.addToRequestQueue(request);
    }

    public void makeUseOfNewLocation(Location location) {
        Calendar calendar = Calendar.getInstance();
        String time = sdf.format(calendar.getTime()).toString();
        Log.i("GPS", " CoordinatesEntity = " + location.getLatitude() + " - " + location.getLongitude());

        Integer areaId = CoordinateAlgorithms.getInsideAreaId(location);
        CampaignInfo campaignInfo = AdAlgorithms.getAdIdToDisplay(areaId, time);
        Integer adId = null;
        Integer campaignInfoId = null;
        if(campaignInfo == null){
            adId = -1;
            campaignInfoId = 7;
        }else{
            adId = campaignInfo.getAdId();
            campaignInfoId=campaignInfo.getCampaignInfoId();
        }

        CoordinatesEntity coordinatesEntity = new CoordinatesEntity();
        Coordinate coordinate = new Coordinate(location.getLatitude(), location.getLongitude());
        coordinatesEntity.setCoordinate(coordinate);
        coordinatesEntity.setTimestamp(new Timestamp(calendar.getTime().getTime()));
        coordinatesEntity.setAreaId(areaId);
        coordinatesEntity.setAdId(adId);
        coordinatesEntity.setDeviceId(Cache.deviceId);
        coordinatesEntity.setCampaignInfoId(campaignInfoId);

        Log.i("GPS", "Calling sendCoordinateToSErver now");
        sendCoordinatesToServer(coordinatesEntity);

        addCampaignRunToCache(campaignInfoId, time);

        CampaignRun crExahusted = campaignRunDAO.getCampaignRunsFromInfoIdAndDate(campaignInfoId, time);
        if(crExahusted !=null){
            Log.i("GPS", "coordinate ad change = " + crExahusted.toString());
        }

        if(adId==-1 || Cache.LAST_AREA==null || Cache.LAST_AREA!=areaId || (crExahusted!=null && crExahusted.getActive()==0)){
            Bitmap bm = null;
            try{
                bm = Utility.getFromInternalStorage(adId);
                if(bm!=null){
                    Cache.LAST_AD = adId;
                    Cache.LAST_AREA = areaId;
                    MainActivity.getInstance().updateImageView(bm);
                }
                Log.i("GPS", "trying to update with " + Cache.LAST_AD + " " + bm);
                Toast.makeText(MainActivity.getInstance(), "Showing AdId = "+adId + " AreaId = " + areaId , Toast.LENGTH_LONG).show();

            }catch (Exception e){
                Log.i("VHEELER", "Could Not Retrieve Ad" + e.getStackTrace());
            }
        }
    }
}
