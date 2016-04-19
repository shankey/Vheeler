package adcar.com.handler;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adcar.com.factory.Factory;
import adcar.com.database.dao.CoordinateDAO;
import adcar.com.model.CoordinateBatchEntity;
import adcar.com.model.CoordinatesEntity;
import adcar.com.network.CustomStringRequest;
import adcar.com.network.NetworkManager;
import adcar.com.network.UrlPaths;

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
}
