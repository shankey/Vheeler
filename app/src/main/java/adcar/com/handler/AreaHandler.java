package adcar.com.handler;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import adcar.com.factory.Factory;
import adcar.com.database.dao.AreaDAO;
import adcar.com.model.Area;
import adcar.com.model.Areas;
import adcar.com.network.CustomStringRequest;
import adcar.com.network.NetworkManager;
import adcar.com.network.UrlPaths;
import adcar.com.utility.Strings;
import adcar.com.utility.Utility;

/**
 * Created by aditya on 07/02/16.
 */
public class AreaHandler {

    public static AreaDAO areaDAO = (AreaDAO) Factory.getInstance().get(Factory.DAO_AREA);
    Gson gson = new GsonBuilder().create();
    public static NetworkManager networkManager = (NetworkManager) Factory.getInstance().get(Factory.NETWORK_MANAGER);

    public void SyncAreas(){
        Log.i("AREASYNC", "Trying to sync areas");
        CustomStringRequest csr = new CustomStringRequest(Request.Method.GET,
                UrlPaths.GET_AREAS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new GsonBuilder().create();
                        Areas areas = gson.fromJson(response.toString(), Areas.class);
                        Log.i("AREASYNC", "Response = "+ areas.toString());
                        SharedPreferences sp = (SharedPreferences) Factory.getInstance().get(Factory.SHARED_PREFERENCES);

                        String existingVersion = Utility.getFromSharedPreferences(Strings.VERSION);
                        Log.i("AREASYNC", "Existing Version = "+ existingVersion);
                        if( existingVersion == null || existingVersion.isEmpty() ||
                                (Integer.parseInt(existingVersion) < Integer.parseInt(areas.getVersion()))){

                            //////////// LOGS //////////////
                            Log.i("AREASYNC", "New Area Saved ");
                            Toast.makeText((Context)Factory.getInstance().get(Factory.BASE_CONTEXT),
                                    "New Area Sync", Toast.LENGTH_SHORT).show();
                            //////////// END LOGS /////////////////

                            Utility.saveToSharedPreference(Strings.VERSION, areas.getVersion());

                            for(Area area : areas.getAreas()){
                                Log.i("AREASYNC", "New Area Saved "+ area);
                                areaDAO.addArea(area);
                            }

                        }else{
                            Toast.makeText((Context)Factory.getInstance().get(Factory.BASE_CONTEXT),
                                    "Old Area Retain", Toast.LENGTH_SHORT).show();
                        }

                        Log.i("AREASYNC", "End Of Sync Sucees "+ areaDAO.getAreas());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("AREASYNC", "End Of Sync FAILURE " + areaDAO.getAreas());
                    }
                });

        networkManager.addToRequestQueue(csr);
    }


}
