package adcar.com.gps;

import android.app.Activity;
import android.location.Location;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.location.LocationListener;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import adcar.com.adcar.MainActivity;
import adcar.com.coordinates.CoordinateAlgorithms;
import adcar.com.database.dao.CoordinateDAO;
import adcar.com.factory.Factory;
import adcar.com.model.Coordinate;
import adcar.com.model.CoordinatesEntity;
import adcar.com.network.CustomStringRequest;
import adcar.com.network.NetworkManager;
import adcar.com.network.UrlPaths;

/**
 * Created by aditya on 18/01/16.
 */
public class GPSListener implements LocationListener {

    private Activity activity;
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy MMM dd HH:mm:ss");
    public static CoordinateDAO coordinateDAO = (CoordinateDAO) Factory.getInstance().get(Factory.DAO_COORDINATE);

    public GPSListener(Activity activity){
        this.activity = activity;

    }

    @Override
    public void onLocationChanged(Location location) {
        makeUseOfNewLocation(location);
        Log.i("GPS", "value of coordinatedao = "+coordinateDAO);
    }

    private void makeUseOfNewLocation(Location location) {
        Calendar calendar = Calendar.getInstance();
        String time = sdf.format(calendar.getTime()).toString();
        Log.i("GPS", " CoordinatesEntity = " + location.getLatitude() + " - " + location.getLongitude());
        MainActivity ui = (MainActivity)activity;
        ui.getLatitude().setText("" + location.getLatitude());
        ui.getLongitude().setText("" + location.getLongitude());
        ui.getTime().setText(time);
        ui.getArea().setText(CoordinateAlgorithms.getInsideAreaId(location));
        Log.i("GPS", "Calling UI ended");
        CoordinatesEntity coordinatesEntity = new CoordinatesEntity();
        Coordinate coordinate = new Coordinate(location.getLatitude(), location.getLongitude());
        coordinatesEntity.setCoordinate(coordinate);
        coordinatesEntity.setTimestamp(new Timestamp(calendar.getTime().getTime()));
        Log.i("GPS", "Calling sendCoordinateToSErver now");
        sendCoordinatesToServer(coordinatesEntity);

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
        CustomStringRequest request = new CustomStringRequest(Request.Method.POST, null, null, UrlPaths.SCHEME_HTTP_TYPE + UrlPaths.BASE_URL_VHEELER,
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
