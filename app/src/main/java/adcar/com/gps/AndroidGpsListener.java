package adcar.com.gps;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;

import adcar.com.adcar.MainActivity;
import adcar.com.adcar.SettingsActivity;
import adcar.com.database.dao.CoordinateDAO;
import adcar.com.factory.Factory;
import adcar.com.handler.CoordinatesHandler;

/**
 * Created by aditya on 04/04/16.
 */
public class AndroidGpsListener implements LocationListener {

    private MainActivity activity;
    private static SettingsActivity settingsActivity;
    LocationManager locationManager = null;
    Context context = null;

    public static CoordinateDAO coordinateDAO = (CoordinateDAO) Factory.getInstance().get(Factory.DAO_COORDINATE);
    public static CoordinatesHandler coordinatesHandler = new CoordinatesHandler();
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

    public static void setSettingsActivity(SettingsActivity activity){
        settingsActivity = activity;
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
            coordinatesHandler.makeUseOfNewLocation(getLocation());
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

    private void populateSettings(Location location){
        if(settingsActivity != null){
            settingsActivity.getLongitude().setText(""+ location.getLongitude());
            settingsActivity.getLatitude().setText("" + location.getLatitude());
        }
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



}
