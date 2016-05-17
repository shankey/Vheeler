package adcar.com.gps;

import android.app.Activity;
import android.graphics.Bitmap;
import android.location.Location;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.location.LocationListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import adcar.com.adcar.MainActivity;
import adcar.com.cache.Cache;
import adcar.com.algorithms.CoordinateAlgorithms;
import adcar.com.database.dao.CoordinateDAO;
import adcar.com.factory.Factory;
import adcar.com.model.servertalkers.Coordinate;
import adcar.com.model.CoordinatesEntity;
import adcar.com.network.CustomStringRequest;
import adcar.com.network.NetworkManager;
import adcar.com.network.UrlPaths;
import adcar.com.utility.Utility;

/**
 * Created by aditya on 18/01/16.
 */
public class GPSListener implements LocationListener {

    private Activity activity;
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy MMM dd HH:mm:ss");
    public static CoordinateDAO coordinateDAO = (CoordinateDAO) Factory.getInstance().get(Factory.DAO_COORDINATE);
    Gson gson = new GsonBuilder().create();
    public GPSListener(Activity activity){
        this.activity = activity;

    }

    @Override
    public void onLocationChanged(Location location) {
        makeUseOfNewLocation(location);
        Log.i("GPS", "value of coordinatedao = " + coordinateDAO);
        Log.i("GPS", "value of coordinates = "+location.getLatitude() + " - " + location.getLongitude());
    }

    private void makeUseOfNewLocation(Location location) {
        Calendar calendar = Calendar.getInstance();
        String time = sdf.format(calendar.getTime()).toString();
        Log.i("GPS", " CoordinatesEntity = " + location.getLatitude() + " - " + location.getLongitude());
        MainActivity ui = (MainActivity)activity;
//        ui.getLatitude().setText("" + location.getLatitude());
//        ui.getLongitude().setText("" + location.getLongitude());
//        ui.getTime().setText(time);
//        ui.getArea().setText(String.valueOf(CoordinateAlgorithms.getInsideAreaId(location)));
        Log.i("GPS", "Calling UI ended");

        Integer areaId = CoordinateAlgorithms.getInsideAreaId(location);

        CoordinatesEntity coordinatesEntity = new CoordinatesEntity();
        Coordinate coordinate = new Coordinate(location.getLatitude(), location.getLongitude());
        coordinatesEntity.setCoordinate(coordinate);
        coordinatesEntity.setTimestamp(new Timestamp(calendar.getTime().getTime()));
        coordinatesEntity.setAreaId(areaId);
        coordinatesEntity.setAdId(areaId);
        coordinatesEntity.setDeviceId(Cache.deviceId);


        Log.i("GPS", "Calling sendCoordinateToSErver now");
        sendCoordinatesToServer(coordinatesEntity);


        if(Cache.LAST_AD==null || Cache.LAST_AD!=areaId){
            Cache.LAST_AD=areaId;
            Bitmap bm = null;
            try{
                bm = Utility.getFromInternalStorage(areaId);
                ui.getAd_main().setImageBitmap(bm);
            }catch (Exception e){
                Log.i("VHEELER", "Could Not Retrieve Ad" + e.getStackTrace());
            }
        }

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

    private void sendCoordinatesToServer(final CoordinatesEntity coordinatesEntity){
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
}
