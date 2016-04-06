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

import java.util.HashMap;
import java.util.Map;

import adcar.com.cache.Cache;
import adcar.com.database.dao.AdDAO;
import adcar.com.factory.Factory;
import adcar.com.model.Ad;
import adcar.com.model.Ads;

import adcar.com.network.CustomStringRequest;
import adcar.com.network.ImageDownload;
import adcar.com.network.NetworkManager;
import adcar.com.network.UrlPaths;
import adcar.com.utility.Strings;
import adcar.com.utility.Utility;

/**
 * Created by aditya on 09/02/16.
 */
public class AdHandler extends Handler {

    public static AdDAO adDAO = (AdDAO) Factory.getInstance().get(Factory.DAO_AD);

    public static NetworkManager networkManager = (NetworkManager) Factory.getInstance().get(Factory.NETWORK_MANAGER);

    public void SyncAds(){
        Log.i("AREASYNC", "Trying to sync areas");
        CustomStringRequest csr = new CustomStringRequest(Request.Method.GET,
                UrlPaths.GET_ADS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Ads ads = gson.fromJson(response.toString(), Ads.class);
                        Log.i("ADSSYNC", "Raw Response = " + response.toString());
                        Log.i("ADSSYNC", "Response = " + ads.toString());

                            //////////// LOGS //////////////
                            Log.i("ADSSYNC", "New Area Saved ");
                            Toast.makeText((Context) Factory.getInstance().get(Factory.BASE_CONTEXT),
                                    "New Ad Sync", Toast.LENGTH_SHORT).show();
                            //////////// END LOGS /////////////////


                            Map<Integer, Ad> adMap = new HashMap<>();
                            adDAO.deleteAd();
                            for(Ad ad: ads.getAds()){
                                new ImageDownload().downloadImage(ad);
                                adDAO.addAd(ad);

                                adMap.put(ad.getAreaId(), ad);
                            }
                        Cache.getCache().setAdMap(adMap);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        networkManager.addToRequestQueue(csr);
    }
}
