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
import adcar.com.factory.Factory;
import adcar.com.database.dao.CoordinateDAO;
import adcar.com.model.CoordinatesEntity;
import adcar.com.model.servertalkers.Coordinate;
import adcar.com.model.servertalkers.CoordinateBatchEntity;
import adcar.com.network.CustomStringRequest;
import adcar.com.network.NetworkManager;
import adcar.com.network.UrlPaths;
import adcar.com.utility.Utility;

/**
 * Created by aditya on 06/02/16.
 */
public class CoordinatesHandler extends Handler {

    public static CoordinateDAO coordinateDAO = (CoordinateDAO)Factory.getInstance().get(Factory.DAO_COORDINATE);

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
        Integer adId = AdAlgorithms.getAdIdToDisplay(areaId);

        CoordinatesEntity coordinatesEntity = new CoordinatesEntity();
        Coordinate coordinate = new Coordinate(location.getLatitude(), location.getLongitude());
        coordinatesEntity.setCoordinate(coordinate);
        coordinatesEntity.setTimestamp(new Timestamp(calendar.getTime().getTime()));
        coordinatesEntity.setAreaId(areaId);
        coordinatesEntity.setAdId(areaId);
        coordinatesEntity.setDeviceId(Cache.deviceId);


        Log.i("GPS", "Calling sendCoordinateToSErver now");
        sendCoordinatesToServer(coordinatesEntity);


        if(Cache.LAST_AD==null || Cache.LAST_AD!=adId){
            Bitmap bm = null;
            try{
                bm = Utility.getFromInternalStorage(adId);
                Log.i("GPS", "trying to update with " + adId + " " + bm);
                Toast.makeText(MainActivity.getInstance(), "Showing Ad = "+adId , Toast.LENGTH_LONG).show();
                if(bm!=null){
                    Cache.LAST_AD = adId;
                    MainActivity.getInstance().updateImageView(bm);
                }

            }catch (Exception e){
                Log.i("VHEELER", "Could Not Retrieve Ad" + e.getStackTrace());
            }
        }
    }
}
