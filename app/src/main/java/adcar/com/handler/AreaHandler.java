package adcar.com.handler;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import adcar.com.adcar.MainActivity;
import adcar.com.cache.Cache;
import adcar.com.factory.Factory;
import adcar.com.database.dao.AreaDAO;
import adcar.com.model.servertalkers.Area;
import adcar.com.model.servertalkers.Areas;
import adcar.com.network.CustomStringRequest;
import adcar.com.network.UrlPaths;
import adcar.com.utility.Strings;
import adcar.com.utility.Utility;

/**
 * Created by aditya on 07/02/16.
 */
public class AreaHandler extends Handler {

    public static AreaDAO areaDAO = (AreaDAO) Factory.getInstance().get(Factory.DAO_AREA);

    public void SyncAreas(){
        Log.i("AREASYNC", "Trying to sync areas");
        CustomStringRequest csr = new CustomStringRequest(Request.Method.GET,
                UrlPaths.GET_AREAS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Areas areas = gson.fromJson(response.toString(), Areas.class);
                        Log.i("AREASYNC", "Response = "+ areas.toString());


                            //////////// LOGS //////////////
                            Log.i("AREASYNC", "New Area Saved ");
                            Utility.sendMessageToHandler(MainActivity.getHandler(), Strings.TOAST, "New Area Sync");

                            //////////// END LOGS /////////////////

                            areaDAO.deleteArea();
                            for(Area area : areas.getAreas()){
                                Log.i("AREASYNC", "New Area Saved "+ area);
                                areaDAO.addArea(area);
                            }

                        Cache.getCache().setArea(areas);
                        //Log.i("AREASYNC", "End Of Sync Sucees "+ areaDAO.getAreas());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Log.i("AREASYNC", "End Of Sync FAILURE " + areaDAO.getAreas());
                    }
                });

        networkManager.addToRequestQueue(csr);
    }


}
