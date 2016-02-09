package adcar.com.handler;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import org.json.JSONObject;

import java.util.List;

import adcar.com.factory.Factory;
import adcar.com.database.dao.CoordinateDAO;
import adcar.com.model.CoordinatesEntity;
import adcar.com.network.NetworkManager;
import adcar.com.network.UrlPaths;

/**
 * Created by aditya on 06/02/16.
 */
public class CoordinatesHandler {

    public static Gson gson = new GsonBuilder().create();
    public static CoordinateDAO coordinateDAO = (CoordinateDAO)Factory.getInstance().get(Factory.DAO_COORDINATE);

    public void sendCoordinatesToServer(final List<CoordinatesEntity> li){

        String jsonCoordinateRequest = gson.toJson(li);
        JSONObject jsonObject = null;
        String jsonRequest = gson.toJson(li);
        Log.i("RESPONSE", jsonRequest);
            JsonObjectRequest jor = new JsonObjectRequest(Request.Method.POST, UrlPaths.POST_COORDINATES,
                    jsonRequest,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.i("RESPONSE", "Success - sendCoordinateResponse " + response.toString());
                            coordinateDAO.deleteCoordinate(li);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i("RESPONSE", "Failure - sendCoordinateResponse " + error.toString());
                        }
                    }
            );
        NetworkManager nm = (NetworkManager)Factory.getInstance().get(Factory.NETWORK_MANAGER);
        nm.addToRequestQueue(jor);
    }
}
