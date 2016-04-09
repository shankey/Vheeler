package adcar.com.gps;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import adcar.com.adcar.MainActivity;
import adcar.com.cache.Cache;
import adcar.com.coordinates.CoordinateAlgorithms;
import adcar.com.database.dao.CoordinateDAO;
import adcar.com.factory.Factory;
import adcar.com.model.Coordinate;
import adcar.com.model.CoordinatesEntity;
import adcar.com.network.CustomStringRequest;
import adcar.com.network.NetworkManager;
import adcar.com.network.UrlPaths;
import adcar.com.utility.Utility;

/**
 * Created by aditya on 04/04/16.
 */
public class AndroidGpsListener implements LocationListener {

    private MainActivity activity;
    LocationManager locationManager = null;
    Context context = null;
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy MMM dd HH:mm:ss");
    public static CoordinateDAO coordinateDAO = (CoordinateDAO) Factory.getInstance().get(Factory.DAO_COORDINATE);
    Gson gson = new GsonBuilder().create();
    public static Long lastUpdated = null;

    public AndroidGpsListener(MainActivity locationListener, LocationManager locationManager, Context context) {
        Log.i("GPS", "inside AndroidGpsListener");
        this.context = context;
        this.locationManager = locationManager;
        this.activity = locationListener;
    }

    public LocationManager getLocationManager() {
        return locationManager;
    }


    @Override
    public void onLocationChanged(Location location) {
        Date last = new Date(location.getTime());
        Log.i("GPS", "location is - " + location.getLatitude() + " " + location.getLongitude() + " " + location.getProvider() + " " + location.getTime() + " " + location.getAccuracy());
        Log.i("GPS"," lastupdated " + lastUpdated + " " + location.getTime() );

        if(lastUpdated!=null){
            Log.i("GPS", ""+ (location.getTime() - lastUpdated));
        }

        if(lastUpdated==null || ((location.getTime() - lastUpdated)/1000 > 15)) {
            makeUseOfNewLocation(getLocation());
            lastUpdated = location.getTime();
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    public void enableLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            Log.i("GPS", "permissions not granted");
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Log.i("GPS", "permissions granted");
        this.locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                5000, 0, this);
        this.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                5000, 0, this);
    }

    public void disableLocationUpdates() {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        this.locationManager.removeUpdates(this);

    }

    public Location getLocation() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null;
        }
        Location networkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        Location gpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if(gpsLocation == null){
            return networkLocation;
        }

        if(networkLocation == null){
            return gpsLocation;
        }

        if((Math.abs(gpsLocation.getTime() - networkLocation.getTime())/1000) < 5){
            if(gpsLocation.getAccuracy() < networkLocation.getAccuracy()){
                return gpsLocation;
            }else{
                return networkLocation;
            }
        }else{
            if(gpsLocation.getTime() > networkLocation.getTime()){
                return gpsLocation;
            }else{
                return networkLocation;
            }
        }
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
            Bitmap bm = null;
            try{
                bm = Utility.getFromInternalStorage(areaId);
                Log.i("GPS", "trying to update with " + areaId + " " + bm);
                Toast.makeText(activity, "Showing Ad = "+areaId , Toast.LENGTH_LONG).show();
                if(bm!=null){
                    Cache.LAST_AD = areaId;
                    MainActivity.getInstance().updateImageView(bm);
                }


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
