package adcar.com.handler;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;


import adcar.com.adcar.MainActivity;
import adcar.com.factory.Factory;
import adcar.com.model.servertalkers.Versions;
import adcar.com.model.servertalkers.VersionsList;
import adcar.com.network.CustomStringRequest;
import adcar.com.network.UrlPaths;
import adcar.com.utility.Strings;
import adcar.com.utility.Utility;

/**
 * Created by aditya on 15/02/16.
 */
public class VersionHandler extends Handler {

    public void SyncVersions() {

        new CampaignHandler().SyncCampaigns();

        CustomStringRequest csr = new CustomStringRequest(Request.Method.GET, UrlPaths.GET_VERSIONS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String existingAreaVersion = Utility.getFromSharedPreferences(Strings.VERSION_AREA);
                        String existingAdVersion = Utility.getFromSharedPreferences(Strings.VERSION_AD);
                        Log.i("VERSION", "Existing Area Version = " + existingAreaVersion + "Existing Ad Version = " + existingAdVersion);
                        VersionsList versions = gson.fromJson(response.toString(), VersionsList.class);
                        Log.i("VERSION", versions.toString());
                        for (Versions ver : versions.getVersions()) {
                            if (ver.getName().equals(Strings.AREA)) {
                                if (existingAreaVersion == null || existingAreaVersion.isEmpty() ||
                                        (Integer.parseInt(existingAreaVersion) < ver.getVersion())) {
                                    Log.i("VERSION", "inside area");
                                    new AreaHandler().SyncAreas();
                                    Utility.saveToSharedPreference(Strings.VERSION_AREA, "" + ver.getVersion());
                                    Utility.sendMessageToHandler(MainActivity.getHandler(), Strings.TOAST, "NEW AREAS");

                                }else{
                                    Utility.sendMessageToHandler(MainActivity.getHandler(), Strings.TOAST, "Old Area Retain");
                                }
                            }

//                            if (ver.getName().equals(Strings.ADS)) {
//                                if (existingAdVersion == null || existingAdVersion.isEmpty() ||
//                                        (Integer.parseInt(existingAdVersion) < ver.getVersion())) {
//                                    Log.i("VERSION", "inside ads");
//                                    new CampaignHandler().SyncAds();
//                                    //Utility.saveToSharedPreference(Strings.VERSION_AD, "" + ver.getVersion());
//                                    Log.i("VERSION", "ads save "+ver.getVersion());
//                                    Toast.makeText((Context) Factory.getInstance().get(Factory.BASE_CONTEXT),
//                                            "NEW ADS COMING", Toast.LENGTH_SHORT).show();
//                                }else{
//                                    Toast.makeText((Context) Factory.getInstance().get(Factory.BASE_CONTEXT),
//                                            "Old Ad Retain", Toast.LENGTH_SHORT).show();
//                                }
//                            }

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("VERSION", "Error Called");
                    }
                }
        );
        networkManager.addToRequestQueue(csr);
    }

}
