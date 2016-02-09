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

import adcar.com.database.dao.AdDAO;
import adcar.com.database.dao.AreaDAO;
import adcar.com.factory.Factory;
import adcar.com.model.Ad;
import adcar.com.model.Ads;
import adcar.com.model.Area;
import adcar.com.model.Areas;
import adcar.com.network.CustomStringRequest;
import adcar.com.network.ImageDownload;
import adcar.com.network.NetworkManager;
import adcar.com.network.UrlPaths;
import adcar.com.utility.Strings;
import adcar.com.utility.Utility;

/**
 * Created by aditya on 09/02/16.
 */
public class AdHandler {

    public static AdDAO adDAO = (AdDAO) Factory.getInstance().get(Factory.DAO_AD);
    Gson gson = new GsonBuilder().create();
    public static NetworkManager networkManager = (NetworkManager) Factory.getInstance().get(Factory.NETWORK_MANAGER);

    public void SyncAds(){
        Log.i("AREASYNC", "Trying to sync areas");
        CustomStringRequest csr = new CustomStringRequest(Request.Method.GET,
                UrlPaths.GET_AREAS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new GsonBuilder().create();
                        Ads ads = gson.fromJson(response.toString(), Ads.class);
                        Log.i("ADSSYNC", "Response = "+ ads.toString());
                        SharedPreferences sp = (SharedPreferences) Factory.getInstance().get(Factory.SHARED_PREFERENCES);

                        String existingVersion = Utility.getFromSharedPreferences(Strings.VERSION_AD);
                        Log.i("ADSSYNC", "Existing Version = "+ existingVersion);
                        if( existingVersion == null || existingVersion.isEmpty() ||
                                (Integer.parseInt(existingVersion) < Integer.parseInt(ads.getVersion()))){

                            //////////// LOGS //////////////
                            Log.i("ADSSYNC", "New Area Saved ");
                            Toast.makeText((Context) Factory.getInstance().get(Factory.BASE_CONTEXT),
                                    "New Ad Sync", Toast.LENGTH_SHORT).show();
                            //////////// END LOGS /////////////////

                            Utility.saveToSharedPreference(Strings.VERSION_AD, ads.getVersion());

                            for(Ad ad: ads.getAds()){
                                new ImageDownload().downloadImage(ad);
                            }
                        }else{
                            Toast.makeText((Context)Factory.getInstance().get(Factory.BASE_CONTEXT),
                                    "Old Area Retain", Toast.LENGTH_SHORT).show();
                        }

                        Log.i("ADSSYNC", "End Of Sync Sucees "+ ads);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("AREASYNC", "End Of Sync FAILURE " + error);
                    }
                });

        networkManager.addToRequestQueue(csr);
    }
}
